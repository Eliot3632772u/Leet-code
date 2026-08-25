public class Main {
    public static void main(String[] args) {
        int m = 3; // Number of rows
        int n = 7; // Number of columns
        Solution solution = new Solution();
        int uniquePaths = solution.uniquePaths(m, n);
        System.out.println("Number of unique paths in a " + m + " x " + n + " grid: " + uniquePaths);
    }
}
