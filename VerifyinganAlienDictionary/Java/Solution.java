class Solution {
    public char[] charMap = new char[26];
    public boolean isAlienSorted(String[] words, String order) {
        
        for(int i = 0; i < order.length(); i++) {
            charMap[(int) (order.charAt(i) - 'a')] = (char)i;
        }

        for(int i = 1; i < words.length; i++) {
            if (compare(words[i - 1], words[i]) == false) return false;
        }

        return true;
    }

    public boolean compare(String s1, String s2) {

        for(int i = 0; i < s1.length() && i < s2.length(); i++) {
            if (charMap[(int) (s1.charAt(i) - 'a')] == charMap[(int) (s2.charAt(i) - 'a')]) continue;
            if (charMap[(int) (s1.charAt(i) - 'a')] > charMap[(int) (s2.charAt(i) - 'a')]) return false;
            if (charMap[(int) (s1.charAt(i) - 'a')] < charMap[(int) (s2.charAt(i) - 'a')]) return true;
        }

        if (s1.length() > s2.length()) return false;

        return true;
    }
}