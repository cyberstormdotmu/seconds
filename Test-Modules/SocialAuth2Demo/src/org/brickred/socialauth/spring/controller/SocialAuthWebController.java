/*
 ===========================================================================
 Copyright (c) 2010 BrickRed Technologies Limited

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ===========================================================================
 */
package org.brickred.socialauth.spring.controller;

import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.brickred.socialauth.spring.bean.SpringSocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Generic controller for managing socialauth-provider connection flow. This is
 * called when the user hits a URL of the following form
 * 
 * /socialauth{pattern}?id={providerId}
 * 
 * where pattern depends on your configuration, for example .do and the
 * providerId may be one of facebook,foursquare, google,
 * hotmail,linkedin,myspace, openid, twitter, yahoo
 * 
 * The connect method is called when the user hits the above URL and it
 * redirects to the actual provider for login. Once the user provides
 * credentials and the provider redirects back to your application, one of the
 * callback methods is called
 */
@Controller
@RequestMapping("/socialauth")
public class SocialAuthWebController {

	private String baseCallbackUrl;
	private String successPageURL;
	private String accessDeniedPageURL;
	@Autowired
	private SocialAuthTemplate socialAuthTemplate;
	@Autowired
	private SocialAuthManager socialAuthManager;
	private final Log LOG = LogFactory.getLog(getClass());

	/**
	 * Constructs a SocialAuthWebController.
	 * 
	 * @param applicationUrl
	 *            the base URL for this application (with context e.g
	 *            http://opensource.brickred.com/socialauthdemo, used to
	 *            construct the callback URL passed to the providers
	 * @param successPageURL
	 *            the URL of success page or controller, where you want to
	 *            access sign in user details like profile, contacts etc.
	 * @param accessDeniedPageURL
	 *            the URL of page where you want to redirect when user denied
	 *            the permission.
	 */
	@Inject
	public SocialAuthWebController(final String applicationUrl,
			final String successPageURL, final String accessDeniedPageURL) {
		this.baseCallbackUrl = applicationUrl;
		this.successPageURL = successPageURL;
		this.accessDeniedPageURL = accessDeniedPageURL;
	}

	/**
	 * Initiates the connection with required provider.It redirects the browser
	 * to an appropriate URL which will be used for authentication with the
	 * requested provider.
	 */
	@SuppressWarnings("unused")
	@RequestMapping(params = "id")
	private String connect(@RequestParam("id") final String providerId,
			final HttpServletRequest request) throws Exception {
		LOG.debug("Getting Authentication URL for :" + providerId);
		System.err.println("Getting Authentication URL for :" + providerId);
		
		
		
		String callbackURL = baseCallbackUrl+"/authSuccess.do";
		System.err.println("Getting Call back url :" + callbackURL);
		String url = socialAuthManager.getAuthenticationUrl(providerId,callbackURL);
		String temp = "http://localhost:8082/SocialOAuth2Demo/authSuccess.do";
		System.err.println("Get final url :" + url);
		
		if(callbackURL.equals(url)) {
			System.err.println(" Inside callbackURL.equals(url) "+providerId);
			url = successPageURL;
			//socialAuthManager.connect(new HashMap<String, String>());
		}
		//socialAuthManager.connect(new HashMap<String, String>());
		//SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
		//System.err.println(" manager.getCurrentAuthProvider() --"+manager.getCurrentAuthProvider());
		socialAuthTemplate.setSocialAuthManager(socialAuthManager);
		return "redirect:" + url;
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "oauth_token")
	private String oauthCallback(final HttpServletRequest request) {
		System.err.println(" ---- 1 ------");
		callback(request);
		return "redirect:/" + successPageURL;
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "code")
	private String oauth2Callback(final HttpServletRequest request) {
		System.err.println(" ---- 2 ------");
		callback(request);
		return "redirect:/" + successPageURL;
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "wrap_verification_code")
	private String hotmailCallback(final HttpServletRequest request) {
		System.err.println(" ---- 3 ------");
		callback(request);
		return "redirect:/" + successPageURL;
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "openid.claimed_id")
	private String openidCallback(final HttpServletRequest request) {
		System.err.println(" ---- 4 ------");
		callback(request);
		return "redirect:/" + successPageURL;
	}

	private void callback(final HttpServletRequest request) {
		System.err.println(" ---- Inside call back method ------");
		SocialAuthManager m = socialAuthTemplate.getSocialAuthManager();
		
		System.err.println(" Social Auth manager 11111 --"+m.getProvider("github"));
		System.err.println(" Social Auth manager 22222 --"+m);
		
		
		AuthProvider p = m.getCurrentAuthProvider();
		System.err.println(" Auth Provide 111111---------"+p);
		
		if (m != null) {
			System.err.println(" Inside if loop means SocialAuthManager is not null ");
			try {
				AuthProvider provider = m.connect(SocialAuthUtil
						.getRequestParametersMap(request));
				LOG.debug("Connected Provider : " + provider.getProviderId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOG.debug("Unable to connect provider because SocialAuthManager object is null.");
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = { "error", "error_reason" })
	private String fbCancel(@RequestParam("error_reason") final String error) {
		System.err.println(" ---- 5 ------");
		LOG.debug("Facebook send an error : " + error);
		if ("user_denied".equals(error)) {
			return "redirect:/" + accessDeniedPageURL;
		}
		return "redirect:/";
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "openid.mode=cancel")
	private String googleCancel(@RequestParam("openid.mode") final String error) {
		System.err.println(" ---- 6 ------");
		LOG.debug("Google send an error : " + error);
		if ("cancel".equals(error)) {
			return "redirect:/" + accessDeniedPageURL;
		}
		return "redirect:/";
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "wrap_error_reason")
	private String hotmailCancel(
			@RequestParam("wrap_error_reason") final String error) {
		System.err.println(" ---- 7 ------");
		LOG.debug("Hotmail send an error : " + error);
		if ("user_denied".equals(error)) {
			return "redirect:/" + accessDeniedPageURL;
		}
		return "redirect:/";
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "oauth_problem")
	private String myspaceCancel(
			@RequestParam("oauth_problem") final String error) {
		System.err.println(" ---- 8 ------");
		LOG.debug("MySpace send an error : " + error);
		if ("user_refused".equals(error)) {
			return "redirect:/" + accessDeniedPageURL;
		}
		return "redirect:/";
	}

	@SuppressWarnings("unused")
	@RequestMapping(params = "error")
	private String gitHubCancel(@RequestParam("error") final String error) {
		System.err.println(" ---- 9 ------");
		LOG.debug("GitHub send an error : " + error);
		if ("access_denied".equals(error)) {
			return "redirect:/" + accessDeniedPageURL;
		}
		return "redirect:/";
	}
}
