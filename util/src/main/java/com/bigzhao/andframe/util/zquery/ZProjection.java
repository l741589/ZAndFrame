package com.bigzhao.andframe.util.zquery;

public interface ZProjection<T,V> {
	V proj(T input);
}
