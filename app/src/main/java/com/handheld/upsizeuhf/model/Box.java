package com.handheld.upsizeuhf.model;

public class Box {
    public String uid;
    public String name;
    public String epc;
    public String epcHeader;
    public String epcRun;
    public boolean selected = false;

    public Box(String uid, String name, String epc, String epcHeader, String epcRun) {
        this.name = name;
        this.uid = uid;
        this.epc = epc;
        this.epcHeader = epcHeader;
        this.epcRun = epcRun;
    }

    public Box() {
        this.name = "";
        this.uid = "";
        this.selected = false;
        this.epc = "";
        this.epcHeader = "";
        this.epcRun = "";
    }
}
