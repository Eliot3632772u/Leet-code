import java.util.LinkedHashMap;

public class Solution {

    LinkedHashMap<Integer, Integer> map;
    final int capacity;
    int size;

    public Solution(int capacity) {
       this.map = new LinkedHashMap<>();
       this.capacity = capacity;
       this.size = 0;
    }
    
    public int get(int key) {
        
        if (!map.containsKey(key)) return -1;
        int value = map.remove(key);
        map.put(key, value);
        return value;
    }
    
    public void put(int key, int value) {
        
        if (map.containsKey(key)) {
            map.remove(key);
            map.put(key, value);
            return;
        }

        if (size == capacity) {
            var it = map.entrySet().iterator();
            it.next();
            it.remove();
            size--;
        }

        map.put(key, value);
        size++;
    }
}