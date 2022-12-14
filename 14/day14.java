import java.io.*;
import java.util.*;

class day14 {

    static Caverne caverneA;
    static Caverne caverneB;
    static boolean verbose = false;

    public static void main(String[] args) {
        caverneA = new Caverne(false, verbose);
        caverneB = new Caverne(true, verbose);
        analyseur("14/input");
        caverneA.ajoutSourceSable(500, 0);
        caverneA.genererCarte();
        caverneB.ajoutSourceSable(500, 0);
        caverneB.genererCarte();
        System.out.println("Nombre de sable est " + caverneA.coulerSable());
        if (verbose) caverneA.voirCarte();
        System.out.println("Nombre de sable est " + caverneB.coulerSable());
        if (verbose) caverneB.voirCarte();
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                caverneA.signal(ligne.trim());
                caverneB.signal(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Coordo {
    int x;
    int y;

    public Coordo() {
    }

    public Coordo(Coordo c) {
        this.x = c.x;
        this.y = c.y;
    }

    public Coordo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordo(String s) {
        decode(s);
    }

    void decode(String s) {
        String[] elems = s.trim().split(",");
        x = Integer.parseInt(elems[0]);
        y = Integer.parseInt(elems[1]);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

class Murs {
    String s;
    List<Coordo> coins = new ArrayList<Coordo>();

    public Murs(String s) {
        this.s = s;
        decode(s);
    }

    void decode(String s) {
        String[] Coordos = s.trim().split("->");
        for (int i = 0; i < Coordos.length; i++) {
            coins.add(new Coordo(Coordos[i]));
        }
    }
}

class Carto {
    Coordo coinHautGauche;
    Coordo coinBasDroite;
    Coordo sourceSable;
    char[][] data;

    Carto(Coordo hautg, Coordo basd) {
        this.coinHautGauche = hautg;
        this.coinBasDroite = basd;
        data = new char[Math.abs(basd.y - hautg.y) + 1][Math.abs(basd.x - hautg.x) + 1];
        clear();
    }

    void clear() {
        for (int y = coinHautGauche.y; y <= coinBasDroite.y; y++) {
            for (int x = coinHautGauche.x; x <= coinBasDroite.x; x++) {
                put(new Coordo(x, y), '.');
            }
        }
    }

    void put(Coordo Coordo, char c) {
        data[Coordo.y - coinHautGauche.y][Coordo.x - coinHautGauche.x] = c;
    }

    char get(Coordo Coordo) {
        return data[Coordo.y - coinHautGauche.y][Coordo.x - coinHautGauche.x];
    }

    char get(Coordo Coordo, int dx, int dy) {
        return data[Coordo.y - coinHautGauche.y + dy][Coordo.x - coinHautGauche.x + dx];
    }

    void see() {
        for (int y = coinHautGauche.y; y <= coinBasDroite.y; y++) {
            for (int x = coinHautGauche.x; x <= coinBasDroite.x; x++) {
                System.out.print(get(new Coordo(x, y)));
            }
            System.out.println("");
        }
    }

    public void ajoutMurs(List<Murs> murs) {
        Iterator<Murs> mur = murs.iterator();
        while (mur.hasNext()) {
            Murs m = mur.next();
            Iterator<Coordo> coin = m.coins.iterator();
            Coordo prev = coin.next();
            put(prev, '#');
            while (coin.hasNext()) {
                Coordo courant = coin.next();
                while (prev.x != courant.x || prev.y != courant.y) {
                    if (prev.x < courant.x)
                        prev.x++;
                    if (prev.x > courant.x)
                        prev.x--;
                    if (prev.y < courant.y)
                        prev.y++;
                    if (prev.y > courant.y)
                        prev.y--;
                    put(prev, '#');
                }
            }
        }
    }

    public void ajoutSourceSable(Coordo sourceSable) {
        this.sourceSable = sourceSable;
        put(sourceSable, '+');
    }
}

class Caverne {
    boolean verbose;
    List<Murs> murs = new ArrayList<Murs>();
    Coordo coinHautGauche;
    Coordo coinBasDroite;
    Carto carte;
    Coordo sourceSable;
    boolean plancher;

    public Caverne(boolean plancher, boolean verbose) {
        this.plancher = plancher;
        this.verbose = verbose;
    }

    public void signal(String s) {
        murs.add(new Murs(s));
    }

    public void ajoutSourceSable(int x, int y) {
        this.sourceSable = new Coordo(x, y);
    }

    public void genererCarte() {
        coinsExtremes();
        carte = new Carto(coinHautGauche, coinBasDroite);
        carte.ajoutMurs(murs);
        carte.ajoutSourceSable(sourceSable);
        if (verbose)
            carte.see();
    }

    public void voirCarte() {
        carte.see();
    }

    public void ajouterSable(int quantite) {
        for (int i = 0; i < quantite; i++) {
            Coordo sable = new Coordo(sourceSable);
            int dx = 0;
            int dy = 1;
            while (dx != 0 || dy != 0) {
                if (sable.y + dy > coinBasDroite.y)
                    break;
                if (carte.get(sable, 0, 1) == '.') {
                    dx = 0;
                    dy = 1;
                } else if (carte.get(sable, -1, 1) == '.') {
                    dx = -1;
                    dy = 1;
                } else if (carte.get(sable, 1, 1) == '.') {
                    dx = 1;
                    dy = 1;
                } else {
                    dx = 0;
                    dy = 0;
                }
                sable.x += dx;
                sable.y += dy;
            }
            carte.put(sable, 'o');
        }
    }

    public int coulerSable() {
        int somme = 0;
        boolean continuer = true;
        while (continuer) {
            Coordo sable = new Coordo(sourceSable);
            int dx = 0;
            int dy = 1;
            while (dx != 0 || dy != 0) {
                if (sable.y + 1 > coinBasDroite.y) {
                    if (!plancher)
                        continuer = false;
                    break;
                }
                if (carte.get(sable, 0, 1) == '.') {
                    dx = 0;
                    dy = 1;
                } else if (carte.get(sable, -1, 1) == '.') {
                    dx = -1;
                    dy = 1;
                } else if (carte.get(sable, 1, 1) == '.') {
                    dx = 1;
                    dy = 1;
                } else {
                    dx = 0;
                    dy = 0;
                }
                sable.x += dx;
                sable.y += dy;
            }
            if (continuer) {
                carte.put(sable, 'o');
                somme++;
                if (sable.x == sourceSable.x && sable.y == sourceSable.y)
                    continuer = false;
            }
        }
        return somme;
    }

    void coinsExtremes() {
        Iterator<Murs> mur = murs.iterator();
        while (mur.hasNext()) {
            Murs m = mur.next();
            Iterator<Coordo> coin = m.coins.iterator();
            while (coin.hasNext()) {
                Coordo c = coin.next();
                if (coinHautGauche == null) {
                    coinHautGauche = new Coordo(c);
                    coinBasDroite = new Coordo(c);
                } else {
                    if (c.x < coinHautGauche.x)
                        coinHautGauche.x = c.x;
                    if (c.y < coinHautGauche.y)
                        coinHautGauche.y = c.y;
                    if (c.x > coinBasDroite.x)
                        coinBasDroite.x = c.x;
                    if (c.y > coinBasDroite.y)
                        coinBasDroite.y = c.y;
                }
            }
        }
        // Patch : pour avoir de l'espace en haut!
        coinHautGauche.y = 0;
        if (plancher) {
            coinBasDroite.y++;
            coinHautGauche.x -= coinBasDroite.y;
            coinBasDroite.x += coinBasDroite.y;
        } else {
            // Patch : pour avoir de l'espace à gauche et à droite
            coinHautGauche.x--;
            coinBasDroite.x++;
            //
        }
    }

}