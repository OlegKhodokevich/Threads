package by.khodokevich.port.entity;

import by.khodokevich.port.util.GenerateId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShipFactory {
    private static final Logger LOGGER = LogManager.getLogger();
    private boolean work;

    public ShipFactory() {
        this.work = true;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public Ship createShip(int capacity, ArrivalPurpose arrivalPurpose) {
        int shipId = GenerateId.generateShipId();

        Ship ship = new Ship(shipId, capacity, arrivalPurpose);
        LOGGER.info(ship + " has arrived at the port.");

        return ship;
    }
}
