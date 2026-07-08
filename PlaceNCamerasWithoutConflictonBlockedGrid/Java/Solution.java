import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Solution {

    static HashSet<Integer> colsSet = new HashSet<>();
    static HashSet<Integer> pos = new HashSet<>();
    static HashSet<Integer> neg = new HashSet<>();
    static List<List<Integer>> grid;
    static int n;
    
    public static boolean canPlaceSecurityCameras(int N, List<List<Integer>> grids) {
    // Write your code here
        grid = grids;
        n = N;
        return dfs(0);
    }
    
    public static boolean dfs(int r) {
        if (r == n) return true;
        
        for(int c = 0; c < n; c++) {
            
            if (grid.get(r).get(c) == 1) continue;
            if (colsSet.contains(c) || pos.contains(r - c) || neg.contains(r + c)) continue;
            
            colsSet.add(c);
            pos.add(r - c);
            neg.add(r + c);
            
            if (dfs(r + 1)) return true;
            
            colsSet.remove(c);
            pos.remove(r - c);
            neg.remove(r + c);
        }
        
        return false;
    }

}