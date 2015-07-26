package com.bigzhao.andframe.util.zquery;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class ZComparator<T> implements Comparator<T> {
	
	@SuppressWarnings("unchecked")
	public static<E> int Compare(E l,E r){
	    if (l instanceof Comparable){
	    	return ((Comparable<E>)l).compareTo(r);
	    }else if (r instanceof Comparable){
	    	return -((Comparable<E>)r).compareTo(l);
	    }
	    throw new ClassCastException("class cannot be cast to Comparable");
	}
}
