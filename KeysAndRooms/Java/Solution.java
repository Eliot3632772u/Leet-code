import java.util.*;

class Solution {

    HashSet<Integer> visited = new HashSet<>();
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {

        dfs(rooms, 0);
        return rooms.size() == visited.size();
    }

    void dfs(List<List<Integer>> rooms, int room) {
        if (visited.contains(room)) return;
        visited.add(room);
        List<Integer> keys = rooms.get(room);
        for(int key : keys) {
            dfs(rooms, key);
        }
    }
}