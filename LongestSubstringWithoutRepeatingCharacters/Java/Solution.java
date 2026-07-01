import java.util.HashSet;
import java.util.Set;

public class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();

        int maxLength = 0;
        int l = 0;

        for(int r = 0; r < s.length(); r++) {
            while (set.contains(s.charAt(r))) {
                set.remove(s.charAt(l));
                l++;
            }

            maxLength = Math.max(maxLength, r - l + 1);
            set.add(s.charAt(r));
        }

        return maxLength;
    }
}