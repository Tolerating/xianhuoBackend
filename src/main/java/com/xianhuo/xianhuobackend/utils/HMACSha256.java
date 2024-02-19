package com.xianhuo.xianhuobackend.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Map;

public class HMACSha256 {
    static String requestAuthSecret = "xianhuoapphenhahoyong";
    public static String getSignature (Map<String, String> params, String nonce, long timestamp) {
        String paramsStr = getParamsString(params);
        String algorithm = "HmacSHA256";
        Mac hmacSha256;
        String digestHexString = null;

        try {
            hmacSha256 = Mac.getInstance(algorithm);

            String key = new StringBuilder().append(requestAuthSecret).append(nonce).toString();
            String message = new StringBuilder().append(Long.toString(timestamp)).append(paramsStr).toString();

            byte[] keyBytes = key.getBytes("utf-8");
            byte[] messageBytes = message.getBytes("utf-8");

            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm));
            byte[] digestBytes = hmacSha256.doFinal(messageBytes);

            digestHexString = byteArrayToHexString(digestBytes);
        } catch (Exception e) {
            System.out.println("[ERROR] not supposed to happen: " + e.getMessage());
        }

        return digestHexString.toUpperCase();
    }

    private static String getParamsString (Map<String, String> params) {

        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < keys.length; i ++) {
            String key = keys[i];

            if (i != 0) {
                sb.append("&");
            }

            sb.append(key).append("=").append(params.get(key));
        }

        return sb.toString();
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString();
    }
}
