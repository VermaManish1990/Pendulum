package com.pend.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.pend.interfaces.Constants;
import com.pend.models.ErrorExceptionModel;
import com.pend.models.ErrorResponseModel;
import com.pendulum.ui.IScreen;

import java.io.UnsupportedEncodingException;

/**
 * <h1><font color="orange">VolleyErrorListener</font></h1>
 * listener class for Volley Error listener.
 *
 * @author Somya
 */
public class VolleyErrorListener implements Response.ErrorListener {
    private final int ACTION;
    private final IScreen SCREEN;
    private final String TAG = VolleyErrorListener.class.getSimpleName();
    private ErrorResponseModel mErrorResponseModel;
    private ErrorExceptionModel mErrorExceptionModel;

    public VolleyErrorListener(final IScreen SCREEN, final int ACTION) {
        LoggerUtil.v(TAG, "VolleyErrorListener");

        this.SCREEN = SCREEN;
        this.ACTION = ACTION;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        LoggerUtil.v(TAG, "onErrorResponse");

        String str;
        int code;

        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {

            try {
                str = new String(error.networkResponse.data, "UTF-8");
                LoggerUtil.e(TAG, str);

                Gson gson = new Gson();
                mErrorResponseModel = gson.fromJson(str, ErrorResponseModel.class);

                if (mErrorResponseModel != null && mErrorResponseModel.error == null)
                    mErrorExceptionModel = gson.fromJson(str, ErrorExceptionModel.class);

            } catch (Exception ex) {
                LoggerUtil.e(TAG, ex.toString());
                SCREEN.updateUi(false, ACTION, "Bad Request");
                return;
            }

            code = error.networkResponse.statusCode;

            LoggerUtil.e(TAG, "Code=" + code + " str=" + str);

            if (code >= 400 && code < 500) {

                LoggerUtil.e(TAG, "ACTION = Bad Request");
                if (mErrorResponseModel != null && mErrorResponseModel.error != null) {
                    SCREEN.updateUi(false, ACTION, mErrorResponseModel);
                } else if (mErrorExceptionModel != null && mErrorExceptionModel.Message != null) {
                    SCREEN.updateUi(false, ACTION, mErrorExceptionModel.Message);
                } else {
                    SCREEN.updateUi(false, ACTION, "Bad Request");
                }

            } else if (code >= 500) {

                LoggerUtil.e(TAG, "ACTION = Server error");
                if (mErrorResponseModel != null && mErrorResponseModel.error != null) {
                    SCREEN.updateUi(false, ACTION, mErrorResponseModel);
                } else if (mErrorExceptionModel != null && mErrorExceptionModel.Message != null) {
                    SCREEN.updateUi(false, ACTION, mErrorExceptionModel.Message);
                } else {
                    SCREEN.updateUi(false, ACTION, "Server error");
                }
            }

        } else if (SCREEN != null) {

            if (error instanceof NoConnectionError) {
                LoggerUtil.e(TAG, "Response=" + error);
                try {
                    str = new String(error.networkResponse.data, "UTF-8");
                    LoggerUtil.e(TAG, "VolleyError=" + str);
                } catch (UnsupportedEncodingException | NullPointerException e) {
                    LoggerUtil.e(TAG, e.toString());
                }


                SCREEN.updateUi(false, ACTION, Constants.NoConnectionError);
            } else if (error instanceof AuthFailureError) {
                SCREEN.updateUi(false, ACTION, Constants.AuthFailureError);
            } else if (error instanceof NetworkError) {
                SCREEN.updateUi(false, ACTION, Constants.NetworkError);
            } else if (error instanceof ParseError) {
                SCREEN.updateUi(false, ACTION, Constants.ParseError);
            } else if (error instanceof ServerError) {
                SCREEN.updateUi(false, ACTION, Constants.ServerError);
            } else if (error instanceof TimeoutError) {
                SCREEN.updateUi(false, ACTION, Constants.TimeoutError);
            } else {
                SCREEN.updateUi(false, ACTION, Constants.ServerError);
            }
        }
    }

}
