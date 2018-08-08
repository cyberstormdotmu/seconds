= Running from Eclipse =
  
To run the local bot server from eclipse you must add "/src/main/webapp" as a source folder.
The main classes to run is LocalBotServer for a non-distributed server and BotServer for a distributed
clusterable server instance.
 
= Extending with custom bots =
 
1.	Create a class that extends BasicAI (table bots) and implements the needed methods.
	Example: com.MyBot

2.	Create a static html page that sets parameters as needed. Set aiClass to your bot class.
	Set game id to your game id as in game.xml in your deployed gar-file. Bot specific options
	can be set by matching parameters with properties in the AI class.
	
	Example: 
	
	---- mybotm.html ----
	
	<html>
		<head>
			<link rel="stylesheet" type="text/css" href="style.css" />
		</head>
		<h1>Start a Batch of MyBots</h1>
		<form action="batch/start" method="get">
		    <p>
		    <table>
		    	<tr>
		    		<td><label for="url">URL or IP: </label></td>
		    		<td><input type="text" name="url" id="url"/><br></td>
		    	</tr>
		    	<tr>
		    		<td><label for="port">Port: </label></td>
		    		<td><input type="text" name="port" id="port"/></td>
		    	</tr>
		    	<!-- STANDARD PARAMETERS -->
		    	<tr>
		    		<td><label for="requested">number of bots: </label></td>
		    		<td><input type="text" name="requested" id="requested"/></td>
		    	</tr>
		    	<!-- BOT SPECIFIC PARAMS -->
		    	<tr>
		    		<td><label for="myDelay">my delay: </label></td>
		    		<td><input type="text" name="myDelay" id="myDelay"/></td>
		    	</tr>
		    </table>
		    <!-- STATIC PARAMETERS -->
	    	<input type="hidden" name="gameId" id="gameId" value="123"/><br>
	   		<input type="hidden" name="aiclass" id="mode" value="com.MyBot"/>
		   	<input type="submit" value="start!"/> <input type="reset"/>
		</form>
	</html>  
	
	---- /mybotm.html ----
	
3.	Place the html page in the classpath. Ie. package it in your JAR file or place
    it in "conf/" in a bot installation.

4.	Edit "conf/menu.properties", add botname as key and the html page as value. For example:
    "MyBot:mybotm.html"
	
5.	Build your bot implementation and place the jar file and all other needed dependencies
	in "bots/" in the bot server (or on any other class path location).
	
6. 	Start the botserver and browse to the index page. The menu should be populated only with the 
	AI implementations as specified in menu.properties.
	
= Customizing bot groups =

1. 	Create a class which implements BotGroupConfig, this gives you the ability to control the naming
	of the bots, and also check lobby attributes prior to seating (eg. checking for specific types of
	tables prior to seating). 
	
2. 	Add the following to your static html page where the value should be the fully qualified
	class name of your group config:

	<input type="hidden" name="botgroupclass" id="mode" value="com.MyGroupConfig"/>
	
	