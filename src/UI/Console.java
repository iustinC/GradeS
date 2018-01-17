package UI;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Repository.NotaFileRepository;
import Repository.StudentFileRepository;
import Repository.TemeFileRepository;
import Service.NotaService;
import Service.StudentService;
import Service.TemeService;
import UI.Menu.Command;
import UI.Menu.MenuCommand;
import Validator.Validator;
import Validator.ValidatorTeme;
import Validator.ValidatorStudent;
import Validator.ValidationException;
import Validator.ValidatorNota;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
/*
public class Console {

    private StudentFileRepository repoStud = new StudentFileRepository(new ValidatorStudent(),"Student.txt");
    private TemeFileRepository repoTeme = new TemeFileRepository(new ValidatorTeme(),"Teme.txt");
    private NotaFileRepository repoNote = new NotaFileRepository(new ValidatorNota(), "Catalog.txt");

    private StudentService controllerStudent = new StudentService(repoStud, repoNote);
    private TemeService controllerTeme = new TemeService(repoTeme);
    private NotaService controllerNote = new NotaService(repoNote,repoStud,repoTeme);

    private MenuCommand mainMenu;

    Scanner scanner = new Scanner(System.in);

    public class ReadStudentCommand implements Command{
        @Override
        public void execute() {
            int idStudent, grupaStudent;
            String nume, email, cadruDidactic;

            System.out.print("Introduceti  id student = ");
            idStudent = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduceti nume student = ");
            nume=scanner.nextLine();
            System.out.println("Intoduceti grupa studentului = ");
            grupaStudent = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Introduceti email-ul studentului = ");
            email = scanner.nextLine();
            System.out.println("Introduceti cadrul didactic al studentului = ");
            cadruDidactic = scanner.nextLine();
            try {
                controllerStudent.add(new Student(idStudent, nume, grupaStudent, email, cadruDidactic));
                System.out.println("S-a citit un student ");
            }catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public class ReadTemaCommand implements Command{
        @Override
        public void execute() {
            int numarTema, deadline;
            String cerinta;

            System.out.print("Introduceti  numar tema =");
            numarTema = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduceti cerinta tema =");
            cerinta = scanner.nextLine();
            System.out.println("Intoduceti deadline tema =");
            deadline = scanner.nextInt();

            try {
                controllerTeme.add(new TemaLaborator(numarTema, cerinta, deadline));
                System.out.println("S-a citit o tema ");
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }

        }
    }

    public class ReadNotaCommand implements Command{
        @Override
        public void execute() {
            int idStudent, numarTema, nota;
            String observatii;

            System.out.print("Introduceti  id-ul studentului =");
            idStudent = scanner.nextInt();
            System.out.print("Introduceti numar tema =");
            numarTema = scanner.nextInt();
            System.out.println("Intoduceti nota =");
            nota = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Introduceti observatii(optional) =");
            observatii = scanner.nextLine();
            if(observatii.equals(""))
                observatii = "null";

            try {
                controllerNote.add(new Nota(idStudent, numarTema, nota), observatii);
                System.out.println("S-a citit o nota ");
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private class UpdateStudentCommand implements Command {
        @Override
        public void execute() {
            int idStudent, grupaStudent;
            String nume, email, cadruDidactic;

            System.out.print("Introduceti  id student = ");
            idStudent = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduceti nume student = ");
            nume=scanner.nextLine();
            System.out.println("Intoduceti grupa studentului = ");
            grupaStudent = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Introduceti email-ul studentului = ");
            email = scanner.nextLine();
            System.out.println("Introduceti cadrul didactic al studentului = ");
            cadruDidactic = scanner.nextLine();

            try {
                controllerStudent.update(new Student(idStudent, nume, grupaStudent, email, cadruDidactic));
                System.out.println("S-a updatat un student ");
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public class UpdateTemaCommand implements Command{
        @Override
        public void execute() {
            int numarTema, deadline;
            String cerinta;

            System.out.print("Introduceti  numar tema =");
            numarTema = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduceti cerinta tema =");
            cerinta = scanner.nextLine();
            System.out.println("Intoduceti deadline tema =");
            deadline = scanner.nextInt();

            try {
                controllerTeme.update(new TemaLaborator(numarTema, cerinta, deadline));
                System.out.println("S-a updatat o tema ");
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public class UpdateNotaCommand implements Command{
        @Override
        public void execute() {
            int idStudent, numarTema, nota;
            String observatii;

            System.out.print("Introduceti  id-ul studentului =");
            idStudent = scanner.nextInt();
            System.out.print("Introduceti numar tema =");
            numarTema = scanner.nextInt();
            System.out.println("Intoduceti nota =");
            nota = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Introduceti observatii(optional) =");
            observatii = scanner.nextLine();
            if(observatii.equals(""))
                observatii = "null";

            try {
                controllerNote.update(new Nota(idStudent, numarTema, nota), observatii);
                System.out.println("S-a updatat o nota ");
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private class DeleteTemaCommand implements Command {
        @Override
        public void execute() {
            int numarTema;

            System.out.print("Introduceti  numar tema =");
            numarTema = scanner.nextInt();

            Optional<TemaLaborator> tm = controllerTeme.delete(numarTema);
            if(tm.isPresent())
                System.out.println("S-a sters  tema ");
            else
                System.out.println("Nota nu exista");

        }
    }

    private class DeleteNotaCommand implements Command {
        @Override
        public void execute() {
            int idStudent, numarTema;

            System.out.print("Introduceti  id student = ");
            idStudent = scanner.nextInt();
            System.out.println("Introduceti numar tema = ");
            numarTema = scanner.nextInt();

            Optional<Nota> nt = controllerNote.delete(String.valueOf(idStudent) + " " + String.valueOf(numarTema));
            if(nt.isPresent())
                System.out.println("S-a sters  nota ");
            else
                System.out.println("Nota nu exista ");

        }
    }

    private class DeleteStudentCommand implements Command {
        @Override
        public void execute() {
            int idStudent, grupaStudent;
            String nume, email, cadruDidactic;

            System.out.print("Introduceti  id student = ");
            idStudent = scanner.nextInt();

            if(controllerNote.findStud(String.valueOf(idStudent)) == true)
                System.out.println("Studentul are note nu se poate sterge \n");
            else {
                Optional<Student> st = controllerStudent.delete(idStudent);
                if(st.isPresent())
                    System.out.println("Studentul a fost sters. \n");
                else
                    System.out.println("Studentul nu exista \n");
            }
        }
    }
    public class PrelungireTemaCommand implements Command{
        @Override
        public void execute() {
            int numarTema;
            int saptamanaNoua;

            System.out.println("Introduceti numar tema = ");
            numarTema = scanner.nextInt();
            System.out.println("Introduceti saptamana noua = ");
            saptamanaNoua = scanner.nextInt();
            try{
                controllerTeme.prelungire(numarTema, saptamanaNoua);
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public class GetAllNoteCommand implements Command {
        @Override
        public void execute() {
            Set<Nota> all = controllerNote.getAll();
            all.forEach(nota -> System.out.println(nota));
        }
    }
    public class GetAllStudentCommand implements Command {
        @Override
        public void execute() {
            Set<Student> all = controllerStudent.getAll();
            all.forEach(student -> System.out.println(student));
        }
    }

    public class GetAllTemeCommand implements Command {
        @Override
        public void execute() {
            Set<TemaLaborator> all = controllerTeme.getAll();
            all.forEach(tema -> System.out.println(tema));
        }
    }

    public class GetAllNoteFilteredByValoare implements Command{
        @Override
        public void execute() {
            int valoareNota;
            System.out.println("Introduceti valoare nota = ");
            valoareNota = scanner.nextInt();
            List<Nota> all = controllerNote.filterAndSortByValue(valoareNota);
            System.out.println(all);
        }
    }

    public class GetAllNoteFilteredByNumarTema implements Command{
        @Override
        public void execute() {
            int numarTema;
            System.out.println("Introduceti numar tema = ");
            numarTema = scanner.nextInt();
            List<Nota> all = controllerNote.filterAndSortByNumarTema(numarTema);
            System.out.println(all);
        }
    }

    public class GetAllNoteFilteredIdStudent implements Command{
        @Override
        public void execute() {
            int idStudent;
            System.out.println("Introduceti id student = ");
            idStudent = scanner.nextInt();
            List<Nota> all = controllerNote.filterAndSortByIdStudent(idStudent);
            System.out.println(all);
        }
    }
    public class GetAllTemeFilteredByDAndNr implements Command{
        @Override
        public void execute() {
            int deadline, numarTema;
            System.out.println("Introduceti deadline = ");
            deadline = scanner.nextInt();
            System.out.println("Introduceti numar tema = ");
            numarTema = scanner.nextInt();
            List<TemaLaborator> all = controllerTeme.filterAndSortByDeadlineAndNrTema(deadline,numarTema);
            System.out.println(all);
        }
    }

    public class GetAllTemeilteredByNumarTema implements Command{
        @Override
        public void execute() {
            int numarTema;
            System.out.println("Introduceti numar tema = ");
            numarTema = scanner.nextInt();
            List<TemaLaborator> all = controllerTeme.filterAndSortByNumarTema(numarTema);
            System.out.println(all);
        }
    }

    public class GetAllTemeFilteredByDeadline implements Command{
        @Override
        public void execute() {
            int deadline;
            System.out.println("Introduceti deadline = ");
            deadline = scanner.nextInt();
            List<TemaLaborator> all = controllerTeme.filterAndSortByDeadline(deadline);
            System.out.println(all);
        }
    }

    public class GetAllStudentsFilteredByIndrumator implements Command{
        @Override
        public void execute() {
            List<Student> all = controllerStudent.filterAndSortByIndrumator();
            System.out.println(all);
        }
    }

    public class GetAllStudentsFilteredByMail implements Command{
        @Override
        public void execute() {
            List<Student> all = controllerStudent.filterAndSortByMail();
            System.out.println(all);
        }
    }

    public class GetAllStudentsFilteredByGrupa implements Command{
        @Override
        public void execute() {
            int grupa;
            System.out.println("Introduceti grupa = ");
            grupa = scanner.nextInt();
            List<Student> all = controllerStudent.filterAndSortByGrupa(grupa);
            System.out.println(all);
        }
    }

    public class GetAllStudentsFilteredByNume implements  Command{
        @Override
        public void execute() {
            String nume;
            System.out.println("Introduceti nume partial = ");
            scanner.nextLine();
            nume = scanner.nextLine();
            List<Student> all = controllerStudent.filterAndSortByName(nume);
            System.out.println(all);
        }
    }
    private void createMenu()
    {
        mainMenu = new MenuCommand(" Meniu Principal");
        MenuCommand crudStudent =new MenuCommand("Operatii CRUD Student");
        MenuCommand crudTema = new MenuCommand("Operatii CRUD Tema de laborator");
        MenuCommand crudNota = new MenuCommand("Operatii CRUD Note");

        crudStudent.addCommand("0. Back to main menu " , mainMenu);
        crudStudent.addCommand("1. Add Student" , new ReadStudentCommand());
        crudStudent.addCommand("2. Update Student" , new UpdateStudentCommand());
        crudStudent.addCommand("3. Delete Student", new DeleteStudentCommand());
        crudStudent.addCommand("4. Get all", new GetAllStudentCommand());
        crudStudent.addCommand("5. Filter by indrumator", new GetAllStudentsFilteredByIndrumator());
        crudStudent.addCommand("6. Filter by mail", new GetAllStudentsFilteredByMail());
        crudStudent.addCommand("7. Filter by grupa", new GetAllStudentsFilteredByGrupa());
        crudStudent.addCommand("8. Filter by nume", new GetAllStudentsFilteredByNume());


        crudTema.addCommand("0. Back to main menu", mainMenu);
        crudTema.addCommand("1. Add Tema de laborator", new ReadTemaCommand());
        crudTema.addCommand("2. Update Tema de laborator", new UpdateTemaCommand());
        crudTema.addCommand("3. Delete Tema de laborator", new DeleteTemaCommand());
        crudTema.addCommand("4. Prelungire nota", new PrelungireTemaCommand());
        crudTema.addCommand("5. Get all", new GetAllTemeCommand());
        crudTema.addCommand("6. Filter by deadline", new GetAllTemeFilteredByDeadline());
        crudTema.addCommand("7. Filter by numarTema", new GetAllTemeilteredByNumarTema());
        crudTema.addCommand("8. Filter by deadline and numarTema", new GetAllTemeFilteredByDAndNr());


        crudNota.addCommand("0. Back to main menu", mainMenu);
        crudNota.addCommand("1. Add Nota", new ReadNotaCommand());
        crudNota.addCommand("2. Update nota", new UpdateNotaCommand());
        crudNota.addCommand("3. Delete nota", new DeleteNotaCommand());
        crudNota.addCommand("4. Get all", new GetAllNoteCommand());
        crudNota.addCommand("5. Filter by valoare", new GetAllNoteFilteredByValoare());
        crudNota.addCommand("6. Filter by numarTema", new GetAllNoteFilteredByNumarTema());
        crudNota.addCommand("7. Filter by idStudent", new GetAllNoteFilteredIdStudent());


        mainMenu.addCommand("0. Exit" ,()-> {System.exit(0);});
        mainMenu.addCommand("1. Crud Student", crudStudent);
        mainMenu.addCommand("2. Crud Tema de laborator", crudTema);
        mainMenu.addCommand("3. Crud Nota", crudNota);


        //mainMenu.addCommand("3 Exit" , new ExitCommand());
    }

    public void runMenu() {
        createMenu();
        MenuCommand crtMenu = mainMenu;
        while (true) {
            System.out.println(crtMenu.getMenuName());
            System.out.println("-----------------------");
            crtMenu.execute();
            System.out.println("Optiunea d-voastra >>");
            int actionNumber = scanner.nextInt();
            if (actionNumber > -1 && actionNumber <= crtMenu.getCommands().size()) {
                Command selectedCommand = crtMenu.getCommands().get(actionNumber);
                if (selectedCommand instanceof MenuCommand)
                    crtMenu = (MenuCommand) selectedCommand;
                else selectedCommand.execute();
            } else System.out.println("Optiunea nu este valida!");
        }
    }





    public Console() {

    }



   /* private void generateTeme() {
        try{
            controllerTeme.add(new TemaLaborator(5,"cmmdc",4));
            controllerTeme.add(new TemaLaborator(8,"cmmmc",2));
            controllerTeme.add(new TemaLaborator(9,"UI + repo",7));
            controllerTeme.add(new TemaLaborator(19,"2 progra,e",10));
            controllerTeme.add(new TemaLaborator(19,"",10));

        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void generateStudenti() {
        try{
            controllerStudent.add(new Student(15,"Andrei",225,"andrei@gmail.com","ioan"));
            controllerStudent.add(new Student(7,"CRISTI",225,"andrei@gmail.com","ioan"));
            controllerStudent.add(new Student(4,"Alex",222,"alex@gmail.com","bogdan"));
            controllerStudent.add(new Student(1,"anghel",221,"anghel@gmail.com","gabi"));
            controllerStudent.add(new Student(16,"George",227,"george@gmail.com","mircea"));
            controllerStudent.add(new Student(9,"",219,"sergiu@gmail.com","alex"));
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }

    }

    public void afisareStudenti(){
        controllerStudent.getAll().forEach(student -> System.out.println(student));
        System.out.println();
    }

    public void afisareTeme(){
        controllerTeme.getAll().forEach(temaLaborator -> System.out.println(temaLaborator));
        System.out.println();
    }

    public void cleanFiles() throws FileNotFoundException{
        PrintWriter pw = new PrintWriter("Student.txt");
        pw.close();

        PrintWriter pw2 = new PrintWriter("Teme.txt");
        pw2.close();
    }

    public void deleteStudenti(){
        controllerStudent.delete(1);
    }

    public void deleteTeme(){
        controllerTeme.delete(9);
    }

    public void updateStudent(){
        try {
            controllerStudent.update(new Student(7, "CRISTIUPDATAT", 223, "emailUpdatat", "cadruUpdatat"));
            controllerStudent.update(new Student(4, "numeUpdatat", 229, "emailUpdatat", "cadruUpdatat"));
            controllerStudent.update(new Student(99, "numeUpdatat", 229, "emailUpdatat", "cadruUpdatat"));
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateTeme(){
        try {
            controllerTeme.update(new TemaLaborator(5, "cerintaUpdatata", 2));
            controllerTeme.update(new TemaLaborator(4, "cerintaUpdatata", 2));
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void prelungireTema(){
        try{
            controllerTeme.prelungire(new TemaLaborator(8,"cmmmc",2),3);
            controllerTeme.prelungire(new TemaLaborator(5,"cmmdc",4),6);
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void generateNote(){
        try{
            controllerNote.add(new Nota(1,5,6),"nimic");
            controllerNote.add(new Nota(1,8,6),"nimic");
            controllerNote.add(new Nota(4,5,2),"null");
            controllerNote.add(new Nota(4,6,-9),"");
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }

    public void updateNote() {
        try{
            controllerNote.update(new Nota(1,5,1),"gresit tot");
            controllerNote.update(new Nota(1,5,8),"si-a reparat greseala");
            controllerNote.update(new Nota(1,19,2),"");
        }catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }*/

