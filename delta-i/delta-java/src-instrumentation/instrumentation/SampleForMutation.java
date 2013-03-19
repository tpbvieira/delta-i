package instrumentation;


public class SampleForMutation {

  public static void main(String[] args) {

    int d,v1 = 45,e,f=0,g;
    int fs; fs = 3;    
    if(v1 != fs){
      System.out.println("v1 != 0");
      if(10 >= v1){
        System.out.println("v1 >= 10");
        v1 = 10;
      }else
        System.out.println("v1 < 10");
    }else
      System.out.println("v1 == 0");
    
    int ff = 16;
    f = 14456;
    v1 += f;
    fs = f + 18;
    
    if(f != 0 || f > 100 || f > 1000){
      
    }
    
    System.getProperty("EndMutation");
  }
}