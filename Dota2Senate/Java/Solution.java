import java.util.LinkedList;

class Solution {
    public String predictPartyVictory(String senate) {
        
        LinkedList<Integer> radiant = new LinkedList<>();
        LinkedList<Integer> dire = new LinkedList<>();

        for(int i = 0; i < senate.length(); i++) {
            if (senate.charAt(i) == 'R') radiant.add(i);
            else dire.add(i);
        }

        while (radiant.size() > 0 && dire.size() > 0) {
            int r = radiant.pop();
            int d = dire.pop();

            if (r < d) radiant.add(r + senate.length());
            else dire.add(d + senate.length());
        }

        if (radiant.isEmpty()) return "Dire";
        return "Radiant";
    }
}