package com.retail.rewards.service;

import com.retail.rewards.entity.Transaction;
import com.retail.rewards.repository.TransactionRepository;
import com.retail.rewards.service.impl.RewardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private static final LocalDate FIXED_DATE = LocalDate.of(2024, 1, 1);


    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(rewardService, "rewardMonths", 3);
    }

    // -----------------------------
    // calculateRewardPoints tests
    // -----------------------------

    @Test
    void shouldCalculateRewardPointsAbove100() {
        // 120 -> (20 * 2) + (50 * 1) = 90
        assertEquals(90, rewardService.calculateRewardPoints(120.0));
    }

    @Test
    void shouldCalculateRewardPointsExactly100() {
        // 100 -> (50 * 1) = 50
        assertEquals(50, rewardService.calculateRewardPoints(100.0));
    }

    @Test
    void shouldCalculateRewardPointsBetween50And100() {
        // 75 -> 25 points
        assertEquals(25, rewardService.calculateRewardPoints(75.0));
    }

    @Test
    void shouldCalculateRewardPointsExactly50() {
        assertEquals(0, rewardService.calculateRewardPoints(50.0));
    }

    @Test
    void shouldReturnZeroPointsForLessThan50() {
        assertEquals(0, rewardService.calculateRewardPoints(40.0));
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> rewardService.calculateRewardPoints(-1.0));
    }

    @Test
    void shouldHandleZeroAmount() {
        assertEquals(0, rewardService.calculateRewardPoints(0.0));
    }

    // -----------------------------
    // getRewardsByCustomerId tests
    // -----------------------------

    @Test
    void shouldReturnRewardsByCustomerId_singleTransaction() {
        Transaction txn = new Transaction(
                1L, 101L, "John", 120.0, LocalDate.now()
        );

        when(transactionRepository.findByCustomerId(101L))
                .thenReturn(List.of(txn));

        var result = rewardService.getRewardsByCustomerId(101L);

        assertNotNull(result);
        assertEquals(90, result.totalRewards());

        verify(transactionRepository, times(1))
                .findByCustomerId(101L);
    }
}
