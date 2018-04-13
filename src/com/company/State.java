package com.company;

import IA.Desastres.*;

import java.util.ArrayList;
import java.util.Random;

public class State {

    public static int NUM_COPTERS = 2;

    public ArrayList< ArrayList<Path> > managedCentres;
    private Grupos G;
    private Centros C;
    private Double[][] groupsAdjM;
    private Double[][] centresAdjM;

    public ArrayList<ArrayList<Path>> getManagedCenters() { return managedCentres; }

    public Double[][] getCentresGroupsAdjM() { return centresAdjM; }

    public Double[][] getGroupsAdjM() { return groupsAdjM; }

    public Grupos getG() { return G; }

    static class Path
    {
        public Integer pathID;
        public ArrayList<Grupo> toRescue = new ArrayList<>();
        public Integer capacity = 15;
    };

    /** Representation of the state: A graph where the nodes are the Centres and the Groups.
     *  The groups are connected to each other and there is a link between each node and each graph.
     *  It will be represented by groupsAdjMatrix(|C|x|C|) and centresAdjMatrix(|G|x|C|), where the
     *  edges have a weight equal to the distance between elements. */

    public State() {};

    public State(int nGrupos, int nCentros, int seed) {
        G = new Grupos(nGrupos, seed);
        C = new Centros(nCentros, NUM_COPTERS, seed);
        setBoard();

        for (int i = 0; i < C.size(); ++i)
            System.out.println("Center: " + i + " " + C.get(i).getCoordX() + " " + C.get(i).getCoordY());

        Path p = new Path();
        p.toRescue.add(G.get(0));
        managedCentres = new ArrayList<>();
        ArrayList<Path> test = new ArrayList<>();
        test.add(p);
        managedCentres.add(test);
        printFirstSolution();

        //managedCentres = genFirstSolutionDummy();
        //printFirstSolution(); System.out.println();

        managedCentres = genFirstSolutionEficient();
        printFirstSolution();
    }

    private void printFirstSolution() {

        String[][] scene = new String[50][50];
        for (int i = 0; i < scene.length; ++i)
            for (int j = 0; j < scene[i].length; ++j) scene[i][j] = ".";

        Centro c;
        for (int i = 0; i < C.size(); ++i) {
            c = C.get(i);
            scene[c.getCoordY()][c.getCoordX()] = "c" + String.valueOf(i);
        }


        for (int i = 0; i < managedCentres.size(); ++i)
            for (int j = 0; j < managedCentres.get(i).size(); ++j) {
                Path p = managedCentres.get(i).get(j);
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

    /** Generation of first solution: Dummy */
    private ArrayList< ArrayList<Path> > genFirstSolutionDummy() {
        ArrayList< ArrayList<Path> > centres = new ArrayList<> ();
        for (int i = 0; i < C.size(); ++i) centres.add(new ArrayList<>());

        Boolean[] rescuedG = new Boolean[G.size()];
        for (int i = 0; i < rescuedG.length; ++i) rescuedG[i] = false;
        int pathId = 0;

        for (int i = 0; i < G.size(); ++i) {
            if (!rescuedG[i]) {
                rescuedG[i] = true;
                Path p = new Path();
                p.pathID = pathId;
                ++pathId;
                p.toRescue.add(G.get(i));
                p.capacity -= G.get(i).getNPersonas();

                for (int j = 0; j < G.size(); ++j) {
                    if (!rescuedG[j] && p.capacity >= G.get(j).getNPersonas()) {
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

        return centres;
    }

    /** Generation of first solution: Efficient
     * In this case the Paths are assigned to the closer centre to 2 of the grups to rescue */
    private ArrayList< ArrayList<Path> > genFirstSolutionEficient() {
        /** centres is the solution */
        ArrayList< ArrayList<Path> > centres = new ArrayList<> ();
        for (int i = 0; i < C.size(); ++i) centres.add(new ArrayList<>());

        /** rescuedG for knowing which groups have been rescued */
        Boolean[] rescuedG = new Boolean[G.size()];
        for (int i = 0; i < rescuedG.length; ++i) rescuedG[i] = false;

        Path p;
        int pathId = 0;
        Integer rescGroups = 0;
        while (rescGroups < G.size()) {
            for (int i = 0; i < G.size(); ++i) {
                if (!rescuedG[i]) {
                    rescuedG[i] = true;

                    p = new Path();
                    p.pathID = pathId;
                    ++pathId;
                    p.toRescue.add(G.get(i));
                    rescGroups++;
                    p.capacity -= G.get(i).getNPersonas();

                    int ngbor = closerDist(groupsAdjM[i], rescuedG, G, p.capacity, 0.0, false);

                    if (ngbor != -1) {
                        rescuedG[ngbor] = true;
                        p.toRescue.add(G.get(ngbor));
                        rescGroups++;
                        p.capacity -= G.get(ngbor).getNPersonas();

                        int ngbor2 = closerDist(groupsAdjM[i], rescuedG, G, p.capacity, groupsAdjM[i][ngbor], true);
                        if (ngbor2 != -1) {
                            rescuedG[ngbor2] = true;
                            p.toRescue.add(G.get(ngbor2));
                            rescGroups++;
                            p.capacity -= G.get(ngbor2).getNPersonas();
                        }
                    }


                    int inCharge = closerCentre(p, C);
                    centres.get(inCharge).add(p);
                }
            }
        }

        /**for (int i = 0; i < centres.size(); ++i) {
            System.out.println("Center number " + i);
            for (int j = 0; j < centres.get(i).size(); ++j) {
                //System.out.println("Path: " + centres.get(i).get(j).pathID);
                printGroup(centres.get(i).get(j));
            }
        }*/

        return centres;
    }

    /** This function checks all the restrictions:
     *      · not being visited
     *      · copter people limit is not exceeded
     *  The result is the closer non rescued group */
    private Integer closerDist(Double[] dist, Boolean[] resc, Grupos G, int cap, Double lastD, Boolean thirdG) {
        int min = -1; int i;

        for (i = 0; i < dist.length; ++i)
            if (!resc[i] && dist[i] != 0.0 && cap > G.get(i).getNPersonas()) { min = i; break; }

        if (min != -1) {
            for (; i < dist.length; ++i)
                if (!resc[i] && dist[i] != 0.0 && dist[i] < dist[min])
                    if (cap > G.get(i).getNPersonas()) min = i;
        }

        /**System.out.println("Closer Dist:" + " (capacity " + cap +")");

        for (int j = 0; j < dist.length; ++j)
            System.out.println(G.get(j).getCoordX() + " " + G.get(j).getCoordY() + " " + G.get(j).getNPersonas() + " " + dist[j] + " " + resc[j]);

        System.out.println();
        System.out.println(min);
        System.out.println();*/

        if (thirdG && 2*lastD < min) min = -1;
        return min;
    }

    private void printGroup(Path p) {
        for (int i = 0; i < p.toRescue.size(); ++i)
            System.out.println(p.toRescue.get(i).getCoordX() + " " + p.toRescue.get(i).getCoordY() + " " + p.toRescue.get(i).getNPersonas());
        System.out.println();
    }

    private void setBoard() {

        /** Adjacency between groups */
        groupsAdjM = new Double[G.size()][G.size()];

        for (int i = 0; i < G.size(); ++i)
            for (int j = 0; j < G.size(); ++j)
                if (i != j) groupsAdjM[i][j] = distance(G.get(i).getCoordX(), G.get(i).getCoordY(), G.get(j).getCoordX(), G.get(j).getCoordY());

        /** Adjacency between centres and groups */
        centresAdjM = new Double[C.size()][G.size()];

        for (int i = 0; i < C.size(); ++i)
            for (int j = 0; j < G.size(); ++j)
                centresAdjM[i][j] = distance( C.get(i).getCoordX(), C.get(i).getCoordY(), G.get(j).getCoordX(),  G.get(j).getCoordY());

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

    private int closerCentre(Path p, Centros C) {
        /** array with the medium distance of groups in this order: 0-1, 1-2, 0-2*/
        Grupo g; Centro c;
        int min = 0; Double dist; Double tmp;

        g = p.toRescue.get(0);
        c = C.get(0);

        if (p.toRescue.size() == 1) {
            dist = distance(g.getCoordX(), g.getCoordY(), c.getCoordX(), c.getCoordY());

            for (int i = 1; i < C.size(); ++i) {
                tmp = distance(g.getCoordX(), g.getCoordY(), C.get(i).getCoordX(), C.get(i).getCoordY());
                if (tmp < dist)
                {
                    min = i;
                    dist = tmp;
                }
            }
        } else  if (p.toRescue.size() == 2) {
            dist = distanceTo(g, p.toRescue.get(1), c);

            for (int i = 1; i < C.size(); ++i) {
                tmp = distanceTo(p.toRescue.get(0), p.toRescue.get(1), C.get(i));
                if (tmp < dist) {
                    min = i;
                    dist = tmp;
                }
            }
        } else {
            dist = distanceTo(g, p.toRescue.get(1), c);
            for (int i = 0; i < C.size(); ++i) {
                tmp = distanceTo(p.toRescue.get(0), p.toRescue.get(1), C.get(i));
                if (tmp < dist) { min = i; dist = tmp;}

                tmp = distanceTo(p.toRescue.get(1), p.toRescue.get(2), C.get(i));
                if (tmp < dist) { min = i; dist = tmp;}

                tmp = distanceTo(p.toRescue.get(2), p.toRescue.get(0), C.get(i));
                if (tmp < dist) { min = i; dist = tmp;}
            }
        }

        /**String[][] scene = new String[50][50];
        for (int i = 0; i < scene.length; ++i)
            for (int j = 0; j < scene[i].length; ++j) scene[i][j] = ".";

        Centro centro;
        for (int i = 0; i < C.size(); ++i) {
            centro = C.get(i);
            scene[centro.getCoordY()][centro.getCoordX()] = "c" + String.valueOf(i);
        }

        for (int i = 0; i < p.toRescue.size(); ++i)
            scene[p.toRescue.get(i).getCoordY()][p.toRescue.get(i).getCoordY()] = String.valueOf(min);

        for ( String[] row : scene)
        {
            for (String elm : row)
                System.out.print(elm);
            System.out.println();
        }
        System.out.println();*/

        return min;
    }

    private Double distanceTo(Grupo g1, Grupo g2, Centro C) {
        /** We only compute the distance between the groups and the center since the distance between groups is always the same*/
        Double g1ToC = distance(C.getCoordX(), C.getCoordY(), g1.getCoordX(), g1.getCoordY());
        Double g2ToC = distance(C.getCoordX(), C.getCoordY(), g2.getCoordX(), g2.getCoordY());

        return g1ToC + g2ToC;
    }

    private Double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt( Math.pow( x1 - x2, 2 ) + Math.pow( y1 - y2, 2 ));
    }


    /** OPERATORS */

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
        if (0 <= vol1 && vol1 < s.managedCentres.get(centre1).size() &&
                0 <= pos1 && pos1 < s.managedCentres.get(centre1).get(vol1).toRescue.size() &&
                0 <= vol2 && vol2 < s.managedCentres.get(centre2).size() &&
                0 <= pos2 && pos2 < s.managedCentres.get(centre2).get(vol2).toRescue.size()) {

            int rescatsVol = 0;
            for (int i = 0; i < s.managedCentres.get(centre2).get(vol2).toRescue.size(); i++)
                rescatsVol += s.managedCentres.get(centre2).get(vol2).toRescue.get(i).getNPersonas();

            /**s'ha de mantenir la restricció d'un màxim de 15 persones per helicòpter*/
            if (rescatsVol + s.managedCentres.get(centre1).get(vol1).toRescue.get(pos1).getNPersonas() <= 15) {
                ret = s;
                Grupo grup_mogut = s.managedCentres.get(centre1).get(vol1).toRescue.get(pos1);

                s.managedCentres.get(centre1).get(vol1).toRescue.remove(pos1);
                s.managedCentres.get(centre2).get(vol2).toRescue.add(pos2, grup_mogut);

                /**actualitzem valors de capacitat*/
                s.managedCentres.get(centre1).get(vol1).capacity -= grup_mogut.getNPersonas();
                s.managedCentres.get(centre2).get(vol2).capacity += grup_mogut.getNPersonas();

                /**eliminem el vol en cas de que quedi vuit*/
                if (s.managedCentres.get(centre1).get(vol1).toRescue.size() == 0)
                    s.managedCentres.get(centre1).remove(vol1);
            }
        }
        return ret;
    }


    /**funcio a la classe State
     * per afegir un nou vol a un centre amb un unic grup, identificat per centre1, vol1 i pos1
     */
    public State move_nou (State s, int centre1, int vol1, int pos1, int centre2) {

        if (s.managedCentres.get(centre1).size() != 0) {

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
        }

        return s;

    }
}
