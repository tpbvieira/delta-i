package gpl.instrumented;

import deltalib.General;
import deltalib.dint.DeltaInt;

// *****************************************************************
   
public class RegionWorkSpace extends  WorkSpace 
{
    DeltaInt counter;

    public RegionWorkSpace( ) 
    {
        counter = General.ZERO;
    }

    public void init_vertex( Vertex v ) 
    {
        v.componentNumber = General.MINUS_ONE;
    }
      
    public void postVisitAction( Vertex v ) 
    {
        v.componentNumber = counter;
    }

    public void nextRegionAction( Vertex v ) 
    {
        counter = General.add(counter, General.ONE);
    }  
    
}