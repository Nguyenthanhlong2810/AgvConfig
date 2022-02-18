package com.aubot.agv.attributes;

public enum StopSource {
    NONE ("None"),
    BUTTON ("Nut nhan"),
    TABLE_STOP ("Table Stop"),
    OUTLINE ("Outline"),
    TABLE_ESTOP("Table EStop"),
    ESTOP ("EStop"),
    LOW_BATTERY ("Pin Yeu"),
    LINE_DETECTOR_CIRCUIT("Mat ket noi do line"),
    CAN_ERROR("Duong Can loi"),
    STOP_SYNCHRONIZED ("Dung dong bo"),
    OVER_LOAD("Qua tai"),
    UNKNOWN("UnKknown");


    private String string;

    StopSource(String string){
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
