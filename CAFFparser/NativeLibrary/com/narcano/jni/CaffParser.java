package com.narcano.jni;

public class CaffParser {

    static {
        System.load("C:\\Users\\Narcano\\Documents\\GitHub\\SzamBiztHF\\CAFFparser\\NativeLibrary\\com\\narcano\\jni\\libnative.dll");
    }
    
    public static void main(String[] args) {
        System.out.println("x");
        CIFF[] ciffs =new CaffParser().CallParser("C:\\Users\\Narcano\\Documents\\GitHub\\SzamBiztHF\\CAFFparser\\NativeLibrary\\com\\narcano\\jni\\1.caff");
        for(CIFF c : ciffs){
            System.out.println("W: "+c.width+" H:"+c.height+" Dur: "+c.duration);
        }
    }

    // Declare a native method sayHello() that receives no arguments and returns void
    public native  CIFF[] CallParser(String Filename);
}
