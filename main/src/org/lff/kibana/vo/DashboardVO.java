package org.lff.kibana.vo;

import java.util.List;

/**
 * Created by liuff on 2017/3/21 22:21
 */
public class DashboardVO {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<VisualizationVO> getVisualizations() {
        return visualizations;
    }

    public void setVisualizations(List<VisualizationVO> visualizations) {
        this.visualizations = visualizations;
    }

    private List<VisualizationVO> visualizations;

}
