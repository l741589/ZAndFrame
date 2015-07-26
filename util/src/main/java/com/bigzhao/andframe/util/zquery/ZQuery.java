package com.bigzhao.andframe.util.zquery;

import com.bigzhao.andframe.util.StringUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class ZQuery<T> extends ArrayList<T>{
	
	public static enum OrderDirection {ASC, DESC}
	private Class<T> classOfT = null;
	
	private static final long serialVersionUID = 5477843620581960798L;
	
	@SuppressWarnings("unchecked")
	public ZQuery(T[] src){
		this(src.length);
		classOfT=(Class<T>)src.getClass().getComponentType();
		for(T e:src) add(e);
	}
	
	public ZQuery(ZQuery<T> src){
		this((Iterable<T>)src);
		classOfT=src.getClassOfT();
	}
	
	/**Use new ZQuery&lt;T&gt;(src){} to instant, otherwise, It's dangerous to use T.class*/
	public ZQuery(Iterable<T> src){
		this(src,null);
		try{
			ParameterizedType pt=(ParameterizedType)getClass().getGenericSuperclass();
			Type cls=pt.getActualTypeArguments()[0];
			this.classOfT=(Class<T>) cls;
		}catch(Exception e){
			this.classOfT=null;
		}
	}
	
	public ZQuery(Iterable<T> src,Class<T> classOfT){
		this();
		this.classOfT=classOfT;
		for(T e:src) add(e);
	}
	
	private ZQuery() {
		super();
		classOfT=null;
	}
	
	private ZQuery(int capacity) {
		super(capacity);
		classOfT=null;
	}
	
	public static<T> ZQuery<T> create(T[] src){
		return new ZQuery<T>(src);
	}
	
	public static ZQuery<Boolean> create(boolean[] src){
		ZQuery<Boolean> q=new ZQuery<Boolean>(src.length);
		q.classOfT=Boolean.class;
		for(boolean b:src) q.add(b);
		return q;
	}
	
	public static ZQuery<Integer> create(int[] src){
		ZQuery<Integer> q=new ZQuery<Integer>(src.length);
		q.classOfT=Integer.class;
		for(int b:src) q.add(b);
		return q;
	}
	
	public static ZQuery<Long> create(long[] src){
		ZQuery<Long> q=new ZQuery<Long>(src.length);
		q.classOfT=Long.class;
		for(long b:src) q.add(b);
		return q;
	}
	
	public static ZQuery<Float> create(float[] src){
		ZQuery<Float> q=new ZQuery<Float>(src.length);
		q.classOfT=Float.class;
		for(float b:src) q.add(b);
		return q;
	}
	
	public static ZQuery<Double> create(double[] src){
		ZQuery<Double> q=new ZQuery<Double>(src.length);
		q.classOfT=Double.class;
		for(double b:src) q.add(b);
		return q;
	}
	
	/**It's dangerous to use T.class */
	public static<T> ZQuery<T> create(Iterable<T> src){
		return new ZQuery<T>(src){};
	}
	
	public static<T> ZQuery<T> create(ZQuery<T> src){
		return new ZQuery<T>(src);
	}
	
	public static<T> ZQuery<T> create(Iterable<T> src, Class<T> classOfT){
		return new ZQuery<T>(src,classOfT);
	}
		
	public static<T> ZQuery<T> create(Class<T> cls){
		ZQuery<T> ret=new ZQuery<T>();
		ret.classOfT=cls;
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	public<V> V[] toArray(Class<V> cls){
		V[] ret=(V[])Array.newInstance(cls, size());
		super.toArray(ret);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(){
		T[] ret=(T[])Array.newInstance(classOfT, size());
		super.toArray(ret);
		return ret;
	}
	
	public boolean[] toBooleanArray(){
		boolean[] ret=new boolean[size()];
		for (int i=0;i<size();++i) ret[i]=(boolean)(Boolean)get(i);
		return ret;
	}
	
	public int[] toIntArray(){
		int[] ret=new int[size()];
		for (int i=0;i<size();++i) ret[i]=(int)(Integer)get(i);
		return ret;
	}
	
	public long[] toLongArray(){
		long[] ret=new long[size()];
		for (int i=0;i<size();++i) ret[i]=(long)(Long)get(i);
		return ret;
	}
	
	public float[] toFloatArray(){
		float[] ret=new float[size()];
		for (int i=0;i<size();++i) ret[i]=(float)(Float)get(i);
		return ret;
	}
	
	public double[] toDoubleArray(){
		double[] ret=new double[size()];
		for (int i=0;i<size();++i) ret[i]=(double)(Double)get(i);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public<V> ZQuery<V> toQuery(Class<V> cls){
		ZQuery<V> q=new ZQuery<V>();
		for(T e:this) q.add((V)e);
		return q;		
	}

	public ZQuery<T> where(ZPredicate<T> predicate){
		ZQuery<T> ret=new ZQuery<T>();
		for (T e:this) if (predicate.predicate(e)) ret.add(e);
		return ret;
	}
	
	public<V> ZQuery<V> select(ZProjection<T, V> projection,boolean filternull){
		ZQuery<V> ret=new ZQuery<V>();
		for(T e:this) {
			V v=projection.proj(e);
			if (!filternull||v!=null) ret.add(v);
		}
		return ret;
	}
	
	public<V> ZQuery<V> select(ZProjection<T, V> projection){
		return select(projection,false);
	}	
	
	public ZQuery<T> orderBy(Comparator<T> comp){
		ZQuery<T> target=new ZQuery<T>(this);
		Collections.sort(target, comp);
		return target;		
	}
	
	public ZQuery<T> orderBy(String property) throws NoSuchMethodException, SecurityException{
		return orderBy(property,OrderDirection.ASC);
	}
	
	public ZQuery<T> orderBy(String property, final OrderDirection direction) throws NoSuchMethodException, SecurityException {
		Class<T> cls=classOfT;
		final Method m=cls.getMethod("get"+ StringUtil.toUpperCase(property, 0, 1));
		return orderBy(new Comparator<T>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public int compare(T o1, T o2) {
				try{
					Comparable l=(Comparable) m.invoke(o1);
					Comparable r=(Comparable) m.invoke(o2);
					if (OrderDirection.ASC==direction)
						return l.compareTo(r);
					else
						return r.compareTo(l);
				}catch(Exception e){
					return 0;
				}
			}
		});	
	}
	
	public ZQuery<T> orderByField(String field, final OrderDirection direction) throws NoSuchFieldException, SecurityException {
		Class<T> cls=classOfT;
		final Field f=cls.getField(field);
		return orderBy(new Comparator<T>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public int compare(T o1, T o2) {
				try{
					Comparable l=(Comparable) f.get(o1);
					Comparable r=(Comparable) f.get(o2);
					if (OrderDirection.ASC==direction)
						return l.compareTo(r);
					else
						return r.compareTo(l);
				}catch(Exception e){
					return 0;
				}
			}
		});
	}
	
	public HashSet<T> toHashSet(){
		HashSet<T> set=new HashSet<T>();
		for(T e:this) set.add(e);
		return set;
	}
	
	public Class<T> getClassOfT() {
		return classOfT;
	}
}
