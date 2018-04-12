package com.company;

import IA.Desastres.*;
import aima.basic.XYLocation;
import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;

/**
 * @author Jordi Foix
 *
 */
public class Heuristic implements HeuristicFunction {

    public double getHeuristicValue(Object S) {
        State state = (State) S;

        //Sumem el temps total de la solucio, fent per cada viatge: calcul del temps a partir de la distancia + 10 minuts + el temps de recollir la gent

        ArrayList<ArrayList<State.Path>> managedCentres = state.getManagedCenters(); //aqui tenim els paths que es fan en aquesta solucio
        Double[][] centresGroupsAdjM = state.getCentresGroupsAdjM();
        Double[][] groupsAdjM = state.getGroupsAdjM();
        Grupos G = state.getG();
        int totalDistance, numberOfTrips, lastIndex = 0;
        double distance, totalTime = 0; //temps en minuts, distància en km

        for (int i = 0; i < managedCentres.size(); ++i) {
            for (int j = 0; j < managedCentres.get(i).size(); ++j) { // centre i, path j
                for (int k = 0; k < managedCentres.get(i).get(j).toRescue.size(); ++k) { //aquest loop és per trobar l'índex dels grups assignats al path j
                    for (int l = 0; l < G.size(); ++l) {
                        if (managedCentres.get(i).get(j).toRescue.get(k) == G.get(l)) {
                            distance = 0;
                            if (k == 0 || k == (managedCentres.get(i).get(j).toRescue.size() - 1) )
                                distance = centresGroupsAdjM[i][l];
                            else
                                distance = groupsAdjM[l][lastIndex];
                            totalTime += distance * (60 / 100); // velocitat = 100km/60min
                            if (G.get(l).getPrioridad() == 1)
                                totalTime += G.get(l).getNPersonas() * 2;
                            else
                                totalTime += G.get(l).getNPersonas();
                            lastIndex = l;
                            break;
                        }
                    }
                }
                totalTime += 10;
            }
        }
        return totalTime;
    }

    
    public int evaluateManhattanDistanceOf(int i, XYLocation loc) {
        int retVal = -1;
        int xpos = loc.getXCoOrdinate();
        int ypos = loc.getYCoOrdinate();
        switch (i) {

            case 1:
                retVal = Math.abs(xpos - 0) + Math.abs(ypos - 1);
                break;
            case 2:
                retVal = Math.abs(xpos - 0) + Math.abs(ypos - 2);
                break;
            case 3:
                retVal = Math.abs(xpos - 1) + Math.abs(ypos - 0);
                break;
            case 4:
                retVal = Math.abs(xpos - 1) + Math.abs(ypos - 1);
                break;
            case 5:
                retVal = Math.abs(xpos - 1) + Math.abs(ypos - 2);
                break;
            case 6:
                retVal = Math.abs(xpos - 2) + Math.abs(ypos - 0);
                break;
            case 7:
                retVal = Math.abs(xpos - 2) + Math.abs(ypos - 1);
                break;
            case 8:
                retVal = Math.abs(xpos - 2) + Math.abs(ypos - 2);
                break;

        }
        return retVal;
    }

}