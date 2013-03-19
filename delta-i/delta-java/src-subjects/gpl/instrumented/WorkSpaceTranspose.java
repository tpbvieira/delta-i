package gpl.instrumented;

import deltalib.General;
import deltalib.dint.DeltaInt;

// of FinishTimeWorkSpace

// DFS Transpose traversal
// *************************************************************************

public class WorkSpaceTranspose extends WorkSpace {
	// Strongly Connected Component Counter
	DeltaInt SCCCounter;

	public WorkSpaceTranspose() {
		SCCCounter = General.ZERO;
	}

	public void preVisitAction(Vertex v) {
		if (v.visited != true) {
			v.strongComponentNumber = SCCCounter;
		}
	}

	public void nextRegionAction(Vertex v) {
		SCCCounter = General.add(SCCCounter, General.ONE);
	}

}
