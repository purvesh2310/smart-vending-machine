package com.pk.lwm2m.util;

import java.util.Random;

public class BootstrapUtility {
	
	int max = 9999;
	int min = 1000;
	
	public int generatePasswordForClient(){
		
		Random rand = new Random();
		int pin = rand.nextInt((max - min) + 1) + min;
		
		return pin;
	}

}
