package com.turt2live.uuid.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class HTTPUtils {

    public static final String JSON_TYPE = "application/json";
    public static final String TEXT_TYPE = "text/plain";

    public static String get(String rawUrl) {
        BufferedReader reader = null;
        try {
            URL url = new URL(rawUrl);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            return result.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    public static String post(String rawUrl, String body, String contentType) {
        BufferedReader reader = null;
        OutputStream out = null;
        
        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            out = connection.getOutputStream();
            out.write(body.getBytes());
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            return result.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) out.close();
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
    
    private static final JSONParser PARSER = new JSONParser();
    
    public static Object postJson(String url, JSONObject body) {
        if (body == null) return null;
        String rawBody = body.toString();
        String rawResponse = post(url, rawBody, JSON_TYPE);
        if (rawResponse == null) return null;
        try {
            return PARSER.parse(rawResponse);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Object postJson(String url, JSONArray body) {
        if (body == null) return null;
        String rawBody = body.toString();
        String rawResponse = post(url, rawBody, JSON_TYPE);
        if (rawResponse == null) return null;
        try {
            return PARSER.parse(rawResponse);
        } catch (Exception e) {
            return null;
        }
    }
}