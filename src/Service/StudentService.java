package Service;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.Repository;
import Repository.StudentFileRepository;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observable;
import Utils.Observer;
import Validator.ValidationException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class StudentService implements Observable<Student> {

    private Repository<Student, Integer> repository;

    private ArrayList<Observer<Student>> observerStudents = new ArrayList<>();

    public StudentService(Repository<Student, Integer> repository) {
        this.repository = repository;
    }

    /**
     *   Add a given entry
     * @param student represents entry that is added
     * @throws ValidationException
     */
    public Student add(Student student) throws ValidationException{
        Student saved = repository.save(student);
        if(saved == null){
            ListEvent<Student> event = createEvent(ListEventType.ADD, saved, repository.findAll());
            notifyObservers(event);
        }
        return saved;
    }

    /**
     *
     * @return  a set with all entries
     */
    public Set<Student> getAll(){
        Set<Student> all = new HashSet<>();
        repository.findAll().forEach(student -> all.add(student));
        return all;
    }

    /**
     *  Delete an entry by a given id
     * @param integer represents given id
     */
    public Optional<Student> delete(Integer integer)
    {
        Optional<Student> deleted = repository.delete(integer);
        if(deleted.isPresent()){
            ListEvent<Student> event = createEvent(ListEventType.REMOVE, deleted.get(), repository.findAll());
            notifyObservers(event);
        }
        return deleted;
    }

    /**
     *  Update a given entry
     * @param student represents student that is updated
     * @throws ValidationException
     */
    public Student update(Student student) throws ValidationException{
        Student updated = repository.update(student);
        if(updated == null){
            ListEvent<Student> event = createEvent(ListEventType.UPDATE, updated, repository.findAll());
            notifyObservers(event);
        }
        return updated;
    }

    /**
     *  Filter and sort students by indrumator
     * @return a list with all students that have "Andrei" as cadruDidactic  and sorted by grupa
     */
   /* public List<Student> filterAndSortByIndrumator(){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.esteIndrumatorAndrei(), Filter.comparatorGrupa);
        return alll;
    }*/

    /**
     *  Filter and sort students by email
     * @return a list with all students that have '.ro' or '.com' in mail and sorted by nume
     */
    /*public List<Student> filterAndSortByMail(){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.verificareMail(), Filter.comparatorNume);
        return alll;
    }*/

    /**
     *  Filter and sort students by a substring
     * @param numePartial represents given substring
     * @return a list with all students that contains given substring and sort by nume
     */
    /*public List<Student> filterAndSortByName(String numePartial){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.verificareNume(numePartial), Filter.comparatorNume);
        return alll;
    }*/

    /**
     *  Filter and sort students by grupa
     * @param grupa represents grupa given
     * @return a list with all students from grupa @grupa and sorted by email
     */
    /*public List<Student> filterAndSortByGrupa(int grupa){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.verificareGrupa(grupa), Filter.comparatorEmail);
        return alll;
    }*/

    /**
     *  Add a specified observer
     * @param o represents the observer
     */
    @Override
    public void addObserver(Observer<Student> o) {
        observerStudents.add(o);
    }

    /**
     *  Remove a specified observer
     * @param o represents the observer
     */
    @Override
    public void removeObserver(Observer<Student> o) {
        observerStudents.remove(o);
    }

    /**
     *  Notify all observers
     * @param event represents a list with events to be updated in observers
     */
    @Override
    public void notifyObservers(ListEvent<Student> event) {
        observerStudents.forEach(studentObserver -> studentObserver.notifyEvent(event));
    }

    public List<Student> getAllStudents(){
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    /**
     *  Create an event of last modification
     * @param type represents type of event
     * @param elem
     * @param l
     * @param <E>
     * @return a new event with last modification
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }

}
