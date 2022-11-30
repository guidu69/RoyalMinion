import java.util.UUID;

public class Minion {

    UUID id;
    int taille;
    double force;

    enum Arme {Couteau, Fusil, Pistolet}

    int vieInit = (int) Math.floor(Math.random() * 500 + 1000);
    int vie;
    Arme arme;
    double puissance;
    boolean zombie = false;

    int nbCombats = 0;

    enum State {EnForme, Mort, Blesse, Fatigue, Zombie, NonGere}

    State state = State.EnForme;

    public boolean CheckDead() {
        if (this.vie <= 0) {
            this.state = State.Mort;
            this.vie = 0;
            return true;
        }
        // Seul moyen de devenir zombie au départ
        else if (this.vie <= 50) {
            this.zombie = true;
            this.state = State.Zombie;
            return false;
        }
        return false;
    }

    public static void TourParTour(Minion m1, Minion m2) {
        int nbT = 0;
        System.out.println("Vie Minion 1 : " + m1.vie + " Etat : " + m1.state + "   Vie minion 2 :" + m2.vie + " Etat : " + m2.state);
        System.out.println("Le minion " + m1.id + " attaque au  " + m1.arme + "  le minion " + m2.id);

        while (m1.state != State.Mort && m2.state != State.Mort && !(m1.state == State.Zombie && m2.state == State.Zombie)) {
            nbT += 1;
            m1.CombatAlea3(m2);
            System.out.println("Vie Minion 1 : " + m1.vie + " Etat : " + m1.state + "   Vie minion 2 :" + m2.vie + " Etat : " + m2.state);
        }

        System.out.println("Combat terminé en " + nbT + " tours.");
    }

    //Autre version du CombatAlea, un peu plus lisible mais peu claire dans la logique
    public void CombatAlea2(Minion ennemi) {

        //Verif des zombies
        if (this.zombie && ennemi.zombie) {
            System.out.println("Combat de zombies ! ");
            int rnd = (int) Math.floor(Math.random() * 2);
            if (rnd == 0) {
                this.state = State.Mort;
            } else ennemi.state = State.Mort;
            return;
        } else if (this.zombie || ennemi.zombie) {
            this.state = State.Zombie;
            this.zombie = true;
            ennemi.zombie = true;
            ennemi.state = State.Zombie;
        }

        //Verif de la possibilité du combat

        switch (this.state) {

            case Mort:
                System.out.println("Attaque de la part d'un zombie");
                break;
            case Blesse:
                if (ennemi.state == State.EnForme || ennemi.state == State.Fatigue && ennemi.puissance >= 2 * this.puissance) {
                    this.vie = 0;
                    this.state = State.Mort;
                } else if (ennemi.state == State.Blesse) {
                    if (this.puissance >= ennemi.puissance) {
                        ennemi.vie = 0;
                        ennemi.state = State.Mort;
                    } else {
                        this.vie = 0;
                        this.state = State.Mort;
                    }
                } else if (ennemi.state == State.Fatigue && ennemi.puissance >= 2 * this.puissance) {
                    this.vie = 0;
                    this.state = State.Mort;
                } else if (ennemi.state == State.Fatigue && ennemi.puissance >= this.puissance) {
                    this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                    this.CheckDead();
                } else if (ennemi.state == State.Fatigue && this.puissance >= ennemi.puissance) {
                    ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 5);
                    ennemi.CheckDead();
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
                            ennemi.vie = 0;
                            ennemi.state = State.Mort;
                        } else if (this.puissance >= ennemi.puissance) {
                            ennemi.state = State.Blesse;
                            ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 6);
                            ennemi.CheckDead();
                            this.state = State.Fatigue;
                        } else if (this.puissance <= ennemi.puissance / 2) {
                            this.vie = 0;
                            this.state = State.Mort;
                        } else {
                            this.state = State.Fatigue;
                            this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                            this.CheckDead();
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
                        if (this.puissance >= 2 * ennemi.puissance) {
                            ennemi.state = State.Mort;
                            ennemi.vie = 0;
                        } else if (this.puissance >= ennemi.puissance) {
                            ennemi.vie -= this.puissance * 8;
                            ennemi.CheckDead();
                        }
                        break;
                    case Fatigue:
                        // On applique un coeff d'augmentation de la puissance
                        double puissanceTMP = this.puissance * 1.25;
                        this.nbCombats += 1;
                        ennemi.nbCombats += 1;
                        if (puissanceTMP >= 2 * ennemi.puissance) {
                            ennemi.state = State.Mort;
                            ennemi.vie = 0;
                        } else if (puissanceTMP >= ennemi.puissance) {
                            ennemi.state = State.Blesse;
                            ennemi.vie = (int) Math.floor(ennemi.vie - puissanceTMP * 10);
                            ennemi.CheckDead();
                            this.state = State.Fatigue;
                        } else if (puissanceTMP <= ennemi.puissance / 2) {
                            this.vie -= ennemi.puissance * 8;
                            this.state = State.Blesse;
                            this.CheckDead();
                        } else {
                            this.state = State.Fatigue;
                            this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                            this.CheckDead();
                        }
                        break;
                    case EnForme:
                        //Cas le plus intéressant//
                        //Pas de coefficients particuliers appliqués, seulement un rapport des puissances
                        while (this.vie > 0 && ennemi.vie > 0) {
                            this.nbCombats += 1;
                            ennemi.nbCombats += 1;
                            if (this.puissance >= 3 * ennemi.puissance) {
                                ennemi.state = State.Mort;
                                ennemi.vie = 0;
                            } else if (this.puissance >= ennemi.puissance) {
                                ennemi.state = State.Blesse;
                                ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 10);
                                ennemi.CheckDead();
                                this.state = State.Fatigue;
                            } else if (this.puissance <= ennemi.puissance / 2) {
                                this.vie = 0;
                                this.state = State.Mort;
                            } else {
                                this.state = State.Fatigue;
                                ennemi.state = State.Fatigue;
                                this.vie = (int) Math.floor(this.vie - ennemi.puissance * 3);
                                ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance);
                                this.CheckDead();
                                ennemi.CheckDead();
                            }
                            break;

                        }

                }
        }
    }

    //Version actuelle
    public void CombatAlea3(Minion ennemi) {

        if (this.state == ennemi.state) {
            System.out.println(CombatRegular(ennemi));
        }
        if (this.state == State.Blesse || ennemi.state == State.Blesse) {
            System.out.println(CombatBlesse(ennemi));
        }
        if (this.state == State.Fatigue || ennemi.state == State.Fatigue) {
            System.out.println(CombatFatigue(ennemi));
        }
        if (this.state == State.Zombie || ennemi.state == State.Zombie) {
            System.out.println("TESTS");
            CombatZombie(ennemi);
        }

    }

    private void CombatZombie(Minion m) {
        if (this.state == State.Zombie) {
            m.state = State.Zombie;
            m.zombie = true;
            System.out.println("Infection d'un zombie");
        }
        if (m.state == State.Zombie) {
            this.zombie = true;
            this.state = State.Zombie;
            System.out.println("Infection par un zombie");

        }
    }

    private int CombatFatigue(Minion m) {
        if (this.state == State.Fatigue) {
            this.puissance *= 0.85;
        }
        if (m.state == State.Fatigue) {
            m.puissance *= 0.85;
        }
        return CombatRegular(m);
    }

    private int CombatBlesse(Minion m) {
        if (this.state == State.Blesse) {
            this.puissance *= 0.5;
        }
        if (m.state == State.Blesse) {
            m.puissance *= 0.5;
        }
        return CombatFatigue(m);
    }

    private int CombatRegular(Minion m) {
        this.nbCombats += 1;
        m.nbCombats += 1;
        int nbTours = 0;
        do {
            switch (this.UpdateState()) {
                case EnForme -> m.vie -= this.puissance * 3;
                case Fatigue -> m.vie -= this.puissance * 2;
                case Blesse -> m.vie -= this.puissance;
                case Mort -> {
                    break;
                }
                case Zombie -> System.out.println("Comment suis-je arrivé là");
                case NonGere -> System.out.println("Non géré pour l'instant");
            }
            switch (m.UpdateState()) {
                case Mort -> {
                    break;
                }
                case Blesse -> this.vie -= m.puissance;
                case Fatigue -> this.vie -= m.puissance * 2;
                case EnForme -> this.vie -= m.puissance * 3;
                case Zombie -> System.out.println("Comment suis-je arrivé là");
                case NonGere -> System.out.println("Non géré pour l'instant");
            }
            nbTours += 1;
        } while (this.CheckDead() == false && m.CheckDead() == false);
        System.out.println("Un minion est mort");
        return nbTours;
    }


    public State UpdateState() {

        switch ((0.1 * this.vieInit <= this.vie && this.vie < 0.8 * this.vieInit) ? 0 : (0.8 * this.vieInit <= this.vie && this.vie < this.vieInit) ? 1 : 2) {
            case 2:
                return State.EnForme;
            case 1:
                return State.Fatigue;
            case 0:
                return State.Blesse;
            default:
                System.out.println("Non géré");
                return State.NonGere;
        }
    }

// Ancienne version de la méthode CombatALea, peu lisible

    public void CombatAlea(Minion ennemi) {
        //Verif de la possibilité du combat
        if (this.state == State.Blesse && ennemi.state != State.Mort) {
            this.state = State.Mort;
        } else if (this.state == State.Fatigue && ennemi.state != State.Mort) {
            double n = Math.random();
            if (n < 0.50) {
                this.state = State.Blesse;
            } else {
                this.nbCombats += 1;
                ennemi.nbCombats += 1;

                if (this.puissance >= 2 * ennemi.puissance) {
                    ennemi.state = State.Mort;
                } else if (this.puissance >= ennemi.puissance) {
                    ennemi.state = State.Blesse;
                    ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 10);
                    ennemi.CheckDead();
                    this.state = State.Fatigue;
                } else if (this.puissance <= ennemi.puissance / 2) {
                    this.state = State.Mort;
                } else {
                    this.state = State.Fatigue;
                    this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                    this.CheckDead();
                }
            }
        } else if (this.state == State.Mort) {

        } else {
            this.nbCombats += 1;
            ennemi.nbCombats += 1;

            if (this.puissance >= 2 * ennemi.puissance) {
                ennemi.state = State.Mort;
            } else if (this.puissance >= ennemi.puissance) {
                ennemi.state = State.Blesse;
                ennemi.vie = (int) Math.floor(ennemi.vie - this.puissance * 10);
                ennemi.CheckDead();
                this.state = State.Fatigue;
            } else if (this.puissance <= ennemi.puissance / 2) {
                this.state = State.Mort;
            } else {
                this.state = State.Fatigue;
                this.vie = (int) Math.floor(this.vie - ennemi.puissance * 5);
                this.CheckDead();
            }
        }

    }

    public static String printState(State state) {
        if (state == State.EnForme) {
            return "En Forme";
        } else if (state == State.Blesse) {
            return "Blessé";
        } else if (state == State.Fatigue) {
            return "Fatigué";
        } else if (state == State.Zombie) {
            return "Zombie";
        } else return "Mort";
    }

    public static String printArme(Arme arme) {
        if (arme == Arme.Pistolet) {
            return "Pistolet";
        } else if (arme == Arme.Couteau) {
            return "Couteau";
        } else if (arme == Arme.Fusil) {
            return "Fusil";
        } else return "Couteau";
    }

//Non utilisée dans la version actuelle du programme. Pourra être utile lorsqu'on disposera d'une interface graphique.

    public void Agression(Minion cible) {
        if (this.puissance >= 1.5 * cible.puissance) {
            cible.state = State.Mort;
        } else if (this.puissance >= cible.puissance) {
            cible.state = State.Blesse;
            cible.vie -= this.puissance * 15;
        }
    }

    public void CalcPuissance() {
        double p = this.force;
        if (this.taille > 190) {
            p *= 1.1;
        }
        if (this.taille <= 160) {
            p *= 0.80;
        }
        if (this.arme == Arme.Pistolet) {
            p *= 3;
        }
        if (this.arme == Arme.Fusil) {
            p *= 5;
        }
        this.puissance = p;
    }

    public Minion() {
        this.id = UUID.randomUUID();
        this.vie = vieInit;
    }

    public Minion(int taille, double force, Arme arme) {
        this.id = UUID.randomUUID();
        this.taille = taille;
        this.force = force;
        this.arme = arme;
        this.state = State.EnForme;
        this.vie = vieInit;
        this.CalcPuissance();
    }


}
