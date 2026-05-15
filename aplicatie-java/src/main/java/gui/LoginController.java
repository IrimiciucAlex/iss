package gui;

import domain.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.UserService;
import service.EmployeeService;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private UserService userService;
    private EmployeeService employeeService;

    public void setServices(UserService userService, EmployeeService employeeService) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @FXML
    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        Manager m = userService.authenticate(user, pass);
        if (m != null) {
            loadMainWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Date incorecte!");
            alert.show();
        }
    }

    private void loadMainWindow() {
        try {
            // Calea direct catre fisier, fara /views/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            MainController controller = loader.getController();
            controller.setService(employeeService);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("HRMS - Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleExit() { System.exit(0); }
}