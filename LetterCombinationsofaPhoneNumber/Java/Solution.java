class Solution {

    Map<Character, String> numbers = new HashMap<>();
    List<String> res = new ArrayList<>();
    LinkedList<Character> stack = new LinkedList<>();

    public List<String> letterCombinations(String digits) {

        numbers.put('2', "abc");
        numbers.put('3', "def");
        numbers.put('4', "ghi");
        numbers.put('5', "jkl");
        numbers.put('6', "mno");
        numbers.put('7', "pqrs");
        numbers.put('8', "tuv");
        numbers.put('9', "wxyz");

        dfs(0, digits);

        return res;
    }

    void dfs(int i, String digits) {

        if (i == digits.length()) {
            res.add(getCombo());
            return;
        }

        String letters = numbers.get(digits.charAt(i));

        for(int in = 0; in < letters.length(); in++) {
            stack.add(letters.charAt(in));
            dfs(i + 1, digits);
            stack.pollLast();
        }
    }

    String getCombo() {
        StringBuilder s = new StringBuilder();
        for(Character c : stack) {
            s.append(c);
        }
        return s.toString();
    }
}