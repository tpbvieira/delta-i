package gpl.instrumented;

import deltalib.dint.DeltaInt;

public interface EdgeIfc {
	public Vertex getStart();

	public Vertex getEnd();

	public void display();

	public Vertex getOtherVertex(Vertex vertex);

	public void adjustAdorns(EdgeIfc the_edge);

	public void setWeight(DeltaInt weight);

	public DeltaInt getWeight();
}
