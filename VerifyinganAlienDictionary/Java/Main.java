public class Main {
    public static void main(String[] args) {
        
        Solution solution = new Solution();
        String[] words = {"hello", "leetcode"};
        String order = "hlabcdefgijkmnopqrstuvwxyz";
        boolean result = solution.isAlienSorted(words, order);
        System.out.println("Result for words: " + String.join(", ", words) + " and order: " + order + " is: " + result);
    }
}