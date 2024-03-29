//CS213 Assignment 1: SongLib
//Names: Parth Patel (psp116), Amanda Kang (ajk267)

package library.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import library.songclass.Song;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class music_controller {
    @FXML
    ListView<Song> list_view;
    @FXML
    Button edit_song;
    @FXML
    Button new_song;
    @FXML
    Button delete_song;
    @FXML
    Label details;
    @FXML
    TextField song_name;
    @FXML
    TextField artist_name;
    @FXML
    TextField album_name;
    @FXML
    TextField song_year;

    private ObservableList<Song> obsList;
    private Song song;
    private int index;

    //private String song = "";
    // private int index = -1;

    public void start(Stage mainStage) throws Exception {

        mainStage.setTitle("Song Library App");
        obsList = FXCollections.observableArrayList();
        //Used to check if there is any session data available
        File file = new File("song_data.txt");
        if (file.exists() && file.length() != 0) {
            //reading previously session's stored data and feeding it into the obsList
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                Scanner scn = new Scanner(line).useDelimiter("\\*");
                Song song = new Song();
                int cnt = 1;
                while (scn.hasNext()){
                    if (cnt == 1){
                        song.setName(scn.next());
                        cnt++;
                    }
                    else if (cnt == 2){
                        song.setArtist(scn.next());
                        cnt++;
                    }
                    else if (cnt == 3){
                        song.setAlbum(scn.next());
                        cnt++;
                    }
                    else{
                        if (cnt == 4)
                            song.setYear(scn.next());
                    }
                }
                scn.close();
                obsList.add(song);
            }
            sc.close();

            list_view.setItems(obsList);
            Song firstSong = obsList.get(0);
            list_view.getSelectionModel().select(firstSong);
            details.setText(firstSong.getDetails());
            song_name.setText(firstSong.getName());
            artist_name.setText(firstSong.getArtist());
            album_name.setText(firstSong.getAlbum());
            song_year.setText(firstSong.getYear());

        }


        list_view.setItems(obsList);
        list_view.getSelectionModel().selectedItemProperty().addListener( //display details of selected item
                (obs, oldVal, newVal) -> {
                    index = list_view.getSelectionModel().getSelectedIndex();
                    if (index != -1) {
                        details.setText(newVal.getDetails());
                        song_name.setText(newVal.getName());
                        artist_name.setText(newVal.getArtist());
                        album_name.setText(newVal.getAlbum());
                        song_year.setText(newVal.getYear());
                    }
                      /* list_view.getSelectionModel().selectedIndexProperty().addListener(
                (obs, oldVal, newVal) -> storeItemDetails(mainStage)
        ); */
                }
        );

        //add
        new_song.setOnAction(e -> add());
        delete_song.setOnAction(e -> delete());
        edit_song.setOnAction(e -> edit());

    }

    //this methods gets called when the application is closed
    //this also stores "session" data so that stored songs can be accessed across sessions
    public void shutdown() {
        try{
            File file = new File("song_data.txt");
            file.createNewFile();
            FileWriter myWriter = new FileWriter(file);
            for (Song song : obsList){
                String song_name = song.getName();
                String artist = song.getArtist();
                String album = song.getAlbum();
                String year = song.getYear();
                myWriter.write(song_name + "*" +artist);
                if(album != null)
                    myWriter.write("*" + album );
                if(year != null)
                    myWriter.write("*" + year);
                myWriter.write("\n");
            }
            myWriter.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void add() {
        //Confirm action
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("WARNING");
        cancelAlert.setContentText("Confirm Action");
        cancelAlert.setHeaderText("Are you sure you want to add this song?");

        Optional<ButtonType> result = cancelAlert.showAndWait();

        if (result.get() == ButtonType.OK) {
            //Error Handling
            if (song_name.getText().isEmpty()) { //no name
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: Your song must have a name");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to add your song.");
                errorAlert.showAndWait();
            } else if (artist_name.getText().isEmpty()) { //no artist
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: Your song must have an artist");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to add your song.");
                errorAlert.showAndWait();
            } else if (isDupe(song_name.getText(), artist_name.getText())) { //duplicate song
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: The song you tried to edit already exists in the list");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to add your song.");
                errorAlert.showAndWait();
            } else {
                //Add Song
                Song newSong = new Song(); //create new song w/textfield inputs
                newSong.setName(song_name.getText().trim());
                newSong.setArtist(artist_name.getText().trim());
                if (song_year.getText() != null && !song_year.getText().isEmpty()) {
                    newSong.setYear(song_year.getText().trim());
                }
                if (album_name.getText() != null && !album_name.getText().isEmpty()) {
                    newSong.setAlbum(album_name.getText().trim());
                }


                obsList.add(newSong); //add to observable list
                list_view.getSelectionModel().select(newSong); //select added song


                Song songComp = new Song(); //create comparator, sort alphabetically
                FXCollections.sort(obsList, songComp);
            }
        }/*else{ //reset detail fields to og values if actiona cancelled
            index = list_view.getSelectionModel().getSelectedIndex();
            song = obsList.get(index);
            song_name.setText(song.getName());
            artist_name.setText(song.getArtist());
            album_name.setText(song.getAlbum());
            song_year.setText(song.getYear());
        } */
    }

        /*String song = song_name.getText();
        String artist = artist_name.getText();
        String album = album_name.getText();
        String year = song_year.getText(); */

    public void delete(){
        //Confirm action
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("WARNING");
        cancelAlert.setContentText("Confirm Action");
        cancelAlert.setHeaderText("Are you sure you want to delete this song?");

        Optional<ButtonType> result = cancelAlert.showAndWait();

        if(result.get() == ButtonType.OK){ //this part of the code SHOULD be working properly now but it gets really messy bc i hardcoded some of it. feel free to take a shot at fixing it! :')
            if(obsList.size() == 0){ //if list is empty when trying to delete
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: There are no songs in the library");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to delete your song.");
                errorAlert.showAndWait();
            }else{
                int ind = list_view.getSelectionModel().getSelectedIndex();
                index = ind; //when i use index, obsList.remove subtracts 1 from it and i dont know why so that's why this line is here
                obsList.remove(obsList.get(index));
                if(obsList.size() == 0){ //if list is empty after deleting
                    song_name.clear();
                    artist_name.clear();
                    album_name.clear();
                    song_year.clear();
                    details.setText("Song Details: -");
                } else{
                    if (ind >= obsList.size()) { //if item is last in list select previous
                        list_view.getSelectionModel().select(index);
                    } else if(ind == 0){
                        list_view.getSelectionModel().select(0); //idk why its not working so i just hardcoded it in :/
                    } else{
                        list_view.getSelectionModel().selectNext();
                    }
                }
            }
        } /*else{ //reset detail fields to og values if actiona cancelled
            index = list_view.getSelectionModel().getSelectedIndex();
            song = obsList.get(index);
            song_name.setText(song.getName());
            artist_name.setText(song.getArtist());
            album_name.setText(song.getAlbum());
            song_year.setText(song.getYear());
        } */
    }
    public void edit(){
        //Confirm action
        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("WARNING");
        cancelAlert.setContentText("Confirm Action");
        cancelAlert.setHeaderText("Are you sure you want to edit this song?");

        Optional<ButtonType> result = cancelAlert.showAndWait();

        index = list_view.getSelectionModel().getSelectedIndex();

        if(result.get() == ButtonType.OK) {
            if(song_name.getText().isEmpty()){ //no name
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: Your song must have a name");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to edit your song.");
                errorAlert.showAndWait();
            } else if(artist_name.getText().isEmpty()) { //no artist
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: Your song must have an artist");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to edit your song.");
                errorAlert.showAndWait();
            } else if(list_view.getSelectionModel().getSelectedIndex() == -1){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: No song is selected");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to edit your song.");
                errorAlert.showAndWait();
            } else if((isDupe(song_name.getText(), artist_name.getText())) && (!song_name.getText().equals(obsList.get(index).getName()) || !artist_name.getText().equals(obsList.get(index).getArtist()))){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("ERROR");
                errorAlert.setContentText("Problem: Your song is already in the list");
                errorAlert.setHeaderText("Whoops! Something went wrong when trying to edit your song.");
                errorAlert.showAndWait();
            } else{
                Song editedSong = new Song();
                int index = list_view.getSelectionModel().getSelectedIndex();
                song = obsList.get(index);
                editedSong.setName(song_name.getText().trim());
                editedSong.setArtist(artist_name.getText().trim());
                if(album_name.getText() != null && !album_name.getText().isEmpty()){
                    editedSong.setAlbum(album_name.getText().trim());
                }
                if(song_year.getText() != null && !song_year.getText().isEmpty()){
                    editedSong.setYear(song_year.getText().trim());
                }
                obsList.remove(index);
                obsList.add(editedSong); //add to observable list
                list_view.getSelectionModel().select(editedSong); //select added song


                Song songComp = new Song(); //create comparator, sort alphabetically
                FXCollections.sort(obsList, songComp);


            }
        }/*else{ //reset detail fields to og values if actiona cancelled
            index = list_view.getSelectionModel().getSelectedIndex();
            song = obsList.get(index);
            song_name.setText(song.getName());
            artist_name.setText(song.getArtist());
            album_name.setText(song.getAlbum());
            song_year.setText(song.getYear());
        } */

    }

 /*   private void storeItemDetails(Stage mainStage) {
        song = list_view.getSelectionModel().getSelectedItem();
        index = list_view.getSelectionModel().getSelectedIndex();

    } */

//test for duplicate by iterating thru obsList
    private boolean isDupe(String name, String artist){
        for(int i=0; i<obsList.size(); i++){
            if(obsList.get(i).getName().equals(name) && obsList.get(i).getArtist().equals(artist)){
                return true;
            }
        }
        return false;
    }


}
