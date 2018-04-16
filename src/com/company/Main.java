package com.company;

import IA.Desastres.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        State test = new State(3, 3, 3);
        HillClimbingSearch(test);
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

    private static void HillClimbingSearch(State state) {
        System.out.println("\nHill Climbing Search -->");
        try {
            Problem problem = new Problem(state, new Operators(), new Goal(), new Heuristic1());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);

            //printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SimulatedAnnealingSearch(State state) {
        System.out.println("\nSimulated Annealing Search -->");
        try {
            Problem problem = new Problem(state, new Operators(), new Goal(), new Heuristic1());
            Search search = new SimulatedAnnealingSearch();                         //TODO: add parametres to the search function
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List actions) {
        System.out.println("#Accions" + actions.size());
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

}
