package Domain;

import Repository.HasID;

public class Nota implements HasID<String>{

    private String idNota;
    private int idStudent;
    private int numarTema;
    private int valoare;

    public int getSaptPredarii() {
        return saptPredarii;
    }

    public void setSaptPredarii(int saptPredarii) {
        this.saptPredarii = saptPredarii;
    }

    private int saptPredarii;

    public Nota( int idStudent, int numarTema, int valoare, int saptPredarii) {
        this.setId(String.valueOf(idStudent) + " " + String.valueOf(numarTema));
        this.idStudent = idStudent;
        this.numarTema = numarTema;
        this.valoare = valoare;
        this.saptPredarii = saptPredarii;
    }

    @Override
    public String getId() {
        return this.idNota;
    }

    @Override
    public void setId(String number) {
        this.idNota = number;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getNumarTema() {
        return numarTema;
    }

    public void setNumarTema(int numarTema) {
        this.numarTema = numarTema;
    }

    public int getValoare() {
        return valoare;
    }

    public void setValoare(int valoare) {
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "idNota='" + idNota + '\'' +
                ", idStudent=" + idStudent +
                ", numarTema=" + numarTema +
                ", valoare=" + valoare +
                '}';
    }
}
