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

    static List<String> result = new ArrayList<>();

    public static List<String> generateAngleBracketSequences(int n) {
    
        bt(0, 0, n, "");
        return result;
    }
    
    public static void bt(int open, int close, int n, String pattern) {
        
        if (open == n && close == n) {
            result.add(pattern);
            return;
        }
        
        if (open < n) {
            bt(open + 1, close, n, pattern + "<");
        }
        
        if (close < n && close < open) {
            bt(open, close + 1, n, pattern + ">");
        }
    }

}