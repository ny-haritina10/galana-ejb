// package caisse;

// import java.sql.Date;
// import java.util.List;
// import java.time.LocalDate;
// import java.time.ZoneId;

// import mg.cnaps.compta.ComptaSousEcriture;

// public class EtatDeCaisse {

//     private 
    
//     @SuppressWarnings("deprecation")
//     public static double getEtatDeCaisse(List<ComptaSousEcriture> sousEcriture, Date dateMin, Date dateMax) 
//         throws Exception
//     {
//         if (dateMax.before(dateMin)) {
//             throw new IllegalArgumentException("dateMax must be after dateMin");
//         }

//         double amount = 0;
//         int exercice = dateMax.getYear() + 1900;

//         for(ComptaSousEcriture ecriture : sousEcriture) {
//             if (ecriture.getCompte().startsWith("530")) {
//                 Date ecritureDate = ecriture.getDaty();                
//                 if (ecritureDate != null && ecriture.getExercice() == exercice && ecritureDate.before(dateMax) && ecritureDate.after(dateMin)) {
//                     amount += ecriture.getDebit();
//                 }
//             }
//         }

//         System.out.println("-----------amount" + amount);
//         return amount;
//     }

//     public static boolean isDateBetweenInclusive(Date dateToCheck, Date startDate, Date endDate) {
//         return (dateToCheck.equals(startDate) || dateToCheck.after(startDate)) && 
//                (dateToCheck.equals(endDate) || dateToCheck.before(endDate));
//     }
// }