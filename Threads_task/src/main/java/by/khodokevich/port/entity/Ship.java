package by.khodokevich.port.entity;

import by.khodokevich.port.exception.ProjectPortException;
import by.khodokevich.port.state.IShipState;
import by.khodokevich.port.state.NewcomerShip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Ship extends Thread {
    private static final Logger LOGGER = LogManager.getLogger();

    private int shipId;
    private int shipCapacity;
    private long maxWaitSeconds;

    private IShipState shipState;
    private ArrivalPurpose arrivalPurpose;

    private Storehouse storehouse;

    public Ship(int shipId, int shipCapacity, ArrivalPurpose arrivalPurpose, long maxWaitSeconds) {
        this.shipId = shipId;
        this.shipCapacity = shipCapacity;
        this.arrivalPurpose = arrivalPurpose;
        this.maxWaitSeconds = maxWaitSeconds;
        shipState = new NewcomerShip();
        storehouse = Storehouse.getInstance();
    }

    public void nextShipStatus() {
        shipState.nextShipStatus(this);
    }

    public void previousShipState() {
        shipState.previousShipStatus(this);
    }

    public void setShipState(IShipState shipState) {
        this.shipState = shipState;
    }

    public int getShipId() {
        return shipId;
    }

    public int getShipCapacity() {
        return shipCapacity;
    }

    public IShipState getShipState() {
        return shipState;
    }

    public ArrivalPurpose getArrivalPurpose() {
        return arrivalPurpose;
    }

    @Override
    public void run() {
        Berth berth = null;
        try {
            shipState.nextShipStatus(this);
            berth = storehouse.getBerth(this, maxWaitSeconds);
            TimeUnit.SECONDS.sleep(1);
            shipState.nextShipStatus(this);

            TimeUnit.SECONDS.sleep(5);
            boolean operation = false;
            if (arrivalPurpose == ArrivalPurpose.LOADING) {
                LOGGER.info(this + "has loaded on " + berth.toString());
                operation = storehouse.takeContainers(shipCapacity);
            } else {
                LOGGER.info(this + " has unloaded on " + berth.toString());
                operation = storehouse.addContainers(shipCapacity);
            }

            shipState.nextShipStatus(this);
            if (operation) {
                LOGGER.info(this + " departed.");
            } else {
                LOGGER.info(this + " had not achieved purpose and departed next port.");
            }

        } catch (InterruptedException | ProjectPortException e) {
            LOGGER.warn(this + " left the port. " + e.getMessage());
        } finally {
            if (berth != null) {
                storehouse.releaseBerth(this, berth);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return shipId == ship.shipId && shipCapacity == ship.shipCapacity && maxWaitSeconds == ship.maxWaitSeconds && arrivalPurpose == ship.arrivalPurpose && Objects.equals(storehouse, ship.storehouse);
    }

    @Override
    public int hashCode() {
        int result = shipId;
        result = result * 13 + shipCapacity;
        result = result * 13 + (int) maxWaitSeconds;
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
