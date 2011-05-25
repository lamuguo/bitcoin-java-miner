package net.sdiz.bitcoin.util;

import net.sdiz.bitcoin.Work;
import net.sdiz.bitcoin.common.MinerConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;


public class MiningUtil {
  private static final Logger LOG = Logger.getLogger(MiningUtil.class.getCanonicalName());

  public static Work fetchWork(MinerConfig config) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode requestNode = mapper.createObjectNode();
    requestNode.put("method", "getwork");
    requestNode.putArray("params");
    requestNode.put("id", 0);
    
    URL bitcoind = new URL(config.getJsonUrl());
    HttpURLConnection connection = (HttpURLConnection) bitcoind.openConnection();
    connection.setConnectTimeout(5000);
    connection.setRequestProperty("Authorization", "Basic ajE2c2Rpei5nYWVtaW5lcjp1bmNvbmZpZ2Vk");
    connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);

    OutputStream requestStream = connection.getOutputStream();
    Writer request = new OutputStreamWriter(requestStream);
    request.write(requestNode.toString());
    request.close();
    requestStream.close();
    LOG.info("Sent JSON request: " + requestNode.toString());

    InputStream responseStream = null;
    
    LOG.info("xlongpolling = " + connection.getHeaderField("X-Long-Polling"));
    
    if (connection.getContentEncoding() != null) {
      if (connection.getContentEncoding().equalsIgnoreCase("gzip")) {
        LOG.info("gzip connection");
        responseStream = new GZIPInputStream(connection.getInputStream());
      } else if (connection.getContentEncoding().equalsIgnoreCase("deflate")) {
        LOG.info("deflate connection");
        responseStream = new InflaterInputStream(connection.getInputStream());
      }
    } else {
      LOG.info("Normal connection");
      responseStream = connection.getInputStream();
    }
    ObjectNode responseNode = (ObjectNode) mapper.readTree(responseStream);
    LOG.info("Received JSON response: " + responseNode);
    JsonNode result = responseNode.get("result");
    return new Work(result.get("data").getTextValue(),
        result.get("hash1").getTextValue(),
        result.get("target").getTextValue(),
        result.get("midstate").getTextValue());
  }

}
