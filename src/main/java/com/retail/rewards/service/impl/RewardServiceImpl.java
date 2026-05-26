package com.retail.rewards.service.impl;

import com.retail.rewards.dto.MonthlyReward;
import com.retail.rewards.dto.RewardResponse;
import com.retail.rewards.entity.Transaction;
import com.retail.rewards.exception.CustomerNotFoundException;
import com.retail.rewards.repository.TransactionRepository;
import com.retail.rewards.service.RewardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository transactionRepository;

    @Value("${rewards.months:3}")
    private int rewardMonths;

    public RewardServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<RewardResponse> getRewards() {
        return transactionRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId))
                .values()
                .stream()
                .map(this::filterLastNMonths)
                .filter(list -> !list.isEmpty())
                .map(this::buildRewardResponse)
                .toList();
    }

    @Override
    public RewardResponse getRewardsByCustomerId(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        List<Transaction> filteredTransactions = filterLastNMonths(transactions);

        if (filteredTransactions.isEmpty()) {
            throw new CustomerNotFoundException(
                    "No transactions found in last " + rewardMonths +
                            " months for customer: " + customerId
            );
        }
        return buildRewardResponse(filteredTransactions);
    }

    private List<Transaction> filterLastNMonths(List<Transaction> transactions) {
        LocalDate cutoffDate = LocalDate.now().minusMonths(rewardMonths);

        return transactions.stream()
                        .filter(txn -> txn.getTransactionDate().isAfter(cutoffDate))
                        .toList();
    }

    private RewardResponse buildRewardResponse(List<Transaction> transactions) {
        Transaction firstTransaction = transactions.get(0);

        List<MonthlyReward> monthlyRewards = transactions.stream().collect(Collectors.groupingBy(transaction -> transaction.getTransactionDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), Collectors.summingLong(transaction -> calculateRewardPoints(transaction.getAmount()))))
                .entrySet().stream().map(entry -> new MonthlyReward(entry.getKey(), entry.getValue()))
                .toList();

        long totalRewards = monthlyRewards.stream().mapToLong(MonthlyReward::points).sum();

        return new RewardResponse(
                firstTransaction.getCustomerId(),
                firstTransaction.getCustomerName(),
                monthlyRewards,
                totalRewards
        );
    }

    @Override
    public long calculateRewardPoints(Double amount) {
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException(
                    "Transaction amount cannot be null or negative"
            );
        }

        if (amount <= 50) {
            return 0;
        }

        if (amount <= 100) {
            return (long) (amount - 50);
        }

        return 50 + (long) ((amount - 100) * 2);
    }
}
