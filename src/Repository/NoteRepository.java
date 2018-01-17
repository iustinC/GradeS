package Repository;

import Domain.Nota;
import Validator.Validator;
import Validator.ValidationException;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteRepository extends InMemoryNotaRepository {

    public NoteRepository(Validator<Nota> validator) {
        super(validator);
        try{
            Class.forName(jdbc_driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public Nota save(Nota entity) throws ValidationException{
        validator.validate(entity);
        try {
            String sql = "INSERT INTO Note VALUES(" +
                         String.valueOf(entity.getIdStudent()) + "," +
                         String.valueOf(entity.getNumarTema()) + "," +
                         String.valueOf(entity.getValoare()) + "," +
                         String.valueOf(entity.getSaptPredarii()) + ")";
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 2627)
                throw new ValidationException("Nota exista deja in baza de date.");
            if(e.getErrorCode() == 547)
                throw new ValidationException("Nu exista studentul sau nota.");
        }
        return null;
    }

    public List<Nota> getAll(){
        try{
            conn = DriverManager.getConnection(url);
            List<Nota> all = new ArrayList<>();
            String selectAll = "SELECT * FROM Note";
            statement = conn.createStatement();
            ResultSet res = statement.executeQuery(selectAll);

            while(res.next())
                all.add(new Nota(Integer.valueOf(res.getString(1)), Integer.valueOf(res.getString(2)), Integer.valueOf(res.getString(3)), Integer.valueOf(res.getString(4))));

            conn.close();
            return all;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Nota> delete(String str){
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String[] fields = str.split(" ");
            String selectNota = "SELECT * FROM Note WHERE idStudent = " + fields[0] + " and numarTema = " + fields[1];
            ResultSet res = statement.executeQuery(selectNota);
            res.next();
            Optional<Nota> deleted = Optional.ofNullable(new Nota(Integer.valueOf(res.getString(1)), Integer.valueOf(res.getString(2)), Integer.valueOf(res.getString(3)),Integer.valueOf(res.getString(4))));
            String sql = "DELETE FROM Note WHERE idStudent = " + fields[0] + " and numarTema = " + fields[1];
            statement.executeUpdate(sql);
            conn.close();
            return deleted;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Nota update(Nota nota) throws  ValidationException{
        validator.validate(nota);
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "UPDATE Note " +
                         "SET idStudent = " + String.valueOf(nota.getIdStudent()) + "," +
                         "numarTema = " + String.valueOf(nota.getNumarTema()) + "," +
                         "valoare = " + String.valueOf(nota.getValoare()) +
                         " WHERE idStudent = " + String.valueOf(nota.getIdStudent()) + " and " + "numarTema = " + String.valueOf(nota.getNumarTema());
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 2627)
                throw new ValidationException("Nota exista deja in baza de date.");
        }
        return null;
    }

    @Override
    public Nota findOne(String s) {
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String[] fields = s.split(" ");
            String sql = "SELECT * FROM Note WHERE idStudent = " + fields[0] + " and numarTema = " + fields[1];
            ResultSet res = statement.executeQuery(sql);
            res.next();
            Nota nt = new Nota(Integer.valueOf(res.getString(1)), Integer.valueOf(res.getString(2)), Integer.valueOf(res.getString(3)),Integer.valueOf(res.getString(4)));
            conn.close();
            return nt;
        }catch (SQLException e){
           if(e.getErrorCode() != 0)
               e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Nota> between(int index, int page){
        try{
            List<Nota> all = new ArrayList<>();
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "SELECT * FROM Note ORDER BY idStudent OFFSET " + String.valueOf(page) + " ROWS FETCH NEXT " + String.valueOf(index) + " ROWS ONLY";
            ResultSet res = statement.executeQuery(sql);

            while (res.next())
                all.add(new Nota(Integer.valueOf(res.getString(1)), Integer.valueOf(res.getString(2)), Integer.valueOf(res.getString(3)), Integer.valueOf(res.getString(4))));
            conn.close();
            return all;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
