package by.khodokevich.port.util;

public class GenerateId {
    private static int shipIdCounter = 0;
    private static int berthId = 0;

    public static int generateShipId() {
        int id = ++shipIdCounter;
        return id;
    }

    public static int generateBerthId() {
        int id = ++berthId;
        return id;
    }
}
