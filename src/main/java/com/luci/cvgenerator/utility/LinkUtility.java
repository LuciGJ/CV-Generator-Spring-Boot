package com.luci.cvgenerator.utility;

import jakarta.servlet.http.HttpServletRequest;

public class LinkUtility {
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
}
