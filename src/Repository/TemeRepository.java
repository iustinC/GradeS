package Repository;

import Domain.TemaLaborator;
import Validator.Validator;
import Validator.ValidationException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemeRepository extends InMemoryTemeRepository {

    public TemeRepository(Validator<TemaLaborator> validator) {
        super(validator);
        try{
            Class.forName(jdbc_driver);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public TemaLaborator save(TemaLaborator entity) throws ValidationException{
        validator.validate(entity);
        try{
            String sql  = "INSERT INTO Teme VALUES(" +
                          "'" + entity.getCerinta() + "'" + "," +
                          String.valueOf(entity.getDeadline()) + ")";
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 2627)
                throw new ValidationException("Tema exista deja in baza de date!");
            else
                e.printStackTrace();
        }
        return null;
    }

    public List<TemaLaborator> getAll(){
        try{
            conn = DriverManager.getConnection(url);
            List<TemaLaborator> all = new ArrayList<>();
            String selectAll = "SELECT * FROM Teme";
            statement = conn.createStatement();
            ResultSet res = statement.executeQuery(selectAll);

            while(res.next())
                all.add(new TemaLaborator(Integer.valueOf(res.getString(1)),res.getString(2),Integer.valueOf(res.getString(3))));

            conn.close();
            return all;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Optional<TemaLaborator> delete(Integer id){
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String selectTema = "SELECT * FROM Teme WHERE numarTema = " + String.valueOf(id);
            ResultSet res = statement.executeQuery(selectTema);
            res.next();
            Optional<TemaLaborator> deleted = Optional.ofNullable(new TemaLaborator(Integer.valueOf(res.getString(1)), res.getString(2), Integer.valueOf(res.getString(3))));
            String sql = "DELETE FROM Teme WHERE numarTema = " + String.valueOf(id);
            statement.executeUpdate(sql);
            conn.close();
            return deleted;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TemaLaborator update(TemaLaborator entity) throws ValidationException{
        validator.validate(entity);
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "UPDATE Teme" +
                         " SET cerinta = " + "'" + entity.getCerinta() + "'" + "," +
                         "deadline = " + String.valueOf(entity.getDeadline()) +
                         " WHERE numarTema = " + String.valueOf(entity.getNumarTema());
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            if(e.getErrorCode() == 2627)
                throw new ValidationException("Tema exista deja in baza de date");
        }
        return null;
    }

    @Override
    public TemaLaborator findOne(Integer id){
        try{
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "SELECT * FROM Teme WHERE numarTema = " + String.valueOf(id);
            ResultSet res = statement.executeQuery(sql);
            if(res.next() == true)
                return new TemaLaborator(Integer.valueOf(res.getString(1)), res.getString(2), Integer.valueOf(res.getString(3)));
            else
                return null;
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            try{
            conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<TemaLaborator> between(int  index, int page){
        try{
            List<TemaLaborator> all = new ArrayList<>();
            conn = DriverManager.getConnection(url);
            statement = conn.createStatement();
            String sql = "SELECT * FROM Teme ORDER BY numarTema OFFSET " + String.valueOf(page) + " ROWS FETCH NEXT " + String.valueOf(index) + " ROWS ONLY";
            ResultSet res = statement.executeQuery(sql);

            while (res.next())
                all.add(new TemaLaborator(Integer.valueOf(res.getString(1)), res.getString(2), Integer.valueOf(res.getString(3))));
            conn.close();
            return all;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
