package com.handheld.upsizeuhf.model;

public class Costume {
    public String runningNo;
    public String actor;
    public String actScence;
    public String code;
    public String type;
    public String size;
    public String codeNo;
    public String epcHeader;
    public String epcRun;
    public String shipBox;
    public String storageBox;
    public String playBox;

    public Costume(String runningNo, String actor, String actScence, String code, String type, String size, String codeNo, String epcHeader, String epcRun, String shipBox, String storageBox, String playBox) {
        this.runningNo = runningNo;
        this.actor = actor;
        this.actScence = actScence;
        this.code = code;
        this.type = type;
        this.size = size;
        this.codeNo = codeNo;
        this.epcHeader = epcHeader;
        this.epcRun = epcRun;
        this.shipBox = shipBox;
        this.storageBox = storageBox;
        this.playBox = playBox;
    }

    public Costume() {
        this.runningNo = "";
        this.actor = "";
        this.actScence = "";
        this.code = "";
        this.type = "";
        this.size = "";
        this.codeNo = "";
        this.epcHeader = "";
        this.epcRun = "";
        this.shipBox = "";
        this.storageBox = "";
        this.playBox = "";
    }
}
