package Validator;

import Domain.Student;

public class ValidatorStudent implements Validator<Student> {
    @Override
    public void validate(Student obj) throws ValidationException {

        String error = "";

        if(obj.getNume().compareTo("") == 0)
            error = error.concat("Numele nu poate fi vid \n");

        if(obj.getGrupa() < 220 || obj.getGrupa() > 227)
            error = error.concat("Grupa trebuie sa fie intre 220 si 227 \n");

        if(error != "")
            throw (new ValidationException(error));
    }
}
