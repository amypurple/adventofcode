import java.io.*;
import java.util.*;

class day8 {

    static Grille grille = new Grille();

    public static void main(String[] args) {
        analyseur("8/input");
        System.out.println("Il y a " + grille.nbrTotal() + " arbres dont " + grille.nbrTotalVisible() + " visibles.");
        System.out.println("La plus belle vue a un pointage scenic de " + grille.meilleurScenic());
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                grille.ajouterLigne(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Grille {
    List<String> lignes;
    int[][] visibilite;
    int[][] scenic;
    int rangees;
    int colonnes;

    Grille() {
        rangees = 0;
        colonnes = 0;
        lignes = new ArrayList<String>();
    }

    void ajouterLigne(String s) {
        int taille = s.length();
        if (taille > 0) {
            if (colonnes < taille)
                colonnes = taille;
            rangees++;
            lignes.add(s);
        }
    }

    int getArbre(int row, int col) {
        String ligne = lignes.get(row);
        return (int) ligne.charAt(col) - '0';
    }

    void initVisibilite() {
        visibilite = new int[rangees][colonnes];
        // Tout à infini
        for (int i = 0; i < rangees; i++) {
            for (int j = 0; j < colonnes; j++) {
                visibilite[i][j] = 99;
            }
        }
        // Ajuster les visibilites
        for (int i = 0; i < rangees; i++) {
            int taille = -1;
            for (int j = 0; j < colonnes; j++) {
                int arbre = getArbre(i, j);
                if (visibilite[i][j] > taille)
                    visibilite[i][j] = taille;
                if (arbre > taille)
                    taille = arbre;
            }
            taille = -1;
            for (int j = colonnes - 1; j >= 0; j--) {
                int arbre = getArbre(i, j);
                if (visibilite[i][j] > taille)
                    visibilite[i][j] = taille;
                if (arbre > taille)
                    taille = arbre;
            }
        }
        for (int j = 0; j < colonnes; j++) {
            int taille = -1;
            for (int i = 0; i < rangees; i++) {
                int arbre = getArbre(i, j);
                if (visibilite[i][j] > taille)
                    visibilite[i][j] = taille;
                if (arbre > taille)
                    taille = arbre;
            }
            taille = -1;
            for (int i = rangees - 1; i >= 0; i--) {
                int arbre = getArbre(i, j);
                if (visibilite[i][j] > taille)
                    visibilite[i][j] = taille;
                if (arbre > taille)
                    taille = arbre;
            }
        }
    }

    int nbrTotal() {
        return rangees * colonnes;
    }

    int nbrTotalVisible() {
        int total = 0;
        initVisibilite();
        for (int i = 0; i < rangees; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (visibilite[i][j] < getArbre(i, j))
                    total++;
            }
        }
        return total;
    }

    int dx[] = { 0, -1, 0, 1 };
    int dy[] = { -1, 0, 1, 0 };

    void initScenic() {
        scenic = new int[rangees][colonnes];
        for (int i = 0; i < rangees; i++) {
            for (int j = 0; j < colonnes; j++) {
                int taille = getArbre(i, j);
                scenic[i][j] = 1;
                boolean verbose = false; // (i == 1 && j == 2) || (i == 3 && j == 2);
                for (int k = 0; k < 4; k++) {
                    int distance = 0;
                    int posY = i;
                    int posX = j;
                    while (true) {
                        if (verbose) {
                            System.out.println("posX:" + posX + ".posY:" + posY + ".dist:" + distance);
                        }
                        posX += dx[k];
                        posY += dy[k];
                        if (posY < 0 || posY == rangees || posX < 0 || posX == colonnes) {
                            if (verbose) {
                                System.out.println("out of bound (" + posX + "," + posY + ")");
                            }
                            break;
                        }
                        distance++;
                        if (verbose)
                            System.out.println("dist:" + distance);
                        if (taille <= getArbre(posY, posX)) {
                            if (verbose) {
                                System.out.println(
                                        "tree " + getArbre(posY, posX) + " at (" + posX + "," + posY + ") blocks view");
                            }
                            break;
                        }
                    }
                    scenic[i][j] = scenic[i][j] * (distance);
                    if (verbose) {
                        System.out.println("---- points:" + distance);
                    }
                }
                if (verbose) {
                    System.out.println(" = " + scenic[i][j]);
                }
            }
        }
    }

    int meilleurScenic() {
        initScenic();
        int meilleur = 0;
        for (int i = 0; i < rangees; i++) {
            for (int j = 0; j < colonnes; j++) {
                if (scenic[i][j] > meilleur)
                    meilleur = scenic[i][j];
            }
        }
        return meilleur;
    }
}
