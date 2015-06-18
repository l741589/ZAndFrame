package com.z.andframe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ZFile {
	
	public static byte[] readAllBytes(File f) throws FileNotFoundException, IOException{
		return ZStream.readAllBytes(new FileInputStream(f));
	}
	
	public static String readAllText(File f) throws FileNotFoundException, IOException{
		return ZStream.readAllText(new FileInputStream(f));
	}
	
	public static String readAllText(String filename) throws FileNotFoundException, IOException{
		return ZStream.readAllText(new FileInputStream(filename));
	}
	
	public static byte[] readAllBytes(String filename) throws FileNotFoundException, IOException{
		return ZStream.readAllBytes(new FileInputStream(filename));
	}
	
	public static void writeAllBytes(String filename,byte[] bytes) throws FileNotFoundException, IOException{
		ZStream.writeAllBytes(new FileOutputStream(filename,false),bytes).close();
	}
	
	public static void writeAllBytes(File f,byte[] bytes) throws FileNotFoundException, IOException{
		ZStream.writeAllBytes(new FileOutputStream(f,false),bytes).close();
	}
}
