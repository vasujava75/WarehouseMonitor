package com.org.central.detector.utility;

public class EventsParserUtility {
    public static EventsData parseSensorData(String data) {
        String[] parts = data.split("; ");
        String sensorId = parts[0].split("=")[1];
        int value = Integer.parseInt(parts[1].split("=")[1]);
        return new EventsData(sensorId, value);
    }
}
