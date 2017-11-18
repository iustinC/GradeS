package Service;

import Domain.Nota;
import Domain.TemaLaborator;
import Repository.Repository;
import Validator.ValidationException;

import java.util.*;

public class TemeService {

    public static int saptamanaCurenta = 5;
    private Repository<TemaLaborator, Integer> repository;

    public TemeService(Repository<TemaLaborator, Integer> repository) {
        this.repository = repository;
    }

    /**
     *  Add a given entry
     * @param tema represents the entry that is added
     * @throws ValidationException
     */
    public void add(TemaLaborator tema) throws ValidationException{
        repository.save(tema);
    }

    /**
     *  Delete an entry by a given id
     * @param integer represents given id
     */
    public Optional<TemaLaborator> delete(Integer integer){
        return repository.delete(integer);
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

    /**
     *  Update a given entry
     * @param tema represents tema to be updated
     * @throws ValidationException
     */
    public void update(TemaLaborator tema) throws  ValidationException{
        repository.update(tema);
    }

    public void prelungire(int numarTema, int saptamanaNoua) throws ValidationException{
        TemaLaborator tema = repository.findOne(numarTema);
        if(saptamanaNoua >= saptamanaCurenta -1)
            throw new ValidationException("Saptamana data nu poate fi actualizata deoarce depaseste termenul");
        tema.setDeadline(saptamanaNoua);
        repository.update(tema);
    }

    /**
     *  Filter and sort teme by deadline
     * @param deadline represents deadline given
     * @return a list with all teme that have deadline lower than @deadline and sorted by numarTema
     */
    public List<TemaLaborator> filterAndSortByDeadline(int deadline){
        List<TemaLaborator> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<TemaLaborator> alll = Filter.filterAndSorter(all, Filter.areDeadlineulMaiMic(deadline), Filter.comparatorNrTema);
        return alll;
    }

    /**
     *  Filter and sort teme by numarTema
     * @param numarTema represents numarTema given
     * @return a list with all teme that have numarTema lower than @numarTema and sorted by deadline
     */
    public List<TemaLaborator> filterAndSortByNumarTema(int numarTema){
        List<TemaLaborator> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<TemaLaborator> alll = Filter.filterAndSorter(all, Filter.esteMaiMicaSauEgalaNumarTema(numarTema), Filter.comparatorDeadline);
        return alll;
    }

    /**
     *  Filter and sort teme by deadline and numarTema
     * @param deadline represents deadline given
     * @param numarTema represents numarTema given
     * @return a list with all teme that have deadline lower than @deadline and numarTema lower than @numarTema and sorted by cerinta
     */
    public List<TemaLaborator> filterAndSortByDeadlineAndNrTema(int deadline, int numarTema){
        List<TemaLaborator> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<TemaLaborator> alll = Filter.filterAndSorter(all, Filter.deadlineMaiMicNumarTemaMaiMare(deadline, numarTema), Filter.comparatorCerinta);
        return alll;
    }
}
