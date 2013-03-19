package gpl.instrumented;

import deltalib.General;
import deltalib.dint.DeltaInt;

public class NumberWorkSpace extends WorkSpace {
	DeltaInt vertexCounter;

	public NumberWorkSpace() {
		vertexCounter = General.ZERO;
	}

	public void preVisitAction(Vertex v) {
		// This assigns the values on the way in
		if (v.visited != true) {
			v.vertexNumber = vertexCounter;
			vertexCounter = General.add(vertexCounter, General.ONE);
		}
	}
}