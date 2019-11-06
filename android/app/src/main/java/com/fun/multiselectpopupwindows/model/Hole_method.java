package com.fun.multiselectpopupwindows.model;

/**
 * Created by wk
 */

public class Hole_method {

    private String rtime;
    private String drill_id;
    private String value;

    public Hole_method(String rtime,String drill_id, String value) {
        this.rtime = rtime;
        this.drill_id = drill_id;
        this.value = value;
    }
    public String getrtime() {
        return rtime;
    }
    public String getvalue() {
        return value;
    }
    public String getdrillid() {
        return drill_id;
    }

}
