package creance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import mg.cnaps.compta.ComptaSousEcriture;

public class Creance {

    public static Map<String, Double> getCreanceSituation(List<ComptaSousEcriture> list, Integer exercice) 
        throws Exception
    {
        Map<String, Double> clientBalances = new HashMap<>();

        for (ComptaSousEcriture entry : list) {
            String compte = entry.getCompte();
            LocalDate date = entry.getDaty().toLocalDate();

            if (compte != null && compte.length() > 0 && compte.startsWith("411")) {
                if (exercice == null || date.getYear() == exercice) {

                    double debit = entry.getDebit();
                    double credit = entry.getCredit();
                    double balance = debit - credit;
                    
                    clientBalances.merge(compte, balance, Double::sum);
                }
            }
        }

        return clientBalances;
    }

    public static double getTotalCreance(Map<String, Double> clientBalances) 
    { return clientBalances.values().stream().mapToDouble(Double::doubleValue).sum(); }

    public static double getTotalCreance(List<ComptaSousEcriture> list, Integer exercice) 
        throws Exception
    {
        Map<String, Double> clientBalances = getCreanceSituation(list, exercice);
        return getTotalCreance(clientBalances);
    }

    public static Map<String, Double> getCreanceSituation(List<ComptaSousEcriture> list) 
        throws Exception
    {
        return getCreanceSituation(list, null);
    }
}