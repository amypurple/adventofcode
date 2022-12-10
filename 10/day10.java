import java.io.*;
import java.util.*;

class day10 {

    static CPU cpu = new CPU();

    public static void main(String[] args) {
        analyseur("input");
        System.out
                .println("La force total du signal est de " + cpu.forceSignal());
        cpu.display();
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                cpu.signal(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class CRT {
    List<String> image = new ArrayList<String>();
    String ligneCourante;
    int position;

    CRT() {
        ligneSuivante();
    }

    void ligneSuivante() {
        if (ligneCourante != null)
            image.add(new String(ligneCourante));
        ligneCourante = "";
        position = 0;
    }

    void o(int X) {
        ligneCourante = ligneCourante + ((position >= (X - 1) && position <= (X + 1)) ? "#" : ".");
        position++;
        if ((position % 40) == 0) {
            ligneSuivante();
        }
    }

    void display() {
        if (position > 0)
            ligneSuivante();
        Iterator<String> ligne = image.iterator();
        while (ligne.hasNext()) {
            System.out.println(ligne.next());
        }
    }
}

class CPU {
    int X;
    int cycle;
    CRT tv = new CRT();
    List<Integer> trace = new ArrayList<Integer>();

    CPU() {
        this.cycle = 0;
        this.X = 1;
    }

    void signal(String ligne) {
        String[] elems = ligne.trim().split(" ");
        if (elems[0].equals("noop"))
            noop();
        if (elems[0].equals("addx"))
            addxV(Integer.parseInt(elems[1]));
    }

    void tick() {
        this.cycle++;
        tv.o(this.X);
        if (((this.cycle + 20) % 40) == 0) {
            trace.add(this.cycle * this.X);
        }
    }

    void addxV(int V) {
        tick();
        tick();
        this.X += V;
    }

    void noop() {
        tick();
    }

    int forceSignal() {
        int somme = 0;
        Iterator<Integer> signal = trace.iterator();
        while (signal.hasNext()) {
            int valeur = signal.next();
            somme += valeur;
        }
        return somme;
    }

    void display() {
        tv.display();
    }
}