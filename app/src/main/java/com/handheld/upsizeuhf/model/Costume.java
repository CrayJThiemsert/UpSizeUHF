package com.handheld.upsizeuhf.model;

import androidx.annotation.NonNull;

public class Costume {
    public int uid;
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
    public boolean isFound = false;

    public Costume(int uid, String runningNo, String actor, String actScence, String code, String type, String size, String codeNo, String epcHeader, String epcRun, String shipBox, String storageBox, String playBox) {
        this.uid = uid;
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
        this.uid = -1;
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

    @NonNull
    @Override
    public String toString() {
        return "Costume = " +
                "uid=" + this.uid + ", " +
                "runningNo=" + this.runningNo + ", " +
                "actor=" + this.actor + ", " +
                "actScence=" + this.actScence + ", " +
                "code=" + this.code + ", " +
                "type=" + this.type + ", " +
                "size=" + this.size + ", " +
                "codeNo=" + this.codeNo + ", " +
                "epcHeader=" + this.epcHeader + ", " +
                "epcRun=" + this.epcRun + ", " +
                "shipBox=" + this.shipBox + ", " +
                "storageBox=" + this.storageBox + ", " +
                "playBox=" + this.playBox + ", ";
    }
}
