// Copyright 2011 Google Inc. All Rights Reserved.

package net.sdiz.bitcoin;

import net.sdiz.bitcoin.common.MinerConfig;
import net.sdiz.bitcoin.util.MiningUtil;

import org.json.JSONException;

import java.io.IOException;

/**
 * @author xiaofengguo@google.com (Xiaofeng Guo)
 *
 */
public class Miner {

  /**
   * @param args
   * @throws JSONException 
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException, JSONException {
    Work work = MiningUtil.fetchWork(new MinerConfig());
  }

}
