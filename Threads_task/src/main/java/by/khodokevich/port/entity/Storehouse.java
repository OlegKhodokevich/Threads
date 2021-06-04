package by.khodokevich.port.entity;

import by.khodokevich.port.exception.ProjectPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Storehouse {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int DEFAULT_MAX_CAPACITY = 100;
    private static final int DEFAULT_BETH_NUMBER = 2;
    private static final int PERCENTAGE_INITIAL_FILLING_STOREHOUSE = 50;
    private static final int MIN_PERCENT_FILLING_STOREHOUSE = 25;
    private static final int MAX_PERCENT_FILLING_STOREHOUSE = 75;

    private static int maxCapacity;
    private static int berthNumber;
    private static Storehouse instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static Timer timer;

    private int currentContainersNumber;
    private ArrayDeque<Berth> berths;
    private Semaphore semaphore;


    private Storehouse() {
        if (maxCapacity == 0) {
            maxCapacity = DEFAULT_MAX_CAPACITY;
        }
        if (berthNumber == 0) {
            berthNumber = DEFAULT_BETH_NUMBER;
        }
        berths = new ArrayDeque<>();
        for (int i = 0; i < berthNumber; i++) {
            berths.offerFirst(new Berth());
        }
        semaphore = new Semaphore(berthNumber, true);
        currentContainersNumber = (maxCapacity * PERCENTAGE_INITIAL_FILLING_STOREHOUSE / 100);
        timer = new Timer();
        timer.schedule(new TimerContainerProvider(),2000, 3000);
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

    public Berth getBerth(Ship ship) throws ProjectPortException {
        try {
            lock.lock();
            semaphore.acquire();
            Berth berth = berths.removeLast();
            LOGGER.info(ship + " get " + berth);
            return berth;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        throw new ProjectPortException("Can't receive berth.");
    }

    public void releaseBerth(Berth berth) throws ProjectPortException {
        try {
            lock.lock();
            berths.offerLast(berth);
            LOGGER.info(berth + " is free.");
        } catch (NullPointerException e) {
            throw new ProjectPortException("Berth is null.");
        } finally {
            semaphore.release();
            lock.unlock();
        }
    }

    public void addContainers(int numberContainers) throws ProjectPortException {
        boolean exit = false;
        try {
            lock.lock();
            while (!exit) {
                if ((currentContainersNumber + numberContainers) <= maxCapacity) {
                    currentContainersNumber = currentContainersNumber + numberContainers;
                    LOGGER.info("Number of containers in storehouse has changed. Current number of containers = " + currentContainersNumber);
                    exit = true;
                } else {
                    LOGGER.warn("Number of containers in storehouse more then max capacity. Please wait.");
                    TimeUnit.SECONDS.sleep(2);
                }
            }
        } catch (InterruptedException e) {
            throw new ProjectPortException("Can't await in add containers.");
        } finally {
            lock.unlock();
        }
    }

    public void takeContainers(int numberContainers) throws ProjectPortException {
        boolean exit = false;
        try {
            lock.lock();
            while (!exit) {
                if ((currentContainersNumber - numberContainers) >= 0) {
                    currentContainersNumber = currentContainersNumber - numberContainers;
                    LOGGER.info("Number of containers in storehouse has changed. Current number of containers = " + currentContainersNumber);
                    exit = true;
                } else {
                    LOGGER.warn("Number of containers in storehouse less then it needs. Please wait.");
                    TimeUnit.SECONDS.sleep(2);
                }
            }
        } catch (InterruptedException e) {
            throw new ProjectPortException("Can't await in add containers ");
        } finally {
            lock.unlock();
        }
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

    class TimerContainerProvider extends TimerTask {
        @Override
        public void run() {
            try {
                lock.lock();
                if(instance == null) {
                    timer.cancel();
                }
                if (currentContainersNumber < maxCapacity * MIN_PERCENT_FILLING_STOREHOUSE / 100) {
                    LOGGER.info("Storehouse received containers. ");
                    addContainers(maxCapacity * MIN_PERCENT_FILLING_STOREHOUSE / 100);
                }
                if (currentContainersNumber > maxCapacity * MAX_PERCENT_FILLING_STOREHOUSE / 100) {
                    LOGGER.info("Storehouse sent containers. ");
                    takeContainers(maxCapacity  * MIN_PERCENT_FILLING_STOREHOUSE / 100);

                }
                System.out.println(" Rum Timer Task");
            } catch (ProjectPortException e) {
                LOGGER.info("Timer container provider can't change current container number. " + e.getMessage());
            }finally {
                lock.unlock();
            }
        }      //TODO

    }

    public static void cancelTimerContainerProvider() {
        timer.cancel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Storehouse)) return false;
        Storehouse that = (Storehouse) o;
        return currentContainersNumber == that.currentContainersNumber && this.berths.equals(berths);
    }

    @Override
    public int hashCode() {
        int result = currentContainersNumber;
        Iterator iterator = berths.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            result = result * 31 + (obj != null ? obj.hashCode() : 0);
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

