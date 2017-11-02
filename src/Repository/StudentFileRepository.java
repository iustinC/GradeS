package Repository;

import Domain.Student;
import Validator.Validator;
import Validator.ValidationException;


import java.io.*;

public class StudentFileRepository extends InMemoryStudentRepository {

    private String fileName;

    public StudentFileRepository(Validator<Student> validator, String fileName) {
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
                String numeStudent = fields[1];
                int grupaStudent = Integer.parseInt(fields[2]);
                String emailStudent = fields[3];
                String cadruStudent = fields[4];

                Student student = new Student(idStudent, numeStudent, grupaStudent, emailStudent, cadruStudent);
                super.save(student);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu a fost gasit.");
        } catch (IOException e) {
            System.out.println("I/O Error.");
        } catch (ValidationException e) {
            System.out.println("Date eronate citite.");
        }
    }


    private void saveToFile() {/**
     *  Save data to file
     */
        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName, false))) {
            super.findAll().forEach(student -> {
                try {
                    out.write(student.getId() + ";" +
                            student.getNume() + ";" +
                            String.valueOf(student.getGrupa()) + ";" +
                            student.getEmail() + ";" +
                            student.getCadruDidactic() + "\n");
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
    public Student save(Student entity) throws ValidationException {
        Student returned = super.save(entity);
        saveToFile();
        return returned;
    }

    /**
     *  Delete an entry with a given id
     * @param integer represents id of entry
     * @return that entry that was removed
     */
    @Override
    public Student delete(Integer integer) {
        Student returned = super.delete(integer);
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
    public Student update(Student entity) throws ValidationException {
        Student returned = super.update(entity);
        if (returned == null )
            saveToFile();
        else
            throw new ValidationException("Studentul nu exista");
        return returned;
    }
}
