package br.com.robytech.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends VBox {

    private Stage primaryStage;
    private String operation;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initComponents();
    }

    private void initComponents() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));

        getChildren().addAll(titleLabel, new Label("Username:"), usernameField, new Label("Password:"), passwordField,
                loginButton);
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    private void handleLogin(String username, String password) {
        // Lógica de validação de login
        if ("admin".equals(username) && "admin@IFCE#2022".equals(password)) {
            openOperationPage();
        } else {
            showAlert("Login Failed", "Invalid credentials. Please try again.");
        }
    }

    private void openOperationPage() {
        Stage newStage = new Stage();

        if ("Gerenciar Sala".equals(operation)) {
            openManageRoomsPage(newStage);
        } else if ("Gerenciar Disciplina".equals(operation)) {
            openManageDisciplinePage(newStage);
        }
    }

    private void openManageRoomsPage(Stage newStage) {
        ManageRoomsPage manageRoomsPage = new ManageRoomsPage();
        manageRoomsPage.show(primaryStage);
        manageRoomsPage.updateListView();
    }

    private void openManageDisciplinePage(Stage newStage) {
        ManageDisciplinePage manageDisciplinePage = new ManageDisciplinePage();
        manageDisciplinePage.show(primaryStage);
        manageDisciplinePage.updateListView();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
