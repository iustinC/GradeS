package Service;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.Repository;
import Validator.ValidationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NotaService {

    private int saptamanaCurenta = 5;

    private Repository<Nota, String> repository;

    private Repository<Student, Integer> repositoryStudent;

    private Repository<TemaLaborator, Integer> repositoryTeme;

    public NotaService(Repository<Nota, String> repository, Repository<Student, Integer> repoStud, Repository<TemaLaborator, Integer> repoTeme) {
        this.repository = repository;
        this.repositoryStudent = repoStud;
        this.repositoryTeme = repoTeme;
    }

    // Add a given entry
    public void add(Nota nota,String observatii) throws ValidationException {
        if(repositoryStudent.findOne(nota.getIdStudent()) == null)
            throw  new ValidationException("Studentul nu exista. \n");
        if(repositoryTeme.findOne(nota.getNumarTema()) == null)
            throw new ValidationException("Tema nu exista. \n");
        if(repository.findOne(String.valueOf(nota.getIdStudent()) + " " +String.valueOf(nota.getNumarTema())) != null)
            throw new ValidationException("Studentul are deja nota la laboratorul acesta. \n");

        TemaLaborator tema = repositoryTeme.findOne(nota.getNumarTema());

        if(tema.getDeadline() - saptamanaCurenta < -2)
            nota.setValoare(1);
        else
            if(tema.getDeadline() - saptamanaCurenta < 0)
                nota.setValoare(nota.getValoare() - (saptamanaCurenta - tema.getDeadline()) * 2);

        saveIdStudent(nota.getIdStudent(), tema.getNumarTema(), nota.getValoare(), tema.getDeadline(), saptamanaCurenta, observatii);
        repository.save(nota);
    }
    /**
     *  Writes in a file with id student as name modifications about his grades
     *
     * @param idStudent represents name of file
     * @param numarTema represents number of tema
     * @param nota  represents value of nota
     * @param deadline represents deadline of nota
     * @param saptamanaPredarii represents saptamana predarii of nota
     * @param observatii obsrvations about note
     */
    public void saveIdStudent(int idStudent, int numarTema,int nota, int deadline, int saptamanaPredarii, String observatii) {
        String pathFile = "C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Students\\" + idStudent + ".txt";
        File file = new File(pathFile);
        try {
            if (!file.exists())
                file.createNewFile();

            try(BufferedWriter out = new BufferedWriter(new FileWriter(pathFile, true))){
                out.write("Adaugare nota, " +
                            String.valueOf(numarTema) + ", " +
                            String.valueOf(nota) + ", " +
                            String.valueOf(deadline) + ", " +
                            String.valueOf(saptamanaPredarii) + ", " +
                            observatii + "\n");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *  Writes in a file with id student as name modifications about his grades
     *
     * @param idStudent represents name of file
     * @param numarTema represents number of tema
     * @param nota  represents value of nota
     * @param deadline represents deadline of nota
     * @param saptamanaPredarii represents saptamana predarii of nota
     * @param observatii obsrvations about note
     */
    public void updateIdStudent(int idStudent, int numarTema,int nota, int deadline, int saptamanaPredarii, String observatii) {
        String pathFile = "C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Students\\" + idStudent + ".txt";
        File file = new File(pathFile);
        try {
            if (!file.exists())
                file.createNewFile();

            try(BufferedWriter out = new BufferedWriter(new FileWriter(pathFile, true))){
                out.write("Modificare nota, " +
                        String.valueOf(numarTema) + ", " +
                        String.valueOf(nota) + ", " +
                        String.valueOf(deadline) + ", " +
                        String.valueOf(saptamanaPredarii) + ", " +
                        observatii + "\n");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    // Return a set with all entries
    public Set<Nota> getAll(){
        Set<Nota> all = new HashSet<>();
        repository.findAll().forEach(nota -> all.add(nota));
        return all;
    }

    // Delete an entry by a given id
    public void delete(String idNota){
        repository.delete(idNota);
    }

    public boolean findStud(String id){
        Set<Nota> all = this.getAll();
        for(Nota nota : all){
            String not = nota.getId();
            String[] strings = not.split(" ");
            if(strings[0].equals(id) )
                return true;

        }
        return false;
    }
    /**
     *  Update a given entry
     * @param nota represents new nota
     * @param observatii represents observatii of nota
     * @throws ValidationException if student or tema de laborator doesn`t exist
     */
    public void update(Nota nota, String observatii) throws ValidationException{
        if(repositoryStudent.findOne(nota.getIdStudent()) == null)
            throw  new ValidationException("Studentul nu exista. \n");
        if(repositoryTeme.findOne(nota.getNumarTema()) == null)
            throw new ValidationException("Tema nu exista. \n");
        if(repository.findOne(String.valueOf(nota.getIdStudent()) + " " +String.valueOf(nota.getNumarTema())) == null)
            throw new ValidationException("Studentul nu are nota la laboratorul acesta. \n");

        Nota notaCurenta = repository.findOne(nota.getId());

        if(notaCurenta.getValoare() < nota.getValoare()){
            TemaLaborator tema = repositoryTeme.findOne(nota.getNumarTema());
            updateIdStudent(nota.getIdStudent(), tema.getNumarTema(), nota.getValoare(), tema.getDeadline(), saptamanaCurenta, observatii);
            repository.update(nota);
        }

    }

}

