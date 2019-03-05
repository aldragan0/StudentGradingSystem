package utils.observer;

import utils.events.AbstractEntityChangeEvent;

public interface Observer {

    /**
     * @param event event used to update the class implementing observer
     */
    void update(AbstractEntityChangeEvent event);
}
