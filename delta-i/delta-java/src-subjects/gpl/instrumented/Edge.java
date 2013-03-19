package gpl.instrumented;

import deltalib.dint.DeltaInt;
// *************************************************************************

public class Edge implements EdgeIfc {
  private Vertex start;

  private Vertex end;

  public Edge(Vertex the_start, Vertex the_end, DeltaInt aweight) {
    start = the_start;
    end = the_end;
    weight = aweight;
  }

  public void adjustAdorns(EdgeIfc the_edge) {
    setWeight(the_edge.getWeight());
  }

  private DeltaInt weight;

  public void setWeight(DeltaInt weight) {
    this.weight = weight;
  }

  public DeltaInt getWeight() {
    return this.weight;
  }

  public Vertex getOtherVertex(Vertex vertex) {
    if (vertex == start)
      return end;
    else if (vertex == end)
      return start;
    else
      return null;
  }

  public Vertex getStart() {
    return start;
  }

  public Vertex getEnd() {
    return end;
  }

  public void display() {
    System.out.print(" Weight=" + weight);
    System.out.println(" start=" + start.getName() + " end="
        + end.getName());
  }
}