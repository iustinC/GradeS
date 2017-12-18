package UI.GUI;

import Domain.Student;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StudentView {

    private StudentController controllerStudent;

    TextField fieldId = createField();
    TextField fieldNume = createField();
    TextField fieldGrupa = createField();
    TextField fieldEmail = createField();
    TextField fieldCadruDidactic =  createField();

    Button btnClearAll = new Button("Clear All");
    Button btnAdd = new Button("Add");
    Button btnUpdate = new Button("Update");
    Button btnDelete = new Button("Delete");

    private BorderPane borderPane;

    TableView<Student> tableView = new TableView<>();

    public StudentView(StudentController controllerStudent) {
        this.controllerStudent = controllerStudent;
        initView();
    }

    private void initView() {
        borderPane = new BorderPane();
        //top AnchorPane
        borderPane.setTop(initTop());
//        //left
        borderPane.setLeft(initLeft());
//        //center
        borderPane.setCenter(initCenter());

    }

    private AnchorPane initCenter() {
        AnchorPane centerPane = new AnchorPane();
        // grid pane
        GridPane gridPane = createGridPane();

        centerPane.getChildren().add(gridPane);
        // horizontal box
        HBox hBox = createHBox();

        centerPane.getChildren().add(hBox);

        AnchorPane.setTopAnchor(gridPane, 20d);
        AnchorPane.setLeftAnchor(gridPane, 20d);
        AnchorPane.setTopAnchor(hBox, 180d);
        AnchorPane.setLeftAnchor(hBox, 50d);
        return centerPane;
    }

    private AnchorPane initTop()
    {
        AnchorPane topAnchorPane=new AnchorPane();
        Label labelTitle = new Label("CRUD Student");

        topAnchorPane.getChildren().add(labelTitle);
        AnchorPane.setTopAnchor(labelTitle,20d);
        AnchorPane.setLeftAnchor(labelTitle,100d);
        labelTitle.setFont(new Font(30));

        return topAnchorPane;
    }

    private AnchorPane initLeft() {
        AnchorPane leftAnchorPane = new AnchorPane();

        TableView<Student> tableView = createTableView();
        leftAnchorPane.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView, 20d);
        AnchorPane.setLeftAnchor(tableView, 20d);

        return leftAnchorPane;
    }

    private TableView<Student> createTableView() {
        TableColumn<Student, String> columnId = new TableColumn<>("ID");
        TableColumn<Student, String> columnNume = new TableColumn<>("Nume");
        TableColumn<Student, String> columnGrupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> columnEmail = new TableColumn<>("Email");
        TableColumn<Student, String> columnCadruDidactic = new TableColumn<>("CadruDidactic");
        tableView.getColumns().addAll(columnId, columnNume, columnGrupa, columnEmail, columnCadruDidactic);

        columnId.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        columnNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        columnCadruDidactic.setCellValueFactory(new PropertyValueFactory<Student, String>("cadruDidactic"));

        tableView.setItems(controllerStudent.getModelStudent());

        tableView.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<Student>() {
                    @Override
                    public void changed(ObservableValue<? extends Student> observable,
                                        Student oldValue, Student newValue) {
                        controllerStudent.showStudentDetails(newValue);
                        fieldId.setDisable(true);
                    }
                });
        return tableView;
    }

    public BorderPane getView(){ return borderPane;}

    private Label createLabel(String s){
        Label l=new Label(s);
        l.setFont(new Font(12));
        l.setTextFill(Color.BLACK);
        l.setStyle("-fx-font-weight: bold");
        return l;
    }

    private TextField createField() {
        TextField textField = new TextField();

        return textField;
    }

    private HBox createHBox() {
        HBox hBox = new HBox();

        hBox.setSpacing(10);

        hBox.getChildren().addAll(btnAdd, btnDelete, btnUpdate, btnClearAll);
        btnAdd.setOnAction(controllerStudent::handleAddMessage);
        btnDelete.setOnAction(controllerStudent::handleDeleteMessage);
        btnUpdate.setOnAction(controllerStudent::handleUpdateMessage);
        btnClearAll.setOnAction(controllerStudent::handleClearFields);
        return hBox;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.add(createLabel("Id"), 0, 0);
        gridPane.add(createLabel("Nume"), 0, 1);
        gridPane.add(createLabel("Grupa"), 0, 2);
        gridPane.add(createLabel("Email"), 0, 3);
        gridPane.add(createLabel("CadruDidactic"), 0, 4);

        gridPane.add(fieldId, 1, 0);
        gridPane.add(fieldNume, 1, 1);
        gridPane.add(fieldGrupa, 1, 2);
        gridPane.add(fieldEmail, 1, 3);
        gridPane.add(fieldCadruDidactic, 1, 4);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100d);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPrefWidth(200d);

        gridPane.getColumnConstraints().addAll(c1, c2);


        return gridPane;
    }
}
