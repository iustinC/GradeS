package Domain;

import Repository.HasID;

public class Student implements HasID<Integer> {

    private int idStudent;
    private String nume;
    private int grupa;
    private String email;
    private String cadruDidactic;

    public Student(int idStudent, String nume, int grupa, String email, String cadruDidactic) {
        setId(idStudent);
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.cadruDidactic = cadruDidactic;
    }
    // Return the id of the current student
    @Override
    public Integer getId() {
        return idStudent;
    }

    // Set the id of the current student
    @Override
    public void setId(Integer idStudent) {
        this.idStudent = idStudent;
    }

    // Get name of the student
    public String getNume() {
        return nume;
    }

    // Set name of the student
    public void setNume(String nume) {
        this.nume = nume;
    }

    // Get grupa of the student
    public int getGrupa() {
        return grupa;
    }

    // Set grupa of the student
    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    // Get email of the student
    public String getEmail() {
        return email;
    }

    // Set email of the student
    public void setEmail(String email) {
        this.email = email;
    }

    // Get cadruDidactic of student
    public String getCadruDidactic() {
        return cadruDidactic;
    }

    // Set cadruDidactic of student
    public void setCadruDidactic(String cadruDidactic) {
        this.cadruDidactic = cadruDidactic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (idStudent != student.idStudent) return false;
        if (grupa != student.grupa) return false;
        if (nume != null ? !nume.equals(student.nume) : student.nume != null) return false;
        if (email != null ? !email.equals(student.email) : student.email != null) return false;
        return cadruDidactic != null ? cadruDidactic.equals(student.cadruDidactic) : student.cadruDidactic == null;
    }

    @Override
    public String toString() {
        return "Student{" +
                "idStudent=" + idStudent +
                ", nume='" + nume + '\'' +
                ", grupa=" + grupa +
                ", email='" + email + '\'' +
                ", cadruDidactic='" + cadruDidactic + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return nume != null ? nume.hashCode() : 0;

    }
}
