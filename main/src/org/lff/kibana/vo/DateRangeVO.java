package org.lff.kibana.vo;

/**
 * Created by liuff on 2017/3/29 23:42
 */
public class DateRangeVO {
    private String from = "now-15m";
    private String to="now";

    public DateRangeVO() {
        //default
    }

    public DateRangeVO(String from, String to) {
        this.from = from;
        this.to = to;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
