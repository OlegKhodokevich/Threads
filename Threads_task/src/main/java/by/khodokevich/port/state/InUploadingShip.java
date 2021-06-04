package by.khodokevich.port.state;

import by.khodokevich.port.entity.Ship;

public class InUploadingShip implements ShipState {
    @Override
    public String getStatusName() {
        return StatusName.IN_WORK.getState();
    }

    @Override
    public void nextShipStatus(Ship ship) {
        ship.setShipState(new UploadedShip());
    }

    @Override
    public void previousShipStatus(Ship ship) {
        ship.setShipState(new WaitingShip());
    }
}
