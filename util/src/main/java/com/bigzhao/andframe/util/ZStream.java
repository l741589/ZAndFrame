package com.bigzhao.andframe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ZStream {
	public static byte[] readAllBytes(InputStream ins) throws IOException{
		return readAllBytes(ins, 512);
	}
	
	public static byte[] readAllBytes(InputStream ins, int bufsize) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] bs=new byte[bufsize];
		int l=0;
		while(true){
			l=ins.read(bs);
			if (l<0) break;
			baos.write(bs,0,l);
		}
		ins.close();
		bs=baos.toByteArray();
		baos.close();
		return bs;
	}
	
	public static String readAllText(InputStream ins,int bufsize) throws IOException{
		return new String(readAllBytes(ins,bufsize));
	}
	
	public static String readAllText(InputStream ins) throws IOException{
		return new String(readAllBytes(ins));
	}
	
	public static String readAllText(InputStream ins,Charset charset) throws IOException{
		return new String(readAllBytes(ins), charset);
	}
	
	public static String readAllText(InputStream ins,int bufsize,Charset charset) throws IOException{
		return new String(readAllBytes(ins,bufsize), charset);
	}
	
	public static OutputStream writeAllBytes(OutputStream os,byte[] bs) throws IOException{
		os.write(bs);
		return os;
	}
	
	public static OutputStream writeAllText(OutputStream os,String s) throws IOException{
		writeAllBytes(os, s.getBytes());
		return os;
	}
	
	public static OutputStream writeAllText(OutputStream os,String s, Charset charset) throws IOException{
		writeAllBytes(os, s.getBytes(charset));
		return os;
	}
}
