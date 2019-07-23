package com.fengchao.guanaitong.util;

import java.util.ResourceBundle;

public class Config {

    private static final ResourceBundle config = ResourceBundle.getBundle("config");

    private Config() {
    }


    public static String getString(String key) {
        return config.getString(key);
    }

    public static int getInt(String key) {
        String temp = config.getString(key);
        int value = Integer.parseInt(temp);
        return value;
    }

    public static boolean getBoolean(String key) {
        String temp = config.getString(key);
        Boolean value = Boolean.parseBoolean(temp);
        return value;
    }

}
