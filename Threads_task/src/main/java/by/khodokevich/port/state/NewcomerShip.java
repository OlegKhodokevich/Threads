package by.khodokevich.port.state;

import by.khodokevich.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewcomerShip implements ShipState {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public String getStatusName() {
        return StatusName.NEWCOMER.getState();
    }

    @Override
    public void nextShipStatus(Ship ship) {
        ship.setShipState(new WaitingShip());
    }

    @Override
    public void previousShipStatus(Ship ship) {
        LOGGER.info("Ship can't get previous status. Current status = " + ship.getShipState());

    }
}
