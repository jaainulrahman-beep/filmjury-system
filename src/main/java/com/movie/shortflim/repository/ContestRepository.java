package com.movie.shortflim.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContestRepository {

    void assignJudgeToEntry(Long entryId, Long judgeId);

    Set<Long> findAssignedJudgeIds(Long entryId);

    void saveScores(Long entryId, Long judgeId, Map<String, Double> scoreMap);

    Map<Long, Map<String, Double>> findScoresByEntry(Long entryId);

    List<Long> findEntriesWithScores();
}
