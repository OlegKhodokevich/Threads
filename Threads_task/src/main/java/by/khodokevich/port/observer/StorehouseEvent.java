package by.khodokevich.port.observer;

import by.khodokevich.port.entity.Storehouse;

import java.util.EventObject;

public class StorehouseEvent extends EventObject {


    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public StorehouseEvent(Storehouse source) {
        super(source);
    }

    @Override
    public Storehouse getSource() {
        return (Storehouse) super.getSource();
    }
}
