package Repository;

import Domain.Student;
import Domain.TemaLaborator;
import Validator.Validator;
import Validator.ValidationException;

import java.io.*;

public class TemeFileRepository extends InMemoryTemeRepository {

    private String fileName;

    public TemeFileRepository(Validator<TemaLaborator> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        readFromFile();
    }


    private void readFromFile()  {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = in.readLine()) != null){
                String[] fields = line.split(";");

                int numarTema = Integer.parseInt(fields[0]);
                String cerintaTema = fields[1];
                int deadline = Integer.parseInt(fields[2]);

                TemaLaborator tema = new TemaLaborator(numarTema, cerintaTema, deadline);
                super.save(tema);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu a fost gasit.");
        } catch (IOException e) {
            System.out.println("I/O Error.");
        } catch (ValidationException e) {
            System.out.println("Date eronate citite.");
        }
    }

    /**
     *  Save data to file
     */
    private void saveToFile() {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName, false))) {
            super.findAll().forEach(temaLaborator -> {
                try {
                    out.write(String.valueOf(temaLaborator.getId()) + ";" +
                            temaLaborator.getCerinta() + ";" +
                            String.valueOf(temaLaborator.getDeadline()) + ";"  + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println("I/O Error");
        }
    }

    /**
     *  Save an entry
     * @param entity
     * @return entity that was saved
     * @throws ValidationException after validation of entry
     */
    @Override
    public TemaLaborator save(TemaLaborator entity) throws ValidationException {
        TemaLaborator returned = super.save(entity);
        saveToFile();
        return returned;
    }

    /**
     *  Delete an entry with a given id
     * @param integer represents id of entry
     * @return that entry that was removed
     */
    @Override
    public TemaLaborator delete(Integer integer) {
        TemaLaborator returned = super.delete(integer);
        saveToFile();
        return returned;
    }
    /**
     *  Update a given entry
     * @param entity represents new entry
     * @return the updated entry
     * @throws ValidationException if the entry doesn`t exist
     */
    @Override
    public TemaLaborator update(TemaLaborator entity) throws ValidationException {
        TemaLaborator returned = super.update(entity);
        if (returned == null ) saveToFile();
        else throw new ValidationException("Tema nu exista");
        return returned;
    }
}
