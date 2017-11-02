package Validator;

import Domain.TemaLaborator;

public class ValidatorTeme implements Validator<TemaLaborator> {
    @Override
    public void validate(TemaLaborator obj) throws ValidationException {

        String error = "";

        if(obj.getCerinta().compareTo("") == 0)
            error = error.concat("Cerinta nu poate fi nula \n");


        if(error != "")
            throw (new ValidationException(error));
    }
}
