import java.io.*;

class day2 {

    static Strategie strategie = new Strategie();

    public static void main(String[] args) {
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

    public Strategie() {
        pointage = 0;
    }

    public void jouer(String ligne) {
        int elfe;
        int moi;
        int resultat;
        String[] choix = ligne.split(" ");
        elfe = choix[0].trim().charAt(0) - 'A' + 1;
        moi = choix[1].trim().charAt(0) - 'X' + 1;
        resultat = Math.abs((4 + moi - elfe) % 3);
        System.out.println(
                "L'elfe fait " + strChoix[elfe] + "(" + elfe + ") et moi je fais " + strChoix[moi] + "(" + moi + ") = "
                        + strResultat[resultat]);
        pointage += moi + 3 * resultat;
    }

    @Override
    public String toString() {
        return "Pointage de la strategie : " + pointage;
    }
}
