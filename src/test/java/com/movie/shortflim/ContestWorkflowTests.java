package com.movie.shortflim;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ContestWorkflowTests {

    private final ContestService service = new ContestService();

    @Test
    void teamCanSubmitEntryAndViewIt() {
        FilmEntry entry = service.submitEntry("Midnight Run", "Thriller", "https://example.com/video", "Northlight Studio");

        assertNotNull(entry.getId());
        assertEquals("Midnight Run", entry.getTitle());
        assertFalse(service.getEntries().isEmpty());
    }

    @Test
    void judgesCanReviewEntriesAndAverageScoresAreCalculated() {
        FilmEntry entry = service.submitEntry("The Quiet Signal", "Drama", "https://example.com/video-2", "Bloom Films");
        Judge judge1 = service.createJudge("Aisha");
        Judge judge2 = service.createJudge("Jon");

        service.assignJudgesToEntry(entry.getId(), List.of(judge1.getId(), judge2.getId()));

        service.submitScores(judge1.getId(), entry.getId(), Map.of(
            "Story", 8.0,
            "Cinematography", 9.0,
            "Performance", 8.5,
            "Editing", 7.5
        ));

        service.submitScores(judge2.getId(), entry.getId(), Map.of(
            "Story", 9.0,
            "Cinematography", 8.5,
            "Performance", 9.0,
            "Editing", 8.0
        ));

        assertEquals(8.44, service.getAverageScore(entry.getId()), 0.01);
        assertEquals(1, service.getLeaderboard().get(0).getEntry().getId());
    }
}
