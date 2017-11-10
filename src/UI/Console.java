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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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

            controllerTeme.delete(numarTema);
            System.out.println("S-a sters  tema ");

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

            controllerNote.delete(String.valueOf(idStudent) + " " + String.valueOf(numarTema));

            System.out.println("S-a sters  nota ");

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
                controllerStudent.delete(idStudent);
                System.out.println("Studentul a fost sters. \n");
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

        crudTema.addCommand("0. Back to main menu", mainMenu);
        crudTema.addCommand("1. Add Tema de laborator", new ReadTemaCommand());
        crudTema.addCommand("2. Update Tema de laborator", new UpdateTemaCommand());
        crudTema.addCommand("3. Delete Tema de laborator", new DeleteTemaCommand());
        crudTema.addCommand("4. Prelungire nota", new PrelungireTemaCommand());
        crudTema.addCommand("5. Get all", new GetAllTemeCommand());

        crudNota.addCommand("0. Back to main menu", mainMenu);
        crudNota.addCommand("1. Add Nota", new ReadNotaCommand());
        crudNota.addCommand("2. Update nota", new UpdateNotaCommand());
        crudNota.addCommand("3. Delete nota", new DeleteNotaCommand());
        crudNota.addCommand("4. Get all", new GetAllNoteCommand());

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

}
