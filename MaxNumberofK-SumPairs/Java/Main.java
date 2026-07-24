public class Main {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {1, 2, 3, 4};
        int k = 5;
        int result = solution.maxOperations(nums, k);
        System.out.println("Max number of K-sum pairs: " + result);
    }
}