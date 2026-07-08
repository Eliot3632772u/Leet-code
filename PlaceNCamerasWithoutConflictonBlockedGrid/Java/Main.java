import java.util.*;

public class Main {
    public static void main(String[] args) {
        int N = 4; // Example input
        List<List<Integer>> grid = new ArrayList<>();
        grid.add(Arrays.asList(0, 0, 0, 0));
        grid.add(Arrays.asList(0, 1, 0, 0));
        grid.add(Arrays.asList(0, 0, 1, 0));
        grid.add(Arrays.asList(0, 0, 0, 0));

        boolean canPlace = Solution.canPlaceSecurityCameras(N, grid);
        System.out.println("Can place security cameras: " + canPlace);
    }
}