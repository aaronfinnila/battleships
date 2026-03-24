package com.battleships;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private UI ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ui = new UI(primaryStage);
        ui.show();
    }

    @Override
    public void stop() {
        if (ui != null) {
            ui.cleanup();
        }
    }
}
