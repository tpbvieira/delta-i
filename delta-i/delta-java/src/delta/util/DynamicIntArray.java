//
// Copyright (C) 2005 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package delta.util ;

/**
 * simplistic dynamic array that differentiates from ArrayList by
 *  - using chunks instead of exponential growth, thus efficiently dealing
 *    with sparse arrays
 *  - managing primitive 'int' types, i.e. not requiring box objects
 *
 * the motivation for this class is memory optimization, i.e. space efficient
 * storage of potentially huge arrays without good a-priori size guesses
 *
 * the API of this class is between a primitive array and a AbstractList. It's
 * not a Collection implementation because it handles primitive types, but the
 * API could be extended to support iterators and the like.
 *
 * NOTE: like standard Collection implementations/arrays, this class is not
 * synchronized
 */
public class DynamicIntArray {
  final static int DEFAULT_CHUNKSIZE = 256;
  final static int INIT_CHUNKS = 16;

  int chunkSize;  /** our allocation sizes */
  int[][] data;   /** the real data */
  int length;     /** max set element index +1 */

  public DynamicIntArray () {
    this( DEFAULT_CHUNKSIZE);
  }

  public DynamicIntArray (int chunkSize) {
    this.chunkSize = chunkSize;

    data = new int[INIT_CHUNKS][];
  }

  public boolean isEmpty () {
    return (length == 0);
  }

  /**
   * Ensure that the given index is valid.
   */
  public void grow(int index) {
    if (index >= length) {
      int i = index / chunkSize;
      int j = index % chunkSize;

      if (i >= data.length) {
        // grow the meta-array by 50%
        int new_size = (i * 3) / 2 + 1;
        int[][] newChunk = new int[new_size][];
        System.arraycopy(data, 0, newChunk, 0, data.length);
        data = newChunk;
      }
      length = index + 1;
    }
  }

  public int get (int index) {
    if (index >= length) {
      throw new IndexOutOfBoundsException("Index " + index +
                                            " is outside of 0.." +
                                            (length - 1));
    }
    int i = index / chunkSize;
    int j = index % chunkSize;

    if (data[i] == null) {
      return 0;
    } else {
      return data[i][j];
    }
  }

  public void set (int index, int value) {
    int i = index / chunkSize;
    int j = index % chunkSize;

    grow(index);
    // fill in the meta-array, if necessary
    if (data[i] == null) {
      data[i] = new int[chunkSize];
    }
    data[i][j] = value;
  }

  public int size() {
    return length;
  }

    /*************************************************
     * added this to support backtracking of state on 
     * DFS visit. -Marcelo
     ************************************************/
    public void setSize(int size) {
        this.length = size;
    }

  public String toString() {
    int i;
    StringBuffer sb = new StringBuffer(length*4);

    sb.append('{');
    int l = length-1;
    for (i=0; i<l; i++) {
      sb.append(get(i));
      sb.append(',');
    }
    sb.append(get(i));
    sb.append('}');

    return sb.toString();
  }

  public int[] toArray (int[] buf) {
      if (buf.length < length) {
          buf = new int[length];
      }
      for (int i=0; i<length; i++) {
          buf[i] = get(i);
      }
      
      return buf;
  }
  
  public void append(int val) {
      this.set(size(), val);
  }
  
  public boolean equals(Object obj){
    boolean result = true;
    DynamicIntArray tmp = (DynamicIntArray)obj;
    
    result = result && (this.chunkSize == tmp.chunkSize);
    result = result && (this.length == tmp.length);
    for (int i = 0; i < this.data.length; i++) {
      for (int j = 0; j < this.data[0].length; j++) {
        result = result && (this.data[i][j] == tmp.data[i][j]);
      }
    }
    
    return result;
  }
  
  /**************************** debug & test ************
  public void dump () {
    int i, j;
    for (i=0; i<chunk.length; i++) {
      System.out.print( "[" + i + "]: ");
      if (chunk[i] != null) {
        System.out.print("{");
        int l = chunk[i].length-1;
        for (j=0; j<l; j++) {
          System.out.print(chunk[i][j]);
          System.out.print(',');
        }
        System.out.print( chunk[i][j]);
        System.out.println("}");
      } else {
        System.out.println( "null");
      }
    }
  }

  public static void main (String[] args) {
    int i;
    DynamicIntArray a = new DynamicIntArray(8);

    a.set(0, 42);
    a.set(13,13);
    a.set(24, 42);

    System.out.println( "--------- " + a.size());
    System.out.println(a);
    System.out.println(); System.out.println();
    a.dump();
  }
   ***************************** end debug & test *********/
}