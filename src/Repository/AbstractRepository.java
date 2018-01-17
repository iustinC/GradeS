package Repository;

import Validator.Validator;
import Validator.ValidationException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractRepository <E extends HasID<ID>, ID> implements Repository<E, ID> {

    static final String url = "jdbc:sqlserver://localhost\\DESKTOP-HLOMB7K:1433;database=LAB13;integratedSecurity=true";
    static final String jdbc_driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static Connection conn = null;
    static Statement statement = null;

    private Map<ID, E> entities = new TreeMap<>();

    protected Validator<E> validator;

    AbstractRepository(Validator<E> validator){
        this.validator = validator;
    }

    /*
    *   Return the size of entities
    */
    @Override
    public long size() {
        return entities.size();
    }

    /**
     *  Save entity as a new entry in repository
     * @param entity
     * @return null if entity was addes
     * @return entity if entity exists in repository
     * @throws ValidationException if entity is not valid
     */
    @Override
    public E save(E entity) throws ValidationException {
        validator.validate(entity);
        if(entities.containsKey(entity.getId()))
            return entities.get(entity.getId());
        entities.put(entity.getId(),entity);
        return null;
    }

    /**
     *  Remove an element with a given ID
     * @param id
     * @return removed element
     */
    @Override
    public Optional<E> delete(ID id) {
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public E update(E entity) throws ValidationException{
        validator.validate(entity);

        if (entities.containsKey(entity.getId())) {
            entities.put(entity.getId(), entity);
            return null;
        }

       return entity;
    }
    /**
     *  Find an entry by a given ID
     * @param id
     * @return the entry that has given ID
     */
    @Override
    public E findOne(ID id) {
        return entities.get(id);
    }

    // Return all values of repository
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public List<E> getAll(){return StreamSupport.stream(entities.values().spliterator(), false).collect(Collectors.toList());}

    @Override
    public List<E> between(int index, int page){
        return null;
    }
}
