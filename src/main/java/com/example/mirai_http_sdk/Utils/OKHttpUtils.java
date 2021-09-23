package com.example.mirai_http_sdk.Utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class OKHttpUtils {
//    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    @Resource
    private OkHttpClient okHttpClient;

    public  String outs="";

    /**
     * post
     *
     * @param url 请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String postString(String url, String params) {

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = params;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("HTTP on Failure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//               System.out.println(response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
//                    System.out.println(Theaders.name(i) + ":" + headers.value(i));
                }
                outs=response.body().toString();
//                System.out.println("onResponse: " + response.body().string());
            }
        });
        return outs;
    }


    /**
     * get
     * @param url 请求的url
     * @return
     */
    public String get(String url)throws IOException  {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }catch (Exception e) {
                return e.toString();
            }

    }
    /**
     * post
     *
     * @param url 请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String post(String url, Map<String, String> params) {
        String responseBody = "";
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request
                .Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
//            logger.error("okhttp post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }
    /**
     * post 上传文件
     *
     * @param url
     * @param params
     * @param fileType
     * @return
     */
    public String postFile(String url, Map<String, Object> params, String fileType) {
        String responseBody = "";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
                    continue;
                }
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }
        Request request = new Request
                .Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
//            logger.error("okhttp postFile error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }
}
