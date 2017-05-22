package cn.tzq.spider.util;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by igotti on 16-3-30.
 */
public class Strings {

    public static final int CR = 13; // <US-ASCII CR, carriage return (13)>
    public static final int LF = 10; // <US-ASCII LF, linefeed (10)>
    public static final int SP = 32; // <US-ASCII SP, space (32)>
    public static final int HT = 9;  // <US-ASCII HT, horizontal-tab (9)>
    public static final String EMPTY = "";
    public static final String HEX_STRING = "0123456789abcdef";
    public static final char[] HEX_DIGITS = HEX_STRING.toCharArray();
//    public static final Pattern EMAIL = Pattern.compile("^[_a-zA-Z\\d\\-\\.]+@[_a-zA-Z\\d\\-]+(\\.[_a-zA-Z\\d\\-]+)+$",Pattern.CASE_INSENSITIVE);
//    public static final Pattern JSON = Pattern.compile("^\\s*[\\{\\[].*?[\\}\\]]\\s*$");
//    public static final Pattern JSON_OBJECT = Pattern.compile("^\\s*\\{.*?\\}\\s*$");
//    public static final Pattern JSON_ARRAY = Pattern.compile("^\\s*\\[.*?\\]\\s*$");


    /**
     * 字符串是否为空,不为空返回true
     *
     * @param s
     * @return 如果字符串为空或者trim之后等于空字符串，则返回false；否则返回true
     */
    public static boolean notEmpty(final String s) {
        return !(s == null || s.trim().equals(""));
    }

    /**
     * 字符序列长度是否大于0
     *
     * @param str
     * @return 长度大于0返回true，否则返回false
     */
    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * 字符串长度是否大于0
     *
     * @param str
     * @return 长度大于0返回true，否则返回false
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    /**
     * 字符序列是否包含有内容
     *
     * @param str
     * @return 如果字符序列包含空白之外的字符，则返回true，否则返回false
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * @param l
     * @return
     */
    public static String toHexString(int[] l) {
        String m = "";
        for (int j = 0; j < l.length * 4; j++) {
            m += HEX_DIGITS[(l[j >> 2] >> ((3 - j % 4) * 8 + 4)) & 15] + HEX_DIGITS[(l[j >> 2] >> ((3 - j % 4) * 8)) & 15];
        }
        return m;
    }

    /**
     * @param hexstr
     * @return
     */
    public static byte[] fromHexString(String hexstr) {
        hexstr = Strings.notEmpty(hexstr) ? hexstr.toLowerCase() : Strings.EMPTY;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(hexstr.length() / 2);
        for (int i = 0; i < hexstr.length(); i = i + 2) {
            baos.write((HEX_STRING.indexOf(hexstr.charAt(i)) << 4 | HEX_STRING.indexOf(hexstr.charAt(i + 1))));
        }
        return baos.toByteArray();
    }

    /**
     * 获取两个字符串的最长公共子序列，不要求连续，按顺序出现即可
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String LCS(String str1, String str2) {
        int l1 = str1.length(), l2 = str2.length();
        int[][] arr = new int[l1 + 1][l2 + 1];

        for (int i = l1 - 1; i >= 0; i--) {
            for (int j = l2 - 1; j >= 0; j--) {
                if (str1.charAt(i) == str2.charAt(j))
                    arr[i][j] = arr[i + 1][j + 1] + 1;
                else
                    arr[i][j] = Math.max(arr[i + 1][j], arr[i][j + 1]);
            }
        }

        int i = 0, j = 0;
        StringBuffer sb = new StringBuffer();
        while (i < l1 && j < l2) {
            if (str1.charAt(i) == str2.charAt(j)) {
                sb.append(str1.charAt(i));
                i++;
                j++;
            } else if (arr[i + 1][j] >= arr[i][j + 1])
                i++;
            else
                j++;
        }
        return sb.toString();
    }


    public static Set<String> lcs(String str1, String str2, Set<String> sequences) {
        if (str1.isEmpty() || str2.isEmpty()) return sequences;
        if (str1.charAt(0) == str2.charAt(0)) {
            Set<String> suffixes = lcs(str1.substring(1), str2.substring(1), new TreeSet<>());
            sequences.addAll(suffixes.stream().map(suffix -> str1.charAt(0) + suffix).collect(Collectors.toList()));
        } else {
            sequences.addAll(lcs(str1.substring(1), str2, new TreeSet<>()));
            sequences.addAll(lcs(str1, str2.substring(1), new TreeSet<>()));
        }
        return sequences;
    }

}
