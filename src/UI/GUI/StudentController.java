package UI.GUI;

import Domain.Student;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observer;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController implements Observer<Student> {

    private StudentService serviceStudent;

    private ObservableList<Student> modelStudent;

    private StudentView viewStudent;

    public StudentController(StudentService serviceStudent) {
        this.serviceStudent = serviceStudent;
        this.modelStudent = FXCollections.observableArrayList(serviceStudent.getAllStudents());
    }

    @Override
    public void notifyEvent(ListEvent<Student> e) {
        modelStudent.setAll(StreamSupport.stream(e.getList().spliterator(),false)
                .collect(Collectors.toList()));  // important
    }

    public ObservableList<Student> getModelStudent() {
        return modelStudent;
    }

    private Student getDetailsFromFieldsStudent() {
        String id = viewStudent.fieldId.getText();
        String nume = viewStudent.fieldNume.getText();
        String grupa = viewStudent.fieldGrupa.getText();
        String email = viewStudent.fieldEmail.getText();
        String cadruDidactic = viewStudent.fieldCadruDidactic.getText();

        return new Student(Integer.valueOf(id), nume, Integer.valueOf(grupa), email, cadruDidactic);
    }

    public void handleAddMessage(ActionEvent actionEvent) {
        try {
            if (serviceStudent.add(getDetailsFromFieldsStudent()) != null)
                showMessage(Alert.AlertType.WARNING,"Warning","Studentul cu id-ul dat exista deja in baza de date.");
            else
                showMessage(Alert.AlertType.INFORMATION, "Salvare", "Mesajul a fost salvat cu succes!");
        } catch (ValidationException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void handleUpdateMessage(ActionEvent actionEvent) {
        try {
            serviceStudent.update(getDetailsFromFieldsStudent());
            showMessage(Alert.AlertType.INFORMATION,"Update","Studentul a fost updatat.");
        } catch (ValidationException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void handleClearFields(ActionEvent actionEvent) {
        viewStudent.fieldId.clear();
        viewStudent.fieldNume.clear();
        viewStudent.fieldGrupa.clear();
        viewStudent.fieldEmail.clear();
        viewStudent.fieldCadruDidactic.clear();
        viewStudent.fieldId.setDisable(false);
    }

    public void handleDeleteMessage(ActionEvent actionEvent) {
        Student student = viewStudent.tableView.getSelectionModel().getSelectedItem();
        if(student != null) {
            Integer id = student.getId();
            if(serviceStudent.delete(id).isPresent()) {
                showMessage(Alert.AlertType.INFORMATION, "Stergere", "Studentul a fost sters.");
            }
        }
        else
            showMessage(Alert.AlertType.WARNING, "Stergere", "Nu exista studentul.");
    }

    public void setView(StudentView view) {
        this.viewStudent = view;
    }


    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Mesaj eroare");
        message.setContentText(text);
        message.showAndWait();
    }


    public void showStudentDetails(Student newValue) {
        if(newValue != null) {
            viewStudent.fieldId.setText(String.valueOf(newValue.getId()));
            viewStudent.fieldNume.setText(newValue.getNume());
            viewStudent.fieldGrupa.setText(String.valueOf(newValue.getGrupa()));
            viewStudent.fieldEmail.setText(newValue.getEmail());
            viewStudent.fieldCadruDidactic.setText(newValue.getCadruDidactic());
        }
    }
}
