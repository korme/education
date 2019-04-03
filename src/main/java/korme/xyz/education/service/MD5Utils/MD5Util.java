package korme.xyz.education.service.MD5Utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface MD5Util {
    String getStringMD5(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
