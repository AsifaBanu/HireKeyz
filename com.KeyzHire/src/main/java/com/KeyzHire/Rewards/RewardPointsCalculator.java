package com.KeyzHire.Rewards;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class RewardPointsCalculator {
    public static void main(String[] args) {
    	List<Transaction> transactions = TestData();
    	Map<String, Map<String, Integer>> result = calculateRewardPoints(transactions);        
    }
    public static Map<String, Map<String, Integer>> calculateRewardPoints(List<Transaction> transactions) {
    	
        Properties properties = LoadProperties();
        int Points_slab1 = Integer.parseInt(properties.getProperty("POINTS_START_SLAB"));
        int Points_slabMax = Integer.parseInt(properties.getProperty("POINTS_INCREMENT_SLAB"));
        int months = Integer.parseInt(properties.getProperty("DURATION_MONTHS"));
        
        Map<String, Map<String, Integer>> rewardsByCustomerAndMonth = transactions.stream()
                .filter(t -> t.getTransactionDate().isAfter(LocalDate.now().minusMonths(months)))
                .collect(Collectors.groupingBy(Transaction::getCustomerId,
                        Collectors.groupingBy(t -> t.getTransactionDate().getMonth().toString(),
                                Collectors.summingInt(t -> {int pointsEarned = 0;
                                if (t.getAmount() > Points_slabMax) {
                                    pointsEarned += 2 * (int) (t.getAmount() - Points_slabMax);
                                }
                                if (t.getAmount() > Points_slab1 && t.getAmount() <= Points_slabMax) {
                                    pointsEarned += (int) (t.getAmount() - Points_slab1);
                                }
                                return pointsEarned;}  ))));

        displayRewards(rewardsByCustomerAndMonth);
        return rewardsByCustomerAndMonth; 
    }
   public static Properties LoadProperties() {
       Properties properties = new Properties();
       try (FileInputStream inputStream = new FileInputStream("config.properties")) {
           properties.load(inputStream);
       } catch (IOException e) {
           e.printStackTrace();
       }
	return properties;
   }
    public static List<Transaction> TestData() {
    	List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("customer1", 120.0, LocalDate.of(2023, Month.JULY, 1)));
        transactions.add(new Transaction("customer2", 70.0, LocalDate.of(2023, Month.JULY, 5)));
        transactions.add(new Transaction("customer1", 60.0, LocalDate.of(2023, Month.JULY, 10)));
        transactions.add(new Transaction("customer2", 130.0, LocalDate.of(2023, Month.AUGUST, 20)));
        transactions.add(new Transaction("customer1", 200.0, LocalDate.of(2023, Month.SEPTEMBER, 15)));
        transactions.add(new Transaction("customer3", 200.0, LocalDate.of(2023, Month.JANUARY, 15)));
        transactions.add(new Transaction("customer3", 300.0, LocalDate.of(2023, Month.JUNE, 15)));
        return transactions;
    }
    public static void displayRewards(Map<String, Map<String, Integer>> rewardsByCustomerAndMonth) {
        System.out.println("Rewards earned per customer per month:");
        for (Map.Entry<String, Map<String, Integer>> entry : rewardsByCustomerAndMonth.entrySet()) {
            System.out.println("Customer: " + entry.getKey());
            for (Map.Entry<String, Integer> monthEntry : entry.getValue().entrySet()) {
                System.out.println("        Month: " + monthEntry.getKey() + ", Points: " + monthEntry.getValue());
            }
        }
    }
}
