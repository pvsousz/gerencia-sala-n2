package br.com.robytech.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuPage extends VBox {

    private Stage primaryStage;
    private LoginPage loginPage;
    private RoomAllocationPage roomAllocationPage;

    public MainMenuPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setAlignment(Pos.CENTER);
        roomAllocationPage = new RoomAllocationPage();

        Button manageRoomButton = new Button("Gerenciar Sala");
        Button manageDisciplineButton = new Button("Gerenciar Disciplina");
        Button viewAllocationsButton = new Button("Visualizar Alocações");

        manageRoomButton.setOnAction(event -> showLoginPage("Gerenciar Sala"));
        manageDisciplineButton.setOnAction(event -> showLoginPage("Gerenciar Disciplina"));
        viewAllocationsButton.setOnAction(event -> roomAllocationPage.show(primaryStage));

        String buttonStyle = "-fx-font-size: 18; -fx-min-width: 380; -fx-min-height: 60;";
        manageRoomButton.setStyle(buttonStyle);
        manageDisciplineButton.setStyle(buttonStyle);
        viewAllocationsButton.setStyle(buttonStyle);

        getChildren().addAll(manageRoomButton, manageDisciplineButton, viewAllocationsButton);
    }

    private void showLoginPage(String operation) {
       
        if (loginPage != null) {
            primaryStage.setScene(null);
        }

    
        loginPage = new LoginPage(primaryStage);
        loginPage.setOperation(operation);

       
        Scene loginScene = new Scene(loginPage, 300, 400); // Substitua os valores de largura e altura conforme
                                                           // necessário
        primaryStage.setScene(loginScene);

        // Define a instância de LoginPage como nulo após o fechamento
        primaryStage.setOnHidden(event -> this.loginPage = null);

        // Abre a janela de login
        primaryStage.show();
    }
}
