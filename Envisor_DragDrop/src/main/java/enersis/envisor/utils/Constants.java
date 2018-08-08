package enersis.envisor.utils;

public class Constants {

	public static final int ALIGN_MIDDLE=2;
	public static final int ALIGN_LEFT=1;
	
	
	public static final int IP=3;
	public static final int SýSS=10;
	public static final int IS=20;
	public static final int BoyRefS=40;
	public static final int IRefS=80;
	public static final int SoSS= 160;
	public static final int SS= 320;
	
	
	//pay ölçerli kombinasyonlar
//	public static final int IP=3;
	public static final int IP_SýSS=IP+SýSS; //13
	public static final int IP_SýSS_BOYREFS=IP+SýSS+BoyRefS; //93
	
	//ýsý sayaçlý kombinasyonlar
//	public static final int IS=6;
	public static final int IS_SýSS=IS+SýSS; //30 
	public static final int IS_SýSS_BOYREFS=8; // 70
	
	//karmaþýk kombinasyonlar
	public static final int IP_IS_IREFS=IP+IS+IRefS; // 103 
	public static final int IP_IS_IREFS_SýSS=IP+IS+IRefS+SýSS; // 113 
	public static final int IP_IS_IREFS_SýSS_BOYREFS=IP+IS+IRefS+BoyRefS; // 143 
	
	
	public static final int ISITMA=2560;
	public static final int SOÐUTMA=2561;
	public static final int SU=2562;
	
	public static final int BILL_NGAS=5;
	public static final int BILL_ELEC=4;
	public static final int BILL_WATER=6;
	
}
