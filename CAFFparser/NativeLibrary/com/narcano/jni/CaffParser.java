package com.narcano.jni;

public class CaffParser {

    static {
        System.load("/home/narcano/SzB/SzamBiztHF/CAFFparser/NativeLibrary/com/narcano/jni/libnative.so");
    }
    
    public static void main(String[] args) {
        System.load("/home/narcano/SzB/SzamBiztHF/CAFFparser/NativeLibrary/com/narcano/jni/libnative.so");
        String s =new CaffParser().CallParser("/home/narcano/SzB/SzamBiztHF/CAFFparser/NativeLibrary/com/narcano/jni/1.caff").uName;
        System.out.println(s);
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    public native  CAFF CallParser(String Filename);
}
