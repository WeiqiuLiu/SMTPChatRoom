package VMail.Encryption;

import java.util.HashMap;
import java.util.Map;


public class BASE64Util {
    private static final Map<Integer, Character> base64CharMap = new HashMap<>();
    private static final String base64CharString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private static BASE64Util instance;

    private BASE64Util() {
        for (int i = 0; i < base64CharString.length(); i++) {
            char c = base64CharString.charAt(i);
            base64CharMap.put(new Integer(i), new Character(c));
        }
    }

    public static BASE64Util getInstance() {
        if (instance == null) {
            synchronized (BASE64Util.class) {
                if (instance == null) {
                    instance = new BASE64Util();
                }
            }
        }
        return instance;
    }

    /**
     * This method is used to encode a normal string to base64 string @param
     * origin The String to be encoded @return The String after encoded.
     */
    public String encode(String origin) {
        if (origin == null) {
            return null;
        }
        if (origin.length() == 0) {
            return "";
        }
        int length = origin.length();
        String binaryString = "";
        for (int i = 0; i < length; i++) {
            int ascii = origin.charAt(i);
            String binaryCharString = Integer.toBinaryString(ascii);
            while (binaryCharString.length() < 8) {
                binaryCharString = "0" + binaryCharString;
            }
            binaryString += binaryCharString;
        }

        int beginIndex = 0;
        int endIndex = beginIndex + 6;
        String base64BinaryString = "";
        String charString = "";
        while ((base64BinaryString = binaryString.substring(beginIndex, endIndex)).length() > 0) {
            while (base64BinaryString.length() < 6) {
                base64BinaryString += "0";
            }
            int index = Integer.parseInt(base64BinaryString, 2);
            char base64Char = base64CharMap.get(index);
            charString = charString + base64Char;
            beginIndex += 6;
            endIndex += 6;
            if (endIndex >= binaryString.length()) {
                endIndex = binaryString.length();
            }
            if (endIndex < beginIndex) {
                break;
            }
        }
        if (length % 3 == 2) {
            charString += "=";
        }
        if (length % 3 == 1) {
            charString += "==";
        }
        return charString;
    }

    public String decode(String encodedString) {
        if (encodedString == null) {
            return null;
        }
        if (encodedString.length() == 0) {
            return "";
        }
        String origin = null;
        String equals = null;
        System.out.println(encodedString.indexOf("="));
        if (encodedString.indexOf("=") != -1) {
            origin = encodedString.substring(0, encodedString.indexOf("="));
            equals = encodedString.substring(encodedString.indexOf("="));
        } else {
            origin = encodedString.substring(0, encodedString.length());
            equals = "";
        }
        String binaryString = "";
        for (int i = 0; i < origin.length(); i++) {
            char c = origin.charAt(i);
            int ascii = base64CharString.indexOf(c);
            String binaryCharString = Integer.toBinaryString(ascii);
            while (binaryCharString.length() < 6) {
                binaryCharString = "0" + binaryCharString;
            }
            binaryString += binaryCharString;
        }
        if (equals.length() == 1) {
            binaryString = binaryString.substring(0, binaryString.length() - 2);
        }
        if (equals.length() == 2) {
            binaryString = binaryString.substring(0, binaryString.length() - 4);
        }

        if (equals.length() == 0) {
            binaryString = binaryString.substring(0, binaryString.length() - 0);
        }

        String charString = "";
        String resultString = "";
        int beginIndex = 0;
        int endIndex = beginIndex + 8;
        while ((charString = binaryString.substring(beginIndex, endIndex)).length() == 8) {
            int ascii = Integer.parseInt(charString, 2);
            resultString += (char) ascii;
            beginIndex += 8;
            endIndex += 8;
            if (endIndex > binaryString.length()) {
                break;
            }
        }
        return resultString;
    }
}