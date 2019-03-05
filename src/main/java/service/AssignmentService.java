package service;

import domain.Assignment;
import repo.CrudRepository;
import utils.events.AbstractEntityChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentService implements Service<Assignment>, Observable{
    private CrudRepository<String, Assignment> repo;
    private List<Observer> observers;

    public AssignmentService(CrudRepository<String, Assignment> repo){
        this.repo = repo;
        observers = new ArrayList<>();
    }

    /**
     * @return number of elements in the repository
     */
    public int getSize(){
        return repo.findAll().size();
    }

    /**
     * @param entity assignment
     * @return assignment with the entity id
     */
    public Assignment getAssignment(Assignment entity){
        return repo.findOne(entity.getID());
    }

    /**
     * add an assignment to the repo
     * @param assignment assignment to be added
     * @return null - if the assignment was added
     *         assignment - otherwise
     */
    public Assignment addAssignment(Assignment assignment){
        return repo.save(assignment);
    }

    /**
     * remove an assignment from the repo
     * @param assignment assignment to be deleted
     * @return assignment - if the assignment was deleted
     *         null - otherwise
     */
    public Assignment deleteAssignment(Assignment assignment){
        return repo.delete(assignment.getID());
    }

    /**
     * update an existent assignment
     * @param assignment assignment to replace the old one
     * @return null - if the assignment was updated
     *         assignment - otherwise
     */
    public Assignment updateAssignment(Assignment assignment){
        return repo.update(assignment);
    }

    /**
     * @param assignment assignment whose deadline will replace the old one's
     * @return assignment - the deadline was updated
     *         null - otherwise
     */
    public Assignment updateDeadlineAssignment(Assignment assignment){
        Assignment actualAssignment = repo.findOne(assignment.getID());
        if((assignment.getDeadline() < 1 || assignment.getDeadline() > 14)
                || actualAssignment == null
                ||(GradeService.getCurrentWeek() > actualAssignment.getDeadline())
                )
            return assignment;
        actualAssignment.setDeadline(assignment.getDeadline());
        return repo.update(actualAssignment);
    }

    /**
     * returns the assignment where the currentWeek is between the week received
     * and the deadline
     * @param currentWeek current week as a delta since the start of the semester
     * @return assignment - if an assignment where the currentWeek
     * is between the weekReceived and the deadline
     *         null - otherwise
     */
    public Assignment getCurrentAssignment(int currentWeek){
        List<Assignment> currentAssignments = getAll().stream()
                .filter(assignment -> assignment.getDeadline() >= currentWeek
                        && assignment.getWeekReceived() < currentWeek)
                .collect(Collectors.toList());
        if(currentAssignments.isEmpty()) return null;
        return currentAssignments.get(0);
    }

    /**
     * returns all the items in the repository
     * @return all items in repo
     */
    @Override
    public Collection<Assignment> getAll() {
        return repo.findAll();
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers(AbstractEntityChangeEvent event) {
        observers.forEach(observer->observer.update(event));
    }
}
