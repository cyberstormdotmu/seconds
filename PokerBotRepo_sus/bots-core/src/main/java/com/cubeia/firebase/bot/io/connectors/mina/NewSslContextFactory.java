package com.cubeia.firebase.bot.io.connectors.mina;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.cubeia.util.IoUtil;

public class NewSslContextFactory {

    /**
     * Protocol to use.
     */
    private static final String PROTOCOL = "TLS";

    private static final String KEY_MANAGER_FACTORY_ALGORITHM;

    static {
    	
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = KeyManagerFactory.getDefaultAlgorithm();
        }

        KEY_MANAGER_FACTORY_ALGORITHM = algorithm;
    }

    private static final char[] BOGUS_PW = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };

    private static SSLContext serverInstance = null;
    
    private static SSLContext clientInstance = null;

    /**
     * Get SSLContext singleton.
     *
     * @return SSLContext
     * @throws java.security.GeneralSecurityException
     *
     */
    public static SSLContext getInstance(boolean server) {
        SSLContext retInstance = null;
        if (server) {
            synchronized(NewSslContextFactory.class) {
                if (serverInstance == null) {
                    try {
                        serverInstance = createBougusServerSslContext();
                    } catch (Exception ioe) {
                    	ioe.printStackTrace();
                    }
                }
            }
            retInstance = serverInstance;
        } else {
            synchronized (NewSslContextFactory.class) {
                if (clientInstance == null) {
                    clientInstance = createBougusClientSslContext();
                }
            }
            retInstance = clientInstance;
        }
        return retInstance;
    }

    
    public static SSLContext createBougusServerSslContext() {
		KeyManager[] kms = null; //= getKeyManagers();
		TrustManagerFactory tmf = null;
		SSLContext context = null;
		InputStream in = null;
		try {
			KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_ALGORITHM);
			in = new BufferedInputStream(new FileInputStream(new File("pokerkeystore.jks")));
			KeyStore ks = KeyStore.getInstance("jks");
			ks.load(in, BOGUS_PW);
			kmFactory.init(ks, BOGUS_PW);
			
			tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			
			kms = kmFactory.getKeyManagers();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			IoUtil.safeClose(in);
		}
		
		try {
			
			context = SSLContext.getInstance(PROTOCOL);
			context.init(kms, tmf.getTrustManagers(), null);
			return context;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return context;
	}
    
    private static SSLContext createBougusClientSslContext() {
    	KeyManager[] kms = null; //= getKeyManagers();
		TrustManagerFactory tmf = null;
		SSLContext context = null;
		InputStream in = null;
		try {
			KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_ALGORITHM);
			in = new BufferedInputStream(new FileInputStream(new File("pokerkeystore.jks")));
			KeyStore ks = KeyStore.getInstance("jks");
			ks.load(in, BOGUS_PW);
			kmFactory.init(ks, BOGUS_PW);
			
			tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			
			kms = kmFactory.getKeyManagers();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			IoUtil.safeClose(in);
		}
    	try {
    		context = SSLContext.getInstance(PROTOCOL);
			context.init(kms, tmf.getTrustManagers(), null);
			return context;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return context;
        
    }
    
    private static KeyManager[] getKeyManagers() {
		InputStream in = null;
		try {
			KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_ALGORITHM);
			in = new BufferedInputStream(new FileInputStream(new File("pokerkeystore.jks")));
			KeyStore ks = KeyStore.getInstance("jks");
			ks.load(in, BOGUS_PW);
			kmFactory.init(ks, BOGUS_PW);
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
			tmf.init(ks);
			
			return kmFactory.getKeyManagers();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			IoUtil.safeClose(in);
		}
		return null;
	}

}
