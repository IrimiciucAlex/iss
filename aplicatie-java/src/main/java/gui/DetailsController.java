package gui;

import domain.Employee;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import service.EmployeeService;

import java.util.Optional;

public class DetailsController {
    @FXML private Label nameLabel;
    @FXML private Label functionLabel;
    @FXML private Label salaryLabel;

    private Employee employee;
    private EmployeeService service;
    private MainController mainController;

    public void setData(Employee employee, EmployeeService service, MainController mainController) {
        this.employee = employee;
        this.service = service;
        this.mainController = mainController;
        updateLabels();
    }

    private void updateLabels() {
        nameLabel.setText(employee.getNume());
        functionLabel.setText(employee.getFunctie());
        salaryLabel.setText(String.format("%.2f", employee.getSalariuPeZi()));
    }

    @FXML
    public void handleEdit() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmare Modificare");
        confirmAlert.setHeaderText("Modificare date angajat");
        confirmAlert.setContentText("Ești sigur că vrei să modifici datele lui " + employee.getNume() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Dialog<Employee> dialog = new Dialog<>();
            dialog.setTitle("Editare Angajat");
            dialog.setHeaderText("Introduceți noile date:");

            ButtonType saveButtonType = new ButtonType("Salvează", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10); grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nameField = new TextField(employee.getNume());
            TextField funcField = new TextField(employee.getFunctie());
            TextField salField = new TextField(employee.getSalariuPeZi().toString());

            grid.add(new Label("Nume:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Funcție:"), 0, 1);
            grid.add(funcField, 1, 1);
            grid.add(new Label("Salariu/Zi:"), 0, 2);
            grid.add(salField, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    try {
                        employee.setNume(nameField.getText());
                        employee.setFunctie(funcField.getText());
                        employee.setSalariuPeZi(Double.parseDouble(salField.getText()));
                        return employee;
                    } catch (NumberFormatException e) { return null; }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(updatedEmp -> {
                service.updateEmployee(updatedEmp);
                updateLabels();
                mainController.refreshTable();
                new Alert(Alert.AlertType.INFORMATION, "Datele au fost actualizate!").show();
            });
        }
    }

    @FXML
    public void handleBonus() {
        showFinancialDialog("Bonus", "100", "Bonus performanță");
    }

    @FXML
    public void handlePenalty() {
        showFinancialDialog("Penalizare", "50", "Întârziere/Abatere");
    }

    /**
     * Metodă generică pentru a afișa dialogul de Bonus sau Penalizare cu Sumă și Motiv
     */
    private void showFinancialDialog(String tip, String sumaDefault, String motivDefault) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(tip);
        dialog.setHeaderText(tip + " pentru " + employee.getNume());

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
                        service.addBonus(employee, suma, motiv);
                    } else {
                        service.addPenalty(employee, suma, motiv);
                    }

                    mainController.refreshTable();
                    new Alert(Alert.AlertType.INFORMATION, tip + " înregistrat cu succes!").show();
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Suma trebuie să fie un număr valid!").show();
                }
            }
        });
    }

    @FXML public void handleClose() { ((Stage) nameLabel.getScene().getWindow()).close(); }
}