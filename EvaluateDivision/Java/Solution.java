import java.util.*;

class Solution {

    class Pair {
        double weight;
        String var;

        Pair(double weight, String var) {
            this.weight = weight;
            this.var = var;
        }

        public String toString(){
            return var + " -> " + weight;
        }
    }

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        
        HashMap<String, List<Pair>> adjList = new HashMap<>();
        HashSet<String> visited = new HashSet<>();
        double[] res = new double[queries.size()];

        int i = 0;
        for(List<String> equ : equations) {
            String v1 = equ.get(0);
            String v2 = equ.get(1);

            List<Pair> lst1 = adjList.getOrDefault(v1, new ArrayList<>());
            List<Pair> lst2 = adjList.getOrDefault(v2, new ArrayList<>());

            lst1.add(new Pair(values[i], v2));
            lst2.add(new Pair(1 / values[i], v1));

            adjList.putIfAbsent(v1, lst1);
            adjList.putIfAbsent(v2, lst2);
            i++;
        }

        for(Map.Entry<String, List<Pair>> e : adjList.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

        i = 0;
        for(List<String> q : queries) {
            res[i] = dfs(adjList, visited, q.get(0), q.get(1), 1);
            visited.clear();
            i++;
        }

        return res;
    }

    double dfs(HashMap<String, List<Pair>> adjList, HashSet<String> visited, String s, String t, double count) {
        System.out.println("dfs(" + s + ", " + t + ')');
        if (!adjList.containsKey(s) || !adjList.containsKey(t)) {
            System.out.println("s or t not on the adjlist");
            return -1;
        }
        if (s.equals(t)) return  1;
        
        visited.add(s);
        for(Pair edge : adjList.get(s)) {
            if (edge.var.equals(t)) {
                return count * edge.weight;
            }
            if (!visited.contains(edge.var)) {
                double ret = dfs(adjList, visited, edge.var, t, count * edge.weight);
                if (ret != -1) return ret;
            } else {
                System.out.println(edge.var + " already visited");
            }
        }
        System.out.println("no solution found returning -1");
        return -1;
    }
}