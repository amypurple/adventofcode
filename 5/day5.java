import java.io.*;
import java.util.*;

class day5 {

    public static List<String> dessin = new ArrayList<String>();
    public static List<String> mouvements = new ArrayList<String>();

    public static CrateMover CrateMover9000 = new CrateMover(9000);
    public static CrateMover CrateMover9001 = new CrateMover(9001);

    public static void main(String[] args) {
        analyseur("input");
        Iterator<String> caisses = dessin.iterator();
        while (caisses.hasNext()) {
            String ligne = caisses.next();
            CrateMover9000.ajouterLigne(ligne);
            CrateMover9001.ajouterLigne(ligne);
        }
        Iterator<String> mouvement = mouvements.iterator();
        while (mouvement.hasNext()) {
            String ligne = mouvement.next();
            CrateMover9000.executerLigne(ligne);
            CrateMover9001.executerLigne(ligne);
        }
        CrateMover9000.montrerMot();
        CrateMover9001.montrerMot();
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        boolean dessinFini = false;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                if (!dessinFini) {
                    if (ligne.trim().length() != 0) {
                        dessin.add(new String(ligne));
                    } else
                        dessinFini = true;
                } else {
                    mouvements.add(ligne);
                }
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void afficherDessin() {
        System.out.println("DESSIN :");
        for (int i = 0; i < dessin.size(); i++) {
            System.out.println(dessin.get(i));
        }
    }

}

class Pile {
    protected String contenu = "";

    public void add(char c) {
        contenu = contenu + c;
    }

    public void push(char c) {
        contenu = c + contenu;
    }

    public char pop() {
        char c = contenu.charAt(0);
        contenu = contenu.substring(1);
        return c;
    }

    public String pick(int size) {
        String s = contenu.substring(0, size);
        contenu = contenu.substring(size);
        return s;
    }

    public void put(String s) {
        contenu = s + contenu;
    }

    public char peek(int position) {
        return (position < contenu.length()) ? contenu.charAt(position) : ' ';
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < contenu.length(); i++) {
            s += "[" + contenu.charAt(i) + "] ";
        }
        return s;
    }

}

class CrateMover {
    Pile[] piles = new Pile[9];
    int maximumPiles = 9;
    int model;

    CrateMover(int model) {
        this.model = model;
        for (int i = 0; i < maximumPiles; i++) {
            piles[i] = new Pile();
        }
    }

    void ajouterLigne(String ligne) {
        if (ligne.contains("[")) {
            for (int i = 0; i + 1 < ligne.length(); i += 4) {
                char c = ligne.charAt(i + 1);
                if (c >= 'A' && c <= 'Z') {
                    piles[i >> 2].add(c);
                }
            }
        }
    }

    void executerLigne(String ligne) {
        String[] elements = ligne.split(" ");
        int nombre = Integer.parseInt(elements[1]);
        int de = Integer.parseInt(elements[3]) - 1;
        int a = Integer.parseInt(elements[5]) - 1;
        // System.out.println("Exec : Deplace " + nombre + " caisses de #" + (de + 1) +
        // " a #" + (a + 1));
        if (model == 9000) {
            for (int i = 0; i < nombre; i++) {
                piles[a].push(piles[de].pop());
            }
        } else {
            piles[a].put(piles[de].pick(nombre));
        }
    }

    void montrerPiles() {
        for (int i = 0; i < maximumPiles; i++) {
            System.out.println("pile #" + (i + 1) + " : " + piles[i]);
        }
    }

    void montrerMot() {
        System.out.print("Avec le CrateMover " + model + ", le mot est : ");
        for (int i = 0; i < maximumPiles; i++) {
            System.out.print(piles[i].peek(0));
        }
        System.out.println("");
    }
}