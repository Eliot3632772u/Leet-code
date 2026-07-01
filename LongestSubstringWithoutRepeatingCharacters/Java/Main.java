public class Main {

    public static void main(String[] args) {
        Solution solution = new Solution();
        String input = "abcabcbb";
        int result = solution.lengthOfLongestSubstring(input);
        System.out.println("Length of longest substring without repeating characters: " + result);
    }
}