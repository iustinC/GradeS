package Repository;

import Domain.Student;
import Validator.Validator;
import Validator.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository extends InMemoryStudentRepository {


    public StudentRepository(Validator<Student> validator) {
        super(validator);
        try{
            Class.forName(jdbc_driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public Student save(Student entity) throws ValidationException {
        validator.validate(entity);
        try {
            String sql = "INSERT INTO Studenti VALUES("
                    +"'" + entity.getNume() + "'" + "," +
                    String.valueOf(entity.getGrupa()) + "," +
                    "'" + entity.getEmail() + "'" + "," +
                    "'" + entity.getCadruDidactic() + "'" + ")";
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 2627)
                throw new ValidationException("Studentul exista deja in baza de date!");
            else
                e.printStackTrace();
        }
        return null;
    }

    public List<Student> getAll(){
        try{
            conn = DriverManager.getConnection(url);
            List<Student> all = new ArrayList<>();
            String selectAll = "SELECT * FROM Studenti";
            statement = conn.createStatement();
            ResultSet res = statement.executeQuery(selectAll);

            while(res.next())
                all.add(new Student(Integer.valueOf(res.getString(1)),res.getString(2),Integer.valueOf(res.getString(3)),res.getString(4),res.getString(5)));

            conn.close();
            return all;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Student> delete(Integer id){
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String selectStud = "SELECT * FROM Studenti WHERE idStudent =  " + String.valueOf(id);
            ResultSet res = statement.executeQuery(selectStud);
            res.next();
            Optional<Student> deleted = Optional.ofNullable(new Student(Integer.valueOf(res.getString(1)),res.getString(2), Integer.valueOf(res.getString(3)),res.getString(4), res.getString(5)));
            String sql = "DELETE FROM Studenti WHERE idStudent =" + String.valueOf(id);
            statement.executeUpdate(sql);
            conn.close();
            return deleted;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Student update(Student entity) throws ValidationException{
        validator.validate(entity);
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "UPDATE Studenti " +
                    "SET nume = " + "'" + entity.getNume() + "'" + "," +
                    "grupa = " + String.valueOf(entity.getGrupa()) + "," +
                    "email = " + "'" + entity.getEmail() + "'" + "," +
                    "cadruDidactic = " + "'" + entity.getCadruDidactic() + "' " +
                    "WHERE idStudent = " + String.valueOf(entity.getId());
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 2627)
                throw new ValidationException("Studentul exista deja in baza de date");
        }
        return null;
    }

    @Override
    public Student findOne(Integer integer) {
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "SELECT * FROM Studenti WHERE idStudent = " + String.valueOf(integer);
            ResultSet res = statement.executeQuery(sql);
            res.next();
            Student st = new Student(Integer.valueOf(res.getString(1)), res.getString(2), Integer.valueOf(res.getString(3)), res.getString(4), res.getString(5));
            conn.close();
            return st;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> between(int index, int page) {
        try{
            List<Student> all = new ArrayList<>();
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "SELECT * FROM Studenti ORDER BY idStudent OFFSET " + String.valueOf(page) + " ROWS  FETCH NEXT " + String.valueOf(index) + " ROWS ONLY" ;
            ResultSet res = statement.executeQuery(sql);

            while(res.next())
                all.add(new Student(Integer.valueOf(res.getString(1)), res.getString(2), Integer.valueOf(res.getString(3)), res.getString(4), res.getString(5)));
            conn.close();
            return all;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
