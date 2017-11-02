package Service;

import Domain.Student;
import Repository.Repository;
import Repository.StudentFileRepository;
import Validator.ValidationException;

import java.util.HashSet;
import java.util.Set;


public class StudentService {
    private Repository<Student, Integer> repository;

    public StudentService(Repository<Student, Integer> repository) {
        this.repository = repository;
    }

    // Add a given entry
    public void add(Student student) throws ValidationException{
        repository.save(student);
    }

    // Return a set with all entries
    public Set<Student> getAll(){
        Set<Student> all = new HashSet<>();
        repository.findAll().forEach(student -> all.add(student));
        return all;
    }

    // Delete an entry by a given id
    public void delete(Integer integer){
        repository.delete(integer);
    }

    // Update a given entry
    public void update(Student student) throws ValidationException{
        repository.update(student);
    }

}
