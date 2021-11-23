package com.narcano.jni;

public class CaffParser {

    static {
        System.load("/home/narcano/SzB/SzamBiztHF/CAFFparser/NativeLibrary/com/narcano/jni/libnative.so");
    }
    
    public static void main(String[] args) {
        System.load("/home/narcano/SzB/SzamBiztHF/CAFFparser/NativeLibrary/com/narcano/jni/libnative.so");
        CIFF[] ciffs =new CaffParser().CallParser("/home/narcano/SzB/SzamBiztHF/CAFFparser/NativeLibrary/com/narcano/jni/1.caff");
        for(CIFF c : ciffs){
            System.out.println("W: "+c.width+" H:"+c.height);
        }
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    public native  CIFF[] CallParser(String Filename);
}
