package UI.GUI;

import Service.NotaService;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ControllerChart {

    NotaService service;

    @FXML
    JFXButton btnExport;

    @FXML
    LineChart<String, Number> lineChart;

    public void initChart(){

    }

    public void setService(NotaService service){
        this.service = service;
    }
}
