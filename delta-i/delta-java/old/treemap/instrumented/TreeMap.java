package treemap.instrumented;
//package muexamples.treemap.instrumented;
//
//import mudelta.lib.dboolean.DeltaBoolean;
//import delta.instr.*;
//import delta.instr.deltabyte.DeltaByte;
//import delta.instr.integer.instrumented.Comparable;
//import delta.instr.integer.instrumented.*;
//import delta.util.*;
//import delta.util.merger.*;
//import delta.util.undo.*;
//
//
///***********************************************************
// *  TreeMap class
// ***********************************************************/
//public class TreeMap {
//
//  static final DeltaInt RED   = DeltaIntCommon.createConstant(0); // false, originally
//  static final DeltaInt BLACK = DeltaIntCommon.createConstant(1); // true
//
//  private transient IDeltaRefEntry root = DeltaRefEntryCommon.createConstant(null);
//  private transient DeltaInt size = DeltaIntCommon.createConstant(0);
//
//
//  /************ special constructor for merger ***********/
//  public TreeMap(ConstructorParameter par) {}
//
//
//  /*** constructor ***/
//  public TreeMap() {}
//
//
//  /*******************************************************/
//  private void incrementSize() {
//    set_size(get_size().add(1));
//  }
//
//  /*******************************************************/
//  private void decrementSize() {
//    set_size(get_size().sub(1));
//  }
//
//  /*******************************************************/
//  private IDeltaRefEntry getEntry(IDeltaRefInteger key) {
//    IDeltaRefEntry p = get_root();
//    while (!p.isNull()) {
//      DeltaInt cmp = compare(key, p.get_key());
//      if (cmp.eq(0))
//        return p;
//      else if (cmp.lt(0))
//        p = p.get_left();
//      else
//        p = p.get_right();
//    }
//    return DeltaRefEntryCommon.createConstant(null);
//  }
//
//  /*******************************************************/
//  public DeltaRef put(IDeltaRefInteger key, DeltaRef value) {
//    IDeltaRefEntry t = get_root();
//
//    if (t.isNull()) {
//      incrementSize();
//      set_root(DeltaRefEntryCommon.createConstant(new Entry(key, value, DeltaRefEntryCommon.createConstant(null))));
//      return DeltaRefCommon.createConstant(null);
//    }
//
//    while (true) {
//      DeltaInt cmp = compare(key, t.get_key());
//      if (cmp.eq(0)) {
//        return t.setValue(value);
//      } else if (cmp.lt(0)) {
//        if (!t.get_left().isNull()) {
//          t = t.get_left();
//        } else {
//          incrementSize();
//          t.set_left(DeltaRefEntryCommon.createConstant(new Entry(key, value, t)));
//          fixAfterInsertion(t.get_left());
//          return DeltaRefCommon.createConstant(null);
//        }
//      } else { // cmp > 0
//        if (!t.get_right().isNull()) {
//          t = t.get_right();
//        } else {
//          incrementSize();
//          t.set_right(DeltaRefEntryCommon.createConstant(new Entry(key, value, t)));
//          fixAfterInsertion(t.get_right());
//          return DeltaRefCommon.createConstant(null);
//        }
//      }
//    }
//  }
//
//  /*******************************************************/
//  public DeltaRef remove(IDeltaRefInteger key) {
//    IDeltaRefEntry p = getEntry(key);
//    if (p.isNull())
//      return DeltaRefCommon.createConstant(null);
//
//    DeltaRef oldValue = p.getValue();
//    deleteEntry(p);
//    return oldValue;
//  }
//
//  /*******************************************************/
//  private DeltaInt compare(IDeltaRefInteger k1, IDeltaRefInteger k2) {
//    return ((Comparable)k1).compareTo(k2);
//  }
//
//  /*******************************************************/
//  private IDeltaRefEntry successor(IDeltaRefEntry t) {
//    if (t.isNull())
//      return DeltaRefEntryCommon.createConstant(null);
//    else if (!t.get_right().isNull()) {
//      IDeltaRefEntry p = t.get_right();
//      while (!p.get_left().isNull())
//        p = p.get_left();
//      return p;
//    } else {
//      IDeltaRefEntry p = t.get_parent();
//      IDeltaRefEntry ch = t;
//      while (!p.isNull() && ch.eq(p.get_right())) {
//        ch = p;
//        p = p.get_parent();
//      }
//      return p;
//    }
//  }
//
//  /*******************************************************/
//  private static DeltaInt colorOf(IDeltaRefEntry p) {
//    return (p.isNull() ? BLACK : p.get_color());
//  }
//
//  /*******************************************************/
//  private static IDeltaRefEntry  parentOf(IDeltaRefEntry p) {
//    return (p.isNull() ? DeltaRefEntryCommon.createConstant(null) : p.get_parent());
//  }
//
//  /*******************************************************/
//  private static void setColor(IDeltaRefEntry p, DeltaInt c) {
//    if (!p.isNull()) {
//      p.set_color(c);
//    }
//  }
//
//  /*******************************************************/
//  private static IDeltaRefEntry  leftOf(IDeltaRefEntry p) {
//    return (p.isNull()) ? DeltaRefEntryCommon.createConstant(null) : p.get_left();
//  }
//
//  /*******************************************************/
//  private static IDeltaRefEntry  rightOf(IDeltaRefEntry p) {
//    return (p.isNull()) ? DeltaRefEntryCommon.createConstant(null) : p.get_right();
//  }
//
//  /*******************************************************/
//  /** From CLR **/
//  private void rotateLeft(IDeltaRefEntry p) {
//    IDeltaRefEntry r = p.get_right();
//    p.set_right(r.get_left());
//
//    if (!r.get_left().isNull()) {
//      r.get_left().set_parent(p);
//    }
//
//    r.set_parent(p.get_parent());
//
//    if (p.get_parent().isNull()) {
//      set_root(r);
//    } else if (p.get_parent().get_left().eq(p)) {
//      p.get_parent().set_left(r);
//    } else {
//      p.get_parent().set_right(r);
//    }
//
//    r.set_left(p);
//    p.set_parent(r);
//  }
//
//  /*******************************************************/
//  /** From CLR **/
//  private void rotateRight(IDeltaRefEntry p) {
//    IDeltaRefEntry l = p.get_left();
//    p.set_left(l.get_right());
//
//    if (!l.get_right().isNull()) {
//      l.get_right().set_parent(p);
//    }
//
//    l.set_parent(p.get_parent());
//
//    if (p.get_parent().isNull()) {
//      set_root(l);
//    } else if (p.get_parent().get_right().eq(p)) {
//      p.get_parent().set_right(l);
//    } else {
//      p.get_parent().set_left(l);
//    }
//
//    l.set_right(p);
//    p.set_parent(l);
//  }
//
//  /*******************************************************/
//  /** From CLR **/
//  private void fixAfterInsertion(IDeltaRefEntry x) {
//    x.set_color(RED);
//
//    while (!x.isNull() && !x.eq(get_root()) && x.get_parent().get_color().eq(RED)) {
//      if (parentOf(x).eq(leftOf(parentOf(parentOf(x))))) {
//        IDeltaRefEntry y = rightOf(parentOf(parentOf(x)));
//        if (colorOf(y).eq(RED)) {
//          setColor(parentOf(x), BLACK);
//          setColor(y, BLACK);
//          setColor(parentOf(parentOf(x)), RED);
//          x = parentOf(parentOf(x));
//        } else {
//          if (x.eq(rightOf(parentOf(x)))) {
//            x = parentOf(x);
//            rotateLeft(x);
//          }
//          setColor(parentOf(x), BLACK);
//          setColor(parentOf(parentOf(x)), RED);
//          if (!parentOf(parentOf(x)).isNull())
//            rotateRight(parentOf(parentOf(x)));
//        }
//      } else {
//        IDeltaRefEntry y = leftOf(parentOf(parentOf(x)));
//        if (colorOf(y).eq(RED)) {
//          setColor(parentOf(x), BLACK);
//          setColor(y, BLACK);
//          setColor(parentOf(parentOf(x)), RED);
//          x = parentOf(parentOf(x));
//        } else {
//          if (x.eq(leftOf(parentOf(x)))) {
//            x = parentOf(x);
//            rotateRight(x);
//          }
//          setColor(parentOf(x),  BLACK);
//          setColor(parentOf(parentOf(x)), RED);
//          if (!parentOf(parentOf(x)).isNull())
//            rotateLeft(parentOf(parentOf(x)));
//        }
//      }
//    }
//    get_root().set_color(BLACK);
//  }
//
//  /*******************************************************
//   * Delete node p, and then rebalance the tree.
//   *******************************************************/
//  private void deleteEntry(IDeltaRefEntry p) {
//    decrementSize();
//
//    // If strictly internal, copy successor's element to p and then make p
//    // point to successor.
//    if (!p.get_left().isNull() && !p.get_right().isNull()) {
//      IDeltaRefEntry s = successor (p);
//      p.set_key(s.get_key());
//      p.setValue(s.getValue());
//      p = s;
//    } // p has 2 children
//
//    // Start fixup at replacement node, if it exists.
//    IDeltaRefEntry replacement = (!p.get_left().isNull() ? p.get_left() : p.get_right());
//
//    if (!replacement.isNull()) {
//      // Link replacement to parent
//      replacement.set_parent(p.get_parent());
//      if (p.get_parent().isNull()) {
//        set_root(replacement);
//      }
//      else if (p.eq(p.get_parent().get_left())) {
//        p.get_parent().set_left(replacement);
//      }
//      else {
//        p.get_parent().set_right(replacement);
//      }
//
//      // Null out links so they are OK to use by fixAfterDeletion.
//      p.set_left(DeltaRefEntryCommon.createConstant(null));
//      p.set_right(DeltaRefEntryCommon.createConstant(null));
//      p.set_parent(DeltaRefEntryCommon.createConstant(null));
//
//      // Fix replacement
//      if (p.get_color().eq(BLACK)) {
//        fixAfterDeletion(replacement);
//      }
//    } else if (p.get_parent().isNull()) { // return if we are the only node.
//      set_root(DeltaRefEntryCommon.createConstant(null));
//    } else { //  No children. Use self as phantom replacement and unlink.
//      if (p.get_color().eq(BLACK)) {
//        fixAfterDeletion(p);
//      }
//      if (!p.get_parent().isNull()) {
//        if (p.eq(p.get_parent().get_left())) {
//          p.get_parent().set_left(DeltaRefEntryCommon.createConstant(null));
//        }
//        else if (p.eq(p.get_parent().get_right())) {
//          p.get_parent().set_right(DeltaRefEntryCommon.createConstant(null));
//        }
//        p.set_parent(DeltaRefEntryCommon.createConstant(null));
//      }
//    }
//  }
//
//  /*******************************************************/
//  /** From CLR **/
//  private void fixAfterDeletion(IDeltaRefEntry x) {
//    while (!x.eq(get_root()) && colorOf(x).eq(BLACK)) {
//      if (x.eq(leftOf(parentOf(x)))) {
//        IDeltaRefEntry sib = rightOf(parentOf(x));
//
//        if (colorOf(sib).eq(RED)) {
//          setColor(sib, BLACK);
//          setColor(parentOf(x), RED);
//          rotateLeft(parentOf(x));
//          sib = rightOf(parentOf(x));
//        }
//
//        if (colorOf(leftOf(sib)).eq(BLACK) &&
//            colorOf(rightOf(sib)).eq(BLACK)) {
//          setColor(sib,  RED);
//          x = parentOf(x);
//        } else {
//          if (colorOf(rightOf(sib)).eq(BLACK)) {
//            setColor(leftOf(sib), BLACK);
//            setColor(sib, RED);
//            rotateRight(sib);
//            sib = rightOf(parentOf(x));
//          }
//          setColor(sib, colorOf(parentOf(x)));
//          setColor(parentOf(x), BLACK);
//          setColor(rightOf(sib), BLACK);
//          rotateLeft(parentOf(x));
//          x = get_root();
//        }
//      } else { // symmetric
//        IDeltaRefEntry sib = leftOf(parentOf(x));
//
//        if (colorOf(sib).eq(RED)) {
//          setColor(sib, BLACK);
//          setColor(parentOf(x), RED);
//          rotateRight(parentOf(x));
//          sib = leftOf(parentOf(x));
//        }
//
//        if (colorOf(rightOf(sib)).eq(BLACK) &&
//            colorOf(leftOf(sib)).eq(BLACK)) {
//          setColor(sib,  RED);
//          x = parentOf(x);
//        } else {
//          if (colorOf(leftOf(sib)).eq(BLACK)) {
//            setColor(rightOf(sib), BLACK);
//            setColor(sib, RED);
//            rotateLeft(sib);
//            sib = leftOf(parentOf(x));
//          }
//          setColor(sib, colorOf(parentOf(x)));
//          setColor(parentOf(x), BLACK);
//          setColor(leftOf(sib), BLACK);
//          rotateRight(parentOf(x));
//          x = get_root();
//        }
//      }
//    }
//
//    setColor(x, BLACK);
//  }
//
//  /**************** get methods for this *****************/
//  public IDeltaRefEntry get_root() {
//    return root;
//  }
//  public DeltaInt get_size() {
//    return size;
//  }
//
//  /**************** set methods for this *****************/
//  public void set_root(IDeltaRefEntry newValue) {
//    UndoUtil.add(new Undoable() {
//      IDeltaRefEntry oldValue = root;
//      public void undo() {
//        root = oldValue;
//      }
//    });
//    root = newValue;
//  }
//  public void set_size(DeltaInt newValue) {
//    UndoUtil.add(new Undoable() {
//      DeltaInt oldValue = size;
//      public void undo() {
//        size = oldValue;
//      }
//    });
//    size = newValue;
//  }
//
//  /**************** linearization support ****************/
//  public int linTag = Constants.getLinTagDefault();
//  public void setLinTag(int value) {
//    linTag = value;
//  }
//  public int getLinTag() {
//    return linTag;
//  }
//
//  public static String[] fieldTypes = new String[]{
//    "delta.instr.DeltaIntCommon",
//    "symexamples.treemap.instrumented.DeltaRefEntryCommon"
//  };
//  public static int classNum =
//    ClassInfo.registerClass("symexamples.treemap.instrumented.TreeMap", fieldTypes);
//  public int getClassNum() {
//    return classNum;
//  }
//
//  /*********** accessible values implementation **********/
//  Object[] allFieldArray = new Object[2];
//  public Object[] getAllFieldValues() {
//    allFieldArray[0] = size;
//    allFieldArray[1] = root;
//    return allFieldArray;
//  }
//  DeltaInt[] intFieldArray = new DeltaInt[1];
//  public DeltaInt[] getIntFieldValues() {
//    intFieldArray[0] = size;
//    return intFieldArray;
//  }
//  DeltaByte[] byteFieldArray = new DeltaByte[0];
//  public DeltaByte[] getByteFieldValues() {
//    return byteFieldArray;
//  }
//  DeltaBoolean[] booleanFieldArray = new DeltaBoolean[0];
//  public DeltaBoolean[] getBooleanFieldValues() {
//    return booleanFieldArray;
//  }
//  DeltaRef[] refFieldArray = new DeltaRef[1];
//  public DeltaRef[] getRefFieldValues() {
//    refFieldArray[0] = root;
//    return refFieldArray;
//  }
//  Object[] arrayFieldArray = new Object[0];
//  public Object[] getArrayFieldValues() {
//    return arrayFieldArray;
//  }
//  public void setFieldValue(int field, Object object) {
//    if (field == 0) size = (DeltaInt) object;
//    else if (field == 1) root = (IDeltaRefEntry) object;
//    else throw new RuntimeException("invalid field number");
//  }
//
//}
