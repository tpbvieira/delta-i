package delta.statemask;

public class MaskPair {
  private int numTrue;
  private StateMask trueMask;
  private StateMask falseMask;

  public void setNumTrue(int num) {
    this.numTrue = num ;
  }

  public int getNumTrue() {
    return numTrue ;
  }
  
  public void setTrueMask(StateMask trueMask) {
    this.trueMask = trueMask ;
  }
  
  public StateMask getTrueMask() {
    return trueMask ;
  }
  
  public void setFalseMask(StateMask falseMask) {
    this.falseMask = falseMask ;
  }
  
  public StateMask getFalseMask() {
    return falseMask ;
  }    
}
