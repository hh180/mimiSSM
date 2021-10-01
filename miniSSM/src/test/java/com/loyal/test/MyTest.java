package com.loyal.test;

import com.loyal.utils.MD5Util;
import org.junit.Test;

public class MyTest {
    @Test
    public void testMD5(){
        String md5 = MD5Util.getMD5("000000");
        System.out.println(md5);
    }
}
