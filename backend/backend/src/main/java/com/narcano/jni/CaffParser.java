package com.narcano.jni;

public class CaffParser {

    static {
        System.loadLibrary("com_narcano_jni_CaffParser");
        //System.loadLibrary("libnative");
    }
    //CIFF[] ciffs =new CaffParser().CallParser("C:\\Users\\Narcano\\Documents\\GitHub\\SzamBiztHF\\CAFFparser\\NativeLibrary\\com\\narcano\\jni\\1.caff");
    
    public native  com.narcano.jni.CIFF[] CallParser(String Filename);
}
