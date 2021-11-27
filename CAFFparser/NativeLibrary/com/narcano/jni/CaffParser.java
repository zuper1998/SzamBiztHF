package com.narcano.jni;

public class CaffParser {

    static {
        System.load("C:\\Users\\Narcano\\Documents\\GitHub\\SzamBiztHF\\CAFFparser\\NativeLibrary\\com\\narcano\\jni\\libnative.dll");
        //System.loadLibrary("libnative");
    }
    //CIFF[] ciffs =new CaffParser().CallParser("C:\\Users\\Narcano\\Documents\\GitHub\\SzamBiztHF\\CAFFparser\\NativeLibrary\\com\\narcano\\jni\\1.caff");
    
    public native  CIFF[] CallParser(String Filename);
}
