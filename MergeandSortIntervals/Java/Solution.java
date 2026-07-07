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

public class Solution {

    public static List<List<Integer>> mergeHighDefinitionIntervals(List<List<Integer>> intervals) {
        
        if (intervals == null || intervals.size() < 2) return intervals;
        
        List<List<Integer>> result = new ArrayList<>();
        
        intervals.sort((a, b) -> a.get(0) - b.get(0));
        
        List<Integer> tmp = intervals.get(0);
        
        for(var lst : intervals) {
            
            if (tmp.get(1) >= lst.get(0)) {
                if (tmp.get(1) < lst.get(1)) tmp.set(1, lst.get(1));
            } else {
                result.add(tmp);
                tmp = lst;
            }
        }
        
        result.add(tmp);
        
        return result;
    }

}