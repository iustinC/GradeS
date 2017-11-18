package Service;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.Repository;
import Repository.StudentFileRepository;
import Validator.ValidationException;

import java.util.*;


public class StudentService {
    private Repository<Student, Integer> repository;
    private Repository<Nota, String> repoNote;

    public StudentService(Repository<Student, Integer> repository, Repository<Nota, String> repoNote) {
        this.repository = repository;
        this.repoNote = repoNote;
    }

    /**
     *   Add a given entry
     * @param student represents entry that is added
     * @throws ValidationException
     */
    public void add(Student student) throws ValidationException{
        repository.save(student);
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

        return repository.delete(integer);
    }

    /**
     *  Update a given entry
     * @param student represents student that is updated
     * @throws ValidationException
     */
    public void update(Student student) throws ValidationException{
        repository.update(student);
    }

    /**
     *  Filter and sort students by indrumator
     * @return a list with all students that have "Andrei" as cadruDidactic  and sorted by grupa
     */
    public List<Student> filterAndSortByIndrumator(){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.esteIndrumatorAndrei(), Filter.comparatorGrupa);
        return alll;
    }

    /**
     *  Filter and sort students by email
     * @return a list with all students that have '.ro' or '.com' in mail and sorted by nume
     */
    public List<Student> filterAndSortByMail(){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.verificareMail(), Filter.comparatorNume);
        return alll;
    }

    /**
     *  Filter and sort students by a substring
     * @param numePartial represents given substring
     * @return a list with all students that contains given substring and sort by nume
     */
    public List<Student> filterAndSortByName(String numePartial){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.verificareNume(numePartial), Filter.comparatorNume);
        return alll;
    }

    /**
     *  Filter and sort students by grupa
     * @param grupa represents grupa given
     * @return a list with all students from grupa @grupa and sorted by email
     */
    public List<Student> filterAndSortByGrupa(int grupa){
        List<Student> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Student> alll = Filter.filterAndSorter(all, Filter.verificareGrupa(grupa), Filter.comparatorEmail);
        return alll;
    }

}
