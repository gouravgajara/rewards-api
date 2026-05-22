package com.retail.rewards.dto;

import java.util.List;

public record RewardResponse(Long customerId,String customerName,List<MonthlyReward> monthlyRewards,Long totalRewards) {}
