import java.util.ArrayList;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int nbMinions = 100;
        System.out.println("Hello world!");
        System.out.println("Nombre total de minions : " + nbMinions);

        ArrayList<Minion> tabP = new ArrayList<>(0);

        // Vie initiale des minions (à changer dans la classe Minion) : entre 1000 et 1500 aléatoirement

        for (int i = 0; i < nbMinions; i++) {
            Minion.Arme[] armeAlea = {Minion.Arme.Couteau, Minion.Arme.Pistolet, Minion.Arme.Fusil};
            Minion m = new Minion();
            m.taille = (int) Math.floor(Math.random() * 50 + 150);
            m.force = Math.floor(Math.random() * 50);
            m.arme = armeAlea[(int) Math.floor(Math.random() * armeAlea.length)];
            m.state = Minion.State.EnForme;
            m.CalcPuissance();
            tabP.add(m);
        }

        int pTot = 0;
        for (int i = 0; i < nbMinions; i++) {
            pTot += tabP.get(i).puissance;
        }
        double pMoy = pTot / nbMinions;

        ArrayList<Minion.State> tabState = new ArrayList<>((int) nbMinions);
        ArrayList<Minion> cimetiere = new ArrayList<>((int) nbMinions);
      //  ArrayList<Minion.Arme> tabArme = new ArrayList<>(0);

        /*for (int i = 0; i < nbMinions; i++) {
            tabArme.add(tabP.get(i).arme);
        }*/

        for (int i = 0; i < nbMinions; i++) {
            tabState.add(tabP.get(i).state);
        }

        int nbComb = 0;
        double countBlesse = 0;
        double countMort = 0;
        double countFatigue = 0;
        double countZombie = 0;
        int ExitZombie = 0;

        while (countMort != (nbMinions - 2)) {
                int r =0;
                int r2 =0;
            //Tirage au sort de deux entiers entre 0 et nbMinions
                 do {
                r = (int) Math.floor(Math.random() * tabP.size());
                r2 = (int) Math.floor(Math.random() * tabP.size());
            }
            while (r == r2);

            // On les fait combattre en premier lieu.
            tabP.get(r).CombatAlea3(tabP.get(r2));
            nbComb += 1;
            //On actualise les états des minions.

            tabState.set(r, tabP.get(r).state);
            tabState.set(r2, tabP.get(r2).state);

            //Verif des états des minions / Envoi au cimetière si nécessaire :

            if(tabState.get(r) == Minion.State.Mort){
                cimetiere.add(tabP.get(r));
                tabP.remove(r);
                tabState.remove(r);
              }

            if(tabState.get(r2) == Minion.State.Mort){
                cimetiere.add(tabP.get(r2));
                tabP.remove(r2);
                tabState.remove(r2);
            }

        }

        if(ExitZombie != 1){

            Minion.TourParTour(tabP.get(0), tabP.get(1));

            tabState.set(0, tabP.get(0).state);
            tabState.set(1, tabP.get(1).state);

            if (tabState.get(0) == Minion.State.Mort) {
                countMort += 1;
                cimetiere.add(tabP.get(0));
                tabP.remove(0);
                tabState.remove(0);

            }else{
                countMort += 1;
                cimetiere.add(tabP.get(1));
                tabP.remove(1);
                tabState.remove(1);
            }

        }

        int cb = 0;
        for (int i = 0; i < cimetiere.size(); i++) {
            cb += cimetiere.get(i).nbCombats;
        }


        System.out.println("fatigués : " + ((countFatigue / nbMinions) * 100) + "%");
        System.out.println("blessés : " + (countBlesse / nbMinions * 100) + "%");
        System.out.println("morts : " + ((countMort / nbMinions) * 100) + "%");
        System.out.println("en forme : " + ((nbMinions - countFatigue - countBlesse - countMort) / nbMinions * 100) + "%");
        System.out.println("Le minion qui a remporté la battle royale possède les statistiques suivantes : ");
        System.out.println("   ID : " + tabP.get(0).id);
        System.out.println("   Puissance : " + tabP.get(0).puissance);
        System.out.println("   Arme : " + tabP.get(0).printArme(tabP.get(0).arme));
        System.out.println("   Vie Finale : " + tabP.get(0).vie + "/ " + tabP.get(0).vieInit);
        System.out.println("   Puissance : " + tabP.get(0).puissance);
        System.out.println("   Nombre de combats : " + tabP.get(0).nbCombats);
        System.out.println("    Zombie : " + (tabP.get(0).zombie ? "OUI" : "NON"));
        System.out.println("Stats générales : ");
        System.out.println("Nombre de combats total : " + nbComb);
        System.out.println("Nombre de combats moyen : " + cb / cimetiere.size());
        System.out.println("Puissance moyenne : " + pMoy);
        System.out.println("Nombre de zombies : " + countZombie);


    }
}