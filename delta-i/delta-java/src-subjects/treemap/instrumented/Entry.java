package treemap.instrumented;

import deltalib.General;
import deltalib.dint.DeltaInt;


/***********************************************************
 *  Entry class
 ***********************************************************/
public class Entry {

  DeltaInt key = General.ZERO;
  private Object value;
  Entry left = null;
  Entry right = null;
  Entry parent;
  DeltaInt color = TreeMap.BLACK;

  Entry(DeltaInt key, Object value, Entry parent) {
    this.key = key;
    this.value = value;
    this.parent = parent;
  }


  /*******************************************************/
  public Object getValue() {
    return value ;
  }

  /*******************************************************/
  public Object setValue(Object value) {
    Object oldValue = this.value;
    this.value = value ;
    return oldValue;
  }
}
