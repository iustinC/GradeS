package UI;

import Domain.Student;
import Domain.TemaLaborator;
import Repository.StudentFileRepository;
import Repository.TemeFileRepository;
import Service.StudentService;
import Service.TemeService;
import Validator.Validator;
import Validator.ValidatorTeme;
import Validator.ValidatorStudent;
import Validator.ValidationException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Console {
    StudentService controllerStudent = new StudentService(new StudentFileRepository(new ValidatorStudent(),"Student.txt"));
    TemeService controllerTeme = new TemeService(new TemeFileRepository(new ValidatorTeme(),"Teme.txt"));

    public Console() {
        generateStudenti();
        generateTeme();
    }

    private void generateTeme() {
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

}
