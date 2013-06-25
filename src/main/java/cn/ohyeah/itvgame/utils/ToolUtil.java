package cn.ohyeah.itvgame.utils;

import java.util.Random;

public class ToolUtil {

	private static long L;
	private static Random random = new Random();
	
	public static long getAutoincrementValue(){
		return L++;
	}
	
	public static int getRandInt(int start, int end) {
		return random.nextInt(end-start)+start;
	}
}
