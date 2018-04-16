package com.company;

import IA.Desastres.*;
import aima.basic.XYLocation;
import aima.search.framework.HeuristicFunction;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Jordi Foix
 *
 */
public class Heuristic1 implements HeuristicFunction {

    public double getHeuristicValue(Object S) {
        /**System.out.println(((State)S).getDistance());
        System.out.println(((State)S).getExtraRescueTime1());
        System.out.println();*/
        return ((State)S).getDistance()*(60.0 / 100.0) + ((State)S).getExtraRescueTime1();
    }

    public static void printFirstSolution(State s) {

        String[][] scene = new String[50][50];
        for (int i = 0; i < scene.length; ++i)
            for (int j = 0; j < scene[i].length; ++j) scene[i][j] = ".";

        Centro c;
        for (int i = 0; i < s.getC().size(); ++i) {
            c = s.getC().get(i);
            scene[c.getCoordY()][c.getCoordX()] = "c" + String.valueOf(i);
        }


        for (int i = 0; i < s.managedCentres.size(); ++i)
            for (int j = 0; j < s.managedCentres.get(i).size(); ++j) {
                State.Path p = s.managedCentres.get(i).get(j);
                for (int l = 0; l < p.toRescue.size(); ++l) {
                    Grupo g = p.toRescue.get(l);
                    scene[g.getCoordY()][g.getCoordX()] = String.valueOf(i);
                }
            }

        for (int i = 0; i < scene.length; ++i) {
            for (int j = 0; j < scene[i].length; ++j)
                System.out.print(scene[i][j]);
            System.out.println();
        }

    }

    public boolean equals(Object obj) {
        boolean retValue;

        retValue = super.equals(obj);
        return retValue;
    }

}