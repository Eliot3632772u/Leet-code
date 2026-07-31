class Solution {
    public String decodeString(String s) {
        
        return decode(s, new int[]{0});
    }
    public String decode(String s, int i[]) {
        StringBuilder res = new StringBuilder();
        while (i[0] < s.length() && s.charAt(i[0]) != ']') {
            if (Character.isDigit(s.charAt(i[0]))) {
                int count = 0;
                while (Character.isDigit(s.charAt(i[0]))) {
                    count = count * 10 + (s.charAt(i[0]) - '0');
                    i[0]++;
                }
                i[0]++;
                String chunk = decode(s, i);
                i[0]++;
                for(int j = 0; j < count; j++) res.append(chunk);
            } else {
                res.append(s.charAt(i[0]));
                i[0]++;
            }
        }
        return res.toString();
    }
}