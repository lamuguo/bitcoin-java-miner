// Copyright 2011 Google Inc. All Rights Reserved.

package net.sdiz.bitcoin;

import net.sdiz.bitcoin.common.MinerConfig;
import net.sdiz.bitcoin.hash.ScanHash;
import net.sdiz.bitcoin.util.MiningUtil;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author xiaofengguo@google.com (Xiaofeng Guo)
 *
 */
public class Miner {
  private static final Logger LOG = Logger.getLogger(Miner.class.getCanonicalName());

  /**
   * @param args
   * @throws JSONException 
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    ScanHash sh = new ScanHash();
    Work work = MiningUtil.fetchWork(new MinerConfig());
    boolean found = sh.scan(work, 1, (1 << 30));
    LOG.info("found = " + found + "work = " + work);
  }
}
