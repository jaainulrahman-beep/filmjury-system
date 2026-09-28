package com.movie.shortflim;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final ContestService contestService;

    public DashboardController(ContestService contestService) {
        this.contestService = contestService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("title", "Short Film Contest");
        model.addAttribute("entryCount", contestService.getEntries().size());
        model.addAttribute("judgeCount", contestService.getJudges().size());
        model.addAttribute("leaderboard", contestService.getLeaderboard());
        return "dashboard";
    }

    @GetMapping("/entries")
    public String entries(Model model) {
        model.addAttribute("entry", new FilmEntry());
        model.addAttribute("entries", contestService.getEntries());
        return "entries";
    }

    @PostMapping("/entries")
    public String submitEntry(@ModelAttribute FilmEntry entry) {
        contestService.submitEntry(entry.getTitle(), entry.getGenre(), entry.getVideoLink(), entry.getTeamName());
        return "redirect:/entries";
    }

    @GetMapping("/judges")
    public String judges(Model model) {
        model.addAttribute("judge", new Judge());
        model.addAttribute("judges", contestService.getJudges());
        model.addAttribute("entries", contestService.getEntries());
        return "judges";
    }

    @PostMapping("/judges")
    public String addJudge(@ModelAttribute Judge judge) {
        contestService.createJudge(judge.getName());
        return "redirect:/judges";
    }

    @PostMapping("/assignments")
    public String assignJudges(@RequestParam Long entryId, @RequestParam List<Long> judgeIds) {
        contestService.assignJudgesToEntry(entryId, judgeIds);
        return "redirect:/judges";
    }

    @GetMapping("/scores")
    public String scores(Model model) {
        model.addAttribute("judges", contestService.getJudges());
        model.addAttribute("entries", contestService.getEntries());
        model.addAttribute("criteria", contestService.getScoringCriteria());
        return "scores";
    }

    @PostMapping("/scores")
    public String submitScores(@RequestParam Long judgeId,
                              @RequestParam Long entryId,
                              @RequestParam Map<String, String> params) {
        Map<String, Double> scores = new LinkedHashMap<>();
        for (String criterion : contestService.getScoringCriteria()) {
            String value = params.getOrDefault(criterion, "0");
            scores.put(criterion, Double.parseDouble(value));
        }

        contestService.submitScores(judgeId, entryId, scores);
        return "redirect:/leaderboard";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        model.addAttribute("leaderboard", contestService.getLeaderboard());
        return "leaderboard";
    }
}
