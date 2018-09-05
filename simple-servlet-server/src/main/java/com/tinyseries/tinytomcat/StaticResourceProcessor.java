package com.tinyseries.tinytomcat;

import java.io.IOException;

/**
 * 静态资源处理类(处理html页面)
 */
public class StaticResourceProcessor {

  public void process(Request request, Response response) {
    try {
      response.sendStaticResource();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
}