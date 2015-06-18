package com.z.andframe.util;

public class StringUtil {
	public static String toUpperCase(String input,int startIndex,int length){
		return 
			input.substring(0,startIndex)+
			input.substring(startIndex,length).toUpperCase()+
			input.substring(startIndex+length);
	}
	
	public static String toUpperCase(String input,int startIndex){
		return 
			input.substring(0,startIndex)+
			input.substring(startIndex).toUpperCase();
	}

    public static String toBigCamel(String s){
        return toUpperCase(s,0,1);
    }

    public static String toSmallCamel(String s){
        return toLowerCase(s,0,1);
    }

	public static String toUpperCase(String input){
		return input.toUpperCase();
	}
	
	public static String toLowerCase(String input,int startIndex,int length){
		return 
			input.substring(0,startIndex)+
			input.substring(startIndex,length).toLowerCase()+
			input.substring(startIndex+length);
	}
	
	public static String toLowerCase(String input,int startIndex){
		return 
			input.substring(0,startIndex)+
			input.substring(startIndex).toLowerCase();
	}
	
	public static String toLowerCase(String input){
		return input.toLowerCase();
	}
}
