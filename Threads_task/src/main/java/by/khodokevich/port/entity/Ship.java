package by.khodokevich.port.entity;

import by.khodokevich.port.state.IShipState;

import java.util.concurrent.TimeUnit;

public class Ship extends Thread {
    private int shipId;
    private int shipCapacity;
    private IShipState shipState;
    private ArrivalPurpose arrivalPurpose;
    private Storehouse storehouse;


    Ship(int shipId, int shipCapacity, ArrivalPurpose arrivalPurpose) {
        this.shipId = shipId;
        this.shipCapacity = shipCapacity;
        this.arrivalPurpose = arrivalPurpose;
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
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();            //TODO
        }
        if (arrivalPurpose == ArrivalPurpose.LOADING) {
            storehouse.takeContainers(shipCapacity);
        } else {
            storehouse.addContainers(shipCapacity);
        }
        shipState.nextShipStatus(this);
    }


}
