package Domain;

import Repository.HasID;

public class TemaLaborator implements HasID<Integer>{

    private int numarTema;
    private String cerinta;
    private int deadline;
    private int nota;

    public TemaLaborator(int numarTema, String cerinta, int deadline) {
        setId(numarTema);
        this.cerinta = cerinta;
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "TemaLaborator{" +
                "numarTema=" + numarTema +
                ", cerinta='" + cerinta + '\'' +
                ", deadline=" + deadline +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemaLaborator that = (TemaLaborator) o;

        return cerinta != null ? cerinta.equals(that.cerinta) : that.cerinta == null;
    }

    @Override
    public int hashCode() {
        return cerinta != null ? cerinta.hashCode() : 0;
    }


    // Return cerinta of tema
    public String getCerinta() {
        return cerinta;
    }

    // Set cerinta of tema
    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    // Get deadline of tema
    public int getDeadline() {
        return deadline;
    }

    // Set deadline of tema
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    // Get id of tema
    @Override
    public Integer getId() {
        return numarTema;
    }

    // Set id of tema
    @Override
    public void setId(Integer integer) {
        numarTema = integer;
    }
}