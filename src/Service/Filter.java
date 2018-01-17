package Service;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class  Filter {
    /**
     *  Predicate to check if the cadruDidactic of student is Andrei
     * @return
     */
    public static Predicate<Student> verificareIndrumator(String nume){
        return student -> student.getCadruDidactic().toLowerCase().contains(nume);
    }

    /**
     *  Predicate to check if email of student cotains '.ro' or '.com'
     * @return
     */
    public static Predicate<Student> verificareMail(String string){
        return student -> student.getEmail().contains(string);
    }

    public static Predicate<Student> verificareNume(String numePartial){
        return student -> student.getNume().toLowerCase().contains(numePartial);
    }

    /**
     *  Predicate to check if grupa of student is a given one
     * @param grupa represents the given grupa of student
     * @return
     */
    public static Predicate<Student> verificareGrupa(String grupa){
        return student -> student.getGrupa() == Integer.valueOf(grupa);
    }

    public static Predicate<Student> verificareId(String id){
        return student -> student.getId() < Integer.valueOf(id);
    }

    /**
     * Comparator for grupa of students
     */
    public static Comparator<Student> comparatorGrupa = (Student student1, Student student2) -> (student1.getGrupa() - student2.getGrupa());

    /**
     * Comparator for name of students
     */
    public static Comparator<Student> comparatorNume = (Student student1, Student student2) -> (student1.getNume().compareTo(student2.getNume()));

    /**
     *  Comparator for email of students
     */
    public static Comparator<Student> comparatorEmail = (Student student1, Student student2) -> (student1.getEmail().compareTo(student2.getEmail()));

    /**
     *  Predicate to check if the deadline of tema is lower than deadline given
     * @param saptamana represents  deadline given
     * @return
     */
    public static Predicate<TemaLaborator> areDeadlineulMaiMic(int saptamana){
        return tema -> tema.getDeadline() < saptamana;
    }

    /**
     *  Predicate to check if the numarTema is lower than given numarTema
     * @param numarTema represents numarTema given
     * @return
     */
    public static Predicate<TemaLaborator> esteMaiMicaNrTema(int numarTema){
        return tema -> tema.getNumarTema() < numarTema;
    }

    /**
     *  Predicate compose to check if tema has deadline lower than deadline given and numarTema lower than numarTema given
     * @param saptamana represents deadline given
     * @param numarTema represents numarTema given
     * @return
     */
    /*public static Predicate<TemaLaborator> deadlineMaiMicNumarTemaMaiMare(int saptamana, int numarTema){
        return areDeadlineulMaiMic(saptamana).and(esteMaiMicaSauEgalaNumarTema(numarTema));
    }*/

    public static Predicate<TemaLaborator> contineCerinta(String cerinta){
        return tema -> tema.getCerinta().contains(cerinta);
    }

    /**
     *  Comparator for numarTema of tema
     */
    public static Comparator<TemaLaborator> comparatorNrTema = (TemaLaborator tema1, TemaLaborator tema2) -> (tema2.getNumarTema() - tema1.getNumarTema());

    /**
     *  Comparator for deadline of tema
     */
    public static Comparator<TemaLaborator> comparatorDeadline = (TemaLaborator tema1, TemaLaborator tema2) -> (tema1.getDeadline() - tema2.getDeadline());

    /**
     *  Comparator for cerinta of tema
     */
    public static Comparator<TemaLaborator> comparatorCerinta = (TemaLaborator tema1, TemaLaborator tema2) -> (tema1.getCerinta().compareTo(tema2.getCerinta()));

    /**
     *  Predicate to check if valoare nota is lower than a given one
     * @param notaData represents nota given
     * @return
     */
    public static Predicate<Nota> areValoareaMaiMica(int notaData){
        return nota -> nota.getValoare() < notaData;
    }

    public static Predicate<Nota> areSaptPredariiMaiMica(int sapt){
        return nota -> nota.getSaptPredarii() < sapt;
    }
    /**
     *  Predicate to check if nota has a given numarTema
     * @param numarTema represents numarTema given
     * @return
     */
    public static Predicate<Nota> toateNoteleDeLaTema(int numarTema){
        return tema -> tema.getNumarTema() == numarTema;
    }

    /**
     *  Predicate to check nota of a given student
     * @param idStudent represents id of student
     * @return
     */
    public static Predicate<Nota> noteleUnuiStudent(int idStudent){
        return nota -> nota.getIdStudent() == idStudent;
    }

    /**
     *  Comparator for valoare of nota
     */
    public static Comparator<Nota> comparatorValoareNota = (Nota nota1, Nota nota2) -> (nota1.getValoare() - nota2.getValoare());

    /**
     *  Comparator for idStudent of nota
     */
    public static Comparator<Nota> comparatorIdStudentNota = (Nota nota1, Nota nota2) -> (nota2.getIdStudent() - nota1.getIdStudent());

    /**
     *  Return a list that is filtered by a predicate given and sorted by a comparator given
     * @param lista list to filter and sort
     * @param predicate predicate to filter the list
     * @param comparator comparator to sort the list
     * @param <E> type of list
     * @return
     */
    public static <E> List<E> filterAndSorter(List<E> lista, Predicate<E> predicate, Comparator<E> comparator){
        List<E> all = lista.stream().filter(predicate).collect(Collectors.toList());
        all.sort(comparator);
        return all;
    }
}
