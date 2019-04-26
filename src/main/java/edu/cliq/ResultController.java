package edu.cliq;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class ResultController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private MenuItem export;

    private List<DataController.Row> rows;

    public  static int chart_no = 0;
    @FXML
    private LineChart<String, Double> chart_qc, chart_fs, chart_u, chart_qt, chart_RFP, chart_gamma, chart_IcSBT,chart_sigma,chart_u0,chart_sigma1,chart_qtn,
            chart_lc, chart_fcPercent,chart_rd,chart_CSR,chart_MSF,chart_Qc1ncs,chart_Ksigma,chart_CSRStar,chart_Kc,chart_CSR7,chart_FS,chart_PI,chart_LPI,chart_LPI1,chart_SU;
    @FXML
    private BarChart<String, Double> chart_SBT, chart_SBTn;
    public static XYChart.Series series_qc = new XYChart.Series();
    public static XYChart.Series series_fs = new XYChart.Series();
    public static XYChart.Series series_u = new XYChart.Series();
    public static XYChart.Series series_qt = new XYChart.Series();
    public static XYChart.Series series_RFP = new XYChart.Series();
    public static XYChart.Series series_gamma = new XYChart.Series();
    public static XYChart.Series series_IcSBT = new XYChart.Series();
    public static XYChart.Series series_SBT = new XYChart.Series();
    public static XYChart.Series series_sigma = new XYChart.Series();
    public static XYChart.Series series_u0 = new XYChart.Series();
    public static XYChart.Series series_sigma1 = new XYChart.Series();
    public static XYChart.Series series_qtn = new XYChart.Series();
    public static XYChart.Series series_lc = new XYChart.Series();
    public static XYChart.Series series_chart_SBTn = new XYChart.Series();
    public static XYChart.Series series_fcPercent = new XYChart.Series();
    public static XYChart.Series series_rd = new XYChart.Series();
    public static XYChart.Series series_CSR = new XYChart.Series();
    public static XYChart.Series series_MSF = new XYChart.Series();
    public static XYChart.Series series_Qc1ncs = new XYChart.Series();
    public static XYChart.Series series_Ksigma = new XYChart.Series();
    public static XYChart.Series series_CSRStar = new XYChart.Series();
    public static XYChart.Series series_Kc = new XYChart.Series();
    public static XYChart.Series series_CSR7 = new XYChart.Series();
    public static XYChart.Series series_FS = new XYChart.Series();
    public static XYChart.Series series_PI = new XYChart.Series();
    public static XYChart.Series series_LPI = new XYChart.Series();
    public static XYChart.Series series_LPI1 = new XYChart.Series();
    public static XYChart.Series series_SU = new XYChart.Series();
    private void handleButtonAction(ActionEvent event) throws IOException {
         if(event.getSource().equals(export)) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Result");
            Map<String, Object[]> data = new LinkedHashMap<>();

            int sno = 1;
            for (int i = 0; i < rows.size(); i++) {
                DataController.Row row = rows.get(i);
                Object object[] = new Object[row.getCells().size()];
                for (int j = 0; j < row.getCells().size(); j++) {
                    object[j] = row.getCells().get(j);
                }
                data.put("" + sno, object);
                sno++;
            }
            //Iterate over data and write to sheet
            Set<String> keyset = data.keySet();
            int rownum = 0;
            for (String key : keyset) {
                Row ro = sheet.createRow(rownum++);
                Object[] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = ro.createCell(cellnum++);
                    cell.setCellValue(obj.toString());
                }
            }
            try {
                //Write the workbook in file system
                FileOutputStream out = new FileOutputStream(new File("result.xlsx"));
                workbook.write(out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setNodeStyle(XYChart.Data<String, Double> data) {
        Node node = data.getNode();
        if (data.getYValue().intValue() > 8) {
            node.setStyle("-fx-bar-fill: -fx-exceeded;");
        } else if (data.getYValue().intValue() > 5) {
            node.setStyle("-fx-bar-fill: -fx-achieved;");
        } else {
            node.setStyle("-fx-bar-fill: -fx-not-achieved;");
        }
    }

    private void setToolTips(){
        Tooltip tooltip = new Tooltip("Double click to view");
        Tooltip.install(chart_qc, tooltip);
        Tooltip.install(chart_fs, tooltip);
        Tooltip.install(chart_u, tooltip);
        Tooltip.install(chart_qt, tooltip);
        Tooltip.install(chart_RFP, tooltip);
        Tooltip.install(chart_gamma, tooltip);
        Tooltip.install(chart_IcSBT, tooltip);
        Tooltip.install(chart_SBT, tooltip);
        Tooltip.install(chart_sigma, tooltip);
        Tooltip.install(chart_u0, tooltip);
        Tooltip.install(chart_sigma1, tooltip);
        Tooltip.install(chart_qtn, tooltip);
        Tooltip.install(chart_lc, tooltip);
        Tooltip.install(chart_SBTn, tooltip);
        Tooltip.install(chart_fcPercent, tooltip);
        Tooltip.install(chart_rd, tooltip);
        Tooltip.install(chart_CSR, tooltip);
        Tooltip.install(chart_MSF, tooltip);
        Tooltip.install(chart_Qc1ncs, tooltip);
        Tooltip.install(chart_Ksigma, tooltip);
        Tooltip.install(chart_CSRStar, tooltip);
        Tooltip.install(chart_Kc, tooltip);
        Tooltip.install(chart_CSR7, tooltip);
        Tooltip.install(chart_FS, tooltip);
        Tooltip.install(chart_PI, tooltip);
        Tooltip.install(chart_LPI, tooltip);
        Tooltip.install(chart_LPI1, tooltip);
        Tooltip.install(chart_SU, tooltip);

    }

    private void ChartClick(){
        chart_qc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 0;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_fs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 1;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_u.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 2;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_qt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 3;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_RFP.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 4;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_gamma.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 5;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_IcSBT.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 6;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_SBT.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 7;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_sigma.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 8;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_u0.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 9;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_sigma1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 10;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_qtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 11;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_lc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 12;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_SBTn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 13;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_fcPercent.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 14;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_rd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 15;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_CSR.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no =  16;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_MSF.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 17;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_Qc1ncs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 18;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_Ksigma.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 19;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_CSRStar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 20;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_Kc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 21;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_CSR7.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 22;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_FS.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 23;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_PI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 24;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_LPI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 25;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_LPI1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 26;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });
        chart_SU.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    chart_no = 27;
                    Parent window = null;
                    try {
                        window = FXMLLoader.load(getClass().getResource("/edu.cliq/chartController.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Scene newScene;
                    newScene = new Scene(window);
                    Stage mainWindow;
                    mainWindow =  new Stage();
                    mainWindow.setScene(newScene);
                    mainWindow.show();
                }
            }
        });

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ChartClick();
        setToolTips();

        export.setOnAction(event -> {
            try {
                handleButtonAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        rows = SplitList(DataController.rows);
        int max = getMaxCells(rows);
        makeColumns(max, tableView);
        tableView.getItems().addAll(rows);


        chart_qc.getData().clear();
        chart_fs.getData().clear();
        chart_u.getData().clear();
        chart_qt.getData().clear();
        chart_RFP.getData().clear();
        chart_gamma.getData().clear();
        chart_IcSBT.getData().clear();
        chart_SBT.getData().clear();
        chart_sigma.getData().clear();
        chart_u0.getData().clear();
        chart_sigma1.getData().clear();
        chart_qtn.getData().clear();
        chart_lc.getData().clear();
        chart_SBTn.getData().clear();
        chart_fcPercent.getData().clear();
        chart_rd.getData().clear();
        chart_CSR.getData().clear();
        chart_MSF.getData().clear();
        chart_Qc1ncs.getData().clear();
        chart_Ksigma.getData().clear();
        chart_CSRStar.getData().clear();
        chart_Kc.getData().clear();
        chart_CSR7.getData().clear();
        chart_FS.getData().clear();
        chart_PI.getData().clear();
        chart_LPI.getData().clear();
        chart_LPI1.getData().clear();
        chart_SU.getData().clear();
        for(int i = 2; i < rows.size(); i++){
            DataController.Row r = rows.get(i);

            double depth =  (double) Math.round(Double.valueOf(r.getCells().get(1).toString()).floatValue() * 100) / 100;
            double qc = (double) Math.round(Double.valueOf(r.getCells().get(2).toString()).floatValue() * 100) / 100;
            double fs = (double) Math.round(Double.valueOf(r.getCells().get(3).toString()).floatValue() * 100) / 100;
            double u = (double) Math.round(Double.valueOf(r.getCells().get(4).toString()).floatValue() * 100) / 100;
            double qt = (double) Math.round(Double.valueOf(r.getCells().get(5).toString()).floatValue() * 100) / 100;
            double RFP = (double) Math.round(Double.valueOf(r.getCells().get(6).toString()).floatValue() * 100) / 100;
            double gamma = (double) Math.round(Double.valueOf(r.getCells().get(7).toString()).floatValue() * 100) / 100;
            double IcSBT = (double) Math.round(Double.valueOf(r.getCells().get(8).toString()).floatValue() * 100) / 100;
            double SBT = (double) Math.round(Double.valueOf(r.getCells().get(9).toString()).floatValue() * 100) / 100;
            double sigma = (double) Math.round(Double.valueOf(r.getCells().get(10).toString()).floatValue() * 100) / 100;
            double u0 = (double) Math.round(Double.valueOf(r.getCells().get(11).toString()).floatValue() * 100) / 100;
            double sigma1 = (double) Math.round(Double.valueOf(r.getCells().get(12).toString()).floatValue() * 100) / 100;
            double qtn = (double) Math.round(Double.valueOf(r.getCells().get(13).toString()).floatValue() * 100) / 100;
            double lc = (double) Math.round(Double.valueOf(r.getCells().get(14).toString()).floatValue() * 100) / 100;
            double SBTn = (double) Math.round(Double.valueOf(r.getCells().get(15).toString()).floatValue() * 100) / 100;
            double fcPercent = (double) Math.round(Double.valueOf(r.getCells().get(16).toString()).floatValue() * 100) / 100;
            double rd = (double) Math.round(Double.valueOf(r.getCells().get(17).toString()).floatValue() * 100) / 100;
            double CSR = (double) Math.round(Double.valueOf(r.getCells().get(18).toString()).floatValue() * 100) / 100;
            double MSF = (double) Math.round(Double.valueOf(r.getCells().get(19).toString()).floatValue() * 100) / 100;
            double Qc1ncs = (double) Math.round(Double.valueOf(r.getCells().get(20).toString()).floatValue() * 100) / 100;
            double Ksigma = (double) Math.round(Double.valueOf(r.getCells().get(21).toString()).floatValue() * 100) / 100;
            double CSRStar = (double) Math.round(Double.valueOf(r.getCells().get(22).toString()).floatValue() * 100) / 100;
            double Kc = (double) Math.round(Double.valueOf(r.getCells().get(23).toString()).floatValue() * 100) / 100;
            double CSR7 = (double) Math.round(Double.valueOf(r.getCells().get(24).toString()).floatValue() * 100) / 100;
            double FS = (double) Math.round(Double.valueOf(r.getCells().get(25).toString()).floatValue() * 100) / 100;
            double PI = (double) Math.round(Double.valueOf(r.getCells().get(26).toString()).floatValue() * 100) / 100;
            double LPI = (double) Math.round(Double.valueOf(r.getCells().get(27).toString()).floatValue() * 100) / 100;
            double LPI1 = (double) Math.round(Double.valueOf(r.getCells().get(28).toString()).floatValue() * 100) / 100;
            double SU = (double) Math.round(Double.valueOf(r.getCells().get(29).toString()).floatValue() * 100) / 100;
            series_qc.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), qc));
            series_fs.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), fs));
            series_u.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),u));
            series_qt.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), qt));
            series_RFP.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),RFP));
            series_gamma.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),gamma));
            series_IcSBT.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),IcSBT));
            series_SBT.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), SBT));
            series_sigma.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), sigma));
            series_u0.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), u0));
            series_sigma1.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), sigma1));
            series_qtn.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), qtn));
            series_lc.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),lc));
            series_chart_SBTn.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),SBTn));
            series_fcPercent.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),fcPercent));
            series_rd.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),rd));
            series_CSR.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), CSR));
            series_MSF.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),MSF));
            series_Qc1ncs.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),Qc1ncs));
            series_Ksigma.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),Ksigma));
            series_CSRStar.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),CSRStar));
            series_Kc.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),Kc));
            series_CSR7.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),CSR7));
            series_FS.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), FS));
            series_PI.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth), PI));
            series_LPI.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),LPI));
            series_LPI1.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),LPI1));
            series_SU.getData().add(new XYChart.Data<String, Double>(String.valueOf(depth),SU));
        }
        chart_qc.getData().add(series_qc);
        chart_fs.getData().add(series_fs);
        chart_u.getData().add(series_u);
        chart_qt.getData().add(series_qt);
        chart_RFP.getData().add(series_RFP);
        chart_gamma.getData().add(series_gamma);
        chart_IcSBT.getData().add(series_IcSBT);
        chart_SBT.getData().add(series_SBT);
        chart_sigma.getData().add(series_sigma);
        chart_u0.getData().add(series_u0);
        chart_sigma1.getData().add(series_sigma1);
        chart_qtn.getData().add(series_qtn);
        chart_lc.getData().add(series_lc);
        chart_SBTn.getData().add(series_chart_SBTn);
        chart_fcPercent.getData().add(series_fcPercent);
        chart_rd.getData().add(series_rd);
        chart_CSR.getData().add(series_CSR);
        chart_MSF.getData().add(series_MSF);
        chart_Qc1ncs.getData().add(series_Qc1ncs);
        chart_Ksigma.getData().add(series_Ksigma);
        chart_CSRStar.getData().add(series_CSRStar);
        chart_Kc.getData().add(series_Kc);
        chart_CSR7.getData().add(series_CSR7);
        chart_FS.getData().add(series_FS);
        chart_PI.getData().add(series_PI);
        chart_LPI.getData().add(series_LPI);
        chart_LPI1.getData().add(series_LPI1);
        chart_SU.getData().add(series_SU);



    }

    public int getMaxCells(List<DataController.Row> rows)
    {
        int max = 0;
        for (DataController.Row row : rows)
            max = Math.max(max, row.getCells().size());
        return max;
    }

    public void makeColumns(int count, TableView<DataController.Row> tableView)
    {
        for (int m = 0; m < count; m++)
        {
            TableColumn<DataController.Row, String> column = new TableColumn<>(Integer.toString(m));
            column.setCellValueFactory(param -> {
                int index = param.getTableView().getColumns().indexOf(param.getTableColumn());
                List<DataController.Cell> cells = param.getValue().getCells();
                return new SimpleStringProperty(cells.size() > index ? cells.get(index).toString() : null);
            });
            tableView.getColumns().add(column);
        }
    }

    private List<DataController.Row> SplitList(List<DataController.Row> rows){
        chart_qc.getData().clear();
        int va = 0;
        double sum  = 0 ;
        boolean ch = false, ch1 = false;
        double p_value = 0, p_sigma = 0, m = 1, pre_m = 1;
        int count = 0;
        boolean check = false;
        List<DataController.Row> result = new ArrayList<>();
        int i = 0;
        int var = 1;
        for(DataController.Row row : rows){
            if(i >= DataController.data -1 || i == 0) {
                DataController.Row r = new DataController.Row();
                for (int j = 0; j < 30; j++) {
                    if(i == 0){
                        DataController.Cell cell = null;
                        switch (j){
                            case 0:
                                cell = new DataController.Cell("S#");
                                break;
                            case 1:
                                cell = new DataController.Cell("Depth");
                                break;
                            case 2:
                                cell = new DataController.Cell("qc");
                                break;
                            case 3:
                                cell = new DataController.Cell("fs");
                                break;
                            case 4:
                                cell = new DataController.Cell("u");
                                break;
                            case 5:
                                cell = new DataController.Cell("qt");
                                break;
                            case 6:
                                cell = new DataController.Cell("RF(%)");
                                break;
                            case 7:
                                cell = new DataController.Cell("γ");
                                break;
                            case 8:
                                cell = new DataController.Cell("Ic SBT");
                                break;
                            case 9:
                                cell = new DataController.Cell("SBT");
                                break;
                            case 10:
                                cell = new DataController.Cell("σ");
                                break;
                            case 11:
                                cell = new DataController.Cell("u0");
                                break;
                            case 12:
                                cell = new DataController.Cell("σ'");
                                break;
                            case 13:
                                cell = new DataController.Cell("Qtn");
                                break;
                            case 14:
                                cell = new DataController.Cell("Ic");
                                break;
                            case 15:
                                cell = new DataController.Cell("SBTn");
                                break;
                            case 16:
                                cell = new DataController.Cell("fc(%)");
                                break;
                            case 17:
                                cell = new DataController.Cell("rd");
                                break;
                            case 18:
                                cell = new DataController.Cell("CSR");
                                break;
                            case 19:
                                cell = new DataController.Cell("MSF");
                                break;
                            case 20:
                                cell = new DataController.Cell("Qc1ncs");
                                break;
                            case 21:
                                cell = new DataController.Cell("Kσ");
                                break;
                            case 22:
                                cell = new DataController.Cell("CSR*");
                                break;
                            case 23:
                                cell = new DataController.Cell("Kc");
                                break;
                            case 24:
                                cell = new DataController.Cell("CRR7.5");
                                break;
                            case 25:
                                cell = new DataController.Cell("FS");
                                break;
                            case 26:
                                cell = new DataController.Cell("Pl");
                                break;
                            case 27:
                                cell = new DataController.Cell("LPI");
                                break;
                            case 28:
                                cell = new DataController.Cell("LPI");
                                break;
                            case 29:
                                cell = new DataController.Cell("SU");
                                break;
                            default:break;
                        }
                        r.getCells().add(cell);
                    }
                    else if(j == 0){
                        DataController.Cell cell = new DataController.Cell(String.valueOf(var));
                        r.getCells().add(cell);
                        var++;
                    }
                    else if (j == DataController.fs || j == DataController.qc || j == DataController.depth || j == DataController.u && count < 4 && !check) {
                            r.getCells().add(row.getCells().get(j));
                            count++;
                    }
                    if(check){
                        double a,b,c, depth, sig, sig1;
                        switch (j){
                            case 5:
                                a = Double.valueOf(r.getCells().get(2).toString()).floatValue();
                                b = Double.valueOf(r.getCells().get(4).toString()).floatValue();
                                double value = (double) Math.round(get_qt(a,b) * 100) / 100 ;
                                DataController.Cell cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 6:
                                b = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                a = Double.valueOf(r.getCells().get(3).toString()).floatValue();
                                value = (double) Math.round(get_RF(a,b) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 7:
                                b = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                a = Double.valueOf(r.getCells().get(6).toString()).floatValue();
                                value = (double) Math.round(get_gamma(a,b) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 8:
                                a = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                b = Double.valueOf(r.getCells().get(3).toString()).floatValue();
                                depth =  Double.valueOf(r.getCells().get(1).toString()).floatValue();
                                if(!ch) {
                                    sig = depth * Double.valueOf(r.getCells().get(7).toString()).floatValue();
                                    ch = true;
                                }
                                else{
                                    sig = (depth - p_value) * Double.valueOf(r.getCells().get(7).toString()).floatValue() + p_sigma;
                                }
                                c = get_sigma(p_value, depth, sig, p_sigma);
                                value = (double) Math.round(get_ICSBT(a,b,c) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                p_value = depth;
                                p_sigma =sig;
                                break;

                            case 9:
                                a = Double.valueOf(r.getCells().get(8).toString()).floatValue();
                                value = Math.round(get_SBT(a));
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;

                            case 10:
                                depth =  Double.valueOf(r.getCells().get(1).toString()).floatValue();
                                if(!ch1) {
                                    sig = depth * Double.valueOf(r.getCells().get(7).toString()).floatValue();
                                    ch1 = true;
                                }
                                else{
                                    sig = (depth - p_value) * Double.valueOf(r.getCells().get(7).toString()).floatValue() + p_sigma;
                                }
                                value = (double) Math.round(sig * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                p_value = depth;
                                p_sigma = sig;
                                break;

                            case 11:
                                depth = Double.valueOf(r.getCells().get(1).toString()).floatValue();
                                value = (double) Math.round(get_u0(depth) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 12:
                                sig = Double.valueOf(r.getCells().get(10).toString()).floatValue();
                                a = Double.valueOf(r.getCells().get(11).toString()).floatValue();
                                value = (double) Math.round(get_sigma1(sig, a) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 13:
                                int co = 0;
                                double n = 1, n_pre = 1;
                                double qtn = 0;
                                a = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                sig = Double.valueOf(r.getCells().get(10).toString()).floatValue();
                                sig1 = Double.valueOf(r.getCells().get(12).toString()).floatValue();
                                do{
                                    n = n_pre;
                                    qtn = get_qtn(a, sig, sig1, n);
                                    b = Double.valueOf(r.getCells().get(3).toString()).floatValue();
                                    double ic = get_ic(a, b, sig, qtn);
                                    n_pre = get_n(ic, sig1);
                                    co++;
                                    if(co == 100){
                                        break;
                                    }

                                }
                                while(!((Math.abs(n - n_pre)) < 0.001));

                                a = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                sig = Double.valueOf(r.getCells().get(10).toString()).floatValue();
                                sig1 = Double.valueOf(r.getCells().get(12).toString()).floatValue();
                                value = (double) Math.round(qtn * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 14:
                                a = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                b = Double.valueOf(r.getCells().get(3).toString()).floatValue();
                                sig = Double.valueOf(r.getCells().get(10).toString()).floatValue();
                                qtn = Double.valueOf(r.getCells().get(13).toString()).floatValue();
                                value = (double) Math.round(get_ic(a,b,sig,qtn) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 15:
                                a = Double.valueOf(r.getCells().get(14).toString()).floatValue();
                                value = (double) Math.round(get_SBTN(a) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 16:
                                a = Double.valueOf(r.getCells().get(14).toString()).floatValue();
                                value = (double) Math.round(get_FC(a) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 17:
                                depth = Double.valueOf(r.getCells().get(1).toString()).floatValue();
                                value = (double) Math.round(get_rd(depth, DataController.m) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 18:
                                sig = Double.valueOf(r.getCells().get(10).toString()).floatValue();
                                sig1 = Double.valueOf(r.getCells().get(12).toString()).floatValue();
                                a  = Double.valueOf(r.getCells().get(17).toString()).floatValue();
                                value = (double) Math.round(get_CSR(sig, sig1 , DataController.pga, a) * 1000) / 1000;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 19:
                                value = (double) Math.round(get_MSF(DataController.method, DataController.m) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 20:
                                co = 0;
                                do {
                                    m = pre_m;
                                    sig1 = Double.valueOf(r.getCells().get(12).toString()).floatValue();
                                    a = get_Cn(sig1, m);
                                    b = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                    c = Double.valueOf(r.getCells().get(16).toString()).floatValue();
                                    pre_m = get_m(get_qc1ncs(a, b, c));
                                    co++;
                                    if(co == 100){
                                        break;
                                    }
                                }
                                while (!(Math.abs(m - pre_m) < 0.001));


                                a = get_Cn(sig1, m);
                                b = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                c = Double.valueOf(r.getCells().get(16).toString()).floatValue();
                                value = (double) Math.round(get_qc1ncs(a, b, c) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                pre_m = 1;
                                break;
                            case 21:
                                a = Double.valueOf(r.getCells().get(20).toString()).floatValue();
                                a  = get_Csigma(a);
                                b = Double.valueOf(r.getCells().get(12).toString()).floatValue();
                                value = (double) Math.round(get_Ksigma(a,b) * 10000) / 10000;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 22:
                                a = Double.valueOf(r.getCells().get(18).toString()).floatValue();
                                b = Double.valueOf(r.getCells().get(19).toString()).floatValue();
                                c = Double.valueOf(r.getCells().get(21).toString()).floatValue();
                                value = (double) Math.round(get_CSR1(a,b,c) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 23:
                                a = Double.valueOf(r.getCells().get(14).toString()).floatValue();
                                value = (double) Math.round(get_KC(a) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 24:
                                a = Double.valueOf(r.getCells().get(20).toString()).floatValue();
                                value = (double) Math.round(get_CRR(DataController.method, a) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 25:
                                a = Double.valueOf(r.getCells().get(24).toString()).floatValue();
                                b = Double.valueOf(r.getCells().get(22).toString()).floatValue();
                                value = (double) Math.round(get_FS(a,b) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 26:
                                a = Double.valueOf(r.getCells().get(25).toString()).floatValue();
                                value = (double) Math.round(get_PL(a) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 27:
                                a = Double.valueOf(r.getCells().get(1).toString()).floatValue();
                                b = Double.valueOf(r.getCells().get(25).toString()).floatValue();
                                value = (double) Math.round(get_LPI(a,b) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 28:

                                for(int ii = var; ii < DataController.rows.size(); ii++) {
                                    DataController.Row rr = DataController.rows.get(i);
                                    sum = sum + Double.valueOf(r.getCells().get(27).toString()).floatValue();
                                }
                                va++;
                                value = (double) Math.round(sum * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                break;
                            case 29:
                                a = Double.valueOf(r.getCells().get(5).toString()).floatValue();
                                sig = Double.valueOf(r.getCells().get(10).toString()).floatValue();
                                value = (double) Math.round(get_SU(a,sig, DataController.nkt) * 100) / 100;
                                cell = new DataController.Cell(String.valueOf(value));
                                r.getCells().add(cell);
                                check = false;
                                break;
                            default:
                                break;

                        }
                    }
                    if(count == 4 && !check){
                        check = true;
                        j = 4;
                        count = 0;
                    }
                }
                result.add(r);
            }
            i++;
        }
        return result;

    }


    private double get_qt(double qc, double u){
        double qt = qc + u*(1 - DataController.coneArea);
        return qt;
    }

    private double get_RF(double fs, double qt){
        double rf;
        rf = fs/(qt * 10);
        return rf;
    }

    private double get_gamma(double rf, double qt){
        double gamma;
        gamma = 9.81* (0.27*(Math.log10(rf)) + 0.36 * Math.log10(qt/0.101) + 1.236);
        return gamma;
    }

    private double get_sigma(double p_value, double depth, double gamma, double p_sigma){
        double sigma;
        sigma = ((depth - p_value) * gamma) + p_sigma;
        return sigma;

    }
    private double get_u0(double depth){
        double u0;
        u0 = (depth - DataController.d) * 9.81;
        return u0;
    }

    private double get_sigma1(double sigma, double u){
        double sigma1;
        sigma1 = sigma - u;
        return sigma1;
    }

    private double get_ICSBT(double qt, double fs, double sigma){
        double ICSBT;
        double f  = (fs/(qt * 1000 - sigma) * 100);
        ICSBT = Math.sqrt(Math.pow((3.47 - Math.log10((qt/0.101))), 2) + Math.pow(Math.log10(f) + 1.22,2));
        return ICSBT;
    }

    private double get_SBT(double ICSBT){
       if(ICSBT > 3.6){
           return 2;
       }
       else if(ICSBT > 2.95 && ICSBT < 3.6){
           return 3;
       }
       else if(ICSBT > 2.6 && ICSBT < 2.95){
           return 4;
       }
       else if(ICSBT > 2.05 && ICSBT < 2.6){
           return 5;
       }
       else if(ICSBT > 1.31 && ICSBT < 2.05){
           return 6;
       }
       else if(ICSBT < 1.31){
           return 7;
       }
       return 0;
    }

    private double get_ic(double qt, double fs, double sigma, double qtn){
        double f  = (fs/((qt * 1000) - sigma)) * 100;
        return Math.sqrt(Math.pow((3.47 - Math.log10(qtn)), 2) + Math.pow(Math.log10(f) + 1.22,2));
    }

    private double get_n(double ic, double sigma1){
       return  0.381 * ic + 0.05 * (sigma1/101) - 0.15;
    }

    private double get_qtn(double qt, double sigma, double sigma1, double n){
        double qtn = (((qt*1000) - sigma)/101) * Math.pow(101/sigma1,n);
        return qtn;
    }
    private double get_SBTN(double ic){
        if(ic > 3.6){
            return 2;
        }
        else if(ic > 2.95 && ic < 3.6){
            return 3;
        }
        else if(ic > 2.6 && ic < 2.95){
            return 4;
        }
        else if(ic > 2.05 && ic < 2.6){
            return 5;
        }
        else if(ic > 1.31 && ic < 2.05){
            return 6;
        }
        else if(ic < 1.31){
            return 7;
        }
        return 0;
    }

    private double get_rd(double depth, double m){
        double rd;
        double alpha = -1.012 - 1.126 * Math.sin((depth/11.73) + 5.133);
        double beta = 0.106 + 0.118 * Math.sin((depth/11.28) + 5.142);
        rd = Math.exp(alpha + (beta * m));
        return rd;
    }


    private double get_CSR(double sigma, double sigma1, double pga, double rd){
        double CSR;
        CSR = 0.65 * (sigma/sigma1 * pga/9.81 * rd);
        return CSR;
    }

    private double get_MSF(String method, double m){
        double MSF;
        if(method.equals(DataController.method1)){
            MSF = 174/Math.pow(m ,2.56);
        }
        else{
            MSF = 6.9 * Math.exp((-(m/4) - 0.058));
        }
        if(MSF <= 1.8){
            return MSF;
        }
        return 1.8;
    }

    private double get_Ksigma(double csigma, double sigma){
        double Ksigma;
        Ksigma = 1 - csigma * (Math.log(sigma/101));
        if(Ksigma <= 1.1){
            return Ksigma;
        }
        return 1.1;

    }

    private double get_Csigma(double qc1n){
        double Csigma;
        Csigma = 1/(37.3 - 8.27 * (Math.pow(qc1n, 0.264)));
        if(Csigma <= 0.3){
            return Csigma;
        }
        return 0.3;
    }

    private double get_m(double qc1n){
        double m;
        m = 1.338 - 0.249 * qc1n;
        return m;
    }

    private double get_Cn(double sigma1, double m){
        double Cn;
        Cn = Math.pow(101.35/sigma1, m);
        if(Cn <= 1.7){
            return Cn;
        }
        return 1.7;
    }

    private double get_FC(double ic){
        if(ic < 1.26){
            return 0;
        }
        else if(ic > 1.26 && ic < 3.5){
           return  1.75 * Math.pow(ic, 3.25) - 3.7;
        }
        return 100;
    }

    private double get_qc1ncs(double cn, double qt, double fc){
        double qc1, qc1n;
        qc1 = (cn * (qt * 1000)/101);
        qc1n = (11.9 + qc1/14.6) * Math.exp(1.63 - (9.7/(fc + 2)) - (15.7/(fc + 2))) ;
        double result = qc1 + qc1n;
        return result;
    }

    private double get_KC(double ic){
        double KC;
        if(ic <= 1.64){
            return 1;
        }
        return  -0.403 * Math.pow(ic, 4) + 5.581 * Math.pow(ic, 3) - 21.63 * Math.pow(ic, 2) + 33.75 * ic - 17.88;
    }

    private double get_CRR(String method, double qc1ncs){
        if (method.equals(DataController.method1)){
            if(qc1ncs >= 50 && qc1ncs < 160){
                return 93 * Math.pow(qc1ncs/1000, 3) + 0.08;
            }
            return 0.833 * Math.pow(qc1ncs/1000, 3) + 0.05;
        }
        else{
            return Math.exp(qc1ncs/113 + (Math.pow(qc1ncs/1000, 2)) + (Math.pow(qc1ncs/1000, 3)) + (Math.pow(qc1ncs/1000, 4)) - 2.80);
        }
    }

    private double get_CSR1(double csr, double msf, double Ksigma){
        return csr/(Ksigma * msf);
    }

    private double get_FS(double CRR, double csr1){
        return CRR/csr1;
    }

    private double get_PL(double FS){
        return 1/(1 + Math.pow((FS/0.9),6.3));
    }

    private double get_LPI(double depth, double fs){
        if(fs > 1){
            return 0;
        }
        return (1 - fs) * (10 - 0.5 * depth);
    }

    private double get_SU(double qt, double sigma, double nkt){
        return (qt * 1000 - sigma)/nkt;
    }

    public LineChart<String, Double> getChart_CSR() {
        return chart_CSR;
    }
}
