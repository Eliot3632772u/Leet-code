class Solution {
    public String gcdOfStrings(String str1, String str2) {

        if (!(str1 + str2).contains(str2 + str1)) return "";

        String tmp;
        if (str2.length() > str1.length()) {
            tmp = str1;
            str1 = str2;
            str2 = tmp;
        }

        if (str2.length() == str1.length()) return str1;

        return gcdOfStrings(str1.substring(str2.length()), str2);
    }
}