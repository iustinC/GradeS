package Repository;

import Validator.ValidationException;

public interface Repository<E, ID> {
    long size();
    E save(E entity) throws ValidationException;
    E delete(ID id);
    E findOne(ID id);
    E update(E entity) throws ValidationException;
    Iterable<E> findAll();
}
