import java.io.*;
import java.util.*;

class day15 {

    static ReseauBalises bsonde;
    static boolean verbose = false;

    public static void main(String[] args) {
        bsonde = new ReseauBalises(verbose);

        // analyseur("15/test");
        // int y = 10;
        analyseur("15/input");
        int y = 2000000;        
        System.out.println("A la ligne " + y + " il y a " + bsonde.espacesLibres(y) + " espaces libres.");
        Coord c = bsonde.rechercherEspaceVide();
        long frequency = ((long) c.x * 4000000l + (long) c.y);
        System.out.println("A la ligne " + c.y + ", frequence " + frequency + " il y a un espace vide.");        



        // System.out.println("A la ligne " + y + " il y a " + bsonde.espacesLibres(y) +
        // " espaces libres.");
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                bsonde.signal(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Etendue implements Comparable<Etendue> {
    int min;
    int max;

    public Etendue(int min, int max) {
        this.set(min, max);
    }

    void set(int a, int b) {
        if (a < b) {
            this.min = a;
            this.max = b;
        } else {
            this.min = b;
            this.max = a;
        }
    }

    @Override
    public int compareTo(Etendue o) {
        return this.min - o.min;
    }
}

class CapteurBalise {
    int capteurX;
    int capteurY;
    int baliseX;
    int baliseY;
    int distance;

    public CapteurBalise(String s) {
        String[] elems = s.split(" ");
        this.capteurX = Integer.parseInt(elems[2].substring(2, elems[2].length() - 1));
        this.capteurY = Integer.parseInt(elems[3].substring(2, elems[3].length() - 1));
        this.baliseX = Integer.parseInt(elems[8].substring(2, elems[8].length() - 1));
        this.baliseY = Integer.parseInt(elems[9].substring(2, elems[9].length()));
        this.calculDistance();
    }

    public CapteurBalise(int capteurX, int capteurY, int baliseX, int baliseY) {
        this.capteurX = capteurX;
        this.capteurY = capteurY;
        this.baliseX = baliseX;
        this.baliseY = baliseY;
        this.calculDistance();
    }

    private void calculDistance() {
        distance = Math.abs(capteurX - baliseX) + Math.abs(capteurY - baliseY);
    }

    public boolean estPertinence(int y) {
        int c = distance - Math.abs(capteurY - y);
        return (c >= 0);
    }

    Etendue range(int y) {
        int c = distance - Math.abs(capteurY - y);
        if (c < 0)
            return null;
        return new Etendue(capteurX - c, capteurX + c);
    }
}

class ReseauBalises {
    boolean verbose;
    List<CapteurBalise> balises = new ArrayList<CapteurBalise>();

    public ReseauBalises(boolean verbose) {
        this.verbose = verbose;
    }

    public void signal(String s) {
        balises.add(new CapteurBalise(s.trim()));
    }

    public int espacesLibres(int y) {
        List<Etendue> etendues = new ArrayList<Etendue>();
        Iterator<CapteurBalise> balise = balises.iterator();
        while (balise.hasNext()) {
            CapteurBalise b = balise.next();
            if (b.estPertinence(y)) {
                boolean nouveau = true;
                Etendue e = b.range(y);
                for (int i = 0; i < etendues.size(); i++) {
                    Etendue test = etendues.get(i);
                    if (!(test.max < e.min - 1 || test.min > e.max + 1)) {
                        int min = (test.min < e.min) ? test.min : e.min;
                        int max = (test.max > e.max) ? test.max : e.max;
                        test.min = min;
                        test.max = max;
                        nouveau = false;
                        break;
                    }
                }
                if (nouveau)
                    etendues.add(e);
            }
        }
        // Simplifier

        Collections.sort(etendues);
        for (int i = 1; i < etendues.size(); i++) {
            Etendue etendueA = etendues.get(i - 1);
            Etendue etendueB = etendues.get(i);
            if (!(etendueA.max < etendueB.min - 1 || etendueA.min > etendueB.max + 1)) {
                int min = (etendueA.min < etendueB.min) ? etendueA.min : etendueB.min;
                int max = (etendueA.max > etendueB.max) ? etendueA.max : etendueB.max;
                etendueA.min = min;
                etendueA.max = max;
                etendues.remove(i);
                i--;
            }
        }

        // Compter les espaces couverts
        int nbr = 0;
        Iterator<Etendue> etendue = etendues.iterator();
        while (etendue.hasNext()) {
            Etendue e = etendue.next();
            System.out.print("(ajout) nbr = " + nbr + " + " + e.max + " - " + e.min + " + 1 = ");
            nbr += e.max - e.min + 1;
            System.out.println(nbr);
            Iterator<CapteurBalise> balisee = balises.iterator();
            List<Integer> occupe = new ArrayList<Integer>();
            while (balisee.hasNext()) {
                CapteurBalise b = balisee.next();
                if (b.baliseY == y) {
                    if (e.min <= b.baliseX && e.max >= b.baliseX) {
                        occupe.add(b.baliseX);
                    }
                }
                if (b.capteurY == y) {
                    if (e.min <= b.capteurX && e.max >= b.capteurX) {
                        occupe.add(b.capteurX);
                    }
                }
            }
            int nbrOccupe = util.removeDuplicates(occupe).size();
            System.out.print("(retrait) nbr = " + nbr + " - " + nbrOccupe + " = ");
            nbr -= nbrOccupe;
            System.out.println(nbr);
        }
        // Retirer les esapces occupes
        return nbr;
    }

    public Coord rechercherEspaceVide() {
        boolean continuer = true;
        int x = 0; int y = 0;
        while (continuer) {
            List<Etendue> etendues = new ArrayList<Etendue>();
            Iterator<CapteurBalise> balise = balises.iterator();
            while (balise.hasNext()) {
                CapteurBalise b = balise.next();
                if (b.estPertinence(y)) {
                    boolean nouveau = true;
                    Etendue e = b.range(y);
                    for (int i = 0; i < etendues.size(); i++) {
                        Etendue test = etendues.get(i);
                        if (!(test.max < e.min - 1 || test.min > e.max + 1)) {
                            int min = (test.min < e.min) ? test.min : e.min;
                            int max = (test.max > e.max) ? test.max : e.max;
                            test.min = min;
                            test.max = max;
                            nouveau = false;
                            break;
                        }
                    }
                    if (nouveau)
                        etendues.add(e);
                }
            }
            // Simplifier
            Collections.sort(etendues);
            for (int i = 1; i < etendues.size(); i++) {
                Etendue etendueA = etendues.get(i - 1);
                Etendue etendueB = etendues.get(i);
                if (!(etendueA.max < etendueB.min - 1 || etendueA.min > etendueB.max + 1)) {
                    int min = (etendueA.min < etendueB.min) ? etendueA.min : etendueB.min;
                    int max = (etendueA.max > etendueB.max) ? etendueA.max : etendueB.max;
                    etendueA.min = min;
                    etendueA.max = max;
                    etendues.remove(i);
                    i--;
                }
            }
            if(etendues.size()>1) {
                for (int i=0;i<etendues.size();i++) {
                    x = etendues.get(0).max + 1;
                    if (x>=0 && x<= 4000000) {
                        continuer = false;
                        break;
                    }
                }
            } else y++;
        }
        return new Coord(x,y);
    }
}

class util {
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }
}
