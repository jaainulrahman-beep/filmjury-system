package com.movie.shortflim.entity;

public class LeaderboardRow {
    private final FilmEntry entry;
    private final double averageScore;
    private final int reviewCount;

    public LeaderboardRow(FilmEntry entry, double averageScore, int reviewCount) {
        this.entry = entry;
        this.averageScore = averageScore;
        this.reviewCount = reviewCount;
    }

    public FilmEntry getEntry() {
        return entry;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public int getReviewCount() {
        return reviewCount;
    }
}
