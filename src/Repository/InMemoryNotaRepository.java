package Repository;

import Domain.Nota;
import Validator.Validator;

public class InMemoryNotaRepository extends AbstractRepository<Nota, String> {
    public InMemoryNotaRepository(Validator<Nota> validator) {
        super(validator);
    }
}
