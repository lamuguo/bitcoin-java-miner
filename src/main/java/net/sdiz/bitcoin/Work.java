package net.sdiz.bitcoin;

public class Work {
  public String data;
  public String hash1;
  public String target;
  public String midstate;

  public Work(String data, String hash1, String target, String midstate) {
    this.data = data;
    this.hash1 = hash1;
    this.target = target;
    this.midstate = midstate;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "{\n" + "  data: " + data + "\n  hash1:" + hash1 + "\n  target: " + target
        + "\n  midstate: " + midstate + "\n}\n";
  }
}
