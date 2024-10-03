package profit;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import mg.cnaps.compta.ComptaSousEcriture;

public class Profit {

    @FunctionalInterface
    private interface DateFilter {
        boolean test(Date date);
    }
    
    public static double getProfit(List<ComptaSousEcriture> list, DateFilter dateFilter, 
                                   Map<String, Double> revenueDetails, Map<String, Double> expenseDetails) 
        throws Exception
    {
        double revenue = 0;
        double expenses = 0;

        for (ComptaSousEcriture entry : list) {
            if (dateFilter == null || dateFilter.test(entry.getDaty())) {
                String compte = entry.getCompte();
                if (compte != null && compte.length() > 0) {
                    char firstDigit = compte.charAt(0);
                    double amount = entry.getCredit() - entry.getDebit();
                    
                    if (firstDigit == '7') {
                        revenue += amount;
                        revenueDetails.put(compte, revenueDetails.getOrDefault(compte, 0.0) + amount);
                    } 
                    
                    else if (firstDigit == '6') {
                        expenses += -amount;
                        expenseDetails.put(compte, expenseDetails.getOrDefault(compte, 0.0) - amount);
                    }
                }
            }
        }

        return revenue - expenses;
    }

    public static double getProfit(List<ComptaSousEcriture> list, 
                                   Map<String, Double> revenueDetails, Map<String, Double> expenseDetails) 
        throws Exception
    {
        return getProfit(list, null, revenueDetails, expenseDetails);
    }

    public static double getProfitByExercice(List<ComptaSousEcriture> list, int exercice, 
                                             Map<String, Double> revenueDetails, Map<String, Double> expenseDetails) 
        throws Exception
    {
        return getProfit(list, date -> date.toLocalDate().getYear() == exercice, revenueDetails, expenseDetails);
    }

    public static double getProfitByMonth(List<ComptaSousEcriture> list, int year, int month, 
                                          Map<String, Double> revenueDetails, Map<String, Double> expenseDetails) 
        throws Exception
    {
        return getProfit(list, 
                         date -> date.toLocalDate().getYear() == year && date.toLocalDate().getMonthValue() == month, 
                         revenueDetails, expenseDetails);
    }
}