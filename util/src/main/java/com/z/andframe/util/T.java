package com.z.andframe.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Roy on 15-3-19.
 */
public class T {
    public static interface Func0<R>{ R f(); }
    public static interface Func1<R,A1>{ R f(A1 a1); }
    public static interface Func2<R,A1,A2>{ R f(A1 a1,A2 a2); }
    public static interface Func3<R,A1,A2,A3>{ R f(A1 a1,A2 a2,A3 a3); }
    public static interface Func4<R,A1,A2,A3,A4>{ R f(A1 a1,A2 a2, A3 a3,A4 a4); }
    public static interface Func5<R,A1,A2,A3,A4,A5>{ R f(A1 a1,A2 a2, A3 a3,A4 a4,A5 a5); }

    public static interface Func0p<R,P>{ R f(P...ps); }
    public static interface Func1p<R,A1,P>{ R f(A1 a1,P...ps); }
    public static interface Func2p<R,A1,A2,P>{ R f(A1 a1,A2 a2,P...ps); }
    public static interface Func3p<R,A1,A2,A3,P>{ R f(A1 a1,A2 a2,A3 a3,P...ps); }
    public static interface Func4p<R,A1,A2,A3,A4,P>{ R f(A1 a1,A2 a2, A3 a3,A4 a4,P...ps); }
    public static interface Func5p<R,A1,A2,A3,A4,A5,P>{ R f(A1 a1,A2 a2, A3 a3,A4 a4,A5 a5,P...ps); }

    public static interface Proc0{ void f(); }
    public static interface Proc1<A1>{ void f(A1 a1); }
    public static interface Proc2<A1,A2>{ void f(A1 a1,A2 a2); }
    public static interface Proc3<A1,A2,A3>{ void f(A1 a1,A2 a2,A3 a3); }
    public static interface Proc4<A1,A2,A3,A4>{ void f(A1 a1,A2 a2, A3 a3,A4 a4); }
    public static interface Proc5<A1,A2,A3,A4,A5>{ void f(A1 a1,A2 a2, A3 a3,A4 a4,A5 a5); }

    public static interface Proc0p<P>{ void f(P...ps); }
    public static interface Proc1p<A1,P>{ void f(A1 a1,P...ps); }
    public static interface Proc2p<A1,A2,P>{ void f(A1 a1,A2 a2,P...ps); }
    public static interface Proc3p<A1,A2,A3,P>{ void f(A1 a1,A2 a2,A3 a3,P...ps); }
    public static interface Proc4p<A1,A2,A3,A4,P>{ void f(A1 a1,A2 a2, A3 a3,A4 a4,P...ps); }
    public static interface Proc5p<A1,A2,A3,A4,A5,P>{ void f(A1 a1,A2 a2, A3 a3,A4 a4,A5 a5,P...ps); }

    public static class Tuple1<T1>{
        private T1 item1;
        public T1 getItem1() { return item1; }
        public void setItem1(T1 item1) { this.item1 = item1; }
        public Tuple1(T1 item1){ this.item1=item1; }
    }

    public static class Tuple2<T1,T2>{
        private T1 item1;
        public T1 getItem1() { return item1; }
        public void setItem1(T1 item1) { this.item1 = item1; }
        private T2 item2;
        public T2 getItem2() { return item2; }
        public void setItem2(T2 item2) { this.item2 = item2; }
        public Tuple2(T1 item1,T2 item2){ this.item1=item1;this.item2=item2; }
    }

    public static class Tuple3<T1,T2,T3>{
        private T1 item1;
        public T1 getItem1() { return item1; }
        public void setItem1(T1 item1) { this.item1 = item1; }
        private T2 item2;
        public T2 getItem2() { return item2; }
        public void setItem2(T2 item2) { this.item2 = item2; }
        private T3 item3;
        public T3 getItem3() { return item3; }
        public void setItem3(T3 item3) { this.item3 = item3; }
        public Tuple3(T1 item1,T2 item2,T3 item3){ this.item1=item1;this.item2=item2;this.item3=item3;  }
    }

    public static class Tuple4<T1,T2,T3,T4>{
        private T1 item1;
        public T1 getItem1() { return item1; }
        public void setItem1(T1 item1) { this.item1 = item1; }
        private T2 item2;
        public T2 getItem2() { return item2; }
        public void setItem2(T2 item2) { this.item2 = item2; }
        private T3 item3;
        public T3 getItem3() { return item3; }
        public void setItem3(T3 item3) { this.item3 = item3; }
        private T4 item4;
        public T4 getItem4() { return item4; }
        public void setItem4(T4 item4) { this.item4 = item4; }
        public Tuple4(T1 item1,T2 item2,T3 item3,T4 item4){ this.item1=item1;this.item2=item2;this.item3=item3;this.item4=item4;  }
    }

    public static class Tuple5<T1,T2,T3,T4,T5>{
        private T1 item1;
        public T1 getItem1() { return item1; }
        public void setItem1(T1 item1) { this.item1 = item1; }
        private T2 item2;
        public T2 getItem2() { return item2; }
        public void setItem2(T2 item2) { this.item2 = item2; }
        private T3 item3;
        public T3 getItem3() { return item3; }
        public void setItem3(T3 item3) { this.item3 = item3; }
        private T4 item4;
        public T4 getItem4() { return item4; }
        public void setItem4(T4 item4) { this.item4 = item4; }
        private T5 item5;
        public T5 getItem5() { return item5; }
        public void setItem5(T5 item5) { this.item5 = item5; }
        public Tuple5(T1 item1,T2 item2,T3 item3,T4 item4,T5 item5){ this.item1=item1;this.item2=item2;this.item3=item3;this.item4=item4;this.item5=item5;  }
    }

    
    public static class Map3<K1,K2,V> extends HashMap<K1,HashMap<K2,V>> {

        public V put(K1 key1, K2 key2,V value) {
            if (get(key1)==null) put(key1,new HashMap<K2, V>());
            return get(key1).put(key2,value);
        }

        public V remove(Object key1,Object key2) {
            if (get(key1)==null) return null;
            return get(key1).remove(key2);
        }

        public V get(Object key1,Object key2) {
            if (get(key1)==null) return null;
            return get(key1).get(key2);
        }
    }

    public static class Map4<K1,K2,K3,V> extends Map3<K1,K2,HashMap<K3,V>> {

        public V put(K1 key1, K2 key2,K3 key3,V value) {
            if (super.get(key1,key2)==null) put(key1,key2,new HashMap<K3, V>());
            return super.get(key1,key2).put(key3,value);
        }

        public V remove(Object key1,Object key2,Object key3) {
            if (super.get(key1,key2)==null) return null;
            return super.get(key1,key2).remove(key3);
        }

        public V get(Object key1,Object key2,Object key3) {
            if (super.get(key1,key2)==null) return null;
            return super.get(key1,key2).get(key3);
        }
    }

    public static class Map5<K1,K2,K3,K4,V> extends Map4<K1,K2,K3,HashMap<K4,V>> {

        public V put(K1 key1, K2 key2,K3 key3,K4 key4,V value) {
            if (super.get(key1,key2,key3)==null) put(key1,key2,key3,new HashMap<K4, V>());
            return super.get(key1,key2,key3).put(key4,value);
        }

        public V remove(Object key1,Object key2,Object key3,Object key4) {
            if (super.get(key1,key2,key3)==null) return null;
            return super.get(key1,key2,key3).remove(key4);
        }

        public V get(Object key1,Object key2,Object key3,Object key4) {
            if (super.get(key1,key2,key3)==null) return null;
            return super.get(key1,key2,key3).get(key4);
        }
    }

    public static <T1,T2> Tuple2<T1,T2> tuple(T1 t1,T2 t2){
        return new Tuple2<>(t1,t2);
    }

    public static <T1,T2,T3> Tuple3<T1,T2,T3> tuple(T1 t1,T2 t2,T3 t3){
        return new Tuple3<>(t1,t2,t3);
    }

    public static <T1,T2,T3,T4> Tuple4<T1,T2,T3,T4> tuple(T1 t1,T2 t2,T3 t3,T4 t4){
        return new Tuple4<>(t1,t2,t3,t4);
    }

    public static <T1,T2,T3,T4,T5> Tuple5<T1,T2,T3,T4,T5> tuple(T1 t1,T2 t2,T3 t3,T4 t4,T5 t5){
        return new Tuple5<>(t1,t2,t3,t4,t5);
    }

    public static class WeakMap extends WeakHashMap<Object,WeakReference<Object>>{


        public Object getObject(Object o){
            return get(o,Object.class);
        }

        @SuppressWarnings("unchecked")
        public <T> T get(Object o,Class<?> cls) {
            WeakReference<?> r=super.get(o);
            if (r==null) return null;
            Object x=r.get();
            if (x==null) return null;
            if (cls.isAssignableFrom(x.getClass())){
                return (T)x;
            }
            return null;
        }

        public Object putObject(Object key,Object value){
            return put(key,value,Object.class);
        }

        public <T> T put(Object key,T value,Class<T> cls){
            WeakReference<?> r=super.put(key, new WeakReference<Object>(value));
            if (r==null) return null;
            Object o=r.get();
            if (o==null) return null;
            if (cls.isAssignableFrom(o.getClass())) return (T)o;
            return null;
        }
    }
}
