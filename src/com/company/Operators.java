package com.company;

import IA.Desastres.*;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Operators  implements SuccessorFunction {

    /**parametre state ha de ser de tipus Object*/
    public List getSuccessors (Object S) {
        State state = (State) S;
        ArrayList retval = new ArrayList();

        /**Possibles successors fent swaps de grups*/
        for (int i1 = 0; i1 < state.managedCentres.size(); i1++) { /**recorrem tots els centres*/
            for (int j1 = 0; j1 < state.managedCentres.get(i1).size(); j1++) { /**recorrem tots els vols de cada centre*/
                for (int k1 = 0; k1 < state.managedCentres.get(i1).get(j1).toRescue.size(); k1++) { /**recorrem l'Array toRescue de cada vol (Path)*/

                    for (int i2 = 0; i2 < state.managedCentres.size(); i2++) { /**recorrem tots els centres*/
                        for (int j2 = 0; j2 < state.managedCentres.get(i2).size(); j2++) { /**recorrem tots els vols de cada centre*/
                            for (int k2 = 0; k2 < state.managedCentres.get(i2).get(j2).toRescue.size(); k2++) { /**recorrem l'Array toRescue de cada vol (Path)*/

                            /**comprovem que no és el mateix element i fem el swap*/
                            if (i1!=i2 || j1!=j2 || k1!=k2) {
                                    State aux = state.getCopy();
                                    aux.swap(i1, j1, k1, i2, j2, k2);
                                    retval.add(new Successor("Swap between centre " + i1 + " i centre" + i2, aux));
                                }
                            }
                        }
                    }

                }
            }
        }

        /**Possibles successors fent moves de grups*/
        //for (int i1 = 0; i1 < state.managedCentres.size(); i1++) { /**recorrem tots els centres*/
            //for (int j1 = 0; j1 < state.managedCentres.get(i1).size(); j1++) { /**recorrem tots els vols de cada centre*/

                    //for (int k1 = 0; k1 < state.managedCentres.get(i1).get(j1).toRescue.size(); k1++) { /**recorrem l'Array toRescue de cada vol (Path)*/

                       //for (int i2 = 0; i2 < state.managedCentres.size(); i2++) { /**recorrem tots els centres*/

                           /**fem un move dels grups a un nou vol d'un centre */
                           //State aux = state.getCopy();
                           //aux.move_nou(i1, j1, k1, i2);
                           //retval.add(new Successor("Move (nou) group between centre " + i1 + " i centre" + i2, aux));

                          // for (int j2 = 0; j2 < state.managedCentres.get(i2).size(); j2++) { /**recorrem tots els vols de cada centre*/

                               /**si el vol té menys de 3 centre fem un move a cada una de les possibles posicions */
                            //   for (int k2 = 0; k2 < state.managedCentres.get(i2).get(j2).toRescue.size(); k2++) {

                            //        State aux2 = state.getCopy();
        //                            aux2.move(i1, j1, k1, i2, j2, k2);
                                   // retval.add(new Successor("Move between centre " + i1 + " i centre" + i2, aux2));
                             //  }
                        //   }
                   //    }
               //     }

        //    }
     //   }

        return retval;
    }
}