import java.util.EnumSet;
import java.util.UUID;

public class Minion {

    UUID id;
    int taille;
    double force;
    enum Arme {Couteau, Fusil, Pistolet}
    int vieInit = (int) Math.floor(Math.random()*500+1000);
    int vie;
    Arme arme;
    double puissance;
    boolean zombie = false;

    int nbCombats = 0;

    enum State {EnForme, Mort, Blesse, Fatigue, Zombie}

    State state = State.EnForme;

    public void CheckVie(){
        if(this.vie <= 0){
            this.state = State.Mort;
            this.vie =0;
        }
        // Seul moyen de devenir zombie au départ
        else if(this.vie <=10 ){
            this.zombie = true;
            this.state = State.Zombie;
        }
    }

    public static void TourParTour(Minion m1, Minion m2) {
    int nbT = 0;
        System.out.println("Vie Minion 1 : " + m1.vie + " Etat : " + m1.state + "   Vie minion 2 :" +  m2.vie + " Etat : " + m2.state);
        System.out.println("Le minion " + m1.id + " attaque au  " + m1.arme + "  le minion " + m2.id);

            while(m1.state != State.Mort && m2.state != State.Mort && !(m1.state == State.Zombie && m2.state == State.Zombie)){
                nbT +=1;
                m1.CombatAlea2(m2);
                System.out.println("Vie Minion 1 : " + m1.vie + " Etat : " + m1.state + "   Vie minion 2 :" +  m2.vie + " Etat : " + m2.state);
            }

            System.out.println("Combat terminé en " + nbT + " tours.");
        }

    public void CombatAlea2(Minion ennemi) {

    //Verif des zombies

        if(this.zombie){
            this.state = State.Zombie;
            ennemi.zombie = true;
            ennemi.state = State.Zombie;
        }else if(ennemi.zombie){
            this.zombie =true;
            this.state = State.Zombie;
            ennemi.state = State.Zombie;
        }else if(this.zombie && ennemi.zombie){
            System.out.println("Combat de zombies ! ");
            int rnd = (int) Math.floor(Math.random()*2);
            if(rnd == 0 ){
                this.state = State.Mort;
            }else ennemi.state = State.Mort ;
            return;
        }else

    //Verif de la possibilité du combat

    switch (this.state) {

        case Mort:
            System.out.println("Attaque de la part d'un zombie");
            break;
        case Blesse:
            if (ennemi.state == State.EnForme || ennemi.state == State.Fatigue && ennemi.puissance >= 2*this.puissance) {
                this.vie = 0;
                this.state = State.Mort;
            }
            else if(ennemi.state == State.Blesse){
                if(this.puissance >= ennemi.puissance){
                    ennemi.vie = 0;
                    ennemi.state = State.Mort;
                }else{
                    this.vie = 0;
                    this.state = State.Mort;
                }
            } else if (ennemi.state == State.Fatigue && ennemi.puissance >= 2*this.puissance) {
                this.vie = 0;
                this.state = State.Mort;
            } else if (ennemi.state == State.Fatigue && ennemi.puissance >= this.puissance) {
                this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                this.CheckVie();
            } else if (ennemi.state == State.Fatigue && this.puissance >= ennemi.puissance) {
                ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 5);
                ennemi.CheckVie();
            } else {
                ennemi.vie = 0;
                ennemi.state = State.Mort;
            }
            break;
        case Fatigue:
            if (ennemi.state != State.Mort) {
                double n = Math.random();
                if (n < 0.50) {
                    this.state = State.Blesse;
                } else {
                    this.nbCombats += 1;
                    ennemi.nbCombats += 1;

                    if (this.puissance >= 2 * ennemi.puissance) {
                        ennemi.vie =0;
                        ennemi.state = State.Mort;
                    } else if (this.puissance >= ennemi.puissance) {
                        ennemi.state = State.Blesse;
                        ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 6);
                        ennemi.CheckVie();
                        this.state = State.Fatigue;
                    } else if (this.puissance <= ennemi.puissance / 2) {
                        this.vie =0;
                        this.state = State.Mort;
                    } else {
                        this.state = State.Fatigue;
                        this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                        this.CheckVie();
                    }
                }
            }
            break;
        case EnForme:
            switch (ennemi.state) {
                case Mort:
                    System.out.println("Attaque sur un zombie");
                    break;
                case Blesse:
                    if(this.puissance >= 2* ennemi.puissance ){
                        ennemi.state = State.Mort;
                        ennemi.vie = 0;
                    }else if(this.puissance >= ennemi.puissance){
                        ennemi.vie -= this.puissance  *8;
                        ennemi.CheckVie();
                    }
                    break;
                case Fatigue:
                    // On applique un coeff d'augmentation de la puissance
                    double puissanceTMP = this.puissance * 1.25;
                    this.nbCombats += 1;
                    ennemi.nbCombats += 1;
                    if (puissanceTMP >= 2 * ennemi.puissance) {
                        ennemi.state = State.Mort;
                        ennemi.vie =0;
                    } else if (puissanceTMP >= ennemi.puissance) {
                        ennemi.state = State.Blesse;
                        ennemi.vie = (int) Math.floor(ennemi.vie - puissanceTMP * 10);
                        ennemi.CheckVie();
                        this.state = State.Fatigue;
                    } else if (puissanceTMP <= ennemi.puissance / 2) {
                        this.vie -= ennemi.puissance *8;
                        this.state = State.Blesse;
                        this.CheckVie();
                    } else {
                        this.state = State.Fatigue;
                        this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                        this.CheckVie();
                    }
                    break;
                case EnForme:
                    //Cas le plus intéressant//
                    //Pas de coefficients particuliers appliqués, seulement un rapport des puissances
                    this.nbCombats += 1;
                    ennemi.nbCombats += 1;
                      if (this.puissance >= 3 * ennemi.puissance) {
                        ennemi.state = State.Mort;
                        ennemi.vie =0;
                    } else if (this.puissance >= ennemi.puissance) {
                        ennemi.state = State.Blesse;
                        ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 10);
                        ennemi.CheckVie();
                        this.state = State.Fatigue;
                    } else if (this.puissance <= ennemi.puissance / 2) {
                        this.vie = 0;
                        this.state = State.Mort;
                    }

                    else {
                        this.state = State.Fatigue;
                        ennemi.state = State.Fatigue;
                        this.vie = (int) Math.floor(this.vie - ennemi.puissance * 3);
                        ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance);
                        this.CheckVie();
                        ennemi.CheckVie();
                    }
                    break;

            }
        }
    }


// Ancienne version de la méthode CombatALea, peu lisible

    public void CombatAlea(Minion ennemi){
        //Verif de la possibilité du combat
        if(this.state == State.Blesse && ennemi.state != State.Mort){
            this.state = State.Mort;
        }
        else if(this.state == State.Fatigue && ennemi.state != State.Mort){
            double n = Math.random();
            if(n<0.50){
                this.state = State.Blesse;
            }
            else {
                this.nbCombats +=1;
                ennemi.nbCombats +=1;

                if(this.puissance >= 2*ennemi.puissance){
                    ennemi.state = State.Mort;
                }
                else if(this.puissance >= ennemi.puissance){
                    ennemi.state = State.Blesse;
                    ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance*10);
                    ennemi.CheckVie();
                    this.state = State.Fatigue;
                }
                else if(this.puissance <= ennemi.puissance/2){
                    this.state = State.Mort;
                }
                else {
                    this.state = State.Fatigue;
                    this.vie = (int) Math.floor(this.vie - ennemi.puissance*5);
                    this.CheckVie();
                }
            }
        }
        else if(this.state == State.Mort){

        }
        else {
            this.nbCombats +=1;
            ennemi.nbCombats +=1;

            if(this.puissance >= 2*ennemi.puissance){
                ennemi.state = State.Mort;
            }
            else if(this.puissance >= ennemi.puissance){
                ennemi.state = State.Blesse;
                ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance*10);
                ennemi.CheckVie();
                this.state = State.Fatigue;
            }
            else if(this.puissance <= ennemi.puissance/2){
                this.state = State.Mort;
            }
            else {
                this.state = State.Fatigue;
                this.vie = (int) Math.floor(this.vie - ennemi.puissance*5);
                this.CheckVie();
            }
        }

    }

    public static String printState(State state){
        if(state == State.EnForme){
            return "En Forme";
        }else if(state == State.Blesse){
            return "Blessé";
        }else if(state == State.Fatigue){
        return "Fatigué";}
        else if(state == State.Zombie){
            return "Zombie";}
        else return "Mort";
            }

    public static String printArme(Arme arme){
        if(arme == Arme.Pistolet){
            return "Pistolet";
        }else if(arme == Arme.Couteau){
            return "Couteau";
        }else if(arme == Arme.Fusil){
            return "Fusil";
        }
        else return "Couteau";
    }

    public void Agression(Minion cible){
        if(this.puissance >= 1.5*cible.puissance){
        cible.state = State.Mort;}
        else if(this.puissance >= cible.puissance){
            cible.state = State.Blesse;
            cible.vie -= this.puissance *15;
        }
    }
    public void CalcPuissance(){
        double p = this.force;
        if(this.taille > 190){
            p*=1.1;
        }
        if(this.taille <=160){
            p*=0.80;
        }
        if(this.arme == Arme.Pistolet){
            p*= 3;
        }
        if(this.arme == Arme.Fusil){
            p*= 5;
        }
        this.puissance = p;
    }

    public Minion(){
        this.id = UUID.randomUUID();
        this.vie = vieInit;
    }

    public Minion (int taille, double force, Arme arme){
        this.id = UUID.randomUUID();
        this.taille = taille;
        this.force = force;
        this.arme = arme;
        this.state = State.EnForme;
        this.vie = vieInit;
        this.CalcPuissance();
    }


}
