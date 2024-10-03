package creance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

import mg.cnaps.compta.ComptaSousEcriture;

public class Creance {

    public static Map<String, Double> getCreanceSituation(List<ComptaSousEcriture> list) 
        throws Exception
    {
        return getCreanceSituation(list, null);
    }

    public static Map<String, Double> getCreanceSituation(List<ComptaSousEcriture> list, Integer exercice) 
        throws Exception
    {
        Map<String, Double> clientBalances = new HashMap<>();
        double credit712Sum = 0.0;
        double credit530Sum = 0.0;

        for (ComptaSousEcriture entry : list) {
            String compte = entry.getCompte();
            LocalDate date = entry.getDaty().toLocalDate();

            // // Process comptes starting with 411
            if (compte != null && compte.length() > 0 && compte.startsWith("411")) {
                if (exercice == null || date.getYear() == exercice) {

                    double debit = entry.getDebit();
                    double credit = entry.getCredit();
                    double balance = debit - credit;
                    
                    clientBalances.merge(compte, balance, Double::sum);
                }
            }

            // Sum credits where compte starts with 712
            // if (compte != null && compte.startsWith("712")) {
            //     credit712Sum += entry.getCredit();
            // }

            // // Sum credits where compte starts with 530
            // if (compte != null && compte.startsWith("530")) {
            //     credit530Sum += entry.getCredit();
            // }
        }

        // Subtract sum of credit 530 from sum of credit 712
        // double creanceDifference = credit712Sum - credit530Sum;
        // clientBalances.put("CreanceDifference", creanceDifference);

        return clientBalances;
    }

    public static double getTotalCreance(Map<String, Double> clientBalances) {
        return clientBalances.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public static double getTotalCreance(List<ComptaSousEcriture> list, Integer exercice) 
        throws Exception
    {
        Map<String, Double> clientBalances = getCreanceSituation(list, exercice);
        return getTotalCreance(clientBalances);
    }
}