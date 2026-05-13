public class Main {
    public static void main(String[] args) {
        
        Solution solution = new Solution();
        int[] nums = {5, 7, 7, 8, 8, 10};
        int target = 8;
        int[] result = solution.searchRange(nums, target);
        System.out.println("Result: [" + result[0] + ", " + result[1] + "]");
    }
}
