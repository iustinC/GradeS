package Repository;

import Validator.ValidationException;

import java.util.List;
import java.util.Optional;

public interface Repository<E, ID> {
    long size();
    E save(E entity) throws ValidationException;
    Optional<E> delete(ID id);
    E findOne(ID id);
    E update(E entity) throws ValidationException;
    Iterable<E> findAll();
    List<E> getAll();
    List<E> between(int index, int page);
}
