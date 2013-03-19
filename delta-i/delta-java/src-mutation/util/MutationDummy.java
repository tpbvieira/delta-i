package util;
import java.util.ArrayList;
import java.util.List;

import de.unisb.cs.st.javalanche.mutation.results.Mutation;
import de.unisb.cs.st.javalanche.mutation.results.Mutation.MutationType;

public class MutationDummy {

  private static List<Mutation> list;

  static{
    list = new ArrayList<Mutation>();

    // line 8 = public static int v1 = 45;
    Mutation mt1 = new Mutation("", "", 8 , 1, MutationType.RIC_PLUS_1);
    mt1.setId(1l);
    Mutation mt2 = new Mutation("", "", 8 , 2, MutationType.RIC_ZERO);
    mt2.setId(2l);
    Mutation mt3 = new Mutation("", "", 8 , 3, MutationType.RIC_MINUS_1);
    mt3.setId(3l);

    list.add(mt1);
    list.add(mt2);
    list.add(mt3);

    // line 8 = public static int f = 0;
    Mutation mt4 = new Mutation("", "", 8 , 1, MutationType.RIC_PLUS_1);
    mt4.setId(4l);
    Mutation mt5 = new Mutation("", "", 8 , 2, MutationType.RIC_MINUS_1);
    mt5.setId(5l);

    list.add(mt4);
    list.add(mt5);

    // line 11 => 0 != v1
    Mutation mt6 = new Mutation("", "", 10 , 1, MutationType.RIC_PLUS_1);
    mt6.setId(6l);
    Mutation mt7 = new Mutation("", "", 10 , 2, MutationType.RIC_MINUS_1);
    mt7.setId(7l);

    list.add(mt6);
    list.add(mt7);
    
    // line 14 = private int v1 = 10;
    Mutation mt8 = new Mutation("", "", 14 , 1, MutationType.RIC_MINUS_1);
    mt8.setId(8l);
    Mutation mt9 = new Mutation("", "", 14 , 2, MutationType.RIC_PLUS_1);
    mt9.setId(9l);
    Mutation mt10 = new Mutation("", "", 14 , 3, MutationType.RIC_ZERO);
    mt10.setId(10l);

    list.add(mt8);
    list.add(mt9);
    list.add(mt10);

    // line 20 = private int v1 = 10;
    Mutation mt11 = new Mutation("", "", 20 , 1, MutationType.RIC_MINUS_1);
    mt11.setId(11l);
    Mutation mt12 = new Mutation("", "", 20 , 2, MutationType.RIC_PLUS_1);
    mt12.setId(12l);
    Mutation mt13 = new Mutation("", "", 20 , 3, MutationType.RIC_ZERO);
    mt13.setId(13l);

    list.add(mt11);
    list.add(mt12);
    list.add(mt13);
    
 // line 21 = private int v1 = 10;
    Mutation mt14 = new Mutation("", "", 21 , 1, MutationType.RIC_MINUS_1);
    mt14.setId(14l);
    Mutation mt15 = new Mutation("", "", 21 , 2, MutationType.RIC_PLUS_1);
    mt15.setId(15l);
    Mutation mt16 = new Mutation("", "", 21 , 3, MutationType.RIC_ZERO);
    mt16.setId(16l);

    list.add(mt14);
    list.add(mt15);
    list.add(mt16);

  }

  public static List<Mutation> getMutations(Class cls){
    return list;
  }

  public static List<Mutation> getMutations(Class cls, int lineNumber){

    List<Mutation> result = new ArrayList<Mutation>();

    for (Mutation mutation : list) {
      if(mutation.getLineNumber() == lineNumber){
        result.add(mutation);
      }        
    }
    
    if(result.size() == 0)
      result = null;

    return result;
  }

  public static int getNumberMutations(Class cls){
    return list.size();
  }

}