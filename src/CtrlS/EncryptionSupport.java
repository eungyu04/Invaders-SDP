package CtrlS;



public class EncryptionSupport {

    private static final char[] numCode =	// 0 1 2 3 4 5 6 7 8 9
            {'q','w','e','r','t','y','u','i','o','p'};

    public static String encrypt(String str) {

        String secret = "";
        for (int i = 0; i < str.length(); i++) {

            char c = (char) str.charAt(i);
            int chNum = (int) c;
            chNum = chNum - 48;
            secret += numCode[chNum];

        }
        return secret;
    }


    public static String decrypt(String str) {

        String original = "";
        for (int i = 0; i < str.length(); i++) {
            char c = (char) str.charAt(i);
            int chNum = (int) c;
            int index = 0;
            for (int j = 0; j < numCode.length; j++) {

                if (chNum == (int) numCode[j]) {
                    index = j;
                    break;
                }
            }

            index = index + 48;
            original += (char) index;
        }
        return original;
    }
}