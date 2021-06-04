package by.khodokevich.port.main;

import by.khodokevich.port.entity.ArrivalPurpose;
import by.khodokevich.port.entity.Ship;
import by.khodokevich.port.entity.ShipFactory;
import by.khodokevich.port.entity.Storehouse;
import by.khodokevich.port.exception.ProjectPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ThreadMain {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws ProjectPortException {
        Storehouse.setBerthNumber(5);
        Storehouse.setMaxCapacity(200);
        ShipFactory shipFactory = new ShipFactory();
        Random random = new Random();

        new Thread() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(60);
                    shipFactory.setWork(false);
                } catch (InterruptedException e) {
                    LOGGER.info("Something wrong with factory stop function.");
                }
            }
        }.start();

        List<Ship> ships = new ArrayList<>();
        Ship ship = null;
        while (shipFactory.isWork()) {
            ArrivalPurpose[] allArrivalPurposes = ArrivalPurpose.values();
            ArrivalPurpose arrivalPurpose = allArrivalPurposes[random.nextInt(allArrivalPurposes.length)];
            int capacity = random.nextInt((20 - 15) + 1) + 15;
            ship = shipFactory.createShip(capacity, arrivalPurpose);
            ships.add(ship);
            ship.start();

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new ProjectPortException("Can't sleep. " + Thread.currentThread().getName());
            }
        }

//        try {
//            ship.join();
//        } catch (InterruptedException e) {
//            throw new ProjectPortException(e);
//        }

        try {
            for (Ship element : ships) {
                element.join();
            }
        } catch (InterruptedException e) {
            throw new ProjectPortException("This thread can't be joined. Thread = " + ship);
        }
        Storehouse.cancelTimerContainerProvider();
    }
}
