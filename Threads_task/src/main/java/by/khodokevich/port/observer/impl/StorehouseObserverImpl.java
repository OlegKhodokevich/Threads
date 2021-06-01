package by.khodokevich.port.observer.impl;

import by.khodokevich.port.entity.Storehouse;
import by.khodokevich.port.exception.ProjectPortException;
import by.khodokevich.port.observer.StorehouseEvent;
import by.khodokevich.port.observer.StorehouseObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StorehouseObserverImpl implements StorehouseObserver {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void updateNumberContainers(StorehouseEvent storehouseEvent) throws ProjectPortException {
        Storehouse storehouse = storehouseEvent.getSource();
        if (storehouse.getCurrentContainersNumber() < Storehouse.getMaxCapacity() * 0.25) {
            LOGGER.info("Storehouse received containers. ");
            storehouse.addContainers((int) (Storehouse.getMaxCapacity() * 0.25));
        }
        if (storehouse.getCurrentContainersNumber() > Storehouse.getMaxCapacity() * 0.75) {
            LOGGER.info("Storehouse sent containers. ");
            storehouse.takeContainers((int) (Storehouse.getMaxCapacity() * 0.25));

        }
    }
}
