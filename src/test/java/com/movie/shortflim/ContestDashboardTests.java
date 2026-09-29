package com.movie.shortflim;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.movie.shortflim.service.ContestService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
class ContestDashboardTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContestService contestService;

    @Test
    void homePageShouldRenderDashboard() throws Exception {
        when(contestService.getEntries()).thenReturn(Collections.emptyList());
        when(contestService.getJudges()).thenReturn(Collections.emptyList());
        when(contestService.getLeaderboard()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("dashboard"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Short Film Contest")));
    }
}
