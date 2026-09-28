package com.movie.shortflim;

public class FilmEntry {
    private Long id;
    private String title;
    private String genre;
    private String videoLink;
    private String teamName;

    public FilmEntry() {
    }

    public FilmEntry(Long id, String title, String genre, String videoLink, String teamName) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.videoLink = videoLink;
        this.teamName = teamName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
