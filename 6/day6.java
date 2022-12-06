import java.io.*;
import java.util.*;

class day6 {

    static Recepteur communication = new Recepteur();

    public static void main(String[] args) {
        analyseur("input");
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                communication.signal(ligne);
                System.out.println("first start marker : " + communication.firstMarker(4));
                System.out.println("first message marker : " + communication.firstMarker(14));
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
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

class Recepteur {
    String ligne;
    int startOfPacket;

    Recepteur() {
    }

    void signal(String ligne) {
        if (ligne.length() >= 4)
            this.ligne = ligne;
    }

    int firstMarker(int size) {
        int max = this.ligne.length();
        int i = size;
        while (i <= max) {
            String packet = this.ligne.substring(i - size, i);
            if (isAllDifferent(packet)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    boolean isAllDifferent(String s) {
        for (int i = 1; i < s.length(); i++) {
            char c1 = s.charAt(i);
            for (int j = 0; j < i; j++) {
                char c2 = s.charAt(j);
                if (c1 == c2)
                    return false;
            }
        }
        return true;
    }

}