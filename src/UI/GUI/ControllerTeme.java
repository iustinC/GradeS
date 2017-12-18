package UI.GUI;

import Domain.Student;
import Domain.TemaLaborator;
import Service.Filter;
import Service.NotaService;
import Service.TemeService;
import Utils.ListEvent;
import Utils.Observer;
import Validator.ValidationException;
import com.Main;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControllerTeme implements Observer<TemaLaborator> {

    @FXML
    TableView tableViewTeme;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnNrTema;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnCerinta;
    @FXML
    TableColumn<TemaLaborator, String> tableColumnDeadline;
    @FXML
    Button btnBack;
    @FXML
    TextField textFieldNrTema;
    @FXML
    TextField textFieldCerinta;
    @FXML
    TextField textFieldDeadline;
    @FXML
    Button btnAdd;
    @FXML
    Button btnUpdate;
    @FXML
    TextField textFieldUpNrTema;
    @FXML
    TextField textFieldUpCerinta;
    @FXML
    TextField textFieldUpDeadline;
    @FXML
    CheckBox checkBoxCerintaFil;
    @FXML
    CheckBox checkBoxNrTemaFil;
    @FXML
    CheckBox checkBoxDeadlineFil;
    @FXML
    TextField textFieldNrTemaFil;
    @FXML
    TextField textFieldCerintaFil;
    @FXML
    ComboBox comboBoxDeadline;
    @FXML
    Button btnDelete;

    ObservableList<String> deadline =
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
                    "10",
                    "11",
                    "12",
                    "13",
                    "14"
            );

    TemeService service;
    NotaService notaService;
    ObservableList<TemaLaborator> model;

    public void initialize(){
        tableColumnCerinta.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("cerinta"));
        tableColumnDeadline.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("deadline"));
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("numarTema"));
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(Main.initView(), 800, 500);
                scene.getStylesheets().add("Resources/CSS/stylesheets.css");
                Main.st.setScene(scene);            }
        });
        comboBoxDeadline.setItems(deadline);
        btnBack.getStyleClass().add("button_home");
    }

    public void filter(){
        Predicate<TemaLaborator> predFinal = (temaLaborator -> true);
        if(checkBoxCerintaFil.isSelected())
            predFinal = predFinal.and(Filter.contineCerinta( textFieldCerintaFil.getText()));
        if(checkBoxDeadlineFil.isSelected())
            predFinal = predFinal.and(Filter.areDeadlineulMaiMic(Integer.valueOf(comboBoxDeadline.getSelectionModel().getSelectedItem().toString())));
        /*

        TO DO VALIDARE DACA NUMAR TEMA E NULL SA NU LASI SA FACI FILTRARE

         */
        if(checkBoxNrTemaFil.isSelected())
            if(!textFieldNrTemaFil.getText().equals(""))
                predFinal = predFinal.and(Filter.esteMaiMicaNrTema(Integer.valueOf(textFieldNrTemaFil.getText())));

        List<TemaLaborator> filtered = this.service.getAllTeme().stream().filter(predFinal).collect(Collectors.toList());
        tableViewTeme.setItems(FXCollections.observableArrayList(filtered));
    }

    public void addTema(){
        try{
            if(this.service.add(new TemaLaborator(Integer.valueOf(textFieldNrTema.getText()), textFieldCerinta.getText(), Integer.valueOf(textFieldDeadline.getText()))) != null)
                    showMessage(Alert.AlertType.WARNING,"Warning","Tema exista deja.");
            else
                    showMessage(Alert.AlertType.INFORMATION, "Salvare", "Tema a fost salvata cu succes!");
        }catch (ValidationException e){
            showErrorMessage(e.getMessage());
        }
    }

    public void updateTema(){
        try{
            service.update(new TemaLaborator(Integer.valueOf(textFieldUpNrTema.getText()), textFieldUpCerinta.getText(), Integer.valueOf(textFieldUpDeadline.getText())));
            showMessage(Alert.AlertType.INFORMATION, "Update", "Tema fost updatata cu succes!");
        }catch (ValidationException e){
            showErrorMessage(e.getMessage());
        }
    }

    public void setService(TemeService service, NotaService notaService){
        this.notaService = notaService;
        this.service = service;
        this.model = FXCollections.observableArrayList(this.service.getAllTeme());
        tableViewTeme.setItems(model);
    }

    public void deleteTema(){
        TemaLaborator tema = (TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        if(notaService.findTema(tema.getNumarTema()) == false)
            this.service.delete(tema.getId());
        else
            showErrorMessage("Tema nu poate fi stearsa deoarece are asignata o nota.");
    }
    public void loadTema(){
        TemaLaborator tema = (TemaLaborator) tableViewTeme.getSelectionModel().getSelectedItem();
        if(tema == null)
            return;
        else{
            textFieldUpDeadline.setText(String.valueOf(tema.getNumarTema()));
            textFieldUpCerinta.setText(String.valueOf(tema.getCerinta()));
            textFieldUpNrTema.setText(String.valueOf(tema.getNumarTema()));
        }
    }

    @Override
    public void notifyEvent(ListEvent<TemaLaborator> e) {
        model.setAll(StreamSupport.stream(e.getList().spliterator(),false)
                .collect(Collectors.toList()));
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
