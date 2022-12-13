import java.io.*;
import java.util.*;

class day9 {

    static Corde corde2 = new Corde(false, 2);
    static Corde corde10 = new Corde(false, 10);

    public static void main(String[] args) {
        analyseur("input");
        System.out
                .println("La trace du dernier noeud de la corde-2-noeuds a une longueur de " + corde2.longueurTrace());
        System.out.println(
                "La trace du dernier noeud de la corde-10-noeuds a une longueur de " + corde10.longueurTrace());
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                corde2.signal(ligne.trim());
                corde10.signal(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Coordonnees implements Comparable<Coordonnees> {
    int x = 0;
    int y = 0;

    Coordonnees() {
    }

    Coordonnees(Coordonnees objet) {
        this.x = objet.x;
        this.y = objet.y;
    }

    void deplacer(String direction, boolean verbose) {
        if (verbose)
            System.out.print(direction);
        int dy = (direction.equals("U")) ? -1 : (direction.equals("D")) ? 1 : 0;
        int dx = (direction.equals("L")) ? -1 : (direction.equals("R")) ? 1 : 0;
        this.x += dx;
        this.y += dy;
    }

    boolean vers(Coordonnees objet, boolean verbose) {
        boolean modif = false;
        int diffX = objet.x - this.x;
        int diffY = objet.y - this.y;
        int distanceX = Math.abs(diffX);
        int distanceY = Math.abs(diffY);
        int directionX = (diffX > 0) ? 1 : -1;
        int directionY = (diffY > 0) ? 1 : -1;
        if (distanceX > 1 || distanceY > 1) {
            if (distanceY != 0) {
                this.y += directionY;
                modif = true;
            }
            if (distanceX != 0) {
                this.x += directionX;
                modif = true;
            }
        }
        if (modif && verbose)
            System.out.print("+");
        return modif;
    }

    @Override
    public int compareTo(Coordonnees o) {
        return (o.y < this.y) ? -2
                : (o.y == this.y && o.x < this.x) ? -1
                        : (o.y == this.y && o.x == this.x) ? 0 : (o.y == this.y && o.x > this.x) ? 1 : 2;
    }
}

class Corde {
    Boolean verbose;
    Coordonnees[] noeuds;
    int nombre;
    List<Coordonnees> trace = new ArrayList<Coordonnees>();

    Corde(boolean verbose, int nombre) {
        this.verbose = verbose;
        this.nombre = nombre;
        noeuds = new Coordonnees[this.nombre];
        for (int i = 0; i < nombre; i++) {
            noeuds[i] = new Coordonnees();
        }
        trace.add(new Coordonnees(noeuds[this.nombre - 1]));
    }

    void signal(String ligne) {
        String[] elems = ligne.trim().split(" ");
        int pas = Integer.parseInt(elems[1]);
        for (int i = 0; i < pas; i++) {
            noeuds[0].deplacer(elems[0], verbose);
            for (int j = 1; j < this.nombre; j++) {
                if (noeuds[j].vers(noeuds[j - 1], verbose) && j == this.nombre - 1)
                    trace.add(new Coordonnees(noeuds[this.nombre - 1]));
            }
        }
    }

    ArrayList<Coordonnees> simplifie() {
        ArrayList<Coordonnees> nouvelleListe = new ArrayList<Coordonnees>();
        for (Coordonnees element : trace) {
            boolean contient = false;
            for (int i = 0; i < nouvelleListe.size(); i++) {
                if (nouvelleListe.get(i).compareTo(element) == 0) {
                    contient = true;
                }
            }
            if (!contient)
                nouvelleListe.add(element);
        }
        return nouvelleListe;
    }

    int longueurTrace() {
        // return trace.size();
        return simplifie().size();
    }
}