package gui;

import domain.Employee;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.EmployeeService;

import java.io.File;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> colName;
    @FXML private TableColumn<Employee, String> colFunction;
    @FXML private TableColumn<Employee, String> colMonth;
    @FXML private TableColumn<Employee, Double> colBaseSalary;
    @FXML private TableColumn<Employee, Double> colFinalSalary;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;

    @FXML private TextField newNameField;
    @FXML private TextField newFunctionField;
    @FXML private TextField newSalaryField;

    private EmployeeService employeeService;
    private ObservableList<Employee> model = FXCollections.observableArrayList();

    public void setService(EmployeeService employeeService) {
        this.employeeService = employeeService;
        initModel();
    }

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colFunction.setCellValueFactory(new PropertyValueFactory<>("functie"));
        colBaseSalary.setCellValueFactory(new PropertyValueFactory<>("salariuPeZi"));
        colMonth.setCellValueFactory(cellData -> new SimpleStringProperty("Mai-2026"));

        colFinalSalary.setCellValueFactory(cellData -> {
            Double finalSal = employeeService.getFinalSalaryForEmployee(cellData.getValue(), "Mai-2026");
            return new SimpleObjectProperty<>(finalSal);
        });

        employeeTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleViewDetails();
                }
            });
            return row;
        });

        employeeTable.setItems(model);
        sortComboBox.setItems(FXCollections.observableArrayList(
                "Nume (Alfabetic)",
                "Salariu (Crescator)",
                "Salariu (Descrescator)"
        ));
        sortComboBox.setOnAction(e -> handleSort());
        searchField.textProperty().addListener((obs, old, nv) -> handleFilter());
    }

    private void initModel() {
        model.setAll(employeeService.getAllEmployees());
    }

    public void refreshTable() {
        employeeTable.refresh();
    }

    @FXML
    public void handleViewDetails() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Eroare", "Te rugam sa selectezi un angajat din tabel!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/details-view.fxml"));
            VBox root = loader.load();
            DetailsController controller = loader.getController();
            controller.setData(selected, employeeService, this);
            Stage stage = new Stage();
            stage.setTitle("Detalii Angajat: " + selected.getNume());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Eroare", "Nu s-a putut deschide fereastra de detalii.");
        }
    }

    @FXML
    public void handleImport() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            boolean hasDuplicates = lines.stream().anyMatch(line -> employeeService.exists(line.split(",")[0].trim()));
            if (hasDuplicates) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Conflict Date Import");
                alert.setHeaderText("Au fost detectate nume care exista deja.");
                alert.setContentText("Cum doriti sa procedati?");
                ButtonType btnAll = new ButtonType("Adauga Tot");
                ButtonType btnMissing = new ButtonType("Doar ce nu am");
                ButtonType btnCancel = new ButtonType("Anuleaza", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(btnAll, btnMissing, btnCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isEmpty() || result.get() == btnCancel) return;
                processImport(lines, result.get() == btnMissing);
            } else {
                processImport(lines, false);
            }
            initModel();
            showInfo("Succes", "Import finalizat!");
        } catch (Exception e) {
            showError("Eroare", "Fisier invalid!");
        }
    }

    private void processImport(List<String> lines, boolean skipExisting) {
        for (String line : lines) {
            String[] data = line.split(",");
            if (data.length == 3) {
                String nume = data[0].trim();
                if (skipExisting && employeeService.exists(nume)) continue;
                try {
                    employeeService.saveEmployee(nume, data[1].trim(), Double.parseDouble(data[2].trim()));
                } catch (Exception ignored) {}
            }
        }
    }

    @FXML
    public void handleAddManual() {
        try {
            String nume = newNameField.getText();
            String functie = newFunctionField.getText();
            String salStr = newSalaryField.getText();
            if (nume.isEmpty() || functie.isEmpty() || salStr.isEmpty()) {
                showError("Eroare", "Toate campurile trebuie completate!");
                return;
            }
            employeeService.saveEmployee(nume, functie, Double.parseDouble(salStr));
            newNameField.clear(); newFunctionField.clear(); newSalaryField.clear();
            initModel();
        } catch (Exception e) {
            showError("Eroare", "Salariul trebuie sa fie un numar valid!");
        }
    }

    @FXML
    public void handleSort() {
        String sel = sortComboBox.getValue();
        if (sel == null) return;
        switch (sel) {
            case "Nume (Alfabetic)":
                model.sort(Comparator.comparing(Employee::getNume));
                break;
            case "Salariu (Crescator)":
                model.sort(Comparator.comparing(e -> employeeService.getFinalSalaryForEmployee(e, "Mai-2026")));
                break;
            case "Salariu (Descrescator)":
                model.sort((e1, e2) -> employeeService.getFinalSalaryForEmployee(e2, "Mai-2026")
                        .compareTo(employeeService.getFinalSalaryForEmployee(e1, "Mai-2026")));
                break;
        }
    }

    @FXML
    public void handleBonus() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Eroare", "Selecteaza un angajat!"); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Acordati bonus lui " + selected.getNume() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                showFinancialDialog(selected, "Bonus", "100", "Bonus performanță");
            }
        });
    }

    @FXML
    public void handlePenalty() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Eroare", "Selecteaza un angajat!"); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Penalizati pe " + selected.getNume() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                showFinancialDialog(selected, "Penalizare", "50", "Întârziere/Abatere");
            }
        });
    }

    private void showFinancialDialog(Employee emp, String tip, String sumaDefault, String motivDefault) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(tip);
        dialog.setHeaderText(tip + " pentru " + emp.getNume());

        ButtonType confirmButton = new ButtonType("Confirmă", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField sumaField = new TextField(sumaDefault);
        TextField motivField = new TextField(motivDefault);

        grid.add(new Label("Sumă:"), 0, 0);
        grid.add(sumaField, 1, 0);
        grid.add(new Label("Motiv:"), 0, 1);
        grid.add(motivField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                try {
                    Double suma = Double.parseDouble(sumaField.getText());
                    String motiv = motivField.getText();
                    if (tip.equals("Bonus")) {
                        employeeService.addBonus(emp, suma, motiv);
                    } else {
                        employeeService.addPenalty(emp, suma, motiv);
                    }
                    refreshTable();
                    showInfo("Succes", tip + " înregistrat!");
                } catch (NumberFormatException e) {
                    showError("Eroare", "Suma trebuie să fie un număr!");
                }
            }
        });
    }

    private void handleFilter() {
        String f = searchField.getText();
        if (f == null || f.isEmpty()) initModel();
        else model.setAll(employeeService.filterEmployees("nume", f));
    }

    @FXML public void handleLogout() { System.exit(0); }
    private void showError(String t, String m) { new Alert(Alert.AlertType.ERROR, m).showAndWait(); }
    private void showInfo(String t, String m) { new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
}