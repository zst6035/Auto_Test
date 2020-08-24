package com.bjsxt.test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Test02 {
    HttpClient httpClient = new DefaultHttpClient();

    @Test
    public void HttpGet() throws Exception {
        HttpGet get = new HttpGet("http://www.baidu.com");
        HttpResponse response = httpClient.execute(get);
        System.out.println(response);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println(result);
        // response.getEntity() 就是获取响应实体内容；也就是响应体；
        //response区分，响应头，响应行，响应体；直接输出response是响应行

    }


    @Test
    public void HttpPost01() throws Exception {
        HttpPost post = new HttpPost("http://localhost:8086/fileup/hello");
        List<NameValuePair> paramList = new ArrayList<>();
        NameValuePair nameValuePair1 = new BasicNameValuePair("name", "张三");
        NameValuePair nameValuePair2 = new BasicNameValuePair("password", "123456");
        paramList.add(nameValuePair1);
        paramList.add(nameValuePair2);
        //UrlEncodedFormEntity 这个类是用来把输入数据编码成合适的内容,然后作为请求体传递
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, HTTP.UTF_8);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        System.out.println(response);
        //直接输出的话，就是对象的地址，必须要转换一下
        //  System.out.println(response.getEntity().toString());
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }

    @Test(testName = "文件上传", description = "上传文件+文本")
    public void filepost() throws Exception {
        String textFileName = "1.png";
        File file = new File("1.png");
        HttpPost post = new HttpPost("http://localhost:8086/fileup/upload");
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        StringBody stringBody1 = new StringBody("zhangsan", ContentType.MULTIPART_FORM_DATA);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //此处的name需要和服务端定义的名称一致
        builder.addPart("file", fileBody);
        builder.addPart("name", stringBody1);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }


    @Test(testName = "第二种方式上传文件")
    public void addBinaryBody() throws Exception {
        String textFileName = "1.png";
        File file = new File(textFileName);
        HttpPost post = new HttpPost("http://localhost:8086/fileup/upload");
        String message = "zhangtingting";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, textFileName);
        builder.addTextBody("name", message, ContentType.DEFAULT_BINARY);
        //有多个参数，就继续添加
        // builder.addTextBody("password", password, ContentType.DEFAULT_BINARY);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        entity = response.getEntity();
        String result = EntityUtils.toString(entity, "utf-8");
        System.out.println(result);
    }


    //下载文件
    @Test(testName = "文件下载")
    public void downLoad() {

        OutputStream out = null;
        InputStream in = null;

        try {

            URIBuilder uriBuilder=new URIBuilder("http://localhost:8086/fileup/download");
            uriBuilder.setParameter("fileName","a.txt");
            HttpGet httpGet=new HttpGet(uriBuilder.build());
//

            HttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println(httpGet.getURI());
            HttpEntity entity = httpResponse.getEntity();
            System.out.println(httpResponse);
            in = entity.getContent();

            long length = entity.getContentLength();
            System.out.println(length);

            String localFileName="a.txt";
            File file = new File(localFileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int readLength = 0;
            while ((readLength = in.read(buffer)) > 0) {
                byte[] bytes = new byte[readLength];
                System.arraycopy(buffer, 0, bytes, 0, readLength);
                out.write(bytes);
            }

            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}