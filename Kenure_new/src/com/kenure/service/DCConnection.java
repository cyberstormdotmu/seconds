package com.kenure.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.slf4j.Logger;
import com.google.gson.JsonObject;
import com.kenure.dao.ICommissioningDAO;
import com.kenure.utils.KenureUtilityContext;
import com.kenure.utils.LoggerUtils;

class DCConnection implements Runnable {

	private Logger log = LoggerUtils.getInstance(DCConnection.class);
	
	String dcStage;
	private Boolean isRunning;
	Socket sock;
	String dcSerialNumber;
	String ip;
	private int port;
	private String username;
	private String password;
	private String networkId;
	private String customerCode;
	private int counterValue;
	Boolean isConfigOk=false;
	Boolean isConnectionOk=false;
	Boolean isLevel1CommStarted = false;
	Boolean isLevelnCommStarted = false;
	ICommissioningDAO commissioningDAO;
	Integer batteryVoltage;
	//private Long unixScheduledTime; (Scheduled Time for future use)
	
	DCConnection(JsonObject json, ICommissioningDAO commissioningDAO){
		this.dcStage = json.get("dcStage").toString().replaceAll("\"","");
		this.dcSerialNumber = json.get("dcSerialNumber").toString().replaceAll("\"","");
		this.ip = json.get("dcIp").toString().replaceAll("\"","");
		this.port = Integer.parseInt(json.get("port").toString());
		this.username = json.get("username").toString().replaceAll("\"","");
		this.password =json.get("password").toString().replaceAll("\"","");
		if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
			this.customerCode = json.get("customerCode").toString().replaceAll("\"","");
			this.networkId = json.get("networkId").toString().replaceAll("\"","");
		}
		this.counterValue = Integer.parseInt(json.get("counter").toString());
		this.commissioningDAO = commissioningDAO;
		//this.unixScheduledTime = Long.parseLong(json.get("unixScheduledTime").toString()); (Scheduled Time for future use)
	}
	DataOutputStream print = null;
	private BufferedReader recieve;
	private static String command;

	/**
	 * This run method is used to fire commands for fetching data from a DataCollector, parsing it and storing it in Database. This method also starts one Receive Thread for fetching response from DC   
	 */
	public void run() {
		Thread.currentThread().setName("PORTAL-DC-CONNECTION");
		synchronized (this) {
			log.info("CONNECTING TO DATACOLLECTOR : {}",ip);
			try {
				//Creating Socket
				sock=new Socket();
				InetAddress inetAddress = InetAddress.getByName(ip);
				SocketAddress sockAddress = new InetSocketAddress(inetAddress, port);
				//Connecting Socket
				sock.connect(sockAddress);
			/*} catch (UnknownHostException e) {
				log.error("Error occured while connecting to {} because, Host Not found - {}", ip, e.getMessage());
			} catch (IOException e) {
				log.error("Some Input/Output Exception occured while connecting to {} - {}", ip, e.getMessage());
			} catch (Exception e) {
				log.error("Some Unexpected Exception occured while connecting to {} - {}", ip, e.getMessage());
			}

			try{*/
				if(sock.isConnected()){
					if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
						CommissioningServiceImpl.dcIpMap.remove(ip);
					}
					System.out.println("Connection Successful for ip - "+ip);
					log.info("Connection Successful for ip - {}",ip);
					if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
						isConnectionOk = true;
						this.commissioningDAO.updateDataCollectorConfigFields(dcSerialNumber,ip,isConnectionOk,null,null);
					}
					//Creating Consumer Thread which calls fetchResponseAndProcessIt() method of DCConnection class for fetching Response
					new Consumer(this).start();
					//Firing Commands to DC
					System.out.println("CLIENT CONNECTED TO "+sock.getInetAddress()+" ON PORT "+sock.getPort());
					log.info("CLIENT CONNECTED TO {} ON PORT {}",sock.getInetAddress(),sock.getPort());
					print = new DataOutputStream(sock.getOutputStream());
					notify();
					wait();
					print.writeBytes(username.concat(KenureUtilityContext.CARRAIGE_RETURN));
					notify();
					wait();
					print.writeBytes(password.concat(KenureUtilityContext.CARRAIGE_RETURN));
					notify();
					wait();
					print.writeBytes(KenureUtilityContext.SET_PIN_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
					notify();
					wait();
					
					if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
						print.writeBytes(KenureUtilityContext.STP_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.CLEAR_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.CLR_CARD_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						Date currentDate = new Date();
						SimpleDateFormat dateFormatUTC = new SimpleDateFormat(KenureUtilityContext.SIMPLE_DATE_FORMAT);
						dateFormatUTC.setTimeZone(TimeZone.getTimeZone(KenureUtilityContext.TIME_ZONE));
						Date utcDate = dateFormatUTC.parse(dateFormatUTC.format(currentDate));
						long currentUtcUnixTimestamp = utcDate.getTime()/KenureUtilityContext.THOUSAND;
						print.writeBytes(KenureUtilityContext.SET_RTC_COMMAND+currentUtcUnixTimestamp+KenureUtilityContext.CARRAIGE_RETURN);
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.UCD_COMMAND+customerCode.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.NID_COMMAND+networkId.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.PKT_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.NET3_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.STS_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						/*print.writeBytes(KenureUtilityContext.USR_COMMAND+username.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						print.writeBytes(KenureUtilityContext.PWD_COMMAND+password.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();*/
						print.writeBytes(KenureUtilityContext.PLM_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						/*if(unixScheduledTime!=-1){
							print.writeBytes(KenureUtilityContext.SET_STT_COMMAND+unixScheduledTime.concat(KenureUtilityContext.CARRAIGE_RETURN)); //(For future use for setting Schedule Unix Time)
							notify();
							wait();
						}*/
					}else if(this.dcStage.equals("LEVEL1-COMMISSIONING")){
						print.writeBytes(KenureUtilityContext.RUN_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
						if(isRunning!=null && !isRunning){
							print.writeBytes(KenureUtilityContext.STT_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
							notify();
							wait();
						}
						print.writeBytes(KenureUtilityContext.CML1_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
					}else if(this.dcStage.equals("LEVELN-COMMISSIONING")){
						print.writeBytes(KenureUtilityContext.CMLN_COMMAND.concat(KenureUtilityContext.CARRAIGE_RETURN));
						notify();
						wait();
					}
					
					print.writeBytes(KenureUtilityContext.LOGOUT.concat(KenureUtilityContext.CARRAIGE_RETURN));
					notifyAll();
					print.flush();
					if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
						isConfigOk = true;
						this.commissioningDAO.updateDataCollectorConfigFields(dcSerialNumber,ip,isConnectionOk,isConfigOk,batteryVoltage);
					}
					else if(this.dcStage.equalsIgnoreCase("LEVEL1-COMMISSIONING")){
						isLevel1CommStarted = true;
						isLevelnCommStarted = null;
						this.commissioningDAO.updateDataCollectorCommissioningFields(dcSerialNumber, ip, isLevel1CommStarted,isLevelnCommStarted);
						CommissioningServiceImpl.dcIpMap.remove(ip);
					}
					else if(this.dcStage.equalsIgnoreCase("LEVELN-COMMISSIONING")){
						isLevel1CommStarted = null;
						isLevelnCommStarted = true;
						this.commissioningDAO.updateDataCollectorCommissioningFields(dcSerialNumber, ip, isLevel1CommStarted,isLevelnCommStarted);
						CommissioningServiceImpl.dcIpMap.remove(ip);
					}
					
				}else{
					if(counterValue==1){
						CommissioningServiceImpl.dcIpMap.remove(ip);
						if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
							isConnectionOk = false;
							isConfigOk = false;
							batteryVoltage = null;
							this.commissioningDAO.updateDataCollectorConfigFields(dcSerialNumber,ip,isConnectionOk,isConfigOk,batteryVoltage);
						}
						else if(this.dcStage.equalsIgnoreCase("LEVEL1-COMMISSIONING")){
							isLevel1CommStarted = false;
							isLevelnCommStarted = null;
							this.commissioningDAO.updateDataCollectorCommissioningFields(dcSerialNumber, ip, isLevel1CommStarted,isLevelnCommStarted);
						}
						else if(this.dcStage.equalsIgnoreCase("LEVELN-COMMISSIONING")){
							isLevel1CommStarted = null;
							isLevelnCommStarted = false;
							this.commissioningDAO.updateDataCollectorCommissioningFields(dcSerialNumber, ip, isLevel1CommStarted,isLevelnCommStarted);
						}
						System.out.println("Counter value is - "+counterValue+" for IP - "+ip+" Connection Unsuccessful even after many tries");
						log.info("Counter value is - {} for IP - {} Connection Unsuccessful even after many tries",counterValue,ip);
					}else{
						CommissioningServiceImpl.dcIpMap.get(ip).addProperty("counter", counterValue-1);
						System.out.println("Counter value is - "+counterValue+" for IP - "+ip);
						log.info("Counter value is - {} for IP - {}",counterValue,ip);
						//Thread.sleep(120000);
					}
				}
			}catch(Exception e){
				if(CommissioningServiceImpl.dcIpMap.containsKey(ip)){
					if(counterValue==1){
						CommissioningServiceImpl.dcIpMap.remove(ip);
						if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
							isConnectionOk = false;
							isConfigOk = false;
							batteryVoltage = null;
							this.commissioningDAO.updateDataCollectorConfigFields(dcSerialNumber,ip,isConnectionOk,isConfigOk,batteryVoltage);
						}
						else if(this.dcStage.equalsIgnoreCase("LEVEL1-COMMISSIONING")){
							isLevel1CommStarted = false;
							isLevelnCommStarted = null;
							this.commissioningDAO.updateDataCollectorCommissioningFields(dcSerialNumber, ip, isLevel1CommStarted,isLevelnCommStarted);
						}
						else if(this.dcStage.equalsIgnoreCase("LEVELN-COMMISSIONING")){
							isLevel1CommStarted = null;
							isLevelnCommStarted = false;
							this.commissioningDAO.updateDataCollectorCommissioningFields(dcSerialNumber, ip, isLevel1CommStarted,isLevelnCommStarted);
						}
						System.out.println("Counter value is - "+counterValue+" for IP - "+ip+" Connection Unsuccessful even after many tries");
						log.info("Counter value is - {} for IP - {} Connection Unsuccessful even after many tries",counterValue,ip);
					}else{
						CommissioningServiceImpl.dcIpMap.get(ip).addProperty("counter", counterValue-1);
						System.out.println("Counter value is - "+counterValue+" for IP - "+ip);
						log.info("Counter value is - {} for IP - {}",counterValue,ip);
						/*try {
							System.out.println("Thread of "+ip+" going to sleep for 2 minutes!");
							Thread.sleep(120000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}*/
					}
				}else{
					System.out.println("Due to Connection Timeout Or Connection Refused Error occured while opening the socket connection to "+ip+" IP, it returned - "+e.getMessage());
					if(this.dcStage.equalsIgnoreCase("INSTALLATION")){
						isConfigOk = false;
						batteryVoltage = null;
						this.commissioningDAO.updateDataCollectorConfigFields(dcSerialNumber,ip,isConnectionOk,isConfigOk,batteryVoltage);
					}
				}
				notifyAll();
				log.error("Due to Connection Timeout Or Connection Refused Error occured while opening the socket connection to {} IP, it returned - {}", ip, e.getMessage());
			}
		}
	}
	//Run Ends

	/**
	 * This method is used to fetch response from connected DC and process and store it in DataBase
	 */
	synchronized void fetchResponseAndProcessIt() throws InterruptedException{
		try{
			recieve = new BufferedReader(new InputStreamReader(sock.getInputStream(),KenureUtilityContext.RADIX));//get inputstream
			String msgRecieved = null;
			try{
				sock.setSoTimeout(2*60*1000);
			}catch(SocketException se){
				log.error("Error occured while setting setSoTimeout for {} IP - {}", ip, se.getMessage());
			}
			while((msgRecieved = recieve.readLine())!= null){
				if(!msgRecieved.trim().equals("")){
					if(msgRecieved.contains(KenureUtilityContext.ERROR_IN_COMMAND_RESPONSE)){
						log.error("Got Error in response on {} command for {} IP  - {}", command, ip, msgRecieved);
					}else{
						log.info("{} - {}", ip, msgRecieved);
					}
					System.out.println(msgRecieved);
					if(msgRecieved.contains(KenureUtilityContext.LOGOUT)){
						break;
					}
					else if(msgRecieved.startsWith(KenureUtilityContext.BLU_TOWER)){
						notify();
						wait();
					}
					else if(msgRecieved.startsWith(KenureUtilityContext.OK_INFO_WAITING)){
						StringBuffer message = new StringBuffer();
						int character;
						while((character = recieve.read())!=-1){
							message.append((char)character);
							if(message!=null && message.toString().contains(KenureUtilityContext.USERNAME)){
								log.info("{} - {}", ip, message);
								break;
							}
						}
						notify();
						wait();
					}
					else if(msgRecieved.contains(KenureUtilityContext.USERNAME)){
						if(msgRecieved.endsWith(username)){
							notify();
							wait();
						}else{
							break;
						}
					}
					else if(msgRecieved.contains(KenureUtilityContext.PASSWORD)){
						if(msgRecieved.endsWith(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
							notify();
							wait();
						}else{
							break;
						}
					}
					else if(msgRecieved.contains(KenureUtilityContext.SET_PIN_COMMAND.substring(0, 5)) || 
						msgRecieved.contains(KenureUtilityContext.STP_COMMAND)	||
						msgRecieved.contains(KenureUtilityContext.CLEAR_COMMAND) || 
						msgRecieved.contains(KenureUtilityContext.CLR_CARD_COMMAND) || 
						msgRecieved.contains(KenureUtilityContext.SET_RTC_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.UCD_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.NID_COMMAND) ||
						msgRecieved.toLowerCase().contains(KenureUtilityContext.NID_RESPONSE.toLowerCase()) ||
						msgRecieved.contains(KenureUtilityContext.PKT_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.NET3_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.STS_COMMAND) ||
						//msgRecieved.contains(KenureUtilityContext.USR_COMMAND) ||
						//msgRecieved.contains(KenureUtilityContext.PWD_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.PLM_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.RUN_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.STT_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.CML1_COMMAND) ||
						msgRecieved.contains(KenureUtilityContext.CMLN_COMMAND)){
						
						if(msgRecieved.startsWith(KenureUtilityContext.GREATER_THAN_SYMBOL))
							command = msgRecieved.substring(1);
						else
							command = msgRecieved;
					}
					else{
						//DO NOTHING
					}
					if(command!=null && command.length()>0 && !command.isEmpty()){
						if(command.contains(KenureUtilityContext.SET_PIN_COMMAND.substring(0, 5))){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.STP_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.CLEAR_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.ENDPOINTS_CLEARED)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.CLR_CARD_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.DATA_ERASED)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.SET_RTC_COMMAND)){
							msgRecieved = recieve.readLine();
							if(msgRecieved!=null && msgRecieved.trim().equals("")){
								msgRecieved = recieve.readLine();
							}
							if(msgRecieved.equalsIgnoreCase(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}else{
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.UCD_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.NID_COMMAND) || command.toLowerCase().contains(KenureUtilityContext.NID_RESPONSE.toLowerCase())){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.PKT_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.NET3_COMMAND)){
								command = null;
								notify();
								wait();
						}
						else if(command.contains(KenureUtilityContext.STS_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.batteryVoltageStr)){
								batteryVoltage = Integer.parseInt(msgRecieved.substring(KenureUtilityContext.batteryVoltageStr.length()).trim());
								command = null;
								notify();
								wait();
							}
						}
						/*else if(command.contains(KenureUtilityContext.USR_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}
						else if(command.contains(KenureUtilityContext.PWD_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}*/
						else if(command.contains(KenureUtilityContext.PLM_COMMAND)){
							command = null;
							notify();
							wait();
						}else if(command.contains(KenureUtilityContext.RUN_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.NO_IN_COMMAND_RESPONSE)){
								command = null;
								isRunning = false;
								notify();
								wait();
							}else if(msgRecieved.contains(KenureUtilityContext.YES_IN_COMMAND_RESPONSE)){
								command = null;
								isRunning = true;
								notify();
								wait();
							}
						}else if(isRunning!=null && !isRunning && command.contains(KenureUtilityContext.STT_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}else if(command.contains(KenureUtilityContext.CML1_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}else if(command.contains(KenureUtilityContext.CMLN_COMMAND)){
							if(msgRecieved.contains(KenureUtilityContext.OK_IN_COMMAND_RESPONSE)){
								command = null;
								notify();
								wait();
							}
						}else{
							//DO NOTHING
						}
					}
				}
			}
			print.close();
			notifyAll();
			sock.close();
			System.out.println("DC Process Completed! session closed for "+ip+" IP");
			log.info("DC Process Completed! session closed for {} IP" , ip);
		}catch(Exception e){
			try {
				print.close();
				notifyAll();
				System.out.println("Error occured while processing - "+ip+" IP - " + e.getMessage());
				log.error("Error occured while processing {} DC - {}", ip, e.getMessage());
				sock.close();
			} catch (IOException ioe) {
				System.out.println("Error Occured while closing the Socket in catch block for "+ip+" IP");
				log.error("Error Occured while closing the Socket in catch block for {} IP - {}", ip, ioe.getMessage());
			} catch(Exception ex){
				System.out.println("Error Occured while closing the Socket in catch block for "+ip+" IP");
				log.error("Unexpected Exception Occured for {} IP - {}", ip, ex.getMessage());
			}
		}
		System.gc();
	}
}


class Consumer extends Thread {
	private Logger log = LoggerUtils.getInstance(Consumer.class);
	DCConnection producer;

	Consumer(DCConnection p) {
		producer = p;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("GET RESPONSE THREAD");
		try {
			System.out.println("calling fetchResponse method of DCConnection class for fetching Response from - "+producer.ip+" IP");
			log.info("calling fetchResponse method of DCConnection class for fetching Response from DC {}",producer.ip);
			producer.fetchResponseAndProcessIt();
		} catch (Exception e) {
			System.out.println("Thread Interrupted Exception occured while fetching Response for "+producer.ip+" IP - "+e.getMessage());
			log.error("Thread Interrupted Exception occured while fetching Response for {} IP - {}", producer.ip, e.getMessage());
			if(producer.dcStage.equalsIgnoreCase("INSTALLATION")){
				producer.isConfigOk = false;
				producer.commissioningDAO.updateDataCollectorConfigFields(producer.dcSerialNumber,producer.ip,producer.isConnectionOk,producer.isConfigOk,producer.batteryVoltage);
			}
			try {
				producer.print.close();
				producer.sock.close();
			} catch (IOException e1) {
				System.out.println("I/O exception occured in Consumer Class while closing Output Stream or socket for "+producer.ip+" IP - "+e1.getMessage());
				log.error("I/O exception occured in Consumer Class while closing Output Stream or socket for {} IP - {}", producer.ip, e1.getMessage());
			}
		}
	}
}