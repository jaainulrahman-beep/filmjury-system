package com.movie.shortflim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class ContestService {

    private final List<FilmEntry> entries = new ArrayList<>();
    private final List<Judge> judges = new ArrayList<>();
    private final Map<Long, Set<Long>> assignmentsByEntry = new LinkedHashMap<>();
    private final Map<Long, Map<Long, Map<String, Double>>> scoresByEntryAndJudge = new LinkedHashMap<>();
    private final List<String> scoringCriteria = List.of("Story", "Cinematography", "Performance", "Editing");
    private final AtomicLong entryIdSequence = new AtomicLong(1L);
    private final AtomicLong judgeIdSequence = new AtomicLong(1L);

    public FilmEntry submitEntry(String title, String genre, String videoLink, String teamName) {
        FilmEntry entry = new FilmEntry(entryIdSequence.getAndIncrement(), title, genre, videoLink, teamName);
        entries.add(entry);
        assignmentsByEntry.putIfAbsent(entry.getId(), new LinkedHashSet<>());
        scoresByEntryAndJudge.putIfAbsent(entry.getId(), new LinkedHashMap<>());
        return entry;
    }

    public Judge createJudge(String name) {
        Judge judge = new Judge(judgeIdSequence.getAndIncrement(), name);
        judges.add(judge);
        return judge;
    }

    public List<FilmEntry> getEntries() {
        return entries;
    }

    public List<Judge> getJudges() {
        return judges;
    }

    public List<String> getScoringCriteria() {
        return scoringCriteria;
    }

    public void assignJudgesToEntry(Long entryId, List<Long> judgeIds) {
        Set<Long> current = assignmentsByEntry.computeIfAbsent(entryId, key -> new LinkedHashSet<>());
        if (judgeIds != null) {
            current.addAll(judgeIds);
        }
    }

    public List<Judge> getAssignedJudgesForEntry(Long entryId) {
        Set<Long> assignedIds = assignmentsByEntry.getOrDefault(entryId, new LinkedHashSet<>());
        return judges.stream()
            .filter(judge -> assignedIds.contains(judge.getId()))
            .toList();
    }

    public void submitScores(Long judgeId, Long entryId, Map<String, Double> scoreMap) {
        if (judgeId == null || entryId == null) {
            throw new IllegalArgumentException("Judge ID and entry ID are required.");
        }

        Set<Long> assigned = assignmentsByEntry.getOrDefault(entryId, new LinkedHashSet<>());
        if (!assigned.contains(judgeId)) {
            throw new IllegalArgumentException("This judge is not assigned to the selected entry.");
        }

        Map<Long, Map<String, Double>> judgeScores = scoresByEntryAndJudge.computeIfAbsent(entryId, key -> new LinkedHashMap<>());
        judgeScores.put(judgeId, normalizeScores(scoreMap));
    }

    public double getAverageScore(Long entryId) {
        List<Double> allScores = new ArrayList<>();
        Map<Long, Map<String, Double>> judgeScores = scoresByEntryAndJudge.get(entryId);
        if (judgeScores == null || judgeScores.isEmpty()) {
            return 0.0;
        }

        for (Map<String, Double> scoreMap : judgeScores.values()) {
            if (scoreMap == null || scoreMap.isEmpty()) {
                continue;
            }
            double total = 0.0;
            for (String criteria : scoringCriteria) {
                total += scoreMap.getOrDefault(criteria, 0.0);
            }
            allScores.add(total / scoringCriteria.size());
        }

        if (allScores.isEmpty()) {
            return 0.0;
        }

        double totalAverage = 0.0;
        for (double average : allScores) {
            totalAverage += average;
        }
        return roundScore(totalAverage / allScores.size());
    }

    public List<LeaderboardRow> getLeaderboard() {
        return entries.stream()
            .map(entry -> new LeaderboardRow(entry, getAverageScore(entry.getId()), getReviewCount(entry.getId())))
            .sorted(Comparator.comparingDouble(LeaderboardRow::getAverageScore).reversed())
            .toList();
    }

    private int getReviewCount(Long entryId) {
        Map<Long, Map<String, Double>> judgeScores = scoresByEntryAndJudge.get(entryId);
        return judgeScores == null ? 0 : judgeScores.size();
    }

    private Map<String, Double> normalizeScores(Map<String, Double> scoreMap) {
        Map<String, Double> normalized = new LinkedHashMap<>();
        for (String criterion : scoringCriteria) {
            normalized.put(criterion, roundScore(scoreMap.getOrDefault(criterion, 0.0)));
        }
        return normalized;
    }

    private double roundScore(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
