public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String str1 = "ABCABC";
        String str2 = "ABC";
        String result = solution.gcdOfStrings(str1, str2);
        System.out.println("The greatest common divisor of strings is: " + result);
    }
}