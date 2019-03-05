package utils.observer;

import utils.events.AbstractEntityChangeEvent;

public interface Observable {
   /**
    * @param obs observer to be added
    */
   void addObserver(Observer obs);

   /**
    * @param event notify all observers subscribed to the observable
    */
   void notifyObservers(AbstractEntityChangeEvent event);
}
