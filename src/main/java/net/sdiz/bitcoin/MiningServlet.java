package net.sdiz.bitcoin;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import net.sdiz.bitcoin.hash.ScanHash;
import net.sdiz.bitcoin.jdo.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class MiningServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(MiningServlet.class
			.getName());

	protected static double timePassed(long start) {
		long now = System.currentTimeMillis();
		double tp = (now - start) / 1000.0;
		if (tp < 0)
			tp += 86400; // midnight
		return tp;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ScanHash sh = new ScanHash();

		Config config = null;
		int accepted = 0, rejected = 0;
		try {
			config = Config.getConfig();
			long targetTotalTime = config.getTargetTotalTime();

			log.info("Start " + config.getUsername() + " [" + config.getAuth()
					+ "]");
			long startTime = System.currentTimeMillis();
			do {
				long startRoundTime = System.currentTimeMillis();
				Work work = fetchWork(config);
				boolean found = sh.scan(work, 1, config.getScanCount());
				if (found) {
					log.warning("found: " + work.data);
					if (submitWork(config, work)) {
						log.warning("Yay! Accepted!");
						accepted++;
					} else {
						log.warning("Doh! Rejected!");
						rejected++;
					}
				} else {
					adjustHashPerRound(config, startRoundTime);
				}
			} while (timePassed(startTime) < targetTotalTime);
			log.info("Times Up!");
		} catch (DeadlineExceededException dee) {
			log.info("DealineExcessedException!");
		} catch (Exception ex) {
			log.severe("Exception Caught! " + ex);
			ex.printStackTrace(System.err);
		} finally {
			log.info("Fin. H=" + sh.getCount() + ", A=" + accepted + ", R="
					+ rejected);
		}

		config.save();

		resp.getWriter().println(
				"Fin. H=" + sh.getCount() + ", A=" + accepted + ", R="
						+ rejected);
	}

	private void adjustHashPerRound(Config config, long startRoundTime) {
		long targetRoundTime = config.getTargetRoundTime();
		double time = timePassed(startRoundTime);
		int scanCount = config.getScanCount();
		scanCount = (int) ((9 * scanCount) + (scanCount / time * targetRoundTime)) / 10;
		config.setScanCount(scanCount);
	}

	private Work fetchWork(Config config) throws IOException, JSONException {
		JSONObject getwork = new JSONObject();
		getwork.put("method", "getwork");
		getwork.put("params", new JSONArray());
		getwork.put("id", 0);

		URL url = new URL(config.getJsonRpcServer());

		URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST);
		req.setPayload(getwork.toString().getBytes());
		req.addHeader(new HTTPHeader("Authorization", config.getAuth()));

		HTTPResponse resp = ufs.fetch(req);
		String content = new String(resp.getContent());
		if (resp.getResponseCode() != 200) {
			throw new IOException( //
					"fetchWork Error: " + resp.getResponseCode() + " "
							+ content);
		}

		JSONObject respwork = new JSONObject(content);
		Object errorP = respwork.get("error");
		if (errorP != JSONObject.NULL) {
			JSONObject error = (JSONObject) errorP;
			throw new IOException( //
					"fetchWork Error: " + error.getString("message") //
							+ " (" + error.getInt("code") + ")");
		}
		JSONObject result = respwork.getJSONObject("result");
		Work work = new Work( //
				result.getString("data"), //
				result.getString("hash1"), //
				result.getString("target"), //
				result.getString("midstate"));
		return work;
	}

	private boolean submitWork(Config config, Work work) throws JSONException,
			IOException {
		JSONObject getwork = new JSONObject();
		getwork.put("method", "getwork");
		getwork.append("params", work.data);
		getwork.put("id", 0);

		URL url = new URL(config.getJsonRpcServer());
		URLFetchService ufs = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST);
		req.setPayload(getwork.toString().getBytes());
		req.addHeader(new HTTPHeader("Authorization", config.getAuth()));

		HTTPResponse resp = ufs.fetch(req);
		String content = new String(resp.getContent());
		if (resp.getResponseCode() != 200) {
			log.severe( //
			"submitWork Error: " + resp.getResponseCode() + " " + content);
			return false;
		}

		JSONObject respwork = new JSONObject(content);
		Object errorP = respwork.get("error");
		if (errorP != JSONObject.NULL) {
			JSONObject error = (JSONObject) errorP;
			log.severe( //
			"submitWork Error: " + error.getString("message") //
					+ " (" + error.getInt("code") + ")");
			return false;
		}

		return respwork.getBoolean("result");
	}

}
