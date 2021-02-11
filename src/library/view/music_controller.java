package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    @FXML
    TextField song_name;
    @FXML
    TextField artist_name;
    @FXML
    TextField album_name;
    @FXML
    TextField song_year;

    private ObservableList<String> obsList;
    private String song = "";
    private int index = -1;

    public void start(Stage mainStage){
        obsList = FXCollections.observableArrayList();
        list_view.setItems(obsList);
        list_view.getSelectionModel().select(0);
        list_view.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldVal, newVal) -> storeItemDetails(mainStage)
        );
    }

    public void add(ActionEvent e){
        String song = song_name.getText();
        String artist = artist_name.getText();
        String album = album_name.getText();
        String year = song_year.getText();

    }
    public void delete(ActionEvent e){

    }
    public void edit(ActionEvent e){

    }

    private void storeItemDetails(Stage mainStage) {
        song = list_view.getSelectionModel().getSelectedItem();
        index = list_view.getSelectionModel().getSelectedIndex();

    }
}
