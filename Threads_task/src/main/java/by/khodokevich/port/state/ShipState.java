package by.khodokevich.port.state;

import by.khodokevich.port.entity.Ship;

public interface ShipState {
    String getStatusName();

    void nextShipStatus(Ship ship);

    void previousShipStatus(Ship ship);
}
