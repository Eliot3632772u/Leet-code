public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 7, 3, 6, 5, 6};
        int pivotIndex = solution.pivotIndex(nums);
        System.out.println("Pivot index: " + pivotIndex);
    }
}
