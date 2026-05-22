package com.retail.rewards.service;

import com.retail.rewards.dto.RewardResponse;
import java.util.List;

public interface IRewardService {
    List<RewardResponse> getRewards();
    RewardResponse getRewardsByCustomerId(Long customerId);
    long calculateRewardPoints(Double amount);
}
