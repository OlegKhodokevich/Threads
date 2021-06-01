package by.khodokevich.port.observer;

import by.khodokevich.port.exception.ProjectPortException;

public interface StorehouseObserver {
    void updateNumberContainers(StorehouseEvent storehouseEvent) throws ProjectPortException;

}
