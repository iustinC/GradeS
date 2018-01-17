package UI.GUI;

import Domain.Nota;
import Domain.Student;
import Domain.TemaLaborator;
import Service.Filter;
import Service.NotaService;
import Service.TemeService;
import Utils.ListEvent;
import Utils.Observable;
import Utils.Observer;
import com.Main;
import com.jfoenix.controls.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ControllerStudent{

    @FXML
    TableView tableViewNote;

    @FXML
    JFXTabPane tabPane;

    @FXML
    TableView tableViewTeme;

    @FXML
    TableColumn<TemaLaborator, String> tableColumnNrTema;

    @FXML
    TableColumn<TemaLaborator, String> tableColumnCerinta;

    @FXML
    TableColumn<TemaLaborator, String> tableColumnDeadline;

    @FXML
    TableColumn<Nota, String> tableColumnNumarTema;

    @FXML
    TableColumn<Nota, String> tableColumnNota;

    @FXML
    JFXButton btnPrevious;

    @FXML
    JFXButton btnForward;

    @FXML
    JFXComboBox comboBoxItems;

    @FXML
    JFXButton btnSignOut1;

    @FXML
    JFXButton btnSignOut2;

    @FXML
    JFXButton btnSignOut3;
    @FXML
    Label labelID;
    @FXML
    Label showID;
    @FXML
    Label labelNume;
    @FXML
    Label showNume;
    @FXML
    Label labelGrupa;
    @FXML
    Label showGrupa;
    @FXML
    Label labelEmail;
    @FXML
    Label showEmail;
    @FXML
    Label labelCadru;
    @FXML
    Label showCadru;
    @FXML
    Label labelMedia;
    @FXML
    Label showMedia;
    @FXML
    JFXTextField textFieldCerintaFil;
    @FXML
    JFXTextField textFieldNrTemaFil;
    @FXML
    JFXComboBox comboBoxDeadline;

    IntegerProperty index = new SimpleIntegerProperty(3);
    IntegerProperty page = new SimpleIntegerProperty(0);
    String email;
    ObservableList<String> deadline =
            FXCollections.observableArrayList(
                    "0",
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
    ObservableList<String> items =
            FXCollections.observableArrayList(
                    "3",
                    "5",
                    "10"
            );

    private TemeService serviceTeme;
    private NotaService serviceNote;
    ObservableList<TemaLaborator> modelTeme;
    ObservableList<Nota> modelNote;

    public void setTableColumns(){
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("numarTema"));
        tableColumnCerinta.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("cerinta"));
        tableColumnDeadline.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("deadline"));

        tableColumnNumarTema.setCellValueFactory(new PropertyValueFactory<Nota, String>("numarTema"));
        tableColumnNota.setCellValueFactory(new PropertyValueFactory<Nota, String>("valoare"));
        tableViewNote.setRowFactory(a->new TableRow<Nota>(){
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Nota person, boolean empty) {
                super.updateItem(person, empty);
                if (person == null) {
                    setTooltip(null);
                } else {
                    String tool = serviceNote.getObservatiiForStudent(ControllerLogin.email,person.getValoare());
                    tooltip.setText(tool);
                    tooltip.setFont(Font.font(15));
                    setTooltip(tooltip);
                }
            }
        });
    }

    public void setButtons(){
        comboBoxItems.setItems(items);
        btnPrevious.setDisable(true);
        comboBoxItems.getSelectionModel().selectFirst();
        comboBoxItems.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                index.set(Integer.valueOf(newValue.toString()));
                page.setValue(0);
                if(index.getValue() + page.getValue() >= serviceTeme.getAllTeme().size())
                    btnForward.setDisable(true);
                else {
                    btnForward.setDisable(false);
                }
                loadData();
            }
        });
        page.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0)
                {
                    btnPrevious.setDisable(true);
                }
                else
                    btnPrevious.setDisable(false);
                if(newValue.intValue() >= serviceTeme.getAllTeme().size() - index.getValue())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);

            }
        });
        btnForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(index.getValue() + page.getValue());
                loadData();
            }
        });
        btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(page.getValue() - index.getValue());
                loadData();
            }
        });

        btnSignOut1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              Main.logOut();
            }
        });

        btnSignOut2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               Main.logOut();
            }
        });

        btnSignOut3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.logOut();
            }
        });
        comboBoxDeadline.setItems(deadline);
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void showData(){
        Student st = serviceNote.getStudentByMail(email);
        if(st != null){
            showID.setText(String.valueOf(st.getId()));
            showNume.setText(st.getNume());
            showGrupa.setText(String.valueOf(st.getGrupa()));
            showEmail.setText(st.getEmail());
            showCadru.setText(st.getCadruDidactic());
            showMedia.setText(String.valueOf(serviceNote.getMedieForStudent(st.getId())));
        }else{
            showID.setText("Date indisponibile");
            showNume.setText("");
            showGrupa.setText("");
            showEmail.setText("");
            showCadru.setText("");
            labelCadru.setText("");
            labelEmail.setText("");
            labelGrupa.setText("");
            labelID.setText("");
            labelNume.setText("");
            showMedia.setText("");
            labelMedia.setText("");
        }
    }
    public void initialize(){
        setTableColumns();
        setButtons();
    }

    public void filter(){
        Predicate<TemaLaborator> predFinal = (temaLaborator -> true);
        boolean ok1 = false, ok2 = false, ok3 = false;
        if(!textFieldCerintaFil.getText().equals(""))
            predFinal = predFinal.and(Filter.contineCerinta( textFieldCerintaFil.getText()));
        else
            ok1 = true;
        if(!comboBoxDeadline.getSelectionModel().getSelectedItem().toString().equals("0"))
            predFinal = predFinal.and(Filter.areDeadlineulMaiMic(Integer.valueOf(comboBoxDeadline.getSelectionModel().getSelectedItem().toString())));
        else
            ok2 = true;
        if(!textFieldNrTemaFil.getText().equals(""))
            predFinal = predFinal.and(Filter.esteMaiMicaNrTema(Integer.valueOf(textFieldNrTemaFil.getText())));
        else
            ok3 = true;
        if(!(ok1 == true && ok2 == true && ok3 == true)) {
            List<TemaLaborator> filtered = this.serviceNote.getAllTeme().stream().filter(predFinal).collect(Collectors.toList());
            modelTeme.setAll(FXCollections.observableArrayList(filtered));
            btnForward.setDisable(true);
            btnPrevious.setDisable(true);
            comboBoxItems.setDisable(true);
        }
        else{
            loadData();
            btnPrevious.setDisable(false);
            btnForward.setDisable(false);
            comboBoxItems.setDisable(false);
        }
    }

    public void loadData(){
        if(index.getValue() + page.getValue() >= serviceTeme.getAllTeme().size())
            btnForward.setDisable(true);
        else
            btnForward.setDisable(false);
        modelTeme.setAll(FXCollections.observableArrayList(this.serviceTeme.getBetween(index.getValue(), page.getValue())));
    }

    public void setService(TemeService serviceTeme, NotaService serviceNote,String email){
        this.serviceTeme = serviceTeme;
        this.serviceNote = serviceNote;
        this.modelNote = FXCollections.observableArrayList(this.serviceNote.getAllNoteForStudent(email));
        this.modelTeme = FXCollections.observableArrayList(this.serviceTeme.getBetween(index.getValue(), page.getValue()));
        tableViewNote.setItems(this.modelNote);
        tableViewTeme.setItems(this.modelTeme);
    }



}
