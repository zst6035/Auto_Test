package com.bjsxt.test;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Test01 {
    @Test
    public void test01(){
        System.out.println(1);
    }

    @Test(dependsOnMethods = {"test01"})
    public void test02(){
        Assert.assertEquals(2,2);
    }



}
