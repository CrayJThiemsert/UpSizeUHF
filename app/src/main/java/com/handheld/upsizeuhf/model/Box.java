package com.handheld.upsizeuhf.model;

import androidx.annotation.NonNull;

public class Box {
    public int uid;
    public String name;
    public String epc;
    public String epcHeader;
    public String epcRun;
    public boolean selected = false;

    public Box(int uid, String name, String epc, String epcHeader, String epcRun) {
        this.name = name;
        this.uid = uid;
        this.epc = epc;
        this.epcHeader = epcHeader;
        this.epcRun = epcRun;
    }

    public Box() {
        this.name = "";
        this.uid = -1;
        this.selected = false;
        this.epc = "";
        this.epcHeader = "";
        this.epcRun = "";
    }

    @NonNull
    @Override
    public String toString() {
        return "Box - " +
                "uid=" + this.uid +
                ", name=" + this.name +
                ", epc=" + this.epc +
                ", epcHeader=" + this.epcHeader +
                ", epcRun=" + this.epcRun;
    }
}
