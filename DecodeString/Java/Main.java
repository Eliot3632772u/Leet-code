public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String s = "3[a2[c]]";
        String result = solution.decodeString(s);
        System.out.println(result); // Output: accaccacc
    }
}