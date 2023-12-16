package br.com.robytech.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SplashScreen extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("ClassRoomManagementSystem");

        StackPane root = new StackPane();
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);

        Label splashLabel = new Label("Bem-vindo ao \nSistema de Gerenciamento de Salas");
        splashLabel.setStyle("-fx-font-size: 24;");
        splashLabel.setTextAlignment(TextAlignment.CENTER);
        vBox.getChildren().add(splashLabel);

        ImageView loadingGif = new ImageView(new Image("assets/loading.gif"));
        loadingGif.setFitWidth(50);
        loadingGif.setFitHeight(50);
        vBox.getChildren().add(loadingGif);

        Scene scene = new Scene(root, 1280, 720);
        root.getChildren().add(vBox);

        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                showMainMenuStage();
            });
        }).start();
    }

    private void showMainMenuStage() {
        MainMenuPage mainMenuPage = new MainMenuPage(primaryStage);
        Stage mainMenuStage = new Stage();
        mainMenuStage.setTitle("ClassRoomManagementSystem - Menu Principal");
        mainMenuStage.setScene(new Scene(mainMenuPage, 1280, 720));
        mainMenuStage.show();

        primaryStage.close();
    }
}
