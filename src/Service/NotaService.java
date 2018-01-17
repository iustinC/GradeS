package Service;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.Repository;
import Utils.ListEvent;
import Utils.ListEventType;
import Utils.Observable;
import Utils.Observer;
import Validator.ValidationException;
import Service.Filter;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class NotaService implements Observable<Nota> {

    private int saptamanaCurenta;

    private Repository<Nota, String> repository;

    private Repository<Student, Integer> repositoryStudent;

    private Repository<TemaLaborator, Integer> repositoryTeme;

    private ArrayList<Observer<Nota>> noteObservs = new ArrayList<>();

    public NotaService(Repository<Nota, String> repository, Repository<Student, Integer> repoStud, Repository<TemaLaborator, Integer> repoTeme) {
        this.repository = repository;
        this.repositoryStudent = repoStud;
        this.repositoryTeme = repoTeme;
    }

    /**
     *  Add a given entry
     */
    public Nota add(Nota nota,String observatii) throws ValidationException {
        //if(repositoryStudent.findOne(nota.getIdStudent()) == null)
        //    throw  new ValidationException("Studentul nu exista. \n");
       // if(repositoryTeme.findOne(nota.getNumarTema()) == null)
        //    throw new ValidationException("Tema nu exista. \n");
        saptamanaCurenta = nota.getSaptPredarii();
        if(repository.findOne(String.valueOf(nota.getIdStudent()) + " " +String.valueOf(nota.getNumarTema())) != null)
            throw new ValidationException("Studentul are deja nota la laboratorul acesta. \n");

        TemaLaborator tema = repositoryTeme.findOne(nota.getNumarTema());

        if(tema.getDeadline() - saptamanaCurenta < -2)
            nota.setValoare(1);
        else
            if(tema.getDeadline() - saptamanaCurenta < 0)
                nota.setValoare(nota.getValoare() - (saptamanaCurenta - tema.getDeadline()) * 2);
        sendMail(repositoryStudent.findOne(nota.getIdStudent()).getEmail(),"Adaugare nota", "A fost adaugata nota " + String.valueOf(nota.getValoare()));
        saveIdStudent(nota.getIdStudent(), tema.getNumarTema(), nota.getValoare(), tema.getDeadline(), saptamanaCurenta, observatii);
        Nota saved = repository.save(nota);
        if(saved == null){
            ListEvent<Nota> event = createEvent(ListEventType.ADD, saved, repository.findAll());
            notifyObservers(event);
        }
        return saved;
    }

    public int getMedieForStudent(int idStudent){
        int numarNote = 0;
        int media = 0;
        for(Nota nota : repository.getAll()){
            if(nota.getIdStudent() == idStudent){
                media+= nota.getValoare();
                numarNote++;
            }
        }
        if(media == 0 && numarNote == 0)
            return 0;
        return media/numarNote;
    }

    public Map<Student, Integer> notaLaboratorStudent(){
        Map<Student, Integer> medii = new HashMap<>();
        for(Student st : repositoryStudent.getAll()){
            medii.put(st, getMedieForStudent(st.getId()));
        }
        return medii;
    }

    public int catiAuMedia(int media){
        int number = 0;
        for(Map.Entry<Student, Integer> entry : notaLaboratorStudent().entrySet())
            if(entry.getValue() == media)
                number++;
        return number;
    }


    public int numarIntarziati(int numarTema, int deadline ){
        int intarziati = 0;
        for(Nota nota : repository.getAll()){
            if(nota.getNumarTema() == numarTema){
                if(nota.getSaptPredarii() - deadline < 3 && nota.getSaptPredarii() - deadline > 0)
                    intarziati++;
            }
        }
        return intarziati;
    }

    public Map<TemaLaborator, Integer> ceaMaiGreaTema(){
        Map<TemaLaborator, Integer> map = new HashMap<>();
        for(TemaLaborator tema : repositoryTeme.getAll()){
                map.put(tema,numarIntarziati(tema.getNumarTema(), tema.getDeadline()));
        }
        return map;
    }

    public static void sendMail(String to, String subject, String text){
        new Thread(()->{
            Email email = new EmailBuilder()
                    .from("Test","17test38@gmail.com")
                    .to(to)
                    .subject(subject)
                    .text(text)
                    .build();
            String username = "17test38@gmail.com";
            String password = "test1997";
            new Mailer("smtp.gmail.com",587,username, password, TransportStrategy.SMTP_TLS).sendMail(email);
        }).start();
    }

    public Map<Student, Integer> potIntraInExamen(){
        Map<Student, Integer> studenti = new HashMap<>();
        studenti = notaLaboratorStudent();
        Map<Student, Integer> fericitii=  new HashMap<>();
        for(HashMap.Entry<Student, Integer> select : studenti.entrySet()){
            if(select.getValue() >= 5)
                fericitii.put(select.getKey(), select.getValue());
        }
        return fericitii;
    }

    public boolean aPredatLaTimp(int idStudent){
        boolean ok = false;
        for(Nota nota : repository.getAll()){
            if(nota.getIdStudent() == idStudent) {
                ok = true;
                if (repositoryTeme.findOne(nota.getNumarTema()).getDeadline() < nota.getSaptPredarii())
                    return false;
            }
        }
        return (ok == true);
    }

    public List<String> auPredatLaTimpTemele(){
       List<String> integralisti = new ArrayList<>();
        for(Student st : repositoryStudent.getAll()){
            if(aPredatLaTimp(st.getId()))
                integralisti.add(st.getNume());
        }
        return integralisti;
    }

    public int mediaGrupa(int grupa){
        int media = 0;
        int numarNote = 0;
        for(Nota nota : repository.getAll()){
            if(repositoryStudent.findOne(nota.getIdStudent()).getGrupa() == grupa){
                media+=getMedieForStudent(nota.getIdStudent());
                numarNote++;
            }
        }
        if(media == 0 && numarNote == 0)
            return 0;
        return media/numarNote;
    }
    public Map<Integer, Integer> mediaPeGrupe(){
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 221; i < 228; i++){
            map.put(i,mediaGrupa(i));
        }
        return map;
    }

    public Student getStudentByMail(String email){
        for(Student st : repositoryStudent.getAll()){
            if(st.getEmail().equals(email))
                return st;
        }
        return null;
    }

    public List<Student> getAllStuds(){
        //return StreamSupport.stream(repositoryStudent.findAll().spliterator(), false).collect(Collectors.toList());
        return repositoryStudent.getAll();
    }

    public List<TemaLaborator> getAllTeme(){
        //return StreamSupport.stream(repositoryTeme.findAll().spliterator(), false).collect(Collectors.toList());
        return repositoryTeme.getAll();
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
                out.write("Adaugare nota;" +
                            String.valueOf(numarTema) + ";" +
                            String.valueOf(nota) + ";" +
                            String.valueOf(deadline) + ";" +
                            String.valueOf(saptamanaPredarii) + ";" +
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
                out.write("Modificare nota;" +
                        String.valueOf(numarTema) + ";" +
                        String.valueOf(nota) + ";" +
                        String.valueOf(deadline) + ";" +
                        String.valueOf(saptamanaPredarii) + ";" +
                        observatii + "\n");
            }catch (IOException e) {
                e.printStackTrace();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return a set with all entries
     */
    public Set<Nota> getAll(){
        Set<Nota> all = new HashSet<>();
        repository.findAll().forEach(nota -> all.add(nota));
        return all;
    }

    /**
     *  Delete an entry by a given id
     * @param idNota represents idNota given
     */
    public Optional<Nota> delete(String idNota){
        Optional<Nota> deleted = repository.delete(idNota);
        if(deleted.isPresent()){
            ListEvent<Nota> event = createEvent(ListEventType.REMOVE, deleted.get(), repository.findAll());
            notifyObservers(event);
        }
        return deleted;
    }

    /**
     *  Find if a student has a nota
     * @param id represents id of student
     * @return  true if student exists,
     *          false, else
     */
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

    public boolean findTema(int numarTema){
        Set<Nota> all = this.getAll();
        for(Nota nota : all){
            String not = nota.getId();
            String[] strings = not.split(" ");
            if(Integer.valueOf(strings[1]) == numarTema )
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
            Nota updated = repository.update(nota);
            sendMail(repositoryStudent.findOne(nota.getIdStudent()).getEmail(),"Modificare nota", "A fost modificata nota " + nota.getValoare());
            ListEvent<Nota> event = createEvent(ListEventType.UPDATE, updated, repository.findAll());
            notifyObservers(event);
            TemaLaborator tema = repositoryTeme.findOne(nota.getNumarTema());
            updateIdStudent(nota.getIdStudent(), tema.getNumarTema(), nota.getValoare(), tema.getDeadline(), saptamanaCurenta, observatii);
        }
    }

    /**
     *  Filter and sort note with valoare lower than a given one
     * @param valoare represents valoare given
     * @return a list that contains all note lower than @valoare and sorted by valoare
     */
    public List<Nota> filterAndSortByValue(int valoare){
        List<Nota> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Nota> alll = Filter.filterAndSorter(all, Filter.areValoareaMaiMica(valoare), Filter.comparatorValoareNota);
        return alll;
    }

    /**
     *  Filter and sort note with a given numarTema
     * @param numarTema represents numarTema given
     * @return a list that contains all note with given numarTema and sorted by IdStudent
     */
    public List<Nota> filterAndSortByNumarTema(int numarTema){
        List<Nota> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Nota> alll = Filter.filterAndSorter(all, Filter.toateNoteleDeLaTema(numarTema), Filter.comparatorIdStudentNota);
        return alll;
    }

    /**
     *  Filter and sort note with a given idStudent
     * @param idStudent represents idStudent given
     * @return a list that contains all note with a given idStudent and sorted by valoare
     */
    public List<Nota> filterAndSortByIdStudent(int idStudent){
        List<Nota> all = new ArrayList<>();
        all.addAll(this.getAll());
        List<Nota> alll = Filter.filterAndSorter(all, Filter.noteleUnuiStudent(idStudent), Filter.comparatorValoareNota);
        return alll;
    }
    /**
     *  Add a specified observer
     * @param o represents the observer
     */
    @Override
    public void addObserver(Observer<Nota> o) {
        noteObservs.add(o);
    }

    /**
     *  Remove a specified observer
     * @param o represents the observer
     */
    @Override
    public void removeObserver(Observer<Nota> o) {
        noteObservs.add(o);
    }

    /**
     *  Notify all observers
     * @param event represents a list with events to be updated in observers
     */
    @Override
    public void notifyObservers(ListEvent<Nota> event) {
        noteObservs.forEach(temaLaboratorObserver -> temaLaboratorObserver.notifyEvent(event));
    }

    /**
     *  Create an event of last modification
     * @param type represents type of event
     * @param elem
     * @param l
     * @param <E>
     * @return a new event with last modification
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l) {
        return new ListEvent<E>(type){
            @Override
            public Iterable<E> getList(){return l;}
            @Override
            public E getElement(){return elem;}
        };
    }

    public List<Nota> getAllNote(){
       // return StreamSupport.stream(repository.findAll().spliterator(),false).collect(Collectors.toList());
        return repository.getAll();
    }

    public List<Nota> getAllNoteForStudent(String email){
        List<Nota> note = new ArrayList<>();
        int id = -1;
        for(Student st : repositoryStudent.getAll())
            if(st.getEmail().equals(email)){
                id = st.getId();
                break;
        }
        for(Nota nt : repository.getAll())
            if(nt.getIdStudent() == id)
                note.add(nt);
        return note;
    }

    public String getObservatiiForStudent(String email, int nota){
        String obs = "";
        int id = -1;
        for(Student st : repositoryStudent.getAll())
            if(st.getEmail().equals(email)){
                id = st.getId();
                break;
            }
        BufferedReader bf = null;
        FileReader fl = null;
        try{
            fl = new FileReader("C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Students\\" + id + ".txt");
            bf = new BufferedReader(fl);

            String currentLine;

            while((currentLine = bf.readLine()) != null){
                String[] fields = currentLine.split(";");
                if(Integer.valueOf(fields[2]) == nota)
                    obs+=fields[5];
            }

        }catch (FileNotFoundException e){
            System.out.println("fiiser negasit");
        }catch (IOException e){
            System.out.println("io exception");
        }
        return obs;
    }

    public List<Nota> getBetween(int index, int page){return repository.between(index, page);}
    public List<Student> getBetweenStudent(int index, int page){
        return repositoryStudent.between(index, page);
    }
    public List<TemaLaborator> getBetweenTeme(int index, int page){
        return  repositoryTeme.between(index, page);
    }
}

