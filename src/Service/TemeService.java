package Service;

import Domain.TemaLaborator;
import Repository.Repository;
import Validator.ValidationException;

import java.util.HashSet;
import java.util.Set;

public class TemeService {

    public static int saptamanaCurenta = 5;
    private Repository<TemaLaborator, Integer> repository;

    public TemeService(Repository<TemaLaborator, Integer> repository) {
        this.repository = repository;
    }

    // Add a given entry
    public void add(TemaLaborator tema) throws ValidationException{
        repository.save(tema);
    }

    // Delete an entry by a given id
    public void delete(Integer integer){
        repository.delete(integer);
    }

    // Return a set with all entries
    public Set<TemaLaborator> getAll(){
        Set<TemaLaborator> all = new HashSet<>();
        repository.findAll().forEach(temaLaborator -> all.add(temaLaborator));
        return all;
    }

    // Update a given entry
    public void update(TemaLaborator tema) throws  ValidationException{
        repository.update(tema);
    }

    public void prelungire(TemaLaborator tema, int saptamanaNoua) throws ValidationException{
        if(saptamanaNoua >= saptamanaCurenta -1)
            throw new ValidationException("Saptamana data nu poate fi actualizata deoarce depaseste termenul");
        tema.setDeadline(saptamanaNoua);
        repository.update(tema);
    }
}
