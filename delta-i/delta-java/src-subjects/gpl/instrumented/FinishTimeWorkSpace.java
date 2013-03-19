package gpl.instrumented;

import deltalib.General;
import deltalib.dint.DeltaInt;


// ***********************************************************************
   
public class FinishTimeWorkSpace extends  WorkSpace {
    DeltaInt FinishCounter;
 
    public FinishTimeWorkSpace() {
        FinishCounter = General.ONE;
    }

    public void preVisitAction(Vertex v ) {
        if ( v.visited!=true ) 
            FinishCounter = General.add(FinishCounter, General.ONE);
        
    }

    public void postVisitAction(Vertex v ) {
      v.finishTime = FinishCounter;
      FinishCounter = General.add(FinishCounter, General.ONE);
    } // of postVisit
    
}