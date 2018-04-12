package com.company;

import IA.Desastres.*;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

public class Operators  implements SuccessorFunction {

    /**parametre state ha de ser de tipus Object*/
    public List getSuccessors (Object S) {
        State state = (State) S;
        ArrayList<State> retval = new ArrayList<>();

        /**Possibles successors fent swaps de grups*/
        for (int i1 = 0; i1 < state.managedCentres.size(); i1++) { /**recorrem tots els centres*/
            for (int j1 = 0; j1 < state.managedCentres.get(i1).size(); j1++) { /**recorrem tots els vols de cada centre*/
                for (int k1 = 0; k1 < state.managedCentres.get(i1).get(j1).toRescue.size(); k1++) { /**recorrem l'Array toRescue de cada vol (Path)*/

                    for (int i2 = 0; i2 < state.managedCentres.size(); i2++) { /**recorrem tots els centres*/
                        for (int j2 = 0; j2 < state.managedCentres.get(i2).size(); j2++) { /**recorrem tots els vols de cada centre*/
                            for (int k2 = 0; k2 < state.managedCentres.get(i2).get(j2).toRescue.size(); k2++) { /**recorrem l'Array toRescue de cada vol (Path)*/

                            /**comprovem que no és el mateix element i fem el swap*/
                            if (i1!=i2 || j1!=j2 || k1!=k2) {
                                    State s = swap(state, i1, j1, k1, i2, j2, k2);
                                    if (s != null) retval.add(s);
                                }
                            }
                        }
                    }

                }
            }
        }

        /**Possibles successors fent moves de grups*/
        for (int i1 = 0; i1 < state.managedCentres.size(); i1++) { /**recorrem tots els centres*/
            for (int j1 = 0; j1 < state.managedCentres.get(i1).size(); j1++) { /**recorrem tots els vols de cada centre*/
                for (int k1 = 0; k1 < state.managedCentres.get(i1).get(j1).toRescue.size(); k1++) { /**recorrem l'Array toRescue de cada vol (Path)*/

                    for (int i2 = 0; i2 < state.managedCentres.size(); i2++) { /**recorrem tots els centres*/
                        /**fem un move dels grups a un nou vol d'un centre*/
                        State aux = move_nou(state, i1, j1, k1, i2);
                        if (aux != null) retval.add(aux);

                        for (int j2 = 0; j2 < state.managedCentres.get(i2).size(); j2++) { /**recorrem tots els vols de cada centre*/
                            if (state.managedCentres.get(i2).get(j2).toRescue.size() < 3) {

                                /**si el vol té menys de 3 centre fem un move a cada una de les possibles posicions*/
                                for (int p = 0; p < state.managedCentres.get(i2).get(j2).toRescue.size(); p++) {
                                    State s = move(state, i1, j1, k1, i2, j2, p);
                                    if (s != null) retval.add(s);
                                }
                            }
                        }
                    }

                }
            }
        }


        return retval;
    }


    /**funció a la classe State*/
    public State swap (State s, int centre1, int vol1, int pos1, int centre2, int vol2, int pos2) {
        //comprovar si es pot fer el canvi
        State ret = null;
        int rescatsVol1 = 0;
        int rescatsVol2 = 0;

        /**calculem el nombre de persones rescatades despres de fer el swap en el primer i segon vol*/
        for (int i = 0; i < s.managedCentres.get(centre1).get(vol1).toRescue.size(); i++) {
            if (i == pos1) rescatsVol2 += s.managedCentres.get(centre1).get(vol1).toRescue.get(i).getNPersonas();
            else rescatsVol1 += s.managedCentres.get(centre1).get(vol1).toRescue.get(i).getNPersonas();
        }

        for (int i = 0; i < s.managedCentres.get(centre2).get(vol2).toRescue.size(); i++) {
            if (i == pos2) rescatsVol1 += s.managedCentres.get(centre2).get(vol2).toRescue.get(i).getNPersonas();
            else rescatsVol2 += s.managedCentres.get(centre2).get(vol2).toRescue.get(i).getNPersonas();
        }

        /**comprovem que en fer el swap mantenim la restriccióde màxim 15 persones per helicòpter*/
        if (rescatsVol1 <= 15 && rescatsVol2 <= 15) {
            ret = (State) s;
            Grupo temp = (Grupo) ret.managedCentres.get(centre1).get(vol1).toRescue.get(pos1);

            ret.managedCentres.get(centre1).get(vol1).toRescue.set(pos1, ret.managedCentres.get(centre2).get(vol2).toRescue.get(pos2));
            ret.managedCentres.get(centre2).get(vol2).toRescue.set(pos2, temp);

            /**actualitzem valors de capacitat*/
            ret.managedCentres.get(centre1).get(vol1).capacity = rescatsVol1;
            ret.managedCentres.get(centre2).get(vol2).capacity = rescatsVol2;
        }
        return ret;
    }


    /**funció a la classe State*/
    public State move (State s, int centre1, int vol1, int pos1, int centre2, int vol2, int pos2) {
        State ret = null;

        /**comprobem que podem afegir un nou rescat, el "path destí" no pot tenir 3 grups*/
        if (s.managedCentres.get(centre2).get(vol2).toRescue.size() < 3) {
            int rescatsVol = 0;
            for (int i = 0; i < s.managedCentres.get(centre2).get(vol2).toRescue.size(); i++)
                rescatsVol += s.managedCentres.get(centre2).get(vol2).toRescue.get(i).getNPersonas();

            /**s'ha de mantenir la restricció d'un màxim de 15 persones per helicòpter*/
            if (rescatsVol + s.managedCentres.get(centre1).get(vol1).toRescue.get(pos1).getNPersonas() <= 15) {
                ret = s;
                Grupo grup_mogut = s.managedCentres.get(centre1).get(vol1).toRescue.get(pos1);

                s.managedCentres.get(centre1).get(vol1).toRescue.remove(pos1);
                s.managedCentres.get(centre1).get(vol1).toRescue.add(pos2, grup_mogut);

                /**actualitzem valors de capacitat*/
                s.managedCentres.get(centre1).get(vol1).capacity -= grup_mogut.getNPersonas();
                s.managedCentres.get(centre2).get(vol2).capacity += grup_mogut.getNPersonas();

                /**eliminem el vol en cas de que quedi vuit*/
                if (s.managedCentres.get(centre1).get(vol1).toRescue.size() == 0)
                    s.managedCentres.get(centre1).remove(s.managedCentres.get(centre1).get(vol1));
            }
        }
        return ret;
    }


    /**funcio a la classe State
     * per afegir un nou vol a un centre amb un unic grup, identificat per centre1, vol1 i pos1
     */
    public State move_nou (State s, int centre1, int vol1, int pos1, int centre2) {
        State ret = (State) s;
        Grupo grup_mogut = s.managedCentres.get(centre1).get(vol1).toRescue.get(pos1);

        s.managedCentres.get(centre1).get(vol1).capacity -= grup_mogut.getNPersonas();
        s.managedCentres.get(centre1).get(vol1).toRescue.remove(pos1);

        /**eliminem el vol en cas de que quedi vuit*/
        if (s.managedCentres.get(centre1).get(vol1).toRescue.size() == 0)
            s.managedCentres.get(centre1).remove(vol1);

        /**Creem nou Path i l'afegim al centre2*/
        State.Path nou = new State.Path();
        nou.toRescue = new ArrayList<Grupo>();
        nou.toRescue.add(grup_mogut);
        s.managedCentres.get(centre2).add(nou);

        return ret;
    }

}