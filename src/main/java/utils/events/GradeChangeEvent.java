package utils.events;

import domain.Grade;

public class GradeChangeEvent extends AbstractEntityChangeEvent{
    private Grade data;
    public GradeChangeEvent(EventType eventType, Grade data){
        super(eventType);
        this.data = data;
    }

    public Grade getData() {
        return data;
    }
}
