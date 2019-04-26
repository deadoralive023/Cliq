package edu.cliq;


import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private ComboBox comboBox;

    @FXML
    private Button next;

    @FXML
    private TextField textField_qc, textField_fs, textField_u, textField_data, textField_depth;
    public static final String method1 = "Robertson(2009)", method2 = "Idriss & Boulanger (2014)";

    public static  List<Row> rows;
    String headers[] = null;
    String items[] = null;

    ObservableList<ObservableList> csvData = FXCollections.observableArrayList();
    public static double qc, fs, u, data, depth, d;
    public static float coneArea,m, pga, nkt;
    public static String method;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboBox.getItems().add("Robertson(2009)");
        comboBox.getItems().add("Idriss & Boulanger (2014)");

        try {
            LoadExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        next.setOnAction(event -> {
            handleButtonAction(event);
        });
    }

    private void handleButtonAction(ActionEvent event) {
        boolean arr[] = new boolean[]{false, false, false, false, false};

        if (textField_qc.getText().matches("\\d{0,7}([\\.]\\d{0,4})?") && !textField_qc.getText().trim().isEmpty()) {
            qc = Integer.valueOf(textField_qc.getText());
            arr[0] = true;
        }
        if (textField_fs.getText().matches("\\d{0,7}([\\.]\\d{0,4})?") && !textField_fs.getText().trim().isEmpty()) {
            fs = Integer.valueOf(textField_fs.getText());
            arr[1] = true;
        }
        if (textField_u.getText().matches("\\d{0,7}([\\.]\\d{0,4})?") && !textField_u.getText().trim().isEmpty()) {
            u = Integer.valueOf(textField_u.getText());
            arr[2] = true;
        }
        if (textField_data.getText().matches("\\d{0,7}([\\.]\\d{0,4})?") && !textField_data.getText().trim().isEmpty()) {
            data = Integer.valueOf(textField_data.getText());
            arr[3] = true;
        }
        if (textField_depth.getText().matches("\\d{0,7}([\\.]\\d{0,4})?") && !textField_depth.getText().trim().isEmpty()) {
            depth = Integer.valueOf(textField_depth.getText());
            arr[4] = true;
        }
        if(arr[0] && arr[1] && arr[2] && arr[3] && arr[4] && !comboBox.getSelectionModel().isEmpty()){
            CreateDialog();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("ALl fields must be filled!");
            alert.showAndWait();
        }
    }
    private void LoadExcel() throws IOException {

        rows = makeSampleData(MainController.filePath);

        int max = getMaxCells(rows);
        makeColumns(max, tableView);
        tableView.getItems().addAll(rows);
    }

    public void makeColumns(int count, TableView<Row> tableView)
    {
        for (int m = 0; m < count; m++)
        {
            TableColumn<Row, String> column = new TableColumn<>(Integer.toString(m));
            column.setCellValueFactory(param -> {
                int index = param.getTableView().getColumns().indexOf(param.getTableColumn());
                List<Cell> cells = param.getValue().getCells();
                return new SimpleStringProperty(cells.size() > index ? cells.get(index).toString() : null);
            });
            tableView.getColumns().add(column);
        }
    }

    public int getMaxCells(List<Row> rows)
    {
        int max = 0;
        for (Row row : rows)
            max = Math.max(max, row.getCells().size());
        return max;
    }

    public List<Row> makeSampleData(String path) throws IOException {

        final Row[] e = {new Row()};
        List<Row> rows = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(path));
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        Sheet sheet = workbook.getSheetAt(0);

        DataFormatter dataFormatter = new DataFormatter();
        final int[] i = {0};
        final TableColumn[] c = new TableColumn[1];
        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                e[0].getCells().add(new Cell(cellValue));

            });
            rows.add(e[0]);
            e[0] = new Row();

        });
        return rows;

    }


    private void CreateDialog(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Enter Parameters");

// Set the button types.
        ButtonType loginButtonType = new ButtonType("Start Calculation", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField p1 = new TextField();
        p1.setPromptText("Water Table Depth");
        TextField p2 = new TextField();
        p2.setPromptText("Cone Area Ratio (a)");
        TextField p3 = new TextField();
        p3.setPromptText("Peak Ground Acceleration");
        TextField p4 = new TextField();
        p4.setPromptText("Nkt");
        TextField p5 = new TextField();
        p4.setPromptText("Earthquake (M)");

        grid.add(new Label("Water Table Depth:"), 0, 0);
        grid.add(p1, 1, 0);
        grid.add(new Label("Cone Area Ratio (a):"), 0, 1);
        grid.add(p2, 1, 1);
        grid.add(new Label("Peak Ground Acceleration:"), 0, 2);
        grid.add(p3, 1, 2);
        grid.add(new Label("Nkt:"), 0, 3);
        grid.add(p4, 1, 3);
        grid.add(new Label("Earthquake (M):"), 0, 4);
        grid.add(p5, 1, 4);

        dialog.getDialogPane().setContent(grid);

// Convert the result to a username-password-pair when the login button is clicked.

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if(!p2.getText().isEmpty()) {
                    coneArea = Float.valueOf(p2.getText().trim()).floatValue();
                }
                if(!p5.getText().isEmpty()) {
                    m = Float.valueOf(p5.getText().trim()).floatValue();
                }
                if(!p3.getText().isEmpty()) {
                    pga = Float.valueOf(p3.getText().trim()).floatValue();
                }
                if(!p4.getText().isEmpty()) {
                    nkt = Float.valueOf(p4.getText().trim()).floatValue();
                }
                if(!p1.getText().isEmpty()) {
                    d = Float.valueOf(p1.getText().trim()).floatValue();
                }
                method = comboBox.getSelectionModel().getSelectedItem().toString();
                qc = Integer.parseInt(textField_qc.getText());
                fs = Integer.parseInt(textField_fs.getText());
                u = Integer.parseInt(textField_u.getText());
                data = Integer.parseInt(textField_data.getText());
                depth = Integer.parseInt(textField_depth.getText());
                Parent window = null;
                try {
                    window = FXMLLoader.load(getClass().getResource("/edu.cliq/resultController.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene newScene;
                newScene = new Scene(window);
                Stage mainWindow = (Stage)  textField_depth.getScene().getWindow();
                mainWindow.setScene(newScene);
                mainWindow.show();
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
    }

    public static class Row
    {
        private final List<Cell> list = new ArrayList<>();

        public List<Cell> getCells()
        {
            return list;
        }
    }

    static class Cell
    {
        private final String value;

        public Cell(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }



}
