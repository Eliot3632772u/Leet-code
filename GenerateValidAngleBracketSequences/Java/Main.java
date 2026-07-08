import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int n = 3; // Example input
        List<String> sequences = Solution.generateAngleBracketSequences(n);
        for (String seq : sequences) {
            System.out.println(seq);
        }
    }
}