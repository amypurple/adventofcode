import java.io.*;

class day2 {

    static Strategie strategie;

    public static void main(String[] args) {
        strategie = new Strategie(false);
        analyseur("input");
        System.out.println(strategie);
        strategie = new Strategie(true);
        analyseur("input");
        System.out.println(strategie);
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                strategie.jouer(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Strategie {

    protected int pointage;
    protected boolean flag;

    String[] strChoix = {
            "INVALID",
            "ROCHE",
            "PAPIER",
            "CISEAUX"
    };

    String[] strResultat = {
            "PERDU",
            "NUL",
            "GAGNE",
    };

    public Strategie(boolean flag) {
        pointage = 0;
        this.flag = flag;
    }

    public void jouer(String ligne) {
        int elfe = 0;
        int moi = 0;
        int resultat = 0;
        String[] choix = ligne.split(" ");
        elfe = choix[0].trim().charAt(0) - 'A' + 1;
        if (flag) {
            resultat = choix[1].trim().charAt(0) - 'X';
            moi = (1 + resultat + elfe) % 3 + 1;
        } else {
            moi = choix[1].trim().charAt(0) - 'X' + 1;
            resultat = (4 + moi - elfe) % 3;
        }
        /*
         * System.out.println(
         * "L'elfe fait " + strChoix[elfe] + "(" + elfe + ") et moi je fais " +
         * strChoix[moi] + "(" + moi + ") = "
         * + strResultat[resultat]);
         */
        pointage += moi + 3 * resultat;
    }

    @Override
    public String toString() {
        return "Pointage de la strategie : " + pointage;
    }
}
