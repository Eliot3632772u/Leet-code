import java.util.*;

public class Main {

    public static void main(String[] args){
        List<List<Integer>> intervals = new ArrayList<>();
        intervals.add(Arrays.asList(1, 3));
        intervals.add(Arrays.asList(2, 6));
        intervals.add(Arrays.asList(8, 10));
        intervals.add(Arrays.asList(15, 18));

        List<List<Integer>> mergedIntervals = Solution.mergeHighDefinitionIntervals(intervals);

        System.out.println("Merged Intervals: " + mergedIntervals);
    }
}