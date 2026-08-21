import java.util.ArrayList;
import java.util.List;

class Solution {
    
    List<Integer> stack = new ArrayList<>();
    List<List<Integer>> res = new ArrayList<>();
    public List<List<Integer>> combinationSum3(int k, int n) {
        
        dfs(1, k, n, 0);
        return res;
    }

    void dfs(int i, int k, int n, int sum) {
        if (k == 0) {
            if (sum == n) {
                res.add(new ArrayList(stack));
            }
            return;
        }

        for(int ii = i; ii < 10; ii++) {
            stack.add(ii);
            dfs (ii + 1, k - 1, n, ii + sum);
            stack.remove(stack.size()-1);
        }
    }
}