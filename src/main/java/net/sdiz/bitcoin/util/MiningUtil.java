package net.sdiz.bitcoin.util;

import net.sdiz.bitcoin.Work;
import net.sdiz.bitcoin.common.MinerConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MiningUtil {

  public static Work fetchWork(MinerConfig config) throws IOException, JSONException {
    JSONObject getwork = new JSONObject();
    getwork.put("method", "getwork");
    getwork.put("params", new JSONArray());
    getwork.put("id", 0);

    HttpClient client = new DefaultHttpClient();
    HttpGet getRequest = new HttpGet(config.getJsonUrl());
    HttpResponse response = client.execute(getRequest);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      InputStream instream = entity.getContent();
      int l;
      byte[] tmp = new byte[2048];
      while ((l = instream.read(tmp)) != -1) {
        System.out.println(new String(tmp));
      }
    }

    return new Work();
  }

}
