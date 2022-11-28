import java.util.ArrayList;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        double nbMinions = 100;
        System.out.println("Hello world!");
        ArrayList<Minion> tabP = new ArrayList<>(0);

        // Vie initiale des minions (à changer dans la classe Minion) : entre 1000 et 1500 aléatoirement

        for (int i = 0; i < nbMinions; i++) {
            Minion.Arme[] armeAlea = {Minion.Arme.Couteau, Minion.Arme.Pistolet, Minion.Arme.Fusil};
            Minion m = new Minion();
            m.taille = (int) Math.floor(Math.random() * 50 + 150);
            m.force = Math.random() * 50;
            m.force = Math.floor(Math.random() * 50);
            m.arme = armeAlea[(int) Math.floor(Math.random() * armeAlea.length)];
            m.state = Minion.State.EnForme;
            m.CalcPuissance();
            tabP.add(m);
        }

        int pTot =0;
        for(int i=0;i<nbMinions;i++){
            pTot+= tabP.get(i).puissance;
        }
        double pMoy = pTot / nbMinions;

        ArrayList<Minion.State> tabState = new ArrayList<>((int)nbMinions);
        ArrayList<Minion> cimetiere = new ArrayList<>((int)nbMinions);
        ArrayList<Minion.Arme> tabArme = new ArrayList<>(0);

        for (int i = 0; i < nbMinions; i++) {
            tabArme.add(tabP.get(i).arme);
            //System.out.println(Minion.printArme(tabArme[i]));
        }

        for(int i= 0;i<nbMinions;i++){
            tabState.add(tabP.get(i).state);
        }
        for(int i= 0;i<nbMinions;i++){
            tabArme.add(tabP.get(i).arme);
        }

        int nbComb = 0;
        double countBlesse = 0;
        double countMort = 0;
        double countFatigue = 0;

        while (countMort != (nbMinions - 1)) {

            // On les fait combattre en premier lieu.
                int r = (int) Math.floor(Math.random() * tabP.size());
                int r2 = (int) Math.floor(Math.random() * tabP.size());
                if (r == r2) {
                    r2 = (int) Math.floor(Math.random() * tabP.size());
                }
                tabP.get(r).CombatAlea(tabP.get(r2));
                nbComb +=1;
                //On actualise les états des minions.

                tabState.set(r, tabP.get(r).state);

                if (tabState.get(r) == Minion.State.Blesse) {
                    countBlesse += 1;
                }
                else if (tabState.get(r) == Minion.State.Fatigue) {
                countFatigue += 1; }

                else if (tabState.get(r) == Minion.State.Mort) {
                    countMort += 1;
                    cimetiere.add(tabP.get(r));
                    tabP.remove(r);
                    tabState.remove(r);

                }
                else {}

        }


            System.out.println("Nombre total de minions : " + nbMinions);
            /* System.out.println(countFatigue);
            System.out.println(countBlesse);
            System.out.println(countMort);
            System.out.println(nbMinions - countFatigue - countBlesse - countMort); */

            System.out.println("fatigués : " + ((countFatigue / nbMinions) * 100) + "%");
            System.out.println("blessés : " +  (countBlesse / nbMinions * 100) +"%");
            System.out.println("morts : " +  ((countMort / nbMinions) * 100) + "%");
            System.out.println("en forme : " +  ((nbMinions - countFatigue - countBlesse - countMort) / nbMinions * 100) + "%");
            System.out.println("Le minion qui a remporté la battle royale possède les statistiques suivantes : ");
            System.out.println ("   Puissance : " + tabP.get(0).puissance);
            System.out.println ("   Arme : " + tabP.get(0).printArme(tabP.get(0).arme));
            System.out.println ("   Vie Finale : " + tabP.get(0).vie +"/ " +tabP.get(0).vieInit );
            System.out.println ("   Puissance : " + tabP.get(0).puissance);
            System.out.println ("   Nombre de combats : " + tabP.get(0).nbCombats);
             System.out.println ("Stats générales : ");
        int cb=0;
        for (int i=0;i< cimetiere.size();i++){
            cb += cimetiere.get(i).nbCombats;
        }
        System.out.println ("Nombre de combats total : " + nbComb);
        System.out.println ("Nombre de combats moyen : " + cb / cimetiere.size());
        System.out.println ("Puissance moyenne : " + pMoy);



    }
    }