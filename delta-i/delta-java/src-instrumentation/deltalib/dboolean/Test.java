package deltalib.dboolean;

public class Test {

  // Testa o tempo gasto para uma copia de array por clone ou por arrarcopy
  public static void main(String[] args) {
    int SIZE = 1000000;
    int[] t = new int[SIZE];

    for (int i = 0; i < t.length; i++) {
      t[i] = i;
    }

 // Por clone
    System.out.println(t + ": " + t[SIZE/2]);
    int[] u = null;
    long a = System.currentTimeMillis();   

    for (int k = 0; k < 100; k++) {
      u = t.clone();
    }
    
    System.out.println(System.currentTimeMillis() - a);        
    System.out.println(u + ": " + u[SIZE/2]);

 // Por arraycopy
    System.out.println(t + ": " + t[SIZE/2]);
    u = null;
    a = System.currentTimeMillis();   

    for (int k = 0; k < 100; k++) {
      u = new int[t.length];
      System.arraycopy(t, 0, u, 0, t.length);
    }

    System.out.println(System.currentTimeMillis() - a);        
    System.out.println(u + ": " + u[SIZE/2]);

  }

}