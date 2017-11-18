package Repository;

import Domain.Nota;
import Validator.Validator;
import Validator.ValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

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
        Path path = Paths.get("Catalog.txt");
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(line ->{
                String[] fields = line.split(";");
                int idStudent = Integer.parseInt(fields[0]);
                int numarTema = Integer.parseInt(fields[1]);
                int valoareNota = Integer.parseInt(fields[2]);

                Nota nota = new Nota(idStudent, numarTema, valoareNota);
                try{
                    super.save(nota);
                }catch (ValidationException e){
                    System.out.println(e.getMessage());
                }
            });
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu a fost gasit.");
        } catch (IOException e) {
            System.out.println("I/O Error.");
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
    public Optional<Nota> delete(String idNota) {
        Optional<Nota> ret = super.delete(idNota);
        if(ret.isPresent()){
            Optional<Nota> returned = ret;
            saveToFile();
            return returned;
        }
        return Optional.empty();
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
