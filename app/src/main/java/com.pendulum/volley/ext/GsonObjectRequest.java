/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.volley.ext;

import android.content.Intent;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author Vijay.Kumar
 */
public abstract class GsonObjectRequest<T> extends JsonRequest<T> {
    private final Gson mGson;
    private final Class<T> mClazz;
    private boolean headerEncoding;
    private static final String TAG = "GsonObjectRequest";

    /***
     * Simple Request
     * @param url
     * @param jsonPayload
     * @param clazz
     * @param errorListener
     */
    public GsonObjectRequest(String url, String jsonPayload, Class<T> clazz, ErrorListener errorListener) {
        this(url, null, jsonPayload, clazz, errorListener);
    }

    /***
     * Request with header
     * @param url
     * @param mRequestHeaders
     * @param jsonPayload
     * @param clazz
     * @param errorListener
     */
    public GsonObjectRequest(int method, String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener) {
        this(method, url, mRequestHeaders, jsonPayload, clazz, errorListener, new Gson());
    }

    /***
     * Request with header
     * @param url
     * @param mRequestHeaders
     * @param jsonPayload
     * @param clazz
     * @param errorListener
     */
    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener) {
        this(url, mRequestHeaders, jsonPayload, clazz, errorListener, new Gson());
    }

    /***
     * * Request with custom GSON object
     * @param url
     * @param jsonPayload
     * @param clazz
     * @param errorListener
     * @param gson
     */
    public GsonObjectRequest(String url, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        this(url, null, jsonPayload, clazz, errorListener, gson);
    }

    /***
     * Request With header and custom GSON object
     * @param url
     * @param mRequestHeaders
     * @param jsonPayload
     * @param clazz
     * @param errorListener
     * @param gson
     */
    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        super(url, mRequestHeaders, jsonPayload, errorListener);
        this.mClazz = clazz;
        headerEncoding = false;
        mGson = gson;
    }

    /***
     * For particular Method request
     * @param method
     * @param url
     * @param mRequestHeaders
     * @param jsonPayload
     * @param clazz
     * @param errorListener
     * @param gson
     */
    public GsonObjectRequest(int method, String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener, Gson gson) {
        super(method, url, mRequestHeaders, jsonPayload, errorListener);
        this.mClazz = clazz;
        headerEncoding = false;
        mGson = gson;
    }

    /***
     * For Gzip request
     * @param url
     * @param mRequestHeaders
     * @param jsonPayload
     * @param clazz
     * @par am errorListener
     * @param headerEncoding
     */
    public GsonObjectRequest(String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, ErrorListener errorListener, boolean headerEncoding) {
        super(url, mRequestHeaders, jsonPayload, errorListener);
        this.mClazz = clazz;
        mGson = new Gson();
        this.headerEncoding = headerEncoding;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            this.mResponse = response;
            String json = "";
            if (!headerEncoding) {
                json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Log.e("JsonResponse", json);
            } else {
                json = handleGzipResponse(response);
            }

            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    private String handleGzipResponse(NetworkResponse response) {
        String output = "";
        try {
            GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(response.data));
            InputStreamReader reader = new InputStreamReader(gStream);
            BufferedReader in = new BufferedReader(reader);
            String read;
            while ((read = in.readLine()) != null) {
                output += read;
            }
            reader.close();
            in.close();
            gStream.close();
        } catch (IOException e) {

        }
        return output;
    }
}

