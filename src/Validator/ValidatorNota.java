package Validator;

import Domain.Nota;

public class ValidatorNota implements Validator<Nota> {
    @Override
    public void validate(Nota obj) throws ValidationException {
        String error = "";

        if(obj.getValoare() < 0 || obj.getValoare() > 10)
            error= error.concat("Nota trebuie sa fie intre 1 si 10.");

        if(error != "")
            throw new ValidationException(error);
    }
}
