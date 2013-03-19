package gpl.instrumented;

import deltalib.General;
import deltalib.dboolean.DeltaBoolean;
import deltalib.dint.DeltaInt;
// *************************************************************************

public class CycleWorkSpace extends WorkSpace {

  public DeltaBoolean AnyCycles;

  public DeltaInt counter;

  public static final DeltaInt WHITE = General.ZERO;

  public static final DeltaInt GRAY = General.ONE;

  public static final DeltaInt BLACK = General.TWO;

  public CycleWorkSpace() {
    AnyCycles = General.FALSE;
    counter = General.ZERO;
  }

  public void init_vertex(Vertex v) {
    v.VertexCycle = General.constant(Integer.MAX_VALUE);
    v.VertexColor = WHITE; // initialize to white color
  }

  public void preVisitAction(Vertex v) {

    // This assigns the values on the way in
    if (General.split(General.neq_bool(v.visited, General.TRUE))) { // if it has not been visited then set the
      // VertexCycle accordingly
      counter = General.add(counter, General.ONE);
      v.VertexCycle = counter;
      v.VertexColor = GRAY; // we make the vertex gray
    }
  }

  public void postVisitAction(Vertex v) {
    v.VertexColor = BLACK; // we are done visiting so make it black
    counter = General.sub(counter, General.ONE);
  } // of postVisitAction

  public void checkNeighborAction(Vertex vsource, Vertex vtarget) {
    // if the graph is directed is enough to check that the source node
    // is gray and the adyacent is gray also to find a cycle
    // if the graph is undirected we need to check that the adyacent is not
    // the father, if it is the father the difference in the VertexCount is
    // only one.

    // directed
    if (General.split(General.and(General.eq_int(vsource.VertexColor, GRAY),General.eq_int(vtarget.VertexColor, GRAY)))) {
      AnyCycles = General.TRUE;
    }
    // undirected case
    if (General.split(General.and(General.and(General.eq_int(vsource.VertexColor, GRAY),General.eq_int(vtarget.VertexColor, GRAY)),
        General.neq_int(vsource.VertexCycle,General.add(vtarget.VertexCycle, General.ONE))))) {
      AnyCycles = General.TRUE;
    }

  } // of checkNeighboor

}
