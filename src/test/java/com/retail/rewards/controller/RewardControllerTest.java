package com.retail.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.RewardResponse;
import com.retail.rewards.service.IRewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRewardService rewardService;

    @Test
    void shouldReturnRewards() throws Exception {
        RewardResponse response = new RewardResponse(
                101L,
                "John",
                List.of(new MonthlyReward("May", 90L)),
                90L
        );

        when(rewardService.getRewards()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(101))
                .andExpect(jsonPath("$[0].customerName").value("John"))
                .andExpect(jsonPath("$[0].totalRewards").value(90));
    }

    @Test
    void shouldReturnCustomerRewardsById() throws Exception {
        RewardResponse response = new RewardResponse(
                101L,
                "John",
                List.of(new MonthlyReward("May", 90L)),
                90L
        );

        when(rewardService.getRewardsByCustomerId(101L)).thenReturn(response);

        mockMvc.perform(get("/api/rewards/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(101))
                .andExpect(jsonPath("$.totalRewards").value(90));
    }
}
