package by.khodokevich.port.entity;

import by.khodokevich.port.util.GenerateId;

import java.util.concurrent.atomic.AtomicBoolean;

public class Berth {
    private int berthID;
    private AtomicBoolean busy;

    public Berth() {
        this.berthID = GenerateId.generateBerthId();
        busy = new AtomicBoolean(false);
    }

    public AtomicBoolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy.set(busy);
    }

    public int getBerthID() {
        return berthID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Berth)) return false;
        Berth berth = (Berth) o;
        return berthID == berth.berthID;
    }

    @Override
    public int hashCode() {
        return berthID + busy.hashCode() * 13;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Berth № ").append(berthID).append('.');
        return sb.toString();
    }
}
