package by.khodokevich.port.main;

import by.khodokevich.port.entity.ArrivalPurpose;
import by.khodokevich.port.entity.Ship;
import by.khodokevich.port.entity.ShipFactory;
import by.khodokevich.port.entity.Storehouse;
import by.khodokevich.port.exception.ProjectPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        while (shipFactory.isWork()) {
            ArrivalPurpose[] allArrivalPurposes = ArrivalPurpose.values();
            ArrivalPurpose arrivalPurpose = allArrivalPurposes[random.nextInt(allArrivalPurposes.length)];
            int maxWaitSecond = random.nextInt((60 - 20) + 1) + 20;
            int capacity = random.nextInt((20 - 15) + 1) + 15;

            Ship ship = shipFactory.createShip(capacity, arrivalPurpose, maxWaitSecond);
            ship.start();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new ProjectPortException(e);
            }
        }

//        Данные инициализации объектов считывать из файла. Данные в файле корректны.

    }
}
