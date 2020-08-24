package com.bjsxt.config;

import java.io.File;

public class SendMail {
    public static void main(String [] args) throws Exception{

        File file = new File("");
        String filePath=file.getCanonicalPath()+"\\Charpter05\\test-output";
        String path = System.getProperty("user.dir");
        System.out.println(path);
        System.out.println(filePath);
       // 获取最新的测试报告
        File reportFile=TestConfig.orderByDate();
        System.out.println(reportFile);
        //发生最新的测试报告
        String  Mails[] = new String [1];
        Mails[0]="1148744992@qq.com";
        TestConfig.sendMail( Mails,reportFile);
    }
}
