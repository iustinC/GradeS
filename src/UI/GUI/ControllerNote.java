package UI.GUI;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Service.Filter;
import Service.NotaService;
import Utils.ListEvent;
import Utils.Observable;
import Utils.Observer;
import Validator.ValidationException;
import com.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ControllerNote implements Observer<Nota> {

    @FXML
    TableView tableView;
    @FXML
    TableColumn<Nota, String> tableColumnId;
    @FXML
    TableColumn<Nota, String> tableColumnIdStud;
    @FXML
    TableColumn<Nota, String> tableColumnNrTema;
    @FXML
    TableColumn<Nota, String> tableColumnNota;
    @FXML
    Button btnBack;
    @FXML
    Button btnAddMenu;
    @FXML
    TextField textFieldIdStud;
    @FXML
    TextField textFieldNrTema;
    @FXML
    TextField textFieldNota;
    @FXML
    TextField textFieldObservatii;
    @FXML
    Button btnUpdate;
    @FXML
    CheckBox checkBoxNota;
    @FXML
    ComboBox comboBoxNota;

    ObservableList<String> note =
            FXCollections.observableArrayList(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10"
            );

    ObservableList<Nota> model;
    NotaService service;

    public void initialize() {

    }
    public void initNote(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Nota, String>("id"));
        tableColumnIdStud.setCellValueFactory(new PropertyValueFactory<Nota, String>("idStudent"));
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<Nota, String>("numarTema"));
        tableColumnNota.setCellValueFactory(new PropertyValueFactory<Nota, String>("valoare"));

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(Main.initView(), 800, 500);
                scene.getStylesheets().add("Resources/CSS/stylesheets.css");
                Main.st.setScene(scene);
            }
        });
        btnBack.getStyleClass().add("button_home");
        comboBoxNota.setItems(note);
        comboBoxNota.getSelectionModel().selectFirst();
    }

    public void addMenu() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/UI/GUI/AddNotaLayout.fxml"));
            root = loader.load();
            ControllerAddNote controller = loader.getController();
            root.getStyleClass().add("pane");
            controller.setService(service);
            controller.load();
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            Scene addScene = new Scene(root, 1100, 500);
            addScene.getStylesheets().add("Resources/CSS/stylesheets.css");
            stage.setScene(addScene);
            stage.setResizable(false);
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData(){
        Nota nota = (Nota) tableView.getSelectionModel().getSelectedItem();
        textFieldIdStud.setText(String.valueOf(nota.getIdStudent()));
        textFieldNrTema.setText(String.valueOf(nota.getNumarTema()));
        textFieldNota.setText(String.valueOf(nota.getValoare()));
    }

    public void deleteNota(){
        Nota nota = (Nota) tableView.getSelectionModel().getSelectedItem();
        if(nota == null){
            showErrorMessage("Nu a fost selectata nicio nota!");
        }
        else
            service.delete(nota.getId());
    }

    public void filter(){
        if(checkBoxNota.isSelected()){
            List<Nota> filter = this.service.getAllNote().stream().filter(Filter.areValoareaMaiMica(comboBoxNota.getSelectionModel().getSelectedIndex())).collect(Collectors.toList());
            tableView.setItems(FXCollections.observableArrayList(filter));
        }
        else
            tableView.setItems(FXCollections.observableArrayList(service.getAllNote()));
    }

    public void updateNota(){
        try{
            this.service.update(new Nota(Integer.valueOf(textFieldIdStud.getText()),Integer.valueOf(textFieldNrTema.getText()),Integer.valueOf(textFieldNota.getText())),textFieldObservatii.getText());
            showMessage(Alert.AlertType.INFORMATION, "Update", "Nota a fost updatata cu succes!");
        }catch (ValidationException e){
            showErrorMessage(e.getMessage());
        }
    }


    public void setService(NotaService service){
        this.service = service;
        this.model = FXCollections.observableArrayList(this.service.getAll());
        tableView.setItems(this.model);
    }

    @Override
    public void notifyEvent(ListEvent<Nota> e) {
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false).collect(Collectors.toList()));
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
}
