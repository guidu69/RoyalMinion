import java.util.EnumSet;
import java.util.UUID;

public class Minion {

    UUID id;
    int taille;
    double force;
    enum Arme {Couteau, Fusil, Pistolet};
    int vieInit = (int) Math.floor(Math.random()*500+1000);
    int vie;
    Arme arme;
    double puissance;

    int nbCombats = 0;

    enum State {EnForme, Mort, Blesse, Fatigue}

    State state = State.EnForme;

public void CheckVie(){
    if(this.vie <= 0){
        this.state = State.Mort;
    }
}

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
                    ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance*5);
                    ennemi.CheckVie();
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
                ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance*5);
                ennemi.CheckVie();
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
        CalcPuissance();
    }


}
