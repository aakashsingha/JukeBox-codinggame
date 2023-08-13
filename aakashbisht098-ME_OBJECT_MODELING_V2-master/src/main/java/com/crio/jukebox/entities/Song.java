package com.crio.jukebox.entities;

import java.util.List;

public class Song extends BaseEntity {

  private String album;
  private String owner;
  private List<String> artists;
  private String songname;


  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public List<String> getArtists() {
    return artists;
  }

  public void setArtists(List<String> artists) {
    this.artists = artists;
  }

  public String getSongname() {
    return songname;
  }

  public void setSongname(String songname) {
    this.songname = songname;
  }

  public Song(String id, String album, String owner, List<String> artists, String songname) {
    setId(id);
    this.album = album;
    this.owner = owner;
    this.artists = artists;
    this.songname = songname;
  }

}
