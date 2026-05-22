package com.retail.rewards.service;

import com.retail.rewards.entity.Transaction;
import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {
 @Mock private TransactionRepository transactionRepository;
 @InjectMocks private RewardService rewardService;
 @Test void shouldCalculateRewardPointsAbove100(){ assertEquals(90,rewardService.calculateRewardPoints(120.0));}
 @Test void shouldCalculateRewardPointsBetween50And100(){ assertEquals(25,rewardService.calculateRewardPoints(75.0));}
 @Test void shouldReturnZeroPoints(){ assertEquals(0,rewardService.calculateRewardPoints(40.0));}
 @Test void shouldThrowExceptionForNegativeAmount(){ assertThrows(IllegalArgumentException.class,()->rewardService.calculateRewardPoints(-1.0));}
 @Test void shouldReturnRewardsByCustomerId(){ when(transactionRepository.findByCustomerId(101L)).thenReturn(List.of(new Transaction(1L,101L,"John",120.0,LocalDate.now()))); assertEquals(90,rewardService.getRewardsByCustomerId(101L).totalRewards());}
 @Test void shouldThrowCustomerNotFound(){ when(transactionRepository.findByCustomerId(999L)).thenReturn(List.of()); assertThrows(CustomerNotFoundException.class,()->rewardService.getRewardsByCustomerId(999L));}
}
