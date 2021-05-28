package by.khodokevich.port.entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Storehouse {
    private static int maxCapacity;
    private static int berthNumber;
    private static final int DEFAULT_MAX_CAPACITY = 100;
    private static final int DEFAULT_BETH_NUMBER = 2;
    private int currentContainersNumber;
    ExecutorService berth;

    private Storehouse() {
        if (maxCapacity == 0) {
            maxCapacity = DEFAULT_MAX_CAPACITY;
        }
        if (berthNumber == 0) {
            berthNumber = DEFAULT_BETH_NUMBER;
        }
        berth = Executors.newFixedThreadPool(maxCapacity);
        currentContainersNumber = (int) 0.5 * maxCapacity;
    }


    private static final class StorehouseHelper {
        private static final Storehouse INSTANCE = new Storehouse();
    }

    public boolean addContainers(int numberContainers) {
        if ((currentContainersNumber + numberContainers) <= maxCapacity) {
            currentContainersNumber = currentContainersNumber + numberContainers;
        }
        return ((currentContainersNumber + numberContainers) < maxCapacity);
    }

    public boolean takeContainers(int numberContainers) {
        if ((currentContainersNumber - numberContainers) >= 0) {
            currentContainersNumber = currentContainersNumber - numberContainers;
        }
        return ((currentContainersNumber + numberContainers) < maxCapacity);
    }



    public static Storehouse getInstance() {
        return StorehouseHelper.INSTANCE;
    }

    public ExecutorService getBerth() {
        return berth;
    }

    public static int getMaxCapacity() {
        return maxCapacity;
    }

    public static void setMaxCapacity(int maxCapacity) {
        Storehouse.maxCapacity = maxCapacity;
    }

    public static int getBerthNumber() {
        return berthNumber;
    }

    public static void setBerthNumber(int berthNumber) {
        Storehouse.berthNumber = berthNumber;
    }

}
