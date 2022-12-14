import java.io.*;
import java.util.*;

class day13 {

    static Paires paires;
    static boolean verbose = false;

    static String signalDetresseDebut = "[[2]]";
    static String signalDetresseFin = "[[6]]";

    public static void main(String[] args) {
        paires = new Paires(verbose);
        analyseur("input");
        paires.comparerPaires();
        paires.signal(signalDetresseDebut);
        paires.signal(signalDetresseFin);
        paires.trier();
        paires.cleDecodage(signalDetresseDebut, signalDetresseFin);
    }

    public static void analyseur(String nomDeFichier) {
        String ligne = null;
        try {
            FileReader fichier = new FileReader(nomDeFichier);
            BufferedReader tamponLecture = new BufferedReader(fichier);
            while ((ligne = tamponLecture.readLine()) != null) {
                paires.signal(ligne.trim());
            }
            tamponLecture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ListeConteneur implements Comparable<ListeConteneur> {
    List<ListeElement> liste = new ArrayList<ListeElement>();

    void ajouter(ListeElement e) {
        liste.add(e);
    }

    void ajouter(int valeur) {
        liste.add(new ListeElement(valeur));
    }

    void ajouterListe(ListeConteneur liste) {
        this.liste.add(new ListeElement(liste));
    }

    ListeConteneur ajouterListe() {
        ListeConteneur nouvelleListe = new ListeConteneur();
        liste.add(new ListeElement(nouvelleListe));
        return nouvelleListe;
    }

    @Override
    public int compareTo(ListeConteneur o) {
        if (liste.size() == 0) {
            if (o.liste.size() == 0) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (o.liste.size() == 0) {
                return 1;
            } else {
                int result = 0;
                for (int i = 0; i < liste.size(); i++) {
                    if (i < o.liste.size()) {
                        result = liste.get(i).compareTo(o.liste.get(i));
                        if (result != 0)
                            return result;
                    } else {
                        return 1;
                    }
                }
                if (liste.size() < o.liste.size())
                    return -1;
            }
        }
        return 0;
    }
}

class ListeElement implements Comparable<ListeElement> {
    int valeur;
    ListeConteneur liste;

    ListeElement(int valeur) {
        this.valeur = valeur;
    }

    ListeElement(ListeConteneur liste) {
        this.liste = liste;
    }

    @Override
    public int compareTo(ListeElement o) {
        if (this.liste == null) {
            if (o.liste == null) {
                // comparer 2 valeurs
                return (this.valeur < o.valeur) ? -1 : (this.valeur == o.valeur) ? 0 : 1;
            } else {
                // comparer valeur avec liste
                ListeConteneur temp = new ListeConteneur();
                temp.ajouter(this.valeur);
                return temp.compareTo(o.liste);
            }
        } else {
            if (o.liste == null) {
                // comparer liste avec valeur
                ListeConteneur temp = new ListeConteneur();
                temp.ajouter(o.valeur);
                return this.liste.compareTo(temp);
            } else {
                return this.liste.compareTo(o.liste);
            }
        }
    }
}

class ListeSpeciale implements Comparable<ListeSpeciale> {
    String signal;
    ListeConteneur liste;
    boolean verbose;

    ListeSpeciale(String signal, boolean verbose) {
        this.signal = signal.trim();
        this.verbose = verbose;
        this.liste = construit(signal);
    }

    private ListeConteneur construit(String s) {
        if (verbose)
            System.out.print("construit(" + s + ") -> ");
        // retirer [ ]
        s = s.substring(1, s.length() - 1);
        if (verbose)
            System.out.println(s);
        // créer un conteneur accumulateur
        ListeConteneur accumulateur = new ListeConteneur();
        // décoder la liste d'éléments
        int indexDebut = 0;
        int indexFin = 0;
        while (indexDebut < s.length()) {
            if (s.startsWith("[", indexDebut)) {
                int compteur = 1;
                indexFin = indexDebut + 1;
                while (compteur > 0) {
                    if (s.charAt(indexFin) == '[')
                        compteur++;
                    if (s.charAt(indexFin) == ']')
                        compteur--;
                    indexFin++;
                }
                String subString = s.substring(indexDebut, indexFin);
                if (verbose)
                    System.out.println(
                            "LISTE : substring de " + indexDebut + " a " + indexFin + " = '" + subString + "'");
                accumulateur.ajouterListe(construit(subString));
            } else {
                indexFin = s.indexOf(",", indexDebut);
                if (indexFin < 0)
                    indexFin = s.length();
                String subString = s.substring(indexDebut, indexFin);
                if (verbose)
                    System.out.println(
                            "VALEUR : substring de " + indexDebut + " a " + indexFin + " = '" + subString + "'");
                accumulateur.ajouter(Integer.parseInt(subString));
            }
            indexDebut = indexFin + 1; // passer la virgule
        }
        return accumulateur;
    }

    @Override
    public int compareTo(ListeSpeciale o) {
        if (this.liste.liste == null) {
            if (o.liste.liste == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (o.liste.liste == null) {
                return -1;
            } else {
                return liste.compareTo(o.liste);
            }
        }
    }
}

class Paires {
    List<ListeSpeciale> listes = new ArrayList<ListeSpeciale>();
    boolean verbose;

    Paires(boolean verbose) {
        this.verbose = verbose;
    }

    void signal(String signal) {
        signal = signal.trim();
        if (signal.length() > 0) {
            if (verbose)
                System.out.println("Paires.signal(" + signal + ")");
            listes.add(new ListeSpeciale(signal, verbose));
        }
    }

    public void comparerPaires() {
        Iterator<ListeSpeciale> liste = listes.iterator();
        int index = 0;
        int somme = 0;
        while (liste.hasNext()) {
            index++;
            ListeSpeciale A = liste.next();
            ListeSpeciale B = liste.next();
            if (verbose)
                System.out.println("Comparer " + A.signal + " et " + B.signal);
            int resultat = A.compareTo(B);
            if (resultat < 0) {
                if (verbose)
                    System.out.println("Bon ordre");
                somme += index;
            }
            if (resultat > 0) {
                if (verbose)
                    System.out.println("Pas dans le bon ordre");
            }
        }
        System.out.println("La somme des indices des paires dans le bon ordre est : " + somme);
    }

    public void trier() {
        Collections.sort(listes);
        if (verbose)
            listes.stream().map(s -> s.signal).forEach(System.out::println);
    }

    public void cleDecodage(String s1, String s2) {
        int indexA = 0;
        int indexB = 0;
        int index = 0;
        while (indexA == 0 || indexB == 0) {
            String signal = listes.get(index++).signal;
            if (signal.equals(s1))
                indexA = index;
            if (signal.equals(s2))
                indexB = index;
        }
        System.out.println("La cle de decodage est (" + indexA + "," + indexB + ") = " + (indexA * indexB));
    }
}