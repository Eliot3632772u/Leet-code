public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 2, 3, 4, 5};
        boolean result = solution.increasingTriplet(nums);
        System.out.println("Does the array contain an increasing triplet subsequence? " + result);
    }
}
