package treemap.original ;

/***********************************************************
 *  TreeMap class
 ***********************************************************/
public class TreeMap {

  static final int RED   = 0 ; // false, originally
  static final int BLACK = 1 ; // true

  private transient Entry root = null;
  private transient int size = 0;

  /*** constructor ***/
  public TreeMap() {  }


  /*******************************************************/
  private void incrementSize() {
    size = size + 1;
  }

  /*******************************************************/
  private void decrementSize() {
    size = size - 1;
  }

  /*******************************************************/
  private Entry getEntry(int key) {
    Entry p = root;
    while (p != null) {
      int cmp = compare(key, p.key);
      if (cmp == 0)
        return p;
      else if (cmp < 0)
        p = p.left;
      else
        p = p.right;
    }
    return null;
  }

  /*******************************************************/
  public Object put(int key, Object value) {
    Entry t = root;

    if (t == null) {
      incrementSize();
      root = new Entry(key, value, null);
      return null;
    }

    while (true) {
      int cmp = compare(key, t.key);
      if (cmp == 0) {
        return t.setValue(value);
      } else if (cmp < 0) {
        if (t.left != null) {
          t = t.left;
        } else {
          incrementSize();
          t.left = new Entry(key, value, t);
          fixAfterInsertion(t.left);
          return null;
        }
      } else { // cmp > 0
        if (t.right != null) {
          t = t.right;
        } else {
          incrementSize();
          t.right = new Entry(key, value, t);
          fixAfterInsertion(t.right);
          return null;
        }
      }
    }
  }

  /*******************************************************/
  public Object remove(int key) {
    Entry p = getEntry(key);
    if (p == null)
      return null;

    Object oldValue = p.getValue();
    deleteEntry(p);
    return oldValue;
  }

  /*******************************************************/
  private int compare(int thisVal, int anotherVal) {
    return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
  }

  /*******************************************************/
  private Entry successor(Entry t) {
    if (t == null)
      return null;
    else if (t.right != null) {
      Entry p = t.right;
      while (p.left != null)
        p = p.left;
      return p;
    } else {
      Entry p = t.parent ;
      Entry ch = t;
      while (p != null && ch == p.right) {
        ch = p;
        p = p.parent ;
      }
      return p;
    }
  }

  /*******************************************************/
  private static int colorOf(Entry p) {
    return (p == null ? BLACK : p.color);
  }

  /*******************************************************/
  private static Entry  parentOf(Entry p) {
    return (p == null ? null: p.parent);
  }

  /*******************************************************/
  private static void setColor(Entry p, int c) {
    if (p != null) {
      p.color = c;
    }
  }

  /*******************************************************/
  private static Entry  leftOf(Entry p) {
    return (p == null)? null: p.left ;
  }

  /*******************************************************/
  private static Entry  rightOf(Entry p) {
    return (p == null)? null: p.right ;
  }

  /*******************************************************/
  /** From CLR **/
  private void rotateLeft(Entry p) {
    Entry r = p.right ;
    p.right = r.left ;

    if (r.left != null) {
      r.left.parent = p ;
    }

    r.parent = p.parent ;

    if (p.parent == null) {
      root = r;
    } else if (p.parent.left == p) {
      p.parent.left = r ;
    } else {
      p.parent.right = r ;
    }

    r.left = p ;
    p.parent = r;
  }

  /*******************************************************/
  /** From CLR **/
  private void rotateRight(Entry p) {
    Entry l = p.left ;
    p.left = l.right ;

    if (l.right != null) {
      l.right.parent = p;
    }

    l.parent = p.parent;

    if (p.parent == null) {
      root = l ;
    } else if (p.parent.right == p) {
      p.parent.right = l ;
    } else {
      p.parent.left = l ;
    }

    l.right = p ;
    p.parent = l ;
  }

  /*******************************************************/
  /** From CLR **/
  private void fixAfterInsertion(Entry x) {
    x.color = RED;

    while (x != null && x != root && x.parent.color == RED) {
      if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
        Entry y = rightOf(parentOf(parentOf(x)));
        if (colorOf(y) == RED) {
          setColor(parentOf(x), BLACK);
          setColor(y, BLACK);
          setColor(parentOf(parentOf(x)), RED);
          x = parentOf(parentOf(x));
        } else {
          if (x == rightOf(parentOf(x))) {
            x = parentOf(x);
            rotateLeft(x);
          }
          setColor(parentOf(x), BLACK);
          setColor(parentOf(parentOf(x)), RED);
          if (parentOf(parentOf(x)) != null)
            rotateRight(parentOf(parentOf(x)));
        }
      } else {
        Entry y = leftOf(parentOf(parentOf(x)));
        if (colorOf(y) == RED) {
          setColor(parentOf(x), BLACK);
          setColor(y, BLACK);
          setColor(parentOf(parentOf(x)), RED);
          x = parentOf(parentOf(x));
        } else {
          if (x == leftOf(parentOf(x))) {
            x = parentOf(x);
            rotateRight(x);
          }
          setColor(parentOf(x),  BLACK);
          setColor(parentOf(parentOf(x)), RED);
          if (parentOf(parentOf(x)) != null)
            rotateLeft(parentOf(parentOf(x)));
        }
      }
    }
    root.color = BLACK;
  }

  /*******************************************************
   * Delete node p, and then rebalance the tree.
   *******************************************************/
  private void deleteEntry(Entry p) {
    decrementSize();

    // If strictly internal, copy successor's element to p and then make p
    // point to successor.
    if (p.left != null && p.right != null) {
      Entry s = successor (p);
      p.key = s.key;
      p.setValue(s.getValue());
      p = s;
    } // p has 2 children

    // Start fixup at replacement node, if it exists.
    Entry replacement = (p.left != null ? p.left : p.right);

    if (replacement != null) {
      // Link replacement to parent
      replacement.parent = p.parent;
      if (p.parent == null) {
        root = replacement ;
      }
      else if (p == p.parent.left) {
        p.parent.left = replacement ;
      }
      else {
        p.parent.right = replacement ;
      }

      // Null out links so they are OK to use by fixAfterDeletion.
      p.left = null ;
      p.right = null ;
      p.parent = null ;

      // Fix replacement
      if (p.color == BLACK)
        fixAfterDeletion(replacement);
    } else if (p.parent == null) { // return if we are the only node.
      root = null;
    } else { //  No children. Use self as phantom replacement and unlink.
      if (p.color == BLACK) {
        fixAfterDeletion(p);
      }
      if (p.parent != null) {
        if (p == p.parent.left) {
          p.parent.left = null;
        }
        else if (p == p.parent.right) {
          p.parent.right = null;
        }
        p.parent = null;
      }
    }
  }

  /*******************************************************/
  /** From CLR **/
  private void fixAfterDeletion(Entry x) {
    while (x != root && colorOf(x) == BLACK) {
      if (x == leftOf(parentOf(x))) {
        Entry sib = rightOf(parentOf(x));

        if (colorOf(sib) == RED) {
          setColor(sib, BLACK);
          setColor(parentOf(x), RED);
          rotateLeft(parentOf(x));
          sib = rightOf(parentOf(x));
        }

        if (colorOf(leftOf(sib))  == BLACK &&
            colorOf(rightOf(sib)) == BLACK) {
          setColor(sib,  RED);
          x = parentOf(x);
        } else {
          if (colorOf(rightOf(sib)) == BLACK) {
            setColor(leftOf(sib), BLACK);
            setColor(sib, RED);
            rotateRight(sib);
            sib = rightOf(parentOf(x));
          }
          setColor(sib, colorOf(parentOf(x)));
          setColor(parentOf(x), BLACK);
          setColor(rightOf(sib), BLACK);
          rotateLeft(parentOf(x));
          x = root ;
        }
      } else { // symmetric
        Entry sib = leftOf(parentOf(x));

      if (colorOf(sib) == RED) {
        setColor(sib, BLACK);
        setColor(parentOf(x), RED);
        rotateRight(parentOf(x));
        sib = leftOf(parentOf(x));
      }

      if (colorOf(rightOf(sib)) == BLACK &&
          colorOf(leftOf(sib)) == BLACK) {
        setColor(sib,  RED);
        x = parentOf(x);
      } else {
        if (colorOf(leftOf(sib)) == BLACK) {
          setColor(rightOf(sib), BLACK);
          setColor(sib, RED);
          rotateLeft(sib);
          sib = leftOf(parentOf(x));
        }
        setColor(sib, colorOf(parentOf(x)));
        setColor(parentOf(x), BLACK);
        setColor(leftOf(sib), BLACK);
        rotateRight(parentOf(x));
        x = root;
      }
      }
    }

    setColor(x, BLACK);
  }

}
