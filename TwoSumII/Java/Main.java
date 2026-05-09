public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] numbers = {2, 7, 11, 15};
        int target = 9;
        int[] result = solution.twoSum(numbers, target);
        System.out.println("Output: [" + result[0] + ", " + result[1] + "]");
    }
}
