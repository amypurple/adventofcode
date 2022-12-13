import java.io.*;
import java.util.*;

class day12 {

    static Carte carte;
    static boolean verbose = false;

    public static void main(String[] args) {
        carte = new Carte(verbose);
        analyseur("input");
        carte.solution1();
        carte.solution2();
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                carte.signal(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Coord {
    int x;
    int y;

    public Coord() {
    }

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void copie(Coord coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordonnees [x=" + x + ", y=" + y + "]";
    }
}

class Carte {
    List<String> signaux = new ArrayList<String>();
    char[][] elevations;
    int[][] pas;
    int pasMinimum;
    boolean[][] visite;
    boolean verbose;
    boolean partie2;
    Coord depart;
    Coord fin;
    Coord taille = new Coord();
    List<Coord> trace = new ArrayList<Coord>();
    List<Coord> departs = new ArrayList<Coord>();

    static int infini = 999999;

    Carte(boolean verbose) {
        this.verbose = verbose;
        this.partie2 = false;
    }

    void signal(String ligne) {
        ligne = ligne.trim();
        if (ligne.length() > 0)
            signaux.add(ligne);
    }

    void initialisation() {
        taille.y = signaux.size();
        taille.x = signaux.get(0).length();
        elevations = new char[taille.y][taille.x];
        pas = new int[taille.y][taille.x];
        visite = new boolean[taille.y][taille.x];
        int index = 0;
        Iterator<String> ligne = signaux.iterator();
        while (ligne.hasNext()) {
            elevations[index] = ligne.next().toCharArray();
            for (int i = 0; i < elevations[index].length; i++) {
                if (partie2 && elevations[index][i] == 'a') {
                    departs.add(new Coord(i, index));
                }
                if (elevations[index][i] == 'S') {
                    elevations[index][i] = 'a';
                    departs.add(new Coord(i, index));
                }
                if (elevations[index][i] == 'E') {
                    elevations[index][i] = 'z';
                    fin = new Coord(i, index);
                }
            }
            index++;
        }
    }

    // Algorithme Dijkstra
    void Dijkstra() {
        // initialisation
        initialisation();
        // Quel est le nombre de pas minimum? infini?
        pasMinimum = infini;
        // Tant qu'il y a des departs a regarder
        Iterator<Coord> lesDeparts = departs.iterator();
        // Boucler
        while (lesDeparts.hasNext()) {
            // depart = prochain depart possible
            depart = lesDeparts.next();
            // Remise a infini des pas
            for (int i = 0; i < taille.y; i++) {
                for (int j = 0; j < taille.x; j++) {
                    pas[i][j] = infini;
                    visite[i][j] = false;
                }
            }
            // Depart est initialise avec 0 pas.
            pas[depart.y][depart.x] = 0;
            // Depart est mis dans la liste a visiter.
            trace.add(depart);
            // Choisir prochain lieu.
            Coord coordChoisie = new Coord();
            // Boucle
            while (trace.size() > 0) {
                int index = 0;
                int indexChoisi = 0;
                Iterator<Coord> coord = trace.iterator();
                while (coord.hasNext()) {
                    Coord nouvelle = coord.next();
                    if (index == 0) {
                        coordChoisie.copie(nouvelle);
                    } else {
                        if (pas[nouvelle.y][nouvelle.x] < pas[coordChoisie.y][coordChoisie.x]) {
                            coordChoisie.copie(nouvelle);
                            indexChoisi = index;
                        }
                    }
                    index++;
                }
                // Retirer le lieu choisi de la trace.
                trace.remove(indexChoisi);
                // Marquer lieu comme etant visite.
                visite[coordChoisie.y][coordChoisie.x] = true;
                // avancer d'un pas
                int petitPas = pas[coordChoisie.y][coordChoisie.x] + 1;
                // Est-ce que le lieu vers le nord est accessible?
                if (coordChoisie.y >= 1 && !visite[coordChoisie.y - 1][coordChoisie.x]
                        && elevations[coordChoisie.y][coordChoisie.x]
                                + 1 >= elevations[coordChoisie.y - 1][coordChoisie.x]) {
                    if (pas[coordChoisie.y - 1][coordChoisie.x] > petitPas) {
                        if (pas[coordChoisie.y - 1][coordChoisie.x] == infini) {
                            // Ajouter le nouveau lieu dans la trace
                            trace.add(new Coord(coordChoisie.x, coordChoisie.y - 1));
                        }
                        pas[coordChoisie.y - 1][coordChoisie.x] = petitPas;
                    }
                }
                // Est-ce que le lieu vers le sud est accessible?
                if (coordChoisie.y < taille.y - 1 && !visite[coordChoisie.y + 1][coordChoisie.x]
                        && elevations[coordChoisie.y][coordChoisie.x]
                                + 1 >= elevations[coordChoisie.y + 1][coordChoisie.x]) {
                    if (pas[coordChoisie.y + 1][coordChoisie.x] > petitPas) {
                        if (pas[coordChoisie.y + 1][coordChoisie.x] == infini) {
                            // Ajouter le nouveau lieu dans la trace
                            trace.add(new Coord(coordChoisie.x, coordChoisie.y + 1));
                        }
                        pas[coordChoisie.y + 1][coordChoisie.x] = petitPas;
                    }
                }
                // Est-ce que le lieu vers l'ouest est accessible?
                if (coordChoisie.x >= 1 && !visite[coordChoisie.y][coordChoisie.x - 1]
                        && elevations[coordChoisie.y][coordChoisie.x]
                                + 1 >= elevations[coordChoisie.y][coordChoisie.x - 1]) {
                    if (pas[coordChoisie.y][coordChoisie.x - 1] > petitPas) {
                        if (pas[coordChoisie.y][coordChoisie.x - 1] == infini) {
                            // Ajouter le nouveau lieu dans la trace
                            trace.add(new Coord(coordChoisie.x - 1, coordChoisie.y));
                        }
                        pas[coordChoisie.y][coordChoisie.x - 1] = petitPas;
                    }
                }
                // Est-ce que le lieu vers l'est est accessible?
                if (coordChoisie.x < taille.x - 1 && !visite[coordChoisie.y][coordChoisie.x + 1]
                        && elevations[coordChoisie.y][coordChoisie.x]
                                + 1 >= elevations[coordChoisie.y][coordChoisie.x + 1]) {
                    if (pas[coordChoisie.y][coordChoisie.x + 1] > petitPas) {
                        if (pas[coordChoisie.y][coordChoisie.x + 1] == infini) {
                            // Ajouter le nouveau lieu dans la trace
                            trace.add(new Coord(coordChoisie.x + 1, coordChoisie.y));
                        }
                        pas[coordChoisie.y][coordChoisie.x + 1] = petitPas;
                    }
                }
            }
            if (pasMinimum > pas[fin.y][fin.x])
                pasMinimum = pas[fin.y][fin.x];
        }
        System.out.println("Le nombre de pas minimum pour se rendre est " + pasMinimum);
    }

    void solution1() {
        partie2 = false;
        Dijkstra();
    }

    void solution2() {
        partie2 = true;
        Dijkstra();
    }
}