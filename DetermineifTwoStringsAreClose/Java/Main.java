public class Main {
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        String word1 = "abc";
        String word2 = "bca";
        boolean result = solution.closeStrings(word1, word2);
        System.out.println("Are the two strings close? " + result);
    }
}
