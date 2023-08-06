package com.KeyzHire.Rewards;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class RewardPointsCalculatorTest {

    private RewardPointsCalculator rewardPointsCalculator;
    private List<Transaction> testData;

    @BeforeEach
    public void setUp() {
        rewardPointsCalculator = new RewardPointsCalculator();
        testData = RewardPointsCalculator.TestData();
    }

    @Test
    public void testCalculateRewardPoints_SingleTransaction() {
        // Given
        List<Transaction> singleTransaction = List.of(new Transaction("customer1", 120.0, LocalDate.of(2023, Month.JULY, 1)));

        // When
        Map<String, Map<String, Integer>> rewards = RewardPointsCalculator.calculateRewardPoints(singleTransaction);

        // Then
        int customer1JulyRewards = rewards.get("customer1").get("JULY");
        Assertions.assertEquals(90, customer1JulyRewards);
    }

    @Test
    public void testCalculateRewardPoints_MultipleTransactions() {
        // When
        Map<String, Map<String, Integer>> rewards = RewardPointsCalculator.calculateRewardPoints(testData);

        // Then
        int customer1JulyRewards = rewards.get("customer1").get("JULY");
        int customer1AugustRewards = rewards.get("customer1").get("AUGUST");
        int customer1SeptemberRewards = rewards.get("customer1").get("SEPTEMBER");
        int customer2JulyRewards = rewards.get("customer2").get("JULY");
        int customer2AugustRewards = rewards.get("customer2").get("AUGUST");
        int customer3JanuaryRewards = rewards.get("customer3").get("JANUARY");
        int customer3JuneRewards = rewards.get("customer3").get("JUNE");

        Assertions.assertEquals(90, customer1JulyRewards);
        Assertions.assertEquals(0, customer1AugustRewards);
        Assertions.assertEquals(0, customer1SeptemberRewards);
        Assertions.assertEquals(20, customer2JulyRewards);
        Assertions.assertEquals(80, customer2AugustRewards);
        Assertions.assertEquals(0, customer3JanuaryRewards);
        Assertions.assertEquals(200, customer3JuneRewards);
    }

    @Test
    public void testTestData() {
        // When
        List<Transaction> transactions = RewardPointsCalculator.TestData();

        // Then
        Assertions.assertNotNull(transactions);
        Assertions.assertEquals(7, transactions.size());
    }
}