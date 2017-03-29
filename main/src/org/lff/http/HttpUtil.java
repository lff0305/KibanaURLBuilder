package org.lff.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by liuff on 2016/6/13 16:01
 */


public class HttpUtil {

    static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    static {
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(50);
    }

    private static CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .build();

    public static HttpResponse post(String url, String data) {

        HttpContext context = HttpClientContext.create();
        HttpPost httpost = null;
        CloseableHttpResponse response = null;

        try {
            httpost = new HttpPost(url);
            httpost.addHeader("content-type", "application/text;charset=UTF-8");
            //  httpost.addHeader("Connection", "close");
            StringEntity input = new StringEntity(data.toString(), Charset.forName("UTF-8"));
            input.setContentType("application/text;charset=UTF-8");
            // httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            httpost.setEntity(input);

            response = httpClient.execute(httpost, context);

            int code = response.getStatusLine().getStatusCode();
            String line;
            StringBuilder result = new StringBuilder();

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                HttpResponse r = new HttpResponse();
                r.setCode(code);
                r.setResponse(result.toString());
                return r;
            }

            HttpResponse r = new HttpResponse();
            r.setCode(code);
            r.setResponse(result.toString());
            return r;
        } catch (Exception e) {
            HttpResponse r = new HttpResponse();
            r.setCode(600);
            r.setResponse(e.getMessage());
            return r;
        } finally {
            try {
                if (httpost != null) {
                    httpost.releaseConnection();
                }
            } catch (Exception ex) {

            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {

            }
        }
    }

    public static HttpResponse delete(String url) {

        HttpContext context = HttpClientContext.create();
        HttpDelete httpDelete = null;
        CloseableHttpResponse response = null;

        try {

            httpDelete = new HttpDelete(url);
            httpDelete.addHeader("content-type", "application/text;charset=UTF-8");
            // httpDelete.addHeader("Connection", "close");

            response = httpClient.execute(httpDelete, context);

            int code = response.getStatusLine().getStatusCode();
            String line;
            StringBuilder result = new StringBuilder();

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
            }

            HttpResponse r = new HttpResponse();
            r.setCode(code);
            r.setResponse(result.toString());

            return r;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (httpDelete != null) {
                    httpDelete.releaseConnection();
                }
            } catch (Exception ex) {

            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {

            }
        }
    }

}


/** Multimple instances version

 package com.sharpview.base.helper.http;

 import org.apache.http.client.methods.CloseableHttpResponse;
 import org.apache.http.client.methods.HttpDelete;
 import org.apache.http.client.methods.HttpPost;
 import org.apache.http.entity.StringEntity;
 import org.apache.http.impl.client.CloseableHttpClient;
 import org.apache.http.impl.client.HttpClients;
 import org.springframework.security.access.method.P;

 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.nio.charset.Charset;

 /**
 * Created by liuff on 2016/6/13 16:01

public class HttpUtil {

    public static HttpResponse send(String url, String data) {

        CloseableHttpClient httpClient = null;
        HttpPost httpost = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClients.createDefault();
            httpost = new HttpPost(url);
            httpost.addHeader("content-type", "application/text;charset=UTF-8");
            httpost.addHeader("Connection", "close");
            StringEntity input = new StringEntity(data.toString(), Charset.forName("UTF-8"));
            input.setContentType("application/text;charset=UTF-8");
            // httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            httpost.setEntity(input);

            response = httpClient.execute(httpost);

            int code = response.getStatusLine().getStatusCode();
            String line;
            StringBuilder result = new StringBuilder();

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
            }

            HttpResponse r = new HttpResponse();
            r.setCode(code);
            r.setResponse(result.toString());
            return r;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (httpost != null) {
                    httpost.releaseConnection();
                }
            } catch (Exception ex) {

            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {

            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {

            }

        }
    }

    public static HttpResponse delete(String url) {

        CloseableHttpClient httpClient = null;
        HttpDelete httpDelete = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClients.createDefault();
            httpDelete = new HttpDelete(url);
            httpDelete.addHeader("content-type", "application/text;charset=UTF-8");
            httpDelete.addHeader("Connection", "close");

            response = httpClient.execute(httpDelete);

            int code = response.getStatusLine().getStatusCode();
            String line;
            StringBuilder result = new StringBuilder();

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (response.getEntity().getContent())));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
            }

            HttpResponse r = new HttpResponse();
            r.setCode(code);
            r.setResponse(result.toString());

            return r;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (httpDelete != null) {
                    httpDelete.releaseConnection();
                }
            } catch (Exception ex) {

            }
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {

            }
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {

            }
        }
    }

}

*/