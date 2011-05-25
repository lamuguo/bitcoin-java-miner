package net.sdiz.bitcoin.util;

import java.io.IOException;
import java.io.InputStream;

import net.sdiz.bitcoin.Work;
import net.sdiz.bitcoin.common.MinerConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MiningUtil {

  public static Work fetchWork(MinerConfig config) throws IOException, JSONException {
    JSONObject getwork = new JSONObject();
    getwork.put("method", "getwork");
    getwork.put("params", new JSONArray());
    getwork.put("id", 0);

    HttpClient client = new DefaultHttpClient();
    HttpPost postRequest = new HttpPost(config.getJsonUrl());
    postRequest.setEntity(new ByteArrayEntity(getwork.toString().getBytes()));
    postRequest.addHeader("Authorization", config.getAuth());
    
    HttpResponse response = client.execute(postRequest);
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
