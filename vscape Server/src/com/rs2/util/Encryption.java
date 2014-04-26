package com.rs2.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption
{

    public static int[] range(int s, int e)
    {
        int values[] = new int[ (e - s) + 1 ];

        for(int d = s, index = 0; d <= e; ++index,++d)
        {
            values[index] = d;
        }

        return values;
    }

    public static String surround_char(char n)
    {
        String string = "";
        int i, k = 0;

        int[] neighbors = range((int)(n - 2), (int)(n + 2));

        for(i = 0; i <= 1; ++i)
        {
            if(k == 0) {
                string += Character.toString((char)neighbors[0]);
                ++k;
            } else if(k == 1) {
                string += Character.toString((char)neighbors[3]);
            }
        }

        return string;

    }

    public static String reverse(String word)
    {
        return new StringBuffer(word).reverse().toString();
    }

    public static String hashing(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        char[] pieces = password.toCharArray();

        String[] L = new String[password.length()];
        int j = 0;

        for (char k: pieces) {
            String str = "";

            for(int i = 1; i <= 32; i++) {
                str += surround_char(k);
            }

            L[j] = reverse(Character.toString(k)+str);
            ++j;

        }

        String final_pass = "";
        for(String z : L) {
            final_pass += z;
        }

        final_pass = final_pass.trim();

        MessageDigest umd5 = MessageDigest.getInstance("MD5");
        umd5.update((salt + final_pass).getBytes("UTF-8"));
        byte[] md5 = umd5.digest();
        BigInteger big = new BigInteger(1, md5);
        String md5s = big.toString(16);

        MessageDigest usha = MessageDigest.getInstance("SHA-256");
        usha.update(md5s.getBytes("UTF-8"));
        byte[] sha = usha.digest();
        BigInteger bigs = new BigInteger(1, sha);
        String shas = bigs.toString(16);

        return shas;

    }

}
