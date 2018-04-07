package com.company;

import IA.Desastres.*;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class Operators  implements SuccessorFunction {

    public List getSuccessors (Object state) {
        ArrayList<State> retval = new ArrayList<>();

        /* Possibles successors fent swaps de grups*/
        for (int i1 = 0; i1 < state.managedCentres.size(); i1++) { //recorrem tots els centres
            for (int j1 = 0; j1 < state.managedCentres[i1].size(); j1++) { //recorrem tots els vols de cada centre
                for (int k1 = 0; k1 < state.managedCentres[i1][j1].toRescue.size(); k1++) { //recorrem l'Array toRescue de cada struct Path (vol)

                    for (int i2 = 0; i2 < state.managedCentres.size(); i2++) { //recorrem tots els centres
                        for (int j2 = 0; j2 < state.managedCentres[i2].size(); j2++) {//recorrem tots els vols de cada centre
                            for (int k2 = 0; k2 < state.managedCentres[i2][j2].toRescue.size(); k2++) { //recorrem l'Array toRescue de cada struct Path (vol)
                                if (i1!=i2 || j1!=j2 || k1!=k2) {
                                    State s = swap(state, i1, j1, k1, i2, j2, k2);
                                    retval.add(s);
                                }
                            }
                        }
                    }

                }
            }
        }
        /* Possibles successors fent moves de grups*/
        for (int i1 = 0; i1 < state.managedCentres.size(); i1++) { //recorrem tots els centres
            for (int j1 = 0; j1 < state.managedCentres[i1].size(); j1++) { //recorrem tots els vols de cada centre
                for (int k1 = 0; k1 < state.managedCentres[i1][j1].toRescue.size(); k1++) { //recorrem l'Array toRescue de cada struct Path (vol)

                    for (int i2 = 0; i2 < state.managedCentres.size(); i2++) { //recorrem tots els centres
                        for (int j2 = 0; j2 < state.managedCentres[i2].size(); j2++) {//recorrem tots els vols de cada centre
                            if (state.managedCentres[i2].[j2].size() < 3) {
                                for (int p = 0; p < 3; p++) {
                                    State s = move(state, i1, j1, k1, i2, j2, p);
                                    retval.add(s);
                                }
                            }
                        }
                    }

                }
            }
        }


        return retval;
    }


    // funció a la classe State
    public State swap (State s, int centre1, int vol1, int pos1, int centre2, int vol2, int pos2) {
        //comprovar si es pot fer el canvi
        State ret = null;
        int rescatsVol1 = 0;
        int rescatsVol2 = 0;
        //calculem el nombre de persones rescatades despres de fer el swap en el primer i segon vol
        for (int i = 0; i < s.managedCentres[centre1][vol1].toRescue.size(); i++) {
            if (i == pos1) rescatsVol2 += s.managedCentres[centre1][vol1].toRescue[i].getNPersonas();
            else rescatsVol1 += s.managedCentres[centre1][vol1].toRescue[i].getNPersonas();
        }
        for (int i = 0; i < s.managedCentres[centre2][vol2].toRescue.size(); i++) {
            if (i == pos2) rescatsVol1 += s.managedCentres[centre2][vol2].toRescue[i].getNPersonas();
            else rescatsVol2 += s.managedCentres[centre2][vol2].toRescue[i].getNPersonas();
        }

        if (rescatsVol1 <= 15 && rescatsVol2 <= 15) {
            ret = (State) s;
            Grupo temp = (Grupo) ret.managedCentres[centre1][vol1].toRescue[pos1];
            ret.managedCentres[centre1][vol1].toRescue[pos1] = ret.managedCentres[centre2][vol2].toRescue[pos2];
            ret.managedCentres[centre2][vol2].toRescue[pos2] = temp;
        }
        return ret;
    }


    // funció a la classe State
    public State move (State s, int centre1, int vol1, int pos1, int centre2, int vol2, int pos2) {
        State ret = null;

        //comprobem que podem afegir un nou rescat, el "path destí" pot tenir un màxim de dos grups i en afegir el nou grup,
        //s'ha de mantenir la restricció d'un màxim de 15 persones per helicòpter
        if (s.managedCentres[centre2][vol2].toRescue.size() < 3) {
            int rescatsVol = 0;
            for (int i = 0; i < s.managedCentres[centre2][vol2].toRescue.size(); i++)
                rescatsVol += s.managedCentres[centre2][vol2].toRescue[i].getNPersonas();
            if (rescatsVol + s.managedCentres[centre1][vol1].toRescue[pos1].getNPersonas() <= 15) {
                ret = (State) s;
                for (int i = 1; i < pos1; i++)
                    s.managedCentres[centre1][vol1].toRescue[i-1] = s.managedCentres[centre1][vol1].toRescue[i];
                for (int i = pos1; i < s.managedCentres[centre1][vol1].toRescue.size()-1; i++)
                    s.managedCentres[centre1][vol1].toRescue[i] = s.managedCentres[centre1][vol1].toRescue[i+1];
                s.managedCentres[centre1][vol1].toRescue.remove(s.managedCentres[centre1][vol1].toRescue.size()-1);
                if (s.managedCentres[centre1][vol1].toRescue.size() == 0)
                    s.managedCentres[centre1].remove(s.managedCentres[centre1].[vol1]);



//falta acabar


            }
        }
        return ret;
    }
}