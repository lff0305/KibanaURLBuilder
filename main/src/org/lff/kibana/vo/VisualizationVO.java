package org.lff.kibana.vo;

/**
 * Created by liuff on 2017/3/21 22:21
 */
public class VisualizationVO {
    private String id;
    private int panelIndex;
    private int size_x;
    private int size_y;
    private int col;
    private int row;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPanelIndex() {
        return panelIndex;
    }

    public void setPanelIndex(int panelIndex) {
        this.panelIndex = panelIndex;
    }

    public int getSize_x() {
        return size_x;
    }

    public void setSize_x(int size_x) {
        this.size_x = size_x;
    }

    public int getSize_y() {
        return size_y;
    }

    public void setSize_y(int size_y) {
        this.size_y = size_y;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
