package treemap.instrumented;
//package muexamples.treemap.instrumented;
//
//import mudelta.lib.dboolean.DeltaBoolean;
//import delta.instr.*;
//import delta.instr.deltabyte.DeltaByte;
//import delta.instr.integer.instrumented.*;
//import delta.util.*;
//import delta.util.merger.*;
//import delta.util.undo.*;
//
//
///***********************************************************
// *  Entry class
// ***********************************************************/
//public class Entry {
//
//    IDeltaRefInteger key = DeltaRefIntegerCommon.createConstant(null);
//    private DeltaRef value = DeltaRefCommon.createConstant(null);
//    IDeltaRefEntry left = DeltaRefEntryCommon.createConstant(null);
//    IDeltaRefEntry right = DeltaRefEntryCommon.createConstant(null);
//    IDeltaRefEntry parent = DeltaRefEntryCommon.createConstant(null);
//    DeltaInt color = TreeMap.BLACK;
//
//    /************ special constructor for merger ***********/
//    public Entry(ConstructorParameter par) {}
//
//
//    /****************** no-arg constructor *****************/
//    public Entry() {}
//
//
//    Entry(IDeltaRefInteger key, DeltaRef value, IDeltaRefEntry parent) {
//        this.key = key;
//        this.value = value;
//        this.parent = parent;
//    }
//
//    /*******************************************************/
//    public DeltaRef getValue() {
//        return value;
//    }
//
//    /*******************************************************/
//    public DeltaRef setValue(DeltaRef value) {
//        DeltaRef oldValue = this.get_value();
//        this.set_value(value);
//        return oldValue;
//    }
//
//    /**************** get methods for this *****************/
//    public IDeltaRefInteger get_key() {
//        return key;
//    }
//    public DeltaRef get_value() {
//        return value;
//    }
//    public IDeltaRefEntry get_left() {
//        return left;
//    }
//    public IDeltaRefEntry get_right() {
//        return right;
//    }
//    public IDeltaRefEntry get_parent() {
//        return parent;
//    }
//    public DeltaInt get_color() {
//        return color;
//    }
//
//    /**************** set methods for this *****************/
//    public void set_key(IDeltaRefInteger newValue) {
//        UndoUtil.add(new Undoable() {
//                         IDeltaRefInteger oldValue = key;
//                         public void undo() {
//                             key = oldValue;
//                         }
//                     });
//        key = newValue;
//    }
//    public void set_value(DeltaRef newValue) {
//        UndoUtil.add(new Undoable() {
//                         DeltaRef oldValue = value;
//                         public void undo() {
//                             value = oldValue;
//                         }
//                     });
//        value = newValue;
//    }
//    public void set_left(IDeltaRefEntry newValue) {
//        UndoUtil.add(new Undoable() {
//                         IDeltaRefEntry oldValue = left;
//                         public void undo() {
//                             left = oldValue;
//                         }
//                     });
//        left = newValue;
//    }
//    public void set_right(IDeltaRefEntry newValue) {
//        UndoUtil.add(new Undoable() {
//                         IDeltaRefEntry oldValue = right;
//                         public void undo() {
//                             right = oldValue;
//                         }
//                     });
//        right = newValue;
//    }
//    public void set_parent(IDeltaRefEntry newValue) {
//        UndoUtil.add(new Undoable() {
//                         IDeltaRefEntry oldValue = parent;
//                         public void undo() {
//                             parent = oldValue;
//                         }
//                     });
//        parent = newValue;
//    }
//    public void set_color(DeltaInt newValue) {
//        UndoUtil.add(new Undoable() {
//                         DeltaInt oldValue = color;
//                         public void undo() {
//                             color = oldValue;
//                         }
//                     });
//        color = newValue;
//    }
//
//}