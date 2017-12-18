package Service;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.Repository;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observable;
import Validator.ValidationException;
import Utils.Observer;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class TemeService implements Observable<TemaLaborator> {

    public static int saptamanaCurenta = 5;

    private Repository<TemaLaborator, Integer> repository;

    private ArrayList<Observer<TemaLaborator>> temeObservers = new ArrayList<>();

    public TemeService(Repository<TemaLaborator, Integer> repository) {
        this.repository = repository;
    }

    /**
     *  Add a given entry
     * @param tema represents the entry that is added
     * @throws ValidationException
     */
    public TemaLaborator add(TemaLaborator tema) throws ValidationException{
        TemaLaborator saved = repository.save(tema);
        if(saved == null){
            ListEvent<TemaLaborator> event = createEvent(ListEventType.ADD, saved, repository.findAll());
            notifyObservers(event);
        }
        return saved;
    }

    /**
     *  Create an event of last modification
     * @param type represents type of event
     * @param elem
     * @param l
     * @param <E>
     * @return a new event with last modification
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l) {
        return new ListEvent<E>(type){
            @Override
            public Iterable<E> getList(){return l;}
            @Override
            public E getElement(){return elem;}
        };
    }

    /**
     *  Delete an entry by a given id
     * @param integer represents given id
     */
    public Optional<TemaLaborator> delete(Integer integer){
        Optional <TemaLaborator> deleted = repository.delete(integer);
        if(deleted.isPresent()){
            ListEvent<TemaLaborator> event = createEvent(ListEventType.REMOVE, deleted.get(), repository.findAll());
            notifyObservers(event);
        }
        return deleted;
    }

    /**
     *
     * @return  a set with all entries
     */
    public Set<TemaLaborator> getAll(){
        Set<TemaLaborator> all = new HashSet<>();
        repository.findAll().forEach(temaLaborator -> all.add(temaLaborator));
        return all;
    }

    public List<TemaLaborator> getAllTeme(){
        return StreamSupport.stream(repository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    /**
     *  Update a given entry
     * @param tema represents tema to be updated
     * @throws ValidationException
     */
    public TemaLaborator update(TemaLaborator tema) throws  ValidationException{
        TemaLaborator updated = repository.update(tema);
        if (updated == null) {
            ListEvent<TemaLaborator> event = createEvent(ListEventType.UPDATE, updated, repository.findAll());
            notifyObservers(event);
        }
        return updated;
    }

    public void prelungire(int numarTema, int saptamanaNoua) throws ValidationException{
        TemaLaborator tema = repository.findOne(numarTema);
        if(saptamanaNoua >= saptamanaCurenta -1)
            throw new ValidationException("Saptamana data nu poate fi actualizata deoarce depaseste termenul");
        tema.setDeadline(saptamanaNoua);
        repository.update(tema);
    }

    /**
     *  Add a specified observer
     * @param o represents the observer
     */
    @Override
    public void addObserver(Observer<TemaLaborator> o) {
        temeObservers.add(o);
    }

    /**
     *  Remove a specified observer
     * @param o represents the observer
     */
    @Override
    public void removeObserver(Observer<TemaLaborator> o) {
        temeObservers.add(o);
    }

    /**
     *  Notify all observers
     * @param event represents a list with events to be updated in observers
     */
    @Override
    public void notifyObservers(ListEvent<TemaLaborator> event) {
        temeObservers.forEach(temaLaboratorObserver -> temaLaboratorObserver.notifyEvent(event));
    }

    /**
     *  Filter and sort teme by deadline
     * @param deadline represents deadline given
     * @return a list with all teme that have deadline lower than @deadline and sorted by numarTema
     */
    /*public List<TemaLaborator> filterAndSortByDeadline(int deadline){
        List<TemaLaborator> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<TemaLaborator> alll = Filter.filterAndSorter(all, Filter.areDeadlineulMaiMic(deadline), Filter.comparatorNrTema);
        return alll;
    }*/

    /**
     *  Filter and sort teme by numarTema
     * @param numarTema represents numarTema given
     * @return a list with all teme that have numarTema lower than @numarTema and sorted by deadline
     */
  /*  public List<TemaLaborator> filterAndSortByNumarTema(int numarTema){
        List<TemaLaborator> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<TemaLaborator> alll = Filter.filterAndSorter(all, Filter.esteMaiMicaSauEgalaNumarTema(numarTema), Filter.comparatorDeadline);
        return alll;
    }*/

    /**
     *  Filter and sort teme by deadline and numarTema
     * @param deadline represents deadline given
     * @param numarTema represents numarTema given
     * @return a list with all teme that have deadline lower than @deadline and numarTema lower than @numarTema and sorted by cerinta
     */
   /* public List<TemaLaborator> filterAndSortByDeadlineAndNrTema(int deadline, int numarTema){
        List<TemaLaborator> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<TemaLaborator> alll = Filter.filterAndSorter(all, Filter.deadlineMaiMicNumarTemaMaiMare(deadline, numarTema), Filter.comparatorCerinta);
        return alll;
    }*/
}
