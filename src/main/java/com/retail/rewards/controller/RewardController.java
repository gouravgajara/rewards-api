package com.retail.rewards.controller;

import com.retail.rewards.dto.RewardResponse;
import com.retail.rewards.service.IRewardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final IRewardService rewardService;

    public RewardController(IRewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public List<RewardResponse> getRewards() {
        return rewardService.getRewards();
    }

    @GetMapping("/{customerId}")
    public RewardResponse getRewardsByCustomerId(@PathVariable Long customerId) {
        return rewardService.getRewardsByCustomerId(customerId);
    }
}
