package Validator;

import Domain.Student;

public class ValidatorStudent implements Validator<Student> {
    @Override
    public void validate(Student obj) throws ValidationException {

        String error = "";

        if(!obj.getEmail().matches("[a-zA-Z1-9]+@[a-zA-Z]+\\.[a-zA-Z]+"))
            error = error.concat("Emailul nu este valid\n");

        if(!obj.getNume().matches("[a-zA-Z][a-zA-Z][ a-zA-Z]+"))
            error = error.concat("Numele nu este valid\n");

        if("".compareTo(obj.getNume()) == 1)
            error = error.concat("Numele nu poate fi vid \n");

        if(obj.getGrupa() < 221 || obj.getGrupa() > 227)
            error = error.concat("Grupa trebuie sa fie intre 221 si 227 \n");

        if(!obj.getCadruDidactic().matches("[a-zA-Z][a-zA-Z][ a-zA-Z]+"))
            error = error.concat("Numele cadrului didactic nu este valid\n");

        if("".compareTo(obj.getCadruDidactic()) == 1)
            error = error.concat("Cadrul didactic nu poate fi vid \n");

        if(error != "")
            throw (new ValidationException(error));
    }
}
