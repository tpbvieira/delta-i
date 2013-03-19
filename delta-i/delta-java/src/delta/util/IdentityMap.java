package delta.util ;

import java.util.LinkedList;
import java.util.List;

public class IdentityMap { // only adds stuff

    public static class Entry {
        Object key ;
        Object value ;
        public Entry(Object key, Object value) {
            this.key = key ;
            this.value = value ;
        }
        public Object getKey() {
            return key ;
        }
        public Object getValue() {
            return value ;
        }
        public void setValue(Object value) {
            this.value = value ;
        }
    }

    private List map ;
    public IdentityMap() {
        map = new LinkedList() ;
    }
    
    public void put(Object key, Object value) {
        Entry record = getInternal(key) ;
        if (record == null) {
            record = new Entry(key, value) ;
            map.add(record) ;
        }
        record.setValue(value) ; // reassign the mapping
    }

    /*
    public void putInto(Object key, Object value) {
        Entry record = getInternal(key) ;
        LinkedList listStates = new LinkedList() ;
        if (record == null) {
            record = new Entry(key, listStates) ;
            map.add(record) ;
        }
        // there is one entry for key in the map
        // and it is associated with listStates
        listStates.add(value) ;
    }
    */

    private Entry getInternal(Object key) {
        for (int i = 0 ; i < map.size() ; i++) {
            Entry record = (Entry) map.get(i) ;
            if (record.getKey() == key) {
                return record ;
            }
        }
        return null ;
    }

    public Object get(Object key) {
        Object result = null ;
        Entry record = getInternal(key) ;
        if (record != null) {
            result = record.getValue() ;
        }
        return result ;
    }

    public boolean containsKey(Object key) {
        return get(key) != null ;
    }

    public int size() {
        return map.size() ;
    }

    // oops... this is no longer a map
    public Entry get(int i) {
        return (Entry) map.get(i) ;
    }

}
