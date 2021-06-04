package by.khodokevich.port.state;

import by.khodokevich.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UploadedShip implements ShipState {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String getStatusName() {
        return StatusName.DEPARTING.getState();
    }

    @Override
    public void nextShipStatus(Ship ship) {
        LOGGER.info("Ship can't get next status. Current status = " + ship.getShipState());
    }

    @Override
    public void previousShipStatus(Ship ship) {
        LOGGER.info("Ship can't get next status. Current status = " + ship.getShipState());
    }
}
