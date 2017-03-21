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

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuff on 2017/3/21 22:05
 */
public class KibanaService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static String KIBANA_URL = "http://10.16.33.175:9200/.kibana/%s/_search";

    public JSONObject build(String dashboardName) {
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
        JSONObject dashboard = get(KIBANA_URL, "dashboard", String.format(search, dashboardName));
        try {
            DashboardVO vo = validateAndParse(dashboard);
        } catch (JSONException e) {
            throw new RuntimeException("Not a valid result. Dashboard name not correct ?", e);
        }
        return dashboard;
    }

    private DashboardVO validateAndParse(JSONObject dashboard) throws JSONException {
        JSONObject hits = dashboard.getJSONObject("hits");
        int total = hits.getInt("total");
        if (total != 1) {
            throw new RuntimeException("Total is not 1");
        }
        JSONArray hitsArray = hits.getJSONArray("hits");
        JSONObject source = hitsArray.getJSONObject(0);
        DashboardVO vo = new DashboardVO();
        vo.setId(source.getString("_id"));
        List<VisualizationVO> visualizations = getVisualizations(source.getJSONObject("_source"));
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
            System.out.println(response.getResponse());
        } else {
            System.out.println(response.getResponse());
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