/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ft.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Apr 19, 2018, 6:46:48 AM
 * @Quote To code is human, to debug is coffee
 */
@Service
public class SmsSender {

    public void sendContent(String source, String destination, String msg, String smsc) throws UnsupportedEncodingException, MalformedURLException, ProtocolException, IOException {
        String url = "http://50.56.73.39:13013/cgi-bin/sendsms?username=greenbee&password=green123&"
                + "from=" + source + "&to=" + destination + "&text=" + URLEncoder.encode(msg, "UTF-8")
                + "&smsc=" + smsc;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "\"Mozilla/5.0\"");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    public void sendMessage(String source, String destination, String msg, String smsc) throws UnsupportedEncodingException,
            MalformedURLException, ProtocolException, IOException {
        String url = "http://50.56.73.39:13013/cgi-bin/sendsms?username=greenbee&password=green123&"
                + "from=" + source + "&to=" + destination + "&text=" + URLEncoder.encode(msg, "UTF-8")
                + "&smsc=" + smsc;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "\"Mozilla/5.0\"");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

}
