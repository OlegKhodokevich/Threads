package by.khodokevich.port.state;

import by.khodokevich.port.entity.Ship;

public interface IShipState {
    String getStatusName();

    void nextShipStatus(Ship ship);

    void previousShipStatus(Ship ship);
}
