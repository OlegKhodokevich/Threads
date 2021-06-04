package by.khodokevich.port.entity;

import by.khodokevich.port.util.GenerateId;

public class Berth {
    private int berthId;

    public Berth() {
        this.berthId = GenerateId.generateBerthId();
    }

    public int getBerthId() {
        return berthId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Berth)) return false;
        Berth berth = (Berth) o;
        return berthId == berth.berthId;
    }

    @Override
    public int hashCode() {
        return berthId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Berth № ").append(berthId).append('.');
        return sb.toString();
    }
}
