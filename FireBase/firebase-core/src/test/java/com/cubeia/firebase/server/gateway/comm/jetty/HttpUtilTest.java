/**
 * Copyright (C) 2011 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.firebase.server.gateway.comm.jetty;

import static com.cubeia.firebase.server.gateway.comm.jetty.JettyServer.HANDSHAKE_HTTP_HEADER;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.junit.Test;

public class HttpUtilTest {

    @Test
    public void testCheckHandshakeServletUpgradeRequestString() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        when(req.getHeader(HANDSHAKE_HTTP_HEADER)).thenReturn("123");
        
        boolean check = HttpUtil.checkHandshake(req, "123");
        assertTrue(check);
    }
    
    @Test
    public void testCheckHandshakeServletUpgradeRequestBadHandshake() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        when(req.getHeader(HANDSHAKE_HTTP_HEADER)).thenReturn("123");
        
        boolean check = HttpUtil.checkHandshake(req, "abc");
        assertFalse(check);
    }
    
    @Test
    public void testCheckHandshakeServletUpgradeRequestStringNull() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        
        boolean check = HttpUtil.checkHandshake(req, null);
        assertTrue(check);
    }

    @Test
    public void testGetRequestHandshakeServletUpgradeRequestNull() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        
        String hs = HttpUtil.getRequestHandshake(req);
        assertEquals(null, hs);
    }

    @Test
    public void testGetRequestHandshakeServletUpgradeRequestFromHeader() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        when(req.getHeader(HANDSHAKE_HTTP_HEADER)).thenReturn("123");
        
        String hs = HttpUtil.getRequestHandshake(req);
        assertEquals("123", hs);
    }
    
    @Test
    public void testGetRequestHandshakeServletUpgradeRequestFromCookie() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        List<HttpCookie> cookies = asList(new HttpCookie(HANDSHAKE_HTTP_HEADER, "abc"));
        when(req.getCookies()).thenReturn(cookies );
        
        String hs = HttpUtil.getRequestHandshake(req);
        assertEquals("abc", hs);
    }
    
    @Test
    public void testGetRequestHandshakeServletUpgradeRequestFromParam() {
        ServletUpgradeRequest req = mock(ServletUpgradeRequest.class);
        Map<String, List<String>> paramMap = new HashMap<String, List<String>>();
        paramMap.put(HANDSHAKE_HTTP_HEADER, Arrays.asList("456"));
        when(req.getParameterMap()).thenReturn(paramMap);
        
        String hs = HttpUtil.getRequestHandshake(req);
        assertEquals("456", hs);
    }
    
}
