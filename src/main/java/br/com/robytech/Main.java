package br.com.robytech;

import br.com.robytech.view.SplashScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(primaryStage);
    }
}
