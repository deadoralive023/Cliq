package edu.cliq;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChartController implements Initializable {

    @FXML
    private LineChart<String, Double> chart;

    @FXML
    private BarChart<String, Double> barChart;

    @FXML
    private Button snap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(ResultController.chart_no == 7 || ResultController.chart_no == 13){
            chart.setVisible(false);
        }
        else{
            barChart.setVisible(false);
        }

        switch (ResultController.chart_no){
            case 0:   chart.getData().add(ResultController.series_qc); break;
            case 1:   chart.getData().add(ResultController.series_fs); break;
            case 2:   chart.getData().add(ResultController.series_u); break;
            case 3:   chart.getData().add(ResultController.series_qt); break;
            case 4:   chart.getData().add(ResultController.series_RFP); break;
            case 5:   chart.getData().add(ResultController.series_gamma); break;
            case 6:   chart.getData().add(ResultController.series_IcSBT); break;
            case 7:   barChart.getData().add(ResultController.series_SBT); break;
            case 8:   chart.getData().add(ResultController.series_sigma); break;
            case 9:   chart.getData().add(ResultController.series_u0); break;
            case 10:   chart.getData().add(ResultController.series_sigma1); break;
            case 11:   chart.getData().add(ResultController.series_qtn); break;
            case 12:   chart.getData().add(ResultController.series_lc); break;
            case 13:   barChart.getData().add(ResultController.series_chart_SBTn); break;
            case 14:   chart.getData().add(ResultController.series_fcPercent); break;
            case 15:   chart.getData().add(ResultController.series_rd); break;
            case 16:   chart.getData().add(ResultController.series_CSR); break;
            case 17:   chart.getData().add(ResultController.series_MSF); break;
            case 18:   chart.getData().add(ResultController.series_Qc1ncs); break;
            case 19:   chart.getData().add(ResultController.series_Ksigma); break;
            case 20:   chart.getData().add(ResultController.series_CSRStar); break;
            case 21:   chart.getData().add(ResultController.series_Kc); break;
            case 22:   chart.getData().add(ResultController.series_CSR7); break;
            case 23:   chart.getData().add(ResultController.series_FS); break;
            case 24:   chart.getData().add(ResultController.series_PI); break;
            case 25:   chart.getData().add(ResultController.series_LPI); break;
            case 26:   chart.getData().add(ResultController.series_LPI1); break;
            case 27:   chart.getData().add(ResultController.series_SU); break;
            default:break;
        }

        snap.setOnAction(event -> {
            try {
                handleButtonAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }

    private void handleButtonAction(ActionEvent event) throws IOException {
        saveChartASPng("chart");
    }
    public void saveChartASPng(String path) {
        WritableImage image = chart.getScene().snapshot(null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFxUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
