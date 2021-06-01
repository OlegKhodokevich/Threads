package by.khodokevich.port.observer;

import by.khodokevich.port.exception.ProjectPortException;

public interface StorehouseObservable<T extends StorehouseObserver>{
    void attach(T observer);

    void detach();

    void notifyObservers() throws ProjectPortException;
}
