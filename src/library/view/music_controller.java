package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class music_controller {
    @FXML
    ListView<String> list_view;
    @FXML
    Button edit_song;
    @FXML
    Button new_song;
    @FXML
    Button delete_song;

    private ObservableList<String> obsList;

    public void start(Stage mainStage){
        obsList = FXCollections.observableArrayList();
        list_view.setItems(obsList);
        list_view.getSelectionModel().select(0);
    }
}
