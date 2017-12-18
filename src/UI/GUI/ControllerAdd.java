package UI.GUI;

import Domain.Student;
import Service.StudentService;
import Utils.ListEvent;
import Utils.Observer;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.soap.Text;


public class ControllerAdd {
  private StudentService service;

  @FXML
  Button btnAdd;
  @FXML
  Button btnCancel;
  @FXML
  TextField textFieldId;
  @FXML
  TextField textFieldNume;
  @FXML
  TextField textFieldGrupa;
  @FXML
  TextField textFieldEmail;
  @FXML
  TextField textFieldCadru;
  @FXML
  TextField textFieldIdUpdate;
  @FXML
  TextField textFieldNumeUpdate;
  @FXML
  TextField textFieldGrupaUpdate;
  @FXML
  TextField textFieldEmailUpdate;
  @FXML
  TextField textFieldCadruUpdate;
  @FXML
  Button btnUpdate;
  @FXML
  Button btnCancelUp;

  public void initialize(){

  }
  public void initAdd(){
      btnAdd.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
              try{
                  if (service.add(new Student(Integer.valueOf(textFieldId.getText()), textFieldNume.getText(), Integer.valueOf(textFieldGrupa.getText()), textFieldEmail.getText(), textFieldCadru.getText())) != null)
                      showMessage(Alert.AlertType.WARNING,"Warning","Studentul cu id-ul dat exista deja in baza de date.");
                  else
                      showMessage(Alert.AlertType.INFORMATION, "Salvare", "Studentul a fost salvat cu succes!");

              }catch (ValidationException e){
                  showErrorMessage(e.getMessage());
              }
          }
      });
      btnCancel.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
              Stage a  = (Stage) btnCancel.getScene().getWindow();
              a.close();
          }
      });
  }

  public void loadStudent(Student student){
      textFieldIdUpdate.setText(String.valueOf(student.getId()));
      textFieldNumeUpdate.setText(student.getNume());
      textFieldGrupaUpdate.setText(String.valueOf(student.getGrupa()));
      textFieldEmailUpdate.setText(student.getEmail());
      textFieldCadruUpdate.setText(student.getCadruDidactic());
  }

  public void initUpdate(){
      btnCancelUp.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
              Stage a  = (Stage) btnCancelUp.getScene().getWindow();
              a.close();
          }
      });
      btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
              try{
                  service.update(new Student(Integer.valueOf(textFieldIdUpdate.getText()), textFieldNumeUpdate.getText(), Integer.valueOf(textFieldGrupaUpdate.getText()), textFieldEmailUpdate.getText(), textFieldCadruUpdate.getText()));
                  showMessage(Alert.AlertType.INFORMATION, "Update", "Studentul a fost updatat cu succes!");
              }catch (ValidationException e){
                  showErrorMessage(e.getMessage());
              }
          }
      });
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

  public void setService(StudentService service){
      this.service = service;
  }
}
