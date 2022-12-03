import java.io.*;
import java.util.*;

class day1 {

    public static List<Elfe> elfes = new ArrayList<Elfe>();

    public static void main(String[] args) {
        analyseur("input");
        triage();
        int total = 0;
        for (int i=1; i<=3 ; i++) {
            System.out.println("TOP "+i+" - " + elfes.get(elfes.size() - i));
            total += elfes.get(elfes.size()-i).calories;
        }
        System.out.println("TOTAL : " + total);
    }

    public static void triage() {
        Collections.sort(elfes, (e1, e2) -> e1.calories - e2.calories);
    }

    public static void analyseur(String nomDeFichier) {
        Boolean vide = true;
        String ligne = null;
        Elfe elfe = null;
        int index = 0;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                ligne = ligne.trim();
                if (ligne.length() == 0) {
                    if (!vide) {
                        elfes.add(elfe);
                        elfe = null;
                    }
                    vide = true;
                } else {
                    if (vide) {
                        index++;
                        elfe = new Elfe(index);
                    }
                    vide = false;
                    elfe.ajoutCalories(ligne);
                }
            }
            tamponLecture.close();
            if (elfe != null) {
                elfes.add(elfe);
                elfe = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Elfe {
    public int numero;
    public int calories;

    public Elfe(int index) {
        numero = index;
        calories = 0;
    }

    public void ajoutCalories(String cal) {
        calories += Integer.parseInt(cal);
    }

    @Override
    public String toString() {
        return "Elfe numero " + numero + " transporte " + calories + " calories.";
    }
}
