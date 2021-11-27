javac -h . CaffParser.java CIFF.java 
# Generate object file for each cpp (can be done in one line)
gcc -c Block.cpp -fPIC -o Block.o 
gcc -c CaffAnim.cpp -fPIC -o CaffAnim.o 
gcc -c CAFF.cpp -fPIC -o CAFF.o 
gcc -c CaffCredits.cpp -fPIC -o CaffCredits.o 
gcc -c CaffHeader.cpp -fPIC -o CaffHeader.o 
gcc -c CAFFparser.cpp -fPIC -o CAFFparser.o 
gcc -c CIFF.cpp -fPIC -o CIFF.o 
gcc -c CIFFparser.cpp -fPIC -o CIFFparser.o 
gcc -c RGBpixel.cpp -fPIC -o RGBpixel.o 
#Compile the JNI part
g++ -fPIC   -c -I"C:/Program Files/Eclipse Adoptium/jdk-17.0.1.12-hotspot/include" -I"C:/Program Files/Eclipse Adoptium/jdk-17.0.1.12-hotspot/include/win32" com_narcano_jni_CaffParser.cpp -o com_narcano_jni_CaffParser.o
# Generate libnative.so library
g++ -shared -fPIC -o libnative.dll Block.o CaffAnim.o CaffCredits.o CaffHeader.o CAFFparser.o CIFF.o CIFFparser.o RGBpixel.o com_narcano_jni_CaffParser.o  -lc