package utils.events;

import domain.Student;

public class StudentChangeEvent extends AbstractEntityChangeEvent{
    private Student data;
    private Student oldData;

    public StudentChangeEvent(EventType eventType, Student data){
        super(eventType);
        this.data = data;
    }
    public StudentChangeEvent(EventType eventType, Student data, Student oldData){
        super(eventType);
        this.data = data;
        this.oldData = oldData;
    }

    public Student getData() {
        return data;
    }

    public Student getOldData() {
        return oldData;
    }
}
