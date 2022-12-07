import java.io.*;
import java.util.*;

class day7 {

    static Lecteur drive = new Lecteur(70000000);

    public static void main(String[] args) {
        analyseur("7/input");
        drive.directoryFromRoot();
        System.out.println(" Nombre de 'dir' : " + drive.nbrDossiers());
        drive.printTotalDossiersAuPlus(100000);
        drive.printDossiersTailleInferieureOuEgale(100000);
        drive.printDossierPourLiberer(30000000);
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                drive.signal(ligne);
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Lecteur {
    int limite;
    Noeud racine;
    Noeud position;

    Lecteur(int limite) {
        this.limite = limite;
        racine = new Noeud();
        position = racine;
    }

    void signal(String ligne) {
        System.out.print(ligne + " => ");
        String[] elems = ligne.split(" ");
        for (int i = 0; i < elems.length; i++)
            System.out.print("[" + elems[i] + "]");
        if (ligne.startsWith("$ ")) {
            String cmd = elems[1];
            if (cmd.equals("cd")) {
                String destination = elems[2];
                if (destination.equals("/")) {
                    position = racine;
                } else if (destination.equals("..")) {
                    position = position.parent;
                } else {
                    position = position.getEnfant(destination);
                }
                System.out.println(" OK ");
            }
            if (cmd.equals("ls")) {
                System.out.println(" OK ");
            }
        } else {
            if (elems[0].equals("dir")) {
                Noeud enfant = new Noeud(elems[1], position);
                position.addEnfant(enfant);
            } else {
                int taille = Integer.parseInt(elems[0]);
                Noeud enfant = new Noeud(elems[1], position);
                enfant.setTaille(taille);
                position.addEnfant(enfant);
            }
            System.out.println(" OK ");
        }
    }

    void directoryFromRoot() {
        racine.directory(0);
    }

    void directoryFromPosition() {
        position.directory(0);
    }

    int nbrDossiers() {
        return 1 + racine.countSubDirectory();
    }

    void printDossiers() {
        List<Noeud> dossiers = racine.getDossiers();
        dossiers.add(racine);
        Iterator<Noeud> liste = dossiers.iterator();
        while (liste.hasNext()) {
            Noeud elem = liste.next();
            System.out.println("dir " + elem.nom + ", size=" + elem.tailleRecursive);
        }
    }

    void printTotalDossiersAuPlus(int limite) {
        int total = 0;
        List<Noeud> dossiers = racine.getDossiers();
        dossiers.add(racine);
        Iterator<Noeud> liste = dossiers.iterator();
        while (liste.hasNext()) {
            Noeud elem = liste.next();
            if (elem.tailleRecursive <= limite) {
                total += elem.tailleRecursive;
                System.out.println("dir " + elem.nom + ", size=" + elem.tailleRecursive);
            }
        }
        System.out.println("Taille des dossiers inferieurs ou egale a " + limite + " (naif) = " + total);
    }

    void printDossiersTailleInferieureOuEgale(int limite) {
        List<Noeud> dossiers = racine.getDossiers();
        dossiers.add(racine);
        Iterator<Noeud> liste = dossiers.iterator();
        while (liste.hasNext()) {
            Noeud elem = liste.next();
            if (elem.tailleRecursive <= limite) {
                elem.set();
            }
        }
        System.out.println("Taille des dossiers inferieurs ou egale a " + limite + " : " + racine.tailleSelected());
    }

    void printDossierPourLiberer(int espaceDemande) {
        int minimum = espaceDemande + racine.tailleRecursive - this.limite;
        int taille = racine.tailleRecursive;
        List<Noeud> dossiers = racine.getDossiers();
        Iterator<Noeud> liste = dossiers.iterator();
        while (liste.hasNext()) {
            Noeud elem = liste.next();
            if (elem.tailleRecursive <= taille && elem.tailleRecursive>= minimum) {
                taille = elem.tailleRecursive;
            }
        }
        System.out.println("Taille du dossier à effacer est de " + taille);
    }


}

class Noeud {
    List<Noeud> enfants = new ArrayList<Noeud>();
    Noeud parent = null;
    int taille = 0;
    boolean selected = false;
    String nom;
    int tailleRecursive = 0;

    Noeud() {
        this.nom = "/";
    }

    Noeud(String nom, Noeud parent) {
        this.parent = parent;
        this.nom = nom;
    }

    Noeud getEnfant(String nom) {
        Noeud resultat = null;
        int i = 0;
        do {
            resultat = enfants.get(i++);
        } while (!resultat.nom.equals(nom));
        return resultat;
    }

    List<Noeud> getDossiers() {
        List<Noeud> dossiers = new ArrayList<Noeud>();
        Iterator<Noeud> liste = enfants.iterator();
        while (liste.hasNext()) {
            Noeud enfant = liste.next();
            if (enfant.isDirectory()) {
                dossiers.add(enfant);
                dossiers.addAll(enfant.getDossiers());
            }
        }
        return dossiers;
    }

    void setParent(Noeud parent) {
        this.parent = parent;
    }

    void addEnfant(Noeud enfant) {
        this.enfants.add(enfant);
    }

    void setTaille(int taille) {
        this.taille = taille;
        this.addTaille(taille);
    }

    void addTaille(int taille) {
        this.tailleRecursive += taille;
        if (this.parent != null)
            this.parent.addTaille(taille);
    }

    void set() {
        selected = true;
        Iterator<Noeud> liste = enfants.iterator();
        while (liste.hasNext()) {
            Noeud enfant = liste.next();
            enfant.set();
        }
    }

    void reset() {
        selected = false;
        Iterator<Noeud> liste = enfants.iterator();
        while (liste.hasNext()) {
            Noeud enfant = liste.next();
            enfant.reset();
        }
    }

    boolean isDirectory() {
        return taille == 0;
    }

    int tailleSelected() {
        int taille = 0;
        if (this.isDirectory()) {
            Iterator<Noeud> liste = enfants.iterator();
            while (liste.hasNext()) {
                Noeud enfant = liste.next();
                taille += enfant.tailleSelected();
            }
        } else {
            taille = this.selected ? this.taille : 0;
        }
        return taille;
    }

    int tailleDossier() {
        int taille = 0;
        if (this.isDirectory()) {
            Iterator<Noeud> liste = enfants.iterator();
            while (liste.hasNext()) {
                Noeud enfant = liste.next();
                taille += enfant.tailleDossier();
            }
        } else {
            taille = this.taille;
        }
        return taille;
    }

    String filename() {
        return this.nom + " (" + (isDirectory() ? "dir)" : "file, size=" + this.taille + ")");
    }

    void directory(int profondeur) {
        System.out.println(" ".repeat(profondeur) + "- " + this.filename());
        Iterator<Noeud> liste = enfants.iterator();
        while (liste.hasNext()) {
            Noeud enfant = liste.next();
            enfant.directory(profondeur + 1);
        }
    }

    int countSubDirectory() {
        int total = 0;
        Iterator<Noeud> liste = enfants.iterator();
        while (liste.hasNext()) {
            Noeud enfant = liste.next();
            if (enfant.isDirectory()) {
                total++;
                total += enfant.countSubDirectory();
            }
        }
        return total;
    }
}
