package com.narcano.jni;



public class CIFF {
    public int width,height,duration;

    public int[] rgb_values;
    public CIFF(){
        width=0;
        height=0;

    }
    public CIFF(int[] arr){
        width=0;
        height=0;
        duration=0;
        rgb_values = arr;
    }
}
