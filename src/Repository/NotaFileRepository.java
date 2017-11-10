package Repository;

import Domain.Nota;
import Validator.Validator;
import Validator.ValidationException;

import java.io.*;

public class NotaFileRepository extends InMemoryNotaRepository{

    private String fileName;

    public NotaFileRepository(Validator<Nota> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        readFromFile();
    }

    /**
     *  Read data from file
     */
    private void readFromFile(){
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = in.readLine()) != null){
                String[] fields = line.split(";");
                int idStudent = Integer.parseInt(fields[0]);
                int numarTema = Integer.parseInt(fields[1]);
                int valoareNota = Integer.parseInt(fields[2]);

                Nota nota = new Nota(idStudent, numarTema, valoareNota);
                super.save(nota);
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
            super.findAll().forEach(nota -> {
                try {
                    out.write(String.valueOf(nota.getIdStudent()) + ";" +
                            String.valueOf(nota.getNumarTema()) + ";" +
                            String.valueOf(nota.getValoare())+ "\n");
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
    public Nota save(Nota entity) throws ValidationException {
        Nota returned = super.save(entity);
        saveToFile();
        return returned;
    }

    public static void metoda(){

    }
    /**
     *  Delete an entry with a given id
     * @param idNota represents id of entry
     * @return that entry that was removed
     */
    @Override
    public Nota delete(String idNota) {
        Nota returned = super.delete(idNota);
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
    public Nota update(Nota entity) throws ValidationException {
        Nota returned = super.update(entity);
        if (returned == null )
            saveToFile();
        else
            throw new ValidationException("Studentul nu exista");
        return returned;
    }

}
