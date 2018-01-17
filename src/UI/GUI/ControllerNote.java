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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
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
    TableColumn<Nota, String> tableColumnSaptPredarii;
    @FXML
    JFXButton btnBack;
    @FXML
    JFXButton btnAddMenu;
    @FXML
    JFXTextField textFieldIdStud;
    @FXML
    JFXTextField textFieldNrTema;
    @FXML
    JFXTextField textFieldNota;
    @FXML
    JFXTextField textFieldObservatii;
    @FXML
    JFXTextField textFieldSaptPredarii;
    @FXML
    JFXButton btnUpdate;
    @FXML
    JFXComboBox comboBoxNota;
    @FXML
    JFXComboBox comboBoxSaptPredarii;
    @FXML
    JFXButton btnPrevious;
    @FXML
    JFXButton btnForward;
    @FXML
    JFXComboBox comboBoxItems;
    @FXML
    JFXButton btnMediaStudentilor;
    @FXML
    JFXButton btnCeaMaiGrea;
    @FXML
    JFXButton btnStudentiExamen;
    @FXML
    JFXButton btnPredatLaTimp;
    @FXML
    JFXButton btnTopulGrupelor;
    @FXML
    JFXButton btnSignOut;

    IntegerProperty index = new SimpleIntegerProperty(3);
    IntegerProperty page = new SimpleIntegerProperty(0);

    ObservableList<String> note =
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
                    "10"
            );
    ObservableList<String> sapt =
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

    ObservableList<Nota> model;
    NotaService service;

    public void initialize() {

    }

    public void initNote(){
        setButtons();
        setTableColumns();
    }

    public void setTableColumns(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Nota, String>("id"));
        tableColumnIdStud.setCellValueFactory(new PropertyValueFactory<Nota, String>("idStudent"));
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<Nota, String>("numarTema"));
        tableColumnNota.setCellValueFactory(new PropertyValueFactory<Nota, String>("valoare"));
        tableColumnSaptPredarii.setCellValueFactory(new PropertyValueFactory<Nota, String>("saptPredarii"));

    }

    public void setButtons(){
        comboBoxItems.setItems(items);
        comboBoxItems.getSelectionModel().selectFirst();
        btnPrevious.setDisable(true);
        comboBoxNota.setItems(note);
        comboBoxSaptPredarii.setItems(sapt);
        comboBoxSaptPredarii.getSelectionModel().selectFirst();
        comboBoxNota.getSelectionModel().selectFirst();
        btnMediaStudentilor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    VBox anchorPane = new VBox();
                    JFXButton btnExport = new JFXButton("Export");
                    btnExport.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try{
                            Document document = new Document(PageSize.A4);
                            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Resources\\PDFs\\mediaStudentilor.pdf"));
                            document.open();
                            document.add(new Paragraph("Media Studentilor "));
                            document.add(new Paragraph(" "));
                            List<String> ceaMaiGrea = new ArrayList<>();

                            for(Map.Entry<Student, Integer> entry : service.notaLaboratorStudent().entrySet()){
                                document.add(new Paragraph(entry.getKey().getNume() + " " +
                                                                  String.valueOf(entry.getValue())));
                            }
                            document.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (DocumentException e){
                            e.printStackTrace();
                        }
                        }
                    });
                    Stage stage = new Stage();
                    final NumberAxis xAxis = new NumberAxis();
                    VBox.setMargin(btnExport,new Insets(10));
                    final NumberAxis yAxis = new NumberAxis();
                    xAxis.setLabel("Studenti");
                    final LineChart<Number,Number> lineChart =
                            new LineChart<Number,Number>(xAxis,yAxis);
                    lineChart.setTitle("Media studentilor");
                    lineChart.setPrefHeight(580);
                    XYChart.Series series = new XYChart.Series();
                    series.setName("Nota");
                    anchorPane.setAlignment(Pos.BOTTOM_RIGHT);
                    anchorPane.getChildren().addAll(lineChart,btnExport);
                    for(int i = 1; i < 11 ; i++){
                         series.getData().add(new XYChart.Data(i, service.catiAuMedia(i)));
                    }
                    Scene scene  = new Scene(anchorPane,800,600);
                    lineChart.getData().add(series);
                    stage.setScene(scene);
                    stage.show();
            }
        });

        btnCeaMaiGrea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox anchorPane = new VBox();
                JFXButton btnExport = new JFXButton("Export");
                Object[] a = service.ceaMaiGreaTema().entrySet().toArray();

                btnExport.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                        Document document = new Document(PageSize.A4);
                        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Resources\\PDFs\\ceaMaiGreaTema.pdf"));
                        document.open();
                        document.add(new Paragraph("Cele mai grele teme sunt "));
                        document.add(new Paragraph(" "));
                        List<String> ceaMaiGrea = new ArrayList<>();
                        Arrays.sort(a, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    return ((Map.Entry<TemaLaborator, Integer>) o2).getValue()
                                            .compareTo(((Map.Entry<TemaLaborator, Integer>) o1).getValue());
                                }
                            });
                            for (Object e : a) {
                                ceaMaiGrea.add(String.valueOf(((Map.Entry<TemaLaborator, Integer>) e).getKey().getNumarTema()) + " " + ((Map.Entry<TemaLaborator, Integer>) e).getKey().getCerinta() + " " + String.valueOf(((Map.Entry<TemaLaborator, Integer>) e).getValue()));
                            }
                        for(String s : ceaMaiGrea){
                            document.add(new Paragraph(s));
                        }
                        document.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (DocumentException e){
                            e.printStackTrace();
                        }

                    }
                });
                Stage stage = new Stage();
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(0);
                xAxis.setUpperBound(20);
                xAxis.setTickUnit(1);
                xAxis.setLabel("Teme");
                VBox.setMargin(btnExport,new Insets(10));
                final LineChart<Number,Number> lineChart =
                        new LineChart<Number,Number>(xAxis,yAxis);
                lineChart.setTitle("Cea mai grea tema");
                lineChart.setPrefHeight(580);
                XYChart.Series series = new XYChart.Series();
                series.setName("Tema");
                anchorPane.setAlignment(Pos.BOTTOM_RIGHT);
                anchorPane.getChildren().addAll(lineChart,btnExport);
                for(Map.Entry<TemaLaborator, Integer> entry : service.ceaMaiGreaTema().entrySet()){
                    series.getData().add(new XYChart.Data(entry.getKey().getNumarTema(), entry.getValue()));
                }
                Scene scene  = new Scene(anchorPane,800,600);
                lineChart.getData().add(series);
                stage.setScene(scene);
                stage.show();
            }
        });

        btnStudentiExamen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox anchorPane = new VBox();
                JFXButton btnExport = new JFXButton("Export");
                btnExport.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            Document document = new Document(PageSize.A4);
                            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Resources\\PDFs\\potIntraInExamen.pdf"));
                            document.open();
                            document.add(new Paragraph("Pot intra in examen urmatorii "));
                            document.add(new Paragraph(" "));
                            for(Map.Entry<Student, Integer> entry : service.potIntraInExamen().entrySet()){
                                document.add(new Paragraph(entry.getKey().getNume() + " " +
                                        String.valueOf(entry.getValue())));
                            }
                            document.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (DocumentException e){
                            e.printStackTrace();
                        }
                    }
                });
                Stage stage = new Stage();
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Studenti");
                VBox.setMargin(btnExport,new Insets(10));
                final LineChart<Number,Number> lineChart =
                        new LineChart<Number,Number>(xAxis,yAxis);
                lineChart.setTitle("Studenti in examen");
                lineChart.setPrefHeight(580);
                XYChart.Series series = new XYChart.Series();
                series.setName("Studenti");
                anchorPane.setAlignment(Pos.BOTTOM_RIGHT);
                anchorPane.getChildren().addAll(lineChart,btnExport);
                for(int i = 1; i < 11 ; i++){
                    series.getData().add(new XYChart.Data(i, service.catiAuMedia(i)));
                }
                Scene scene  = new Scene(anchorPane,800,600);
                lineChart.getData().add(series);
                stage.setScene(scene);
                stage.show();
            }
        });

        btnPredatLaTimp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox anchorPane = new VBox();
                JFXButton btnExport = new JFXButton("Export");
                btnExport.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            Document document = new Document(PageSize.A4);
                            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Resources\\PDFs\\temePredateLaTimp.pdf"));
                            document.open();
                            document.add(new Paragraph("Au temele predate la timp "));
                            document.add(new Paragraph(" "));
                            for(String nume : service.auPredatLaTimpTemele()){
                                document.add(new Paragraph(nume));
                            }
                            document.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (DocumentException e){
                            e.printStackTrace();
                        }
                    }
                });
                VBox.setMargin(btnExport,new Insets(10));
                Stage stage = new Stage();
                ListView<String> lineChart = new ListView<>();
                lineChart.setPrefHeight(550);
                lineChart.setItems(FXCollections.observableArrayList(service.auPredatLaTimpTemele()));
                XYChart.Series series = new XYChart.Series();
                series.setName("Studenti");
                anchorPane.setAlignment(Pos.BOTTOM_RIGHT);
                anchorPane.getChildren().addAll(lineChart,btnExport);
                Scene scene  = new Scene(anchorPane,800,600);
                stage.setScene(scene);
                stage.show();
            }
        });

        btnTopulGrupelor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox anchorPane = new VBox();
                JFXButton btnExport = new JFXButton("Export");
                Object[] a = service.mediaPeGrupe().entrySet().toArray();

                btnExport.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            Document document = new Document(PageSize.A4);
                            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Iustin\\IdeaProjects\\LAB3\\src\\Resources\\PDFs\\topulGrupelor.pdf"));
                            document.open();
                            document.add(new Paragraph("Topul grupelor "));
                            document.add(new Paragraph(" "));
                            List<String> grupeTop = new ArrayList<>();
                            Arrays.sort(a, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    return ((Map.Entry<TemaLaborator, Integer>) o2).getValue()
                                            .compareTo(((Map.Entry<TemaLaborator, Integer>) o1).getValue());
                                }
                            });

                            for (Object e : a) {
                                grupeTop.add(String.valueOf(((Map.Entry<Integer, Integer>) e).getKey()) + " " + ((Map.Entry<Integer, Integer>) e).getValue());
                            }
                            for(String nume : grupeTop){
                                document.add(new Paragraph(nume));
                            }
                            document.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (DocumentException e){
                            e.printStackTrace();
                        }
                    }
                });
                Stage stage = new Stage();
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(220);
                xAxis.setUpperBound(228);
                xAxis.setTickUnit(1);
                xAxis.setLabel("Grupe");
                VBox.setMargin(btnExport,new Insets(10));
                final LineChart<Number,Number> lineChart =
                        new LineChart<Number,Number>(xAxis,yAxis);
                lineChart.setTitle("Topul grupelor");
                lineChart.setPrefHeight(580);
                XYChart.Series series = new XYChart.Series();
                series.setName("Grupe");
                anchorPane.setAlignment(Pos.BOTTOM_RIGHT);
                anchorPane.getChildren().addAll(lineChart,btnExport);
                for(Map.Entry<Integer, Integer> entry : service.mediaPeGrupe().entrySet()){
                    series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                }
                Scene scene  = new Scene(anchorPane,800,600);
                lineChart.getData().add(series);
                stage.setScene(scene);
                stage.show();
            }
        });


        comboBoxItems.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                index.set(Integer.valueOf(newValue.toString()));
                page.setValue(0);
                if(index.getValue() + page.getValue() >= service.getAllNote().size())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);
                setupData();
            }
        });
        page.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.intValue() == 0)
                    btnPrevious.setDisable(true);
                else
                    btnPrevious.setDisable(false);
                if(newValue.intValue() >= service.getAllNote().size() - index.getValue())
                    btnForward.setDisable(true);
                else
                    btnForward.setDisable(false);
            }
        });
        btnForward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(index.getValue() + page.getValue());
                setupData();
            }
        });
        btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                page.setValue(page.getValue() - index.getValue());
                setupData();
            }
        });
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = new Scene(ControllerLogin.initView(), 800, 500);
                scene.getStylesheets().add("Resources/CSS/stylesheets.css");
                Main.st.setScene(scene);
            }
        });
        btnSignOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.logOut();
            }
        });
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
            Scene addScene = new Scene(root, 1013, 578);
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
        textFieldSaptPredarii.setText(String.valueOf(nota.getValoare()));
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
        Predicate<Nota> predFinal = (nota -> true);
        boolean ok1 = false, ok2 = false, ok3 = false;
        if(!comboBoxNota.getSelectionModel().getSelectedItem().toString().equals("0"))
            predFinal = predFinal.and(Filter.areValoareaMaiMica(Integer.valueOf(comboBoxNota.getSelectionModel().getSelectedItem().toString())));
        else
            ok1 = true;
        if(!comboBoxSaptPredarii.getSelectionModel().getSelectedItem().toString().equals("0"))
            predFinal = predFinal.and(Filter.areSaptPredariiMaiMica(Integer.valueOf(comboBoxSaptPredarii.getSelectionModel().getSelectedItem().toString())));
        else
            ok2 = true;
        if(!(ok1 == true && ok2 == true)){
            List<Nota> filtered = this.service.getAllNote().stream().filter(predFinal).collect(Collectors.toList());
            model.setAll(FXCollections.observableArrayList(filtered));
            btnForward.setDisable(true);
            btnPrevious.setDisable(true);
            comboBoxItems.setDisable(true);
        }else{
            setupData();
            btnPrevious.setDisable(false);
            btnForward.setDisable(false);
            comboBoxItems.setDisable(false);
        }
    }

    public void updateNota(){
        try{
            this.service.update(new Nota(Integer.valueOf(textFieldIdStud.getText()),Integer.valueOf(textFieldNrTema.getText()),Integer.valueOf(textFieldNota.getText()),Integer.valueOf(textFieldSaptPredarii.getText())),textFieldObservatii.getText());
            showMessage(Alert.AlertType.INFORMATION, "Update", "Nota a fost updatata cu succes!");
        }catch (ValidationException e){
            showErrorMessage(e.getMessage());
        }
    }

    public void setupData(){
        if(index.getValue() + page.getValue() >= service.getAllNote().size())
            btnForward.setDisable(true);
        else
            btnForward.setDisable(false);
        model.setAll(FXCollections.observableArrayList(this.service.getBetween(index.getValue(), page.getValue())));
    }


    public void setService(NotaService service){
        this.service = service;
        this.model = FXCollections.observableArrayList(this.service.getBetween(index.getValue(), page.getValue()));
        tableView.setItems(this.model);
        if(service.getAllNote().size() <= index.getValue())
            btnForward.setDisable(true);
    }

    @Override
    public void notifyEvent(ListEvent<Nota> e) {
        //model.setAll(FXCollections.observableArrayList(this.service.getAllNote()));
        setupData();
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
