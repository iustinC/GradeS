package Repository;

import Domain.Student;
import Validator.Validator;

public class InMemoryStudentRepository extends AbstractRepository<Student, Integer> {
    public InMemoryStudentRepository(Validator<Student> validator) {
        super(validator);
    }
}
