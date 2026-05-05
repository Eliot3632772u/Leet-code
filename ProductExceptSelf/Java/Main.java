public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] nums1 = {1, 2, 3, 4};
        int[] result1 = solution.productExceptSelf(nums1);
        System.out.println(Arrays.toString(result1)); // Output: [24, 12, 8, 6]

        int[] nums2 = {0, 0};
        int[] result2 = solution.productExceptSelf(nums2);
        System.out.println(Arrays.toString(result2)); // Output: [0, 0]

        int[] nums3 = {1, 2, 3, 0};
        int[] result3 = solution.productExceptSelf(nums3);
        System.out.println(Arrays.toString(result3)); // Output: [0, 0, 0, 6]
    }
}