import java.io.*;
import java.util.*;

class day3 {

    public static List<GroupeElfes> groupes = new ArrayList<GroupeElfes>();
    public static List<Rucksack> sacs = new ArrayList<Rucksack>();
    static int nombreDeGroupes;
    static int nombreDeSacs;

    public static void main(String[] args) {
        nombreDeSacs = 0;
        nombreDeGroupes = 0;
        analyseur("input");
        sommeDesPriorites();
        sommeDesBadges();
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                if (nombreDeSacs % 3 == 0) {
                    groupes.add(new GroupeElfes());
                    nombreDeGroupes++;
                }
                sacs.add(new Rucksack(ligne.trim()));
                nombreDeSacs++;
                groupes.get(nombreDeGroupes - 1).ajoutRucksack(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sommeDesPriorites() {
        int total = 0;
        for (int i = 0; i < nombreDeSacs; i++) {
            int priorite = sacs.get(i).getPriorite();
            System.out.println("Sac #" + (i + 1) + " a un item de priorite : " + priorite);
            total += priorite;
        }
        System.out.println("La somme des priorites est de : " + total);
    }

    public static void sommeDesBadges() {
        int total = 0;
        for (int i = 0; i < nombreDeGroupes; i++) {
            int priorite = groupes.get(i).getbadgeEffort();
            System.out.println("Groupe #" + (i + 1) + " a un badge d'effort de priorite : " + priorite);
            total += priorite;
        }
        System.out.println("La somme badges d'effort est de : " + total);
    }
}

class GroupeElfes {
    protected String[] sacs = new String[3];
    protected int index;
    protected int badgeEffort;

    public GroupeElfes() {
        this.index = 0;
    }

    public void ajoutRucksack(String ligne) {
        this.sacs[index++] = ligne.trim();
        if (index == 3)
            this.badgeEffort = itemPriorite();
    }

    public int itemPriorite() {
        char sac1[] = sacs[0].toCharArray();
        char sac2[] = sacs[1].toCharArray();
        char sac3[] = sacs[2].toCharArray();
        int priorite = 0;
        int i = 0;
        while (i < sac1.length) {
            int j = 0;
            while (j < sac2.length) {
                if (sac1[i] == sac2[j]) {
                    int k = 0;
                    while (k < sac3.length) {
                        if (sac1[i] == sac3[k] && sac2[j] == sac3[k]) {
                            char c = sac1[i];
                            priorite = (c >= 'a' && c <= 'z') ? c - 'a' + 1 : c - 'A' + 27;
                            break;
                        }
                        k++;
                    }
                }
                j++;
                if (priorite > 0)
                    break;
            }
            i++;
            if (priorite > 0)
                break;
        }
        return priorite;
    }

    public int getbadgeEffort() {
        return this.badgeEffort;
    }
}

class Rucksack {
    protected String contenu;
    protected String[] compartiments = new String[2];
    protected int tailleParComparitment;
    protected int priorite;

    public Rucksack(String ligne) {
        this.contenu = ligne;
        this.tailleParComparitment = ligne.length() >> 1;
        this.compartiments[0] = new String(ligne.substring(0, tailleParComparitment));
        this.compartiments[1] = new String(ligne.substring(tailleParComparitment, ligne.length()));
        this.priorite = itemPriorite();
    }

    public int itemPriorite() {
        char comparitement1[] = compartiments[0].toCharArray();
        char comparitement2[] = compartiments[1].toCharArray();
        int priorite = 0;
        int i = 0;
        while (i < comparitement1.length) {
            int j = 0;
            while (j < comparitement2.length) {
                if (comparitement1[i] == comparitement2[j]) {
                    char c = comparitement1[i];
                    priorite = (c >= 'a' && c <= 'z') ? c - 'a' + 1 : c - 'A' + 27;
                    break;
                }
                j++;
            }
            i++;
        }
        return priorite;
    }

    public int getPriorite() {
        return this.priorite;
    }
}
