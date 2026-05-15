import gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import repository.*;
import service.EmployeeService;
import service.UserService;
import domain.Manager;
import utils.PasswordUtils;

public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Initializare Repository-uri
        RepositoryDBManager managerRepo = new RepositoryDBManager();
        RepositoryDBEmployee employeeRepo = new RepositoryDBEmployee();
        RepositoryDBAttendance attendanceRepo = new RepositoryDBAttendance();
        RepositoryDBPayroll payrollRepo = new RepositoryDBPayroll();
        RepositoryDBBonus bonusRepo = new RepositoryDBBonus();
        RepositoryDBPenalty penaltyRepo = new RepositoryDBPenalty();

        // 2. Initializare Servicii
        UserService userService = new UserService(managerRepo);
        EmployeeService employeeService = new EmployeeService(
                employeeRepo, bonusRepo, penaltyRepo, payrollRepo
        );

        // --- SECTIUNE TEST: Creare Manager Initial ---
        // Verificam daca exista deja un manager, daca nu, cream unul pentru login
        if (managerRepo.findByUsername("admin") == null) {
            Manager testManager = new Manager(
                    "admin",
                    PasswordUtils.hashPassword("admin123"),
                    "Administrator Test",
                    "admin@test.com"
            );
            managerRepo.save(testManager);
            System.out.println(">>> User de test creat: admin / admin123");
        }
        // ----------------------------------------------

        // 3. Incarcare interfata de Login
        // Folosim "/" pentru ca fișierul e direct în resources
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
        VBox root = loader.load();

        // 4. Transmitere servicii catre LoginController
        LoginController loginController = loader.getController();
        loginController.setServices(userService, employeeService);

        // 5. Configurare scena si afisare
        Scene scene = new Scene(root);
        primaryStage.setTitle("HR Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            // Testam conexiunea Hibernate inainte de a porni UI-ul
            HibernateUtils.getSessionFactory().openSession().close();
            System.out.println(">>> Hibernate s-a conectat cu succes la SQLite!");
        } catch (Exception e) {
            System.err.println(">>> EROARE GRAVA HIBERNATE: " + e.getMessage());
            e.printStackTrace();
        }
        launch(args);
    }
}