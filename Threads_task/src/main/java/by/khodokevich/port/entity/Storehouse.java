package by.khodokevich.port.entity;

import by.khodokevich.port.exception.ProjectPortException;
import by.khodokevich.port.observer.StorehouseEvent;
import by.khodokevich.port.observer.StorehouseObservable;
import by.khodokevich.port.observer.StorehouseObserver;
import by.khodokevich.port.observer.impl.StorehouseObserverImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Storehouse implements StorehouseObservable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int DEFAULT_MAX_CAPACITY = 100;
    private static final int DEFAULT_BETH_NUMBER = 2;
    private static final int PERCENTAGE_INITIAL_FILLING_STOREHOUSE = 50;

    private static int maxCapacity;
    private static int berthNumber;

    private int currentContainersNumber;
    private ArrayList<Berth> berths;

    private static Storehouse instance;

    private Semaphore semaphore;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    private StorehouseObserverImpl observer = new StorehouseObserverImpl();

    private Storehouse() {
        if (maxCapacity == 0) {
            maxCapacity = DEFAULT_MAX_CAPACITY;
        }
        if (berthNumber == 0) {
            berthNumber = DEFAULT_BETH_NUMBER;
        }
        berths = new ArrayList<>();
        for (int i = 0; i < berthNumber; i++) {
            berths.add(new Berth());
        }
        semaphore = new Semaphore(berthNumber, true);
        currentContainersNumber = (maxCapacity * PERCENTAGE_INITIAL_FILLING_STOREHOUSE / 100);
    }

    public Berth getBerth(Ship ship, long maxWaitSeconds) throws ProjectPortException {
        try {
            lock.lock();
            if (semaphore.tryAcquire(maxWaitSeconds, TimeUnit.SECONDS)) {
                for (Berth berth : berths) {
                    if (!berth.isBusy().get()) {
                        berth.setBusy(true);
                        LOGGER.info(ship + " get " + berth);
                        return berth;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        throw new ProjectPortException(ship.toString() + " can't wait any longer. It is depart.");
    }

    public void releaseBerth(Ship ship, Berth berth) {
        berth.setBusy(false);
        LOGGER.info(ship + " depart from " + berth);
        semaphore.release();
    }

    public boolean addContainers(int numberContainers) throws ProjectPortException {
        boolean exit = false;
        boolean operate = false;
        try {
            lock.lock();
            while (!exit) {
                if ((currentContainersNumber + numberContainers) <= maxCapacity) {
                    currentContainersNumber = currentContainersNumber + numberContainers;
                    LOGGER.info("Number of containers in storehouse has changed. Current number of containers = " + currentContainersNumber);
                    exit = true;
                    operate = true;
                    notifyObservers();
                } else {
                    LOGGER.warn("Number of containers in storehouse more then max capacity. Please wait.");
                    notifyObservers();
                    exit = !(condition.await(5, TimeUnit.SECONDS) | (currentContainersNumber + numberContainers) <= maxCapacity);
                }
            }
        } catch (InterruptedException e) {
            throw new ProjectPortException("Can't await in add containers.");
        } finally {
            lock.unlock();
        }
        return operate;
    }

    public boolean takeContainers(int numberContainers) throws ProjectPortException {
        boolean exit = false;
        boolean operate = false;
        try {
            lock.lock();
            while (!exit) {
                if ((currentContainersNumber - numberContainers) >= 0) {
                    currentContainersNumber = currentContainersNumber - numberContainers;
                    LOGGER.info("Number of containers in storehouse has changed. Current number of containers = " + currentContainersNumber);
                    exit = true;
                    operate = true;
                    notifyObservers();
                } else {
                    LOGGER.warn("Number of containers in storehouse less then it needs. Please wait.");
                    notifyObservers();
                    exit = !(condition.await(5, TimeUnit.SECONDS) | (currentContainersNumber - numberContainers) >= 0);

                }
            }
        } catch (InterruptedException e) {
            throw new ProjectPortException("Can't await in add containers ");
        } finally {
            lock.unlock();
        }
        return operate;
    }


    public static Storehouse getInstance() {
        if (instance == null) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new Storehouse();
                    LOGGER.info(instance.toString());
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
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

    public int getCurrentContainersNumber() {
        return currentContainersNumber;
    }

    @Override
    public void attach(StorehouseObserver observer) {
    }

    @Override
    public void detach() {
    }

    @Override
    public void notifyObservers() throws ProjectPortException {
        StorehouseEvent storehouseEvent = new StorehouseEvent(this);
        if (observer == null) {
            throw new ProjectPortException("Observer is null. " + this);
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new ProjectPortException("Can't sleep in notifyObserver.");
        }
        observer.updateNumberContainers(storehouseEvent);
        condition.signalAll();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Storehouse)) return false;
        Storehouse that = (Storehouse) o;
        return currentContainersNumber == that.currentContainersNumber && Objects.equals(berths, that.berths);
    }

    @Override
    public int hashCode() {
        int result = currentContainersNumber;
        Iterator iterator = berths.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            result = result * 31 + (obj!=null ? obj.hashCode() : 0);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Storehouse{").append("currentContainersNumber = ").append(currentContainersNumber);
        sb.append(", max capacity = ").append(maxCapacity);
        sb.append(", number of berths = ").append(berths.size()).append('}');
        return sb.toString();
    }
}

