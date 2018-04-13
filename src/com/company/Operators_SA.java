package com.company;

import IA.Desastres.*;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Operators_SA implements SuccessorFunction {

    /**
     * parametre state ha de ser de tipus Object
     */
    public List getSuccessors(Object S) {
        State st = (State) S.getCopy();
        ArrayList retval = new ArrayList();

        Random generator = new Random();
        int p = generator.nextInt(100);

        int i1, i2, j1, j2, k1, k2;

        /**Swap*/
        if (0 <= p && p < 50) {
            //System.out.println("Intentem fer un swap random");
            i1 = generator.nextInt(st.managedCentres.size());
            if (st.managedCentres.get(i1).size() != 0) {
                j1 = generator.nextInt(st.managedCentres.get(i1).size());
                if (st.managedCentres.get(i1).get(j1).toRescue.size() != 0) {
                    k1 = generator.nextInt(st.managedCentres.get(i1).get(j1).toRescue.size());
                    i2 = generator.nextInt(st.managedCentres.size());
                    if (st.managedCentres.get(i2).size() != 0) {
                        j2 = generator.nextInt(st.managedCentres.get(i2).size());
                        if (st.managedCentres.get(i2).get(j2).toRescue.size() != 0) {
                            k2 = generator.nextInt(st.managedCentres.get(i2).get(j2).toRescue.size());
                            st = st.swap(st, i1, j1, k1, i2, j2, k2);
                            if (st != null) {
                                retval.add(st);
                                //System.out.println("Swap random realitzat. size retval: " + retval.size());
                            }
                        }
                    }
                }
            }
        }

        /**Move*/
        else if (50 <= p && p < 75) {
            //System.out.println("Intentem fer un move random");
            i1 = generator.nextInt(st.managedCentres.size());
            if (st.managedCentres.get(i1).size() != 0) {
                j1 = generator.nextInt(st.managedCentres.get(i1).size());
                if (st.managedCentres.get(i1).get(j1).toRescue.size() != 0) {
                    k1 = generator.nextInt(st.managedCentres.get(i1).get(j1).toRescue.size());
                    i2 = generator.nextInt(st.managedCentres.size());
                    if (st.managedCentres.get(i2).size() != 0) {
                        j2 = generator.nextInt(st.managedCentres.get(i2).size());
                        if (st.managedCentres.get(i2).get(j2).toRescue.size() != 0 &&
                                st.managedCentres.get(i2).get(j2).toRescue.size() < 3) {
                            k2 = generator.nextInt(st.managedCentres.get(i2).get(j2).toRescue.size() + 1);
                            st = st.move(st, i1, j1, k1, i2, j2, k2);
                            if (st != null) {
                                retval.add(st);
                                //System.out.println("Move random realitzat. size retval: " + retval.size());
                            }
                        }
                    }
                }
            }
        }

        /**Move nou*/
        else if (75 <= p && p < 100) {
            //System.out.println("Intentem fer un move nou random");
            i1 = generator.nextInt(st.managedCentres.size());
            if (st.managedCentres.get(i1).size() != 0) {
                j1 = generator.nextInt(st.managedCentres.get(i1).size());
                if (st.managedCentres.get(i1).get(j1).toRescue.size() != 0) {
                    k1 = generator.nextInt(st.managedCentres.get(i1).get(j1).toRescue.size());
                    i2 = generator.nextInt(st.managedCentres.size());
                    st = st.move_nou(st, i1, j1, k1, i2);
                    if (st != null) {
                        retval.add(st);
                        //System.out.println("Move nou random realitzat. size retval: " + retval.size());
                    }
                }
            }
        }

        return retval;
    }
}