package by.khodokevich.port.entity;

import by.khodokevich.port.exception.ProjectPortException;
import by.khodokevich.port.state.ShipState;
import by.khodokevich.port.state.NewcomerShip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Ship extends Thread {
    private static final Logger LOGGER = LogManager.getLogger();

    private int shipId;
    private int shipCapacity;

    private ShipState shipState;
    private ArrivalPurpose arrivalPurpose;

    public Ship(int shipId, int shipCapacity, ArrivalPurpose arrivalPurpose) {
        this.shipId = shipId;
        this.shipCapacity = shipCapacity;
        this.arrivalPurpose = arrivalPurpose;
        shipState = new NewcomerShip();
    }

    public void nextShipStatus() {
        shipState.nextShipStatus(this);
    }

    public void previousShipState() {
        shipState.previousShipStatus(this);
    }

    public void setShipState(ShipState shipState) {
        this.shipState = shipState;
    }

    public int getShipId() {
        return shipId;
    }

    public int getShipCapacity() {
        return shipCapacity;
    }

    public ShipState getShipState() {
        return shipState;
    }

    public ArrivalPurpose getArrivalPurpose() {
        return arrivalPurpose;
    }

    @Override
    public void run() {
        Berth berth = null;

        Storehouse storehouse = Storehouse.getInstance();
        try {
            shipState.nextShipStatus(this);
            berth = storehouse.getBerth(this);
            TimeUnit.SECONDS.sleep(1);
            shipState.nextShipStatus(this);

            TimeUnit.SECONDS.sleep(10);

            if (arrivalPurpose == ArrivalPurpose.LOADING) {
                LOGGER.info(this + "has loaded on " + berth.toString());
                storehouse.takeContainers(shipCapacity);
            } else {
                LOGGER.info(this + " has unloaded on " + berth.toString());
                storehouse.addContainers(shipCapacity);
            }

            shipState.nextShipStatus(this);
            LOGGER.info(this + " departed.");

        } catch (InterruptedException | ProjectPortException e) {
            LOGGER.warn(this + " left the port. " + e.getMessage());
        } finally {
            if (berth != null) {
                try {
                    storehouse.releaseBerth(berth);
                } catch (ProjectPortException e) {
                    LOGGER.info("Can't be release berth.");
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return shipId == ship.shipId && shipCapacity == ship.shipCapacity && arrivalPurpose == ship.arrivalPurpose;
    }

    @Override
    public int hashCode() {
        int result = shipId;
        result = result * 13 + shipCapacity;
        result = result * 13 + arrivalPurpose.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ship № ").append(shipId).append('{');
        sb.append(" capacity = ").append(shipCapacity);
        sb.append(", state = ").append(shipState.getStatusName());
        sb.append(", purpose of arrival = ").append(arrivalPurpose).append('}');
        return sb.toString();
    }

}
