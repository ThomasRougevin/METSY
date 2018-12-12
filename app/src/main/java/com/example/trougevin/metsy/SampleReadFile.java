package fr.isep.ii3510.assignment3musiclibrary;

public class SampleReadFile {
    private String band, album, song;

    public SampleReadFile(String allerg) {
        this.allerg = allerg;
    }

    public String getAllerg() {
        return allerg;
    }

    public void setAllerg(String allerg) {
        this.allerg = allerg;
    }

    private String allerg;

    public SampleReadFile() {
        this.band = band;
        this.album = album;
        this.song = song;
    }
    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }


}
