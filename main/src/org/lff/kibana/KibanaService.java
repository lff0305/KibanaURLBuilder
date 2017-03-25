package org.lff.kibana;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lff.http.HttpResponse;
import org.lff.http.HttpUtil;
import org.lff.kibana.vo.DashboardVO;
import org.lff.kibana.vo.VisualizationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuff on 2017/3/21 22:05
 */
public class KibanaService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String ELASTICSEARCH_URL = "http://10.16.33.175:9200/.kibana/%s/_search";

    public String build(String dashboardName, String kibanaURL) {
        String search =
                "{\n" +
                        "   \"query\": {\n" +
                        "      \"term\": {\n" +
                        "         \"_id\": {\n" +
                        "            \"value\": \"%s\"\n" +
                        "         }\n" +
                        "      }\n" +
                        "   }\n" +
                        "}";
        JSONObject dashboard = get(ELASTICSEARCH_URL, "dashboard", String.format(search, dashboardName));
        try {
            DashboardVO vo = validateAndParse(dashboard);
            logger.info("Get Dashboard vo {}", vo);
            return build(kibanaURL, vo);
        } catch (JSONException e) {
            throw new RuntimeException("Not a valid result. Dashboard name not correct ?", e);
        }
    }

    private String build(String kibanaURL, DashboardVO vo) {
        String url = kibanaURL + "/app/kibana#/dashboard/" + vo.getId() + "?embed=true";
        url += "&_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-15m,mode:quick,to:now))";
        url += "&_a=(";
        url += "filters:!(),options:(darkTheme:!f),";
        url += "panels:!(";
        boolean first = true;
        for (VisualizationVO panel : vo.getVisualizations()) {
            if (!first) {
                url += ",";
            }
            first = false;
            url += "(";
            url += buildPanel(panel);
            url += ")";
        }
        url += ")"; //end panels
        url += ",";
        url += buildQuery();
        url += ",";
        url += buildTitle(vo.getTitle());
        url += ")"; //end _a
        return url;
    }

    private String buildTitle(String title) {
        try {
            String s = URLEncoder.encode(title, "UTF-8");
            s = escapeURIPathParam(s);
            return String.format("title:'%s',uiState:()", s);
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to encodeurl", e);
            return null;
        }
    }

    public static String escapeURIPathParam(String input) {
        int len = input.length();
        String result = "";
        for (int i=0; i<len; i++) {
            char c = input.charAt(i);
            if (c == '+') {
                result += "%20";
                continue;
            }
            result += c;
        }
        return result;
    }


    private String buildQuery() {
        // query:(query_string:(analyze_wildcard:!t,query:'*'))
        String s = "query:";
        s += "(";
        s += "query_string:(analyze_wildcard:!t,query:'*')";
        s += ")";
        return s;
    }

    private String buildPanel(VisualizationVO panel) {
        // (col:1,id:New-Visualization,panelIndex:1,row:1,size_x:3,size_y:3,type:visualization)
        String s = "";
        s += "col:" + panel.getCol();
        s += ",id:" + panel.getId();
        s += ",panelIndex:" + panel.getPanelIndex();
        s += ",row:" + panel.getRow();
        s += ",size_x:" + panel.getSize_x();
        s += ",size_y:" + panel.getSize_y();
        s += ",type:visualization";
        return s;
    }

    private DashboardVO validateAndParse(JSONObject dashboard) throws JSONException {
        JSONObject hits = dashboard.getJSONObject("hits");
        int total = hits.getInt("total");
        if (total != 1) {
            throw new RuntimeException("Total is not 1");
        }
        JSONArray hitsArray = hits.getJSONArray("hits");
        JSONObject hit0 = hitsArray.getJSONObject(0);
        DashboardVO vo = new DashboardVO();
        vo.setId(hit0.getString("_id"));
        JSONObject source = hit0.getJSONObject("_source");
        vo.setTitle(source.getString("title"));
        List<VisualizationVO> visualizations = getVisualizations(source);
        vo.setVisualizations(visualizations);
        return vo;
    }

    private List<VisualizationVO> getVisualizations(JSONObject source) throws JSONException {
        List<VisualizationVO> result = new ArrayList<>();
        String strPanels = source.getString("panelsJSON");
        JSONArray panels = new JSONArray(strPanels);
        for (int i=0; i<panels.length(); i++) {
            JSONObject panel = panels.getJSONObject(i);
            String id = panel.getString("id");
            String type = panel.getString("type");
            int panelIndex = panel.getInt("panelIndex");
            int size_x = panel.getInt("size_x");
            int size_y = panel.getInt("size_y");
            int col = panel.getInt("col");
            int row = panel.getInt("row");
            switch (type) {
                case "visualization": {
                    VisualizationVO v = new VisualizationVO();
                    v.setId(id);
                    v.setCol(col);
                    v.setRow(row);
                    v.setSize_x(size_x);
                    v.setSize_y(size_y);
                    v.setPanelIndex(panelIndex);
                    result.add(v);
                }
            }
        }
        return result;
    }

    private static JSONObject get(String kibanaUrl, String path, String data) {
        HttpResponse response = HttpUtil.send(String.format(kibanaUrl, path), data);
        if (response.getCode() != 200) {
        } else {
            logger.error(response.getResponse());
        }

        try {
            JSONObject o = new JSONObject(response.getResponse());
            return o;
        } catch (JSONException e) {
            logger.error("Failed to parse {}", response.getResponse());
            throw new RuntimeException(e.getCause());
        }
    }
}