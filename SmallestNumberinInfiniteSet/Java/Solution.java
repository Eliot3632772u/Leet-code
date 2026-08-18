import java.util.*;

class SmallestInfiniteSet {

    PriorityQueue<Integer> queue;
    Set<Integer> set;
    int current;

    public SmallestInfiniteSet() {
        this.queue = new PriorityQueue<>();
        this.set = new HashSet<>();
        this.current = 1;
    }
    
    public int popSmallest() {
        if (!queue.isEmpty()) {
            Integer n = queue.poll();
            set.remove(n);
            return n;
        }
        return current++;
    }
    
    public void addBack(int num) {
        
        if (num < current && !set.contains(num)) {
            set.add(num);
            queue.offer(num);
        }
    }
}
