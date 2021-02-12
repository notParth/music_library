package library.songclass;
import java.util.Comparator;
import java.util.Locale;

public class Song implements Comparator<Song>{

    private String name;
    private String artist;
    private String album;
    private String year;

    public Song(){

    }

    public String getName(){
        return this.name;
    }
    public String getArtist(){
        return this.artist;
    }
    public String getAlbum(){
        return this.album;
    }
    public String getYear(){
        return this.year;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }
    public void setAlbum(String album){
        this.album = album;
    }
    public void setYear(String year){
        this.year = year;
    }

    public String toString(){
        String s = this.name + " by " + this.artist;
        return s;
    }

    public String getDetails(){
        String salbum = (this.album == null) ? "-" : this.album;
        String syear = (this.year == null) ? "-" : this.year;
        String s = "Song Details: \n Name: " + this.name + "\n Artist: " + this.artist + "\n Album: " + salbum + "\n Year: " + syear;
        return s;
    }

    public int compare(Song s1, Song s2){
        String name1 = s1.getName().toLowerCase();
        String name2 = s2.getName().toLowerCase();
        String artist1 = s1.getArtist().toLowerCase();
        String artist2 = s2.getArtist().toLowerCase();
        if(name1.equals(name2)){
            return artist1.compareTo(artist2);
        }
        return name1.compareTo(name2);
    }

}
