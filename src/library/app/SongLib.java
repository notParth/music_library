//CS213 Assignment 1: SongLib
//Names: Parth Patel (psp116), Amanda Kang (ajk267)

package library.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import library.view.music_controller;

public class SongLib extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(
                getClass().getResource("/library/view/music.fxml"));
        AnchorPane root = (AnchorPane) loader.load();

        music_controller listController = loader.getController();
        listController.start(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        //the closing event of the application invokes the method shutdown()
        //shutdown is used to creates and store session data
        stage.setOnHidden(e -> listController.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
