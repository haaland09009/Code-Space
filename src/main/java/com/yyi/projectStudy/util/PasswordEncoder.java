package com.yyi.projectStudy.util;

import java.security.MessageDigest;
import java.util.UUID;

public class PasswordEncoder {
    public static String encoding(String value) {

        System.out.println("해싱 전 비밀번호 : " + value);

        String hashCode = null;

        // password hashing...
        try {
            // sha-1 방식으로 messageDigest 실행 (sha-1은 이제 잘 쓰지 않는다.)
            MessageDigest messageDigest = MessageDigest.getInstance("sha-1");
            messageDigest.reset();
            messageDigest.update(value.getBytes("UTF-8")); //해싱할 값 세팅..
            byte [] chars = messageDigest.digest(); //해싱 값... 도출...

            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i < chars.length ; i++) {
                String tmp = Integer.toHexString(chars[i] & 0xff);
                if(tmp.length() == 1) {
                    sb.append("0");
                }
                sb.append(tmp);
            }

            hashCode = sb.toString();

        }catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println("해싱 후 비밀번호 : " + hashCode);

        return hashCode;
    }
    public static void main(String[] args) {
//        System.out.println(encoding("myPassword123"));
        UUID uuid = UUID.randomUUID();
        System.out.println(encoding(String.valueOf(uuid)));

    }

}
