package com.atinternet.tracker;

public class Utility {

    public static double parseDoubleFromString(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception ignored) {
            return 0.0;
        }
    }

    public static boolean parseBooleanFromString(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception ignored) {
            return false;
        }
    }

    public static int parseIntFromString(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static String stringJoin(char delimiter, String... strs) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strs) {
            if (first) {
                first = false;
                sb.append(s);
            } else {
                sb.append(delimiter).append(s);
            }
        }

        return sb.toString();
    }
}
