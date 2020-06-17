package org.celllife.idart.rest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.celllife.idart.database.hibernate.RegimeTerapeutico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 
 * @author Victor Aravena victor.aravena@ciochile.cl
 *
 */

public class ApiAuthRest {
	static String username = null;
	static String password = null;
	static String URLBase = null;
	static String URLReportingBase = null;
	/**
	 * HTTP POST
	 * @param URLPath
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static Boolean getRequestPost(String URLPath, StringEntity input) throws Exception {
        String URL = URLBase + URLPath;
        Boolean response =  false;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
        	HttpPost httpPost = new HttpPost(URL);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            BasicScheme scheme = new BasicScheme();
            Header authorizationHeader = scheme.authenticate(credentials, httpPost);
            httpPost.setHeader(authorizationHeader);
            httpPost.setEntity(input);
            HttpResponse responseRequest = httpclient.execute(httpPost);
            
    		if (responseRequest.getStatusLine().getStatusCode() != 204 && responseRequest.getStatusLine().getStatusCode() != 201) {
    			throw new RuntimeException("POST Failed : HTTP error code : "
    				+ responseRequest.getStatusLine().getStatusCode());
    		}
    		httpclient.getConnectionManager().shutdown();
    		response = true;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    /**
	 * HTTP GET
	 * @param URLPath
	 * @return
	 * @throws Exception
	 */
	public static String getRequestGet(String URLPath) throws Exception {
        String URL = URLBase + URLPath;
        String response =  "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(URL);

            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            BasicScheme scheme = new BasicScheme();
            Header authorizationHeader = scheme.authenticate(credentials, httpGet);
            httpGet.setHeader(authorizationHeader);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            
            System.out.println("GET Executing request: " + httpGet.getRequestLine());
            System.out.println(response);
            response = httpclient.execute(httpGet,responseHandler);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

	/**
	 * HTTP GET
	 * @param URLPath
	 * @return
	 * @throws Exception
	 */
	public static String getReportingRequestGet(String URLPath) throws Exception {
        String URL = URLReportingBase + URLPath;
        String response =  "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(URL);

            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            BasicScheme scheme = new BasicScheme();
            Header authorizationHeader = scheme.authenticate(credentials, httpGet);
            httpGet.setHeader(authorizationHeader);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            
            System.out.println("Executing request: " + httpGet.getRequestLine());
            System.out.println(response);
            response = httpclient.execute(httpGet,responseHandler);
            
           
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    /**
     * POSTGREST HTTP GET
     * @param URLPath
     * @return
     * @throws Exception
     */
    public static HttpResponse postgrestRequestGetAll(String URLPath) throws Exception {
//        String URL = URLBase + URLPath;
        String URL = URLPath;
        HttpResponse response =  null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
      //  httpclient.getParams().setParameter("http.socket.timeout", new Integer(1000));
      //  httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        try {
            HttpGet httpGet = new HttpGet(URL);

//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpGet);
//            httpGet.setHeader(authorizationHeader);
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            System.out.println("GET Executing request: " + httpGet.getRequestLine());
            response = httpclient.execute(httpGet);

        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    public static StringBuilder postgrestRequestGetBuffer(String URLPath) throws Exception {
//        String URL = URLBase + URLPath;
        String URL = URLPath;
        HttpResponse response =  null;
        BufferedReader reader = null;
        String line = null;
        StringBuilder str = null;

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(URL);

//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpGet);
//            httpGet.setHeader(authorizationHeader);
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            System.out.println("GET Executing request: " + httpGet.getRequestLine());
            response = httpclient.execute(httpGet);

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));

             str = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
            }
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return str;
    }


    /**
     * POSTGREST HTTP GET
     * @param URLPath
     * @return
     * @throws Exception
     */
    public static HttpResponse postgrestRequestGet(String URLPath) throws Exception {
        String URL = URLBase + URLPath;
        HttpResponse response = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(URL);
//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpGet);
//            httpGet.setHeader(authorizationHeader);
            response = httpclient.execute(httpGet);

            System.out.println("GET Executing request: " + httpGet.getRequestLine());
            System.out.println(response);

        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

    /**
     * POSTGREST HTTP POST
     * @param URLPath
     * @param input
     * @return
     * @throws Exception
     */
    public static HttpResponse postgrestRequestPost(String URLPath, StringEntity input) throws Exception {
        String URL = URLPath;
        HttpResponse responseRequest = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(URL);
//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpPost);
//            httpPost.setHeader(authorizationHeader);
            httpPost.setEntity(input);
            responseRequest = httpclient.execute(httpPost);

            if (responseRequest.getStatusLine().getStatusCode() != 204 && responseRequest.getStatusLine().getStatusCode() != 201) {
                System.out.println(("POST Failed : HTTP error code : "+ responseRequest.getStatusLine().getStatusCode()));
            }
            httpclient.getConnectionManager().shutdown();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return responseRequest;
    }

    /**
     * POSTGREST HTTP PATCH
     * @param URLPath
     * @param input
     * @return
     * @throws Exception
     */
    public static HttpResponse postgrestRequestPatch(String URLPath, StringEntity input) throws Exception {
        String URL = URLPath;
        HttpResponse responseRequest = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPatch httpPatch = new HttpPatch(URL);
//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpPatch);
//            httpPatch.setHeader(authorizationHeader);
            httpPatch.setEntity(input);
            responseRequest = httpclient.execute(httpPatch);

            if (responseRequest.getStatusLine().getStatusCode() != 204 && responseRequest.getStatusLine().getStatusCode() != 201) {
                System.out.println(("PATCH Failed : HTTP error code : "+ responseRequest.getStatusLine().getStatusCode()));
            }
            httpclient.getConnectionManager().shutdown();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return responseRequest;
    }

    /**
     * POSTGREST HTTP PUT
     * @param URLPath
     * @param input
     * @return
     * @throws Exception
     */
    public static HttpResponse postgrestRequestPut(String URLPath, StringEntity input) throws Exception {
        String URL = URLPath;
        HttpResponse responseRequest = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPut httpPut = new HttpPut(URL);
//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpPut);
//            httpPut.setHeader(authorizationHeader);
            httpPut.setEntity(input);
            responseRequest = httpclient.execute(httpPut);

            if (responseRequest.getStatusLine().getStatusCode() != 204 && responseRequest.getStatusLine().getStatusCode() != 201) {
                System.out.println(("PUT Failed : HTTP error code : "+ responseRequest.getStatusLine().getStatusCode()));
            }
            httpclient.getConnectionManager().shutdown();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return responseRequest;
    }

    /**
     * POSTGREST HTTP DELETE
     * @param URLPath
     * @return
     * @throws Exception
     */
    public static HttpResponse postgrestRequestDelete(String URLPath) throws Exception {
        String URL = URLPath;
        HttpResponse responseRequest = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpDelete httpDelete = new HttpDelete(URL);

//            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
//            BasicScheme scheme = new BasicScheme();
//            Header authorizationHeader = scheme.authenticate(credentials, httpDelete);
//            httpDelete.setHeader(authorizationHeader);
            responseRequest = httpclient.execute(httpDelete);

            System.out.println("DELETE Executing request: " + httpDelete.getRequestLine());
            System.out.println(responseRequest);

        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return responseRequest;
    }

    public static String getServerStatus(String url) throws IOException {

        String result = "";
        int code = 200;
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();

            code = connection.getResponseCode();
            connection.disconnect();
            if (code == 200) {
                result = "-> Green <-\t" + "Code: " + code;
                ;
            } else {
                result = "-> Yellow <-\t" + "Code: " + code;
            }
        } catch (Exception e) {
            result = "-> Red <-\t" + "Wrong domain - Exception: " + e.getMessage();

        }
        //  System.out.println(url + "\t\tStatus:" + result);

        return result;
    }

	public static void setUsername(String username) {
		ApiAuthRest.username = username;
	}

	public static void setPassword(String password) {
		ApiAuthRest.password = password;
	}

	public static void setURLBase(String uRLBase) {
		URLBase = uRLBase;
	}
	
	public static void setURLReportingBase(String uRLReportingBase) {
		URLReportingBase = uRLReportingBase;
	}
  
}
