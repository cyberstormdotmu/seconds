package com.medicare.util;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

/**
 * Servlet per il caricamento del Lof4j in fase di startup dell'applicazione
 */
public class Log4jInit extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(Log4jInit.class);

	public void init()
	{
		String logDir = null;
		String propDir = null;
		
		//Carico il file di log
		try
		{
			if (System.getProperty("catalina.base") != null)
			{
				//Siamo su Tomcat
				logDir = System.getProperty("catalina.base");
				propDir = System.getProperty("catalina.base"); 
				logDir = logDir.replace('\\','/') + "/logs";
				propDir = propDir.replace('\\','/') + "/conf";
			}
		} catch (Exception ex) {
			throw new java.lang.UnsupportedOperationException("Impossibile costruire il file di log: " + ex.getMessage());
		}
		
		try
		{
			System.setProperty("authlogfilepath", logDir);
			System.setProperty("authpropfilepath", propDir);
			/*String prefix = getServletContext().getRealPath("/");
			String file = getInitParameter("log4j-init-file");
			PropertyConfigurator.configure(prefix + file);*/
			Logger log = Logger.getLogger(this.getClass().getName());
			log.debug("Log4J inizializzato correttamente");
		}
		catch (Exception e)
		{
			System.out.println("Errore nell'inizializzazione del Log4J: " + e.getMessage());
		}
	}

	/*public void doGet(HttpServletRequest req, HttpServletResponse res)
	{}*/

}
