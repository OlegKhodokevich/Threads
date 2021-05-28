package by.khodokevich.port.entity;

import by.khodokevich.port.util.GenerateId;

import java.util.Random;
import java.util.concurrent.ExecutorService;

public class ShipFactory {
    public Ship createShip (int ShipCapacity, ArrivalPurpose arrivalPurpose) {
        int shipId = GenerateId.generateShipId();
        Random random = new Random(47);
        int capacity = 10 + random.nextInt((20-15)+1) + 15;
        Ship ship = new Ship(shipId, capacity, arrivalPurpose);
        Storehouse storehouse = Storehouse.getInstance();
        ExecutorService executor = storehouse.getBerth();
        executor.execute(ship);
        return ship;
    }
}
