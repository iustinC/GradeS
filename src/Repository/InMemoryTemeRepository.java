package Repository;

import Domain.TemaLaborator;
import Validator.Validator;

public class InMemoryTemeRepository extends AbstractRepository<TemaLaborator, Integer> {
    public InMemoryTemeRepository(Validator<TemaLaborator> validator) {
        super(validator);
    }
}
