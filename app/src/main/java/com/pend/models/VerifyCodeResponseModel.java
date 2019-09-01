package com.pend.models;

import com.pend.BaseResponseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class VerifyCodeResponseModel extends BaseResponseModel {

   public VerifyCodeResponse Data;

   public static class VerifyCodeResponse implements Serializable{
      public  String Status;
      public  String Details;
   }

}
