package com.technologies.pixelbox.vepami;

public class ParserNumbers {

    public static int getInt(String s) {
        try {
            return Integer.valueOf(s);
        }
        catch (Exception ex) {
            return -1;
        }
    };

    public static float getFloat(String s) {
        try {
            return Float.valueOf(s);
        }
        catch(Exception ex) {
            return -1;
        }
    }
}
