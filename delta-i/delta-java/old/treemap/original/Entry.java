package treemap.original ;


/***********************************************************
 *  Entry class
 ***********************************************************/
public class Entry {

  int key;
  private Object value;
  Entry left = null;
  Entry right = null;
  Entry parent;
  int color = TreeMap.BLACK;

  Entry(int key, Object value, Entry parent) {
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
