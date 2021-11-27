package com.narcano.jni;

public class CaffParser {

    static {
        //NativeLoader.loadLibrary(CaffParser.class.getClassLoader(), "backend/jni-library");
        System.loadLibrary("jni-library");
    }
    //CIFF[] ciffs =new CaffParser().CallParser("C:\\Users\\Narcano\\Documents\\GitHub\\SzamBiztHF\\CAFFparser\\NativeLibrary\\com\\narcano\\jni\\1.caff");
    
    public native  CIFF[] CallParser(String Filename);
}
