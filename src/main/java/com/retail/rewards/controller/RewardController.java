package com.retail.rewards.controller;

import com.retail.rewards.dto.RewardResponse;
import com.retail.rewards.service.RewardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/all")
    public List<RewardResponse> getRewards() {
        return rewardService.getRewards();
    }

    @GetMapping("/{customerId}")
    public RewardResponse getRewardsByCustomerId(@PathVariable Long customerId) {
        return rewardService.getRewardsByCustomerId(customerId);
    }
}
