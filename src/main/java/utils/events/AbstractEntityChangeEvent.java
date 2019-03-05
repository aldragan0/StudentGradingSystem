package utils.events;

public abstract class AbstractEntityChangeEvent implements Event {
    private EventType eventType;
    public AbstractEntityChangeEvent(EventType eventType){
        this.eventType = eventType;
    }
    public EventType getEventType(){
        return this.eventType;
    }
}
