// Copyright 2011 Google Inc. All Rights Reserved.

package net.sdiz.bitcoin.common;

import org.apache.commons.codec.binary.Base64;

/**
 * @author xiaofengguo@google.com (Xiaofeng Guo)
 * 
 */
public class MinerConfig {
	private final String jsonUrl = "http://mining.bitcoin.cz:8332";
	private transient String authorization = null;
	private final String username = "j16sdiz.gaeminer";
	private final String password = "unconfiged";

	public String getJsonUrl() {
		return jsonUrl;
	}

	public String getAuth() {
		if (authorization == null) {
			String auth = getUsername() + ":" + getPassword();
			authorization = "Basic "
					+ Base64.encodeBase64String(auth.getBytes());
		}
		return authorization;
	}

	private String getUsername() {
		return username;
	}

	private String getPassword() {
		return password;
	}
}
