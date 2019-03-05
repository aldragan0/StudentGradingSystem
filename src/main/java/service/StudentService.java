package service;

import domain.Index;
import domain.Student;
import exceptions.ValidationException;
import repo.PagedCrudRepository;
import utils.DataConvert;
import utils.events.AbstractEntityChangeEvent;
import utils.events.EventType;
import utils.events.StudentChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StudentService implements Observable, Service<Student>{
    private PagedCrudRepository<String, Student> repo;
    private List<Observer> observers;

    public StudentService(PagedCrudRepository<String, Student> repo){
        this.repo = repo;
        this.observers = new ArrayList<>();
    }

    /**
     * @return a list containing the elements of the next repo page
     */
    public List<Student> getNextRepoPage(){
        return repo.getNextPage();
    }

    /**
     * @return a list containing the elements of the prev repo page
     */
    public List<Student> getPrevRepoPage(){
        return repo.getPrevPage();
    }

    /**
     * @param pageNumber set the currentPage of the repo
     */
    public void setCurrentPageNumber(int pageNumber){
        repo.setPageNumber(pageNumber);
    }

    /**
     * @return the current page of the repo
     */
    public int getCurrentPageNumber(){
        return repo.getPageNumber();
    }

    /**
     * returns a student from the repo
     * @param entity student
     * @return student with the entity id
     */
    public Student getStudent(Student entity){
         return repo.findOne(entity.getID());
    }

    public Student getStudent(String id){
        return repo.findOne(id);
    }

    /**
     * adds a student to the repo
     * @param student student
     * @return null - if the student was successfully added
     *         student - otherwise
     */
    public Student addStudent(Student student){
        Student.assignId(student);
        Student response = repo.save(student);
        if(response == null){
            StudentChangeEvent event = new StudentChangeEvent(EventType.ADD, student);
            notifyObservers(event);
        }
        return response;
    }

    /**
     * @param student student which must be deleted
     * @return response - if the student was successfully deleted
     *         null - otherwise
     */
    public Student removeStudent(Student student){
        Student response = repo.delete(student.getID());
        if(response != null){
            StudentChangeEvent event = new StudentChangeEvent(EventType.REMOVE, response);
            notifyObservers(event);
        }
        return response;
    }

    /**
     * @param student student containing updated information
     * @return null - if the student was successfully updated
     *         student - otherwise
     */
    public Student updateStudent(Student student){
        Student oldStudent = repo.findOne(student.getID());
        Student response = repo.update(student);
        if(response == null){
            StudentChangeEvent event = new StudentChangeEvent
                    (EventType.UPDATE, student, oldStudent);
            notifyObservers(event);
        }
        return response;
    }

    /**
     * @return all students contained inside the repo
     */
    public Collection<Student> getAll(){
        return repo.findAll();
    }

    /**
     * sets the number of elements per page
     * @param numEntities number of element that a page will contain
     */
    public void setPageSize(int numEntities){
        repo.setEntitiesPerPage(numEntities);
    }

    /**
     * return the number of elements in the repo
     * @return number of items in the repo
     */
    public int getSize(){
        return repo.findAll().size();
    }

    /**
     * loads all students from a data file into the repository
     * @param filePath the path of the file which stores the data
     * @throws ValidationException if one or more students could not be added
     */
    public void loadStudents(File filePath){
        Index.resetIndex();
        List<Student> students = DataConvert.loadStudentsDataFromCSV(filePath);
        List<String> errorMessage = new ArrayList<>();
        students.forEach(student -> {
            try{
                addStudent(student);
            } catch (ValidationException ex){
                errorMessage.add("=========================================\n");
                errorMessage.add(student.toString() + " - " + ex.getMessage());
                errorMessage.add("=========================================\n");
            }
        });
        if(!errorMessage.isEmpty()){
            String error = errorMessage.stream().reduce("", (s1, s2)-> s1 += s2);
            throw new ValidationException(error);
        }
    }

    @Override
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers(AbstractEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}