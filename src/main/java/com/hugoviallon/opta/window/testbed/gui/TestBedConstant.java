package com.hugoviallon.opta.window.testbed.gui;

import java.text.SimpleDateFormat;

public class TestBedConstant {

    public static final int ARC = 5;
    public static final int DELTA_Y_TEXT = 15;
    public static final int DELTA_X_TEXT = 5;

    public static SimpleDateFormat getHHMMSSFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    public static SimpleDateFormat getYYMMDDFormat() {
        return new SimpleDateFormat("yy/MM/dd");
    }

}
