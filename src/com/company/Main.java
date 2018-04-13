package com.company;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        State test = new State(100, 10, 5);
        HillClimbingSearch(test);
    }

    private static void HillClimbingSearch(State state) {
        System.out.println("\nHill Climbing Search -->");
        try {
            Problem problem = new Problem(state, new Operators(), new Goal(), new Heuristic());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem, search);
            System.out.println(agent.getActions().size());
            System.out.println();
            printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SimulatedAnnealingSearch(State state) {
        System.out.println("\nSimulated Annealing Search -->");
        try {
            Problem problem = new Problem(state, new Operators(), new Goal(), new Heuristic());
            Search search = new SimulatedAnnealingSearch();                         //TODO: add parametres to the search function
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
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
