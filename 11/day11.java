import java.io.*;
import java.util.*;

class day11 {

    static TroupeSinges singesRelax = new TroupeSinges(3l);
    static TroupeSinges singesStress = new TroupeSinges(1l);
    static boolean verbose = false;

    public static void main(String[] args) {
        analyseur("11/input");
        singesRelax.executer(20, verbose);
        singesRelax.totalInspections();
        singesStress.executer(1, verbose);
        singesStress.totalInspections();
        singesStress.executer(20 - 1, verbose);
        singesStress.totalInspections();
        singesStress.executer(1000 - 20, verbose);
        singesStress.totalInspections();
        singesStress.executer(10000 - 1000, verbose);
        singesStress.totalInspections();
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                singesRelax.signal(ligne.trim());
                singesStress.signal(ligne.trim());
            }
            tamponLecture.close();
            singesRelax.finDeSignal(verbose);
            singesStress.finDeSignal(verbose);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class EffetOperation {

    String[] equation;

    EffetOperation(String ligne) {
        equation = ligne.split(" ");
    }

    long prend(long ancien) {
        long nouveau = 0;
        long valeur = 0;
        for (int i = 1; i < equation.length; i += 2) {
            String operateur = equation[i];
            String v = equation[i + 1];
            if (v.equals("old"))
                valeur = ancien;
            else
                valeur = Long.parseLong(v);
            if (operateur.equals("="))
                nouveau = valeur;
            if (operateur.equals("+"))
                nouveau += valeur;
            if (operateur.equals("*"))
                nouveau *= valeur;
        }
        return nouveau;
    }

    @Override
    public String toString() {
        return String.join(" ", equation);
    }
}

class Lancer {
    long item;
    int versSinge;

    Lancer(long item, int singe) {
        this.item = item;
        this.versSinge = singe;
    }
}

class Singe {
    List<Long> liste = new ArrayList<Long>();
    EffetOperation operation;
    long testDivisible;
    int singeSiVrai;
    int singeSiFaux;
    long nombreObjetsInspectes;
    int numero;

    Singe(int numero) {
        this.numero = numero;
        this.nombreObjetsInspectes = 0;
    }

    void listeItems(String ligne) {
        String[] elems = ligne.split(",");
        for (int i = 0; i < elems.length; i++) {
            recoit(Long.parseLong(elems[i].trim()));
        }
    }

    void recoit(long item) {
        liste.add(item);
    }

    Lancer[] execute(long modulo, long relax, boolean verbose) {
        if (verbose)
            System.out.println("Singe :" + numero);
        List<Lancer> listeLancer = new ArrayList<Lancer>();
        for (int i = 0; i < liste.size(); i++) {
            long item = liste.get(i);
            long niveauStress = operation.prend(item) / relax;
            Integer destination = ((niveauStress % testDivisible) == 0l) ? singeSiVrai : singeSiFaux;
            if (verbose)
                System.out.println("item[" + item + "] => " + niveauStress + " vers Singe " + destination);
            listeLancer.add(new Lancer(niveauStress % modulo, destination));
            nombreObjetsInspectes++;
        }
        liste.clear();
        return listeLancer.toArray(new Lancer[listeLancer.size()]);
    }

    void status() {
        System.out.println("Singe " + numero + ":");
        System.out.println("  items: " + liste.toString());
        System.out.println("  equation: " + operation.toString());
        System.out.println("  nombre d'inspections faites: " + nombreObjetsInspectes);
        System.out.println("  test division par " + testDivisible);
        System.out.println("    si vrai lancer a singe " + singeSiVrai);
        System.out.println("    sinon lancer a singe " + singeSiFaux);
    }
}

class TroupeSinges {
    List<Singe> singes = new ArrayList<Singe>();
    Singe singeCourant = null;
    long modulo;
    int nbrTours = 0;
    long relax;

    TroupeSinges(long relax) {
        this.relax = relax;
    }

    void signal(String ligne) {
        String[] elems = ligne.split(":");
        String cmd = elems[0].trim();
        String parametre = "";
        if (elems.length > 1) {
            // System.out.println(elems[1] + ".");
            parametre = elems[1].trim();
        }
        if (cmd.startsWith("Monkey")) {
            if (singeCourant != null)
                singes.add(singeCourant);
            singeCourant = new Singe(cmd.charAt(cmd.length() - 1) - '0');
        }
        if (cmd.equals("Starting items")) {
            singeCourant.listeItems(parametre);
        }
        if (cmd.equals("Operation")) {
            singeCourant.operation = new EffetOperation(parametre);
        }
        if (cmd.equals("Test")) {
            singeCourant.testDivisible = Integer.parseInt(parametre.split(" ")[2]);
        }
        if (cmd.equals("If true")) {
            singeCourant.singeSiVrai = Integer.parseInt(parametre.split(" ")[3]);
        }
        if (cmd.equals("If false")) {
            singeCourant.singeSiFaux = Integer.parseInt(parametre.split(" ")[3]);
        }
    }

    void status() {
        Iterator<Singe> singe = singes.iterator();
        while (singe.hasNext()) {
            singe.next().status();
        }
    }

    void finDeSignal(boolean verbose) {
        if (singeCourant != null)
            singes.add(singeCourant);
        if (verbose)
            status();

        modulo = relax;
        Iterator<Singe> singe = singes.iterator();
        while (singe.hasNext()) {
            modulo = modulo * singe.next().testDivisible;
        }
        if (verbose)
            System.out.println("modulo : " + modulo);
    }

    void executer(int nbrTours, boolean verbose) {
        for (int i = 0; i < nbrTours; i++) {
            Iterator<Singe> singe = singes.iterator();
            while (singe.hasNext()) {
                Lancer[] lance = singe.next().execute(modulo, relax, verbose);
                for (int j = 0; j < lance.length; j++) {
                    singes.get(lance[j].versSinge).recoit(lance[j].item);
                }
            }
            if (verbose) {
                System.out.println("Tour # " + (i + 1));
                status();
            }
        }
        this.nbrTours += nbrTours;
    }

    void totalInspections() {
        int total = 0;
        int index = 0;
        long[] nombre = new long[singes.size()];
        System.out.println("== Apres tour " + this.nbrTours + " ==");
        Iterator<Singe> singe = singes.iterator();
        while (singe.hasNext()) {
            Singe s = singe.next();
            nombre[index] = s.nombreObjetsInspectes;
            total += nombre[index];
            System.out.println("Monkey " + index + " a inspecte les items " + s.nombreObjetsInspectes + " fois.");
            index++;
        }
        Arrays.sort(nombre);
        long monkeybusiness = nombre[nombre.length - 1] * nombre[nombre.length - 2];
        System.out.println("Total inpections: " + total);
        System.out.println("Niveau de Monkey Business: " + monkeybusiness);
    }
}