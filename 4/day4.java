import java.io.*;
import java.util.*;

class day4 {

    public static List<Assignements> elfes = new ArrayList<Assignements>();
    static int nombreDeGroupes;

    public static void main(String[] args) {
        analyseur("input");
        System.out.println("Il y a " + compterPairesContenues() + " paires qui se contiennent.");
        System.out.println("Il y a " + compterPairesRecoupees() + " paires qui se recoupent.");
    }

    public static int compterPairesContenues() {
        int total = 0;
        for (int i = 0; i < nombreDeGroupes; i++) {
            total += elfes.get(i).contientOuContenu ? 1 : 0;
        }
        return total;
    }

    public static int compterPairesRecoupees() {
        int total = 0;
        for (int i = 0; i < nombreDeGroupes; i++) {
            total += elfes.get(i).overlap ? 1 : 0;
        }
        return total;
    }    

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                elfes.add(new Assignements(ligne));
                nombreDeGroupes++;
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Assignements {
    protected String ligne;
    protected String[] paires;
    protected Sections section1;
    protected Sections section2;
    public boolean contientOuContenu;
    public boolean overlap;

    public Assignements(String ligne) {
        this.ligne = ligne.trim();
        setPaires();
    }

    private void setPaires() {
        this.paires = this.ligne.split(",");
        this.section1 = new Sections(paires[0]);
        this.section2 = new Sections(paires[1]);
        contientOuContenu = section1.contienOuEstContenu(section2);
        if (contientOuContenu)
            System.out.println("Paires : " + this.paires[0] + " et " + this.paires[1] + " se contiennent.");
        overlap = section1.overlap(section2);
        if (overlap)
            System.out.println("Paires : " + this.paires[0] + " et " + this.paires[1] + " se recoupent.");
    }
}

class Sections {
    public int min;
    public int max;

    public Sections(String ligne) {
        String[] temp = ligne.split("-");
        this.min = Integer.parseInt(temp[0]);
        this.max = Integer.parseInt(temp[1]);
    }

    public boolean contient(Sections o) {
        return this.min <= o.min && this.max >= o.max;
    }

    public boolean estContenu(Sections o) {
        return this.min >= o.min && this.max <= o.max;
    }

    public boolean contienOuEstContenu(Sections o) {
        return this.contient(o) || this.estContenu(o);
    }

    public boolean overlap(Sections o) {
        return !(this.min > o.max || this.max < o.min);
    }

}