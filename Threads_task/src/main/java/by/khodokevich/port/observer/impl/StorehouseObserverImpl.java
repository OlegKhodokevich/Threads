package by.khodokevich.port.observer.impl;

import by.khodokevich.port.entity.Storehouse;
import by.khodokevich.port.exception.ProjectPortException;
import by.khodokevich.port.observer.StorehouseEvent;
import by.khodokevich.port.observer.StorehouseObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StorehouseObserverImpl implements StorehouseObserver {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MIN_PERCENT_FILLING_STOREHOUSE = 25;
    private static final int MAX_PERCENT_FILLING_STOREHOUSE = 75;

    @Override
    public void updateNumberContainers(StorehouseEvent storehouseEvent) throws ProjectPortException {
        Storehouse storehouse = storehouseEvent.getSource();
        if (storehouse.getCurrentContainersNumber() < Storehouse.getMaxCapacity() * MIN_PERCENT_FILLING_STOREHOUSE / 100) {
            LOGGER.info("Storehouse received containers. ");
            storehouse.addContainers(Storehouse.getMaxCapacity() * MIN_PERCENT_FILLING_STOREHOUSE / 100);
        }
        if (storehouse.getCurrentContainersNumber() > Storehouse.getMaxCapacity() * MAX_PERCENT_FILLING_STOREHOUSE / 100) {
            LOGGER.info("Storehouse sent containers. ");
            storehouse.takeContainers(Storehouse.getMaxCapacity()  * MAX_PERCENT_FILLING_STOREHOUSE / 100);

        }
    }
}
