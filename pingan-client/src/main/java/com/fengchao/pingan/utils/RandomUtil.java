package com.fengchao.pingan.utils;

import java.util.Random;

public class RandomUtil {

    public static String randomString(String seed, int length){
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(seed.length());
            sb.append(seed.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return randomString(str, length);
    }

    public static String getRandomNumber(int length) {
        String str="0123456789";
        return randomString(str, length);
    }

    public static void main(String args[]) {
        System.out.println(getRandomNumber(10));
    }
}
