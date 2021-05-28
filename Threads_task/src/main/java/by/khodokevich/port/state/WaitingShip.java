package by.khodokevich.port.state;

import by.khodokevich.port.entity.Ship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitingShip implements IShipState{
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String getStatusName() {
        return StatusName.WAITING.getState();
    }

    @Override
    public void nextShipStatus(Ship ship) {
        ship.setShipState(new InUploadingShip());
    }

    @Override
    public void previousShipStatus(Ship ship) {
        LOGGER.info("Ship can't get previous status. Current status = " + ship.getShipState());
    }
}
