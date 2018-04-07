package com.company;

import IA.Desastres.*;

import java.util.ArrayList;
import java.util.Random;


public class State {

    public static int NUM_COPTERS = 2;
    //Atributes
    private ArrayList< ArrayList<Path> > managedCentres;
    private Grupos G;
    private Centros C;
    private Double[][] groupsAdjM;
    private Double[][] centresAdjM;

    /** Representation of the state: A graph where the nodes are the Centres and the Groups.
     *  The groups are connected to each other and there is a link between each node and each graph.
     *  It will be represented by groupsAdjMatrix(|C|x|C|) and centresAdjMatrix(|G|x|C|), where the
     *  edges have a weight equal to the distance between elements. */

    public State() {};

    public State(int nGrupos, int nCentros, int seed) {
        G = new Grupos(nGrupos, seed);
        C = new Centros(nCentros, NUM_COPTERS, seed);

        setBoard();
        managedCentres = genFirstSolutionDummy();
    }


    /** Generation of first solution: Dummy */
    private ArrayList< ArrayList<Path> > genFirstSolutionDummy() {
        ArrayList< ArrayList<Path> > centres = new ArrayList<> ();
        for (int i = 0; i < C.size(); ++i) centres.add(new ArrayList<>());

        Boolean[] rescuedG = new Boolean[G.size()];
        for (int i = 0; i < rescuedG.length; ++i) rescuedG[i] = false;

        for (int i = 0; i < G.size(); ++i) {
            if (!rescuedG[i]) {
                rescuedG[i] = true;
                Path p = new Path();
                p.toRescue.add(G.get(i));
                p.capacity -= G.get(i).getNPersonas();

                for (int j = 0; j < G.size(); ++j) {
                    if (!rescuedG[j] && p.capacity - G.get(j).getNPersonas() >= 0) {
                        p.capacity -= G.get(j).getNPersonas();
                        p.toRescue.add(G.get(j));
                        rescuedG[j] = true;
                    }
                }

                Random generator = new Random();
                int inCharge = generator.nextInt(C.size());
                centres.get(inCharge).add(p);
            }
        }

        for (int i = 0; i < centres.size(); ++i) {
            System.out.println("Center number " + i);
            for (int j = 0; j < centres.get(i).size(); ++j)
                printGroup(centres.get(i).get(j));
        }

        return centres;
    }

    /** Generation of first solution: Efficient
     * In this case the Paths are assigned to the closer centre to 2 of the grups to rescue */
    private ArrayList< ArrayList<Path> > genFirstSolutionEficient() {
        ArrayList< ArrayList<Path> > centres = new ArrayList<> ();
        for (int i = 0; i < C.size(); ++i) centres.add(new ArrayList<>());

        Boolean[] rescuedG = new Boolean[G.size()];
        for (int i = 0; i < rescuedG.length; ++i) rescuedG[i] = false;

        for (int i = 0; i < groupsAdjM.length; ++i) {

            for (int j = 0; j < groupsAdjM.length; ++j) {
                if ()
            }
        }

        return centres;
    }

    private void printGroup(Path p) {
        for (int i = 0; i < p.toRescue.size(); ++i)
            System.out.print(p.toRescue.get(i).getNPersonas() + " ");
        System.out.println();
    }

    private void setBoard() {

        /** Adjacency between groups */
        groupsAdjM = new Double[G.size()][G.size()];

        for (int i = 0; i < G.size(); ++i)
            for (int j = 0; j < G.size(); ++j)
                if (i != j) groupsAdjM[i][j] = Math.hypot( G.get(i).getCoordX() - G.get(j).getCoordX(), G.get(i).getCoordY() - G.get(j).getCoordY());

        /** Adjacency between centres and groups */
        centresAdjM = new Double[C.size()][G.size()];

        for (int i = 0; i < C.size(); ++i)
            for (int j = 0; j < G.size(); ++j)
                centresAdjM[i][j] = Math.hypot( C.get(i).getCoordX() - G.get(j).getCoordX(), C.get(i).getCoordY() - G.get(j).getCoordY());

        /** For Debugging */

        for (Grupo g: G) {
            System.out.println(g.getCoordX() + " " + g.getCoordY() + " " + g.getNPersonas() + " " + g.getPrioridad());
        } System.out.println();

        /**for (Centro c: C) {
            System.out.println(c.getCoordX() + " " + c.getCoordY() + " " + c.getNHelicopteros());
        } System.out.println();

        for (int i = 0; i < groupsAdjM.length; ++i) {
            for (int j = 0; j < groupsAdjM[0].length; ++j)
                System.out.print(groupsAdjM[i][j] + " ");
            System.out.println();
        } System.out.println();

        for (int i = 0; i < centresAdjM.length; ++i) {
            for (int j = 0; j < centresAdjM[0].length; ++j)
                System.out.print(centresAdjM[i][j] + " ");
            System.out.println();
        }*/
    }

    class Path
    {
        public Integer copterID;
        public ArrayList<Grupo> toRescue = new ArrayList<>();
        public Integer capacity = 15;
    };
}
