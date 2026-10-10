package com.oibsip.reservation;

import java.util.*;

public class TrainData {
    private static final Map<String, String> TRAINS = new LinkedHashMap<>();
    static {
        TRAINS.put("12345", "Rajdhani Express");
        TRAINS.put("12346", "Shatabdi Express");
        TRAINS.put("12347", "Duronto Express");
        TRAINS.put("12348", "Garib Rath Express");
        TRAINS.put("12349", "Tejas Express");
    }
    public static String getName(String number) { return TRAINS.getOrDefault(number, ""); }
    public static Set<String> numbers() { return TRAINS.keySet(); }
}
