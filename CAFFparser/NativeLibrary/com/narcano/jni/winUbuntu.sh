#Compile the JNI part
g++ -fPIC   -c -I"/mnt/c/Program Files/Java/jdk1.7.0_80/include" -I"/mnt/c/Program Files/Java/jdk1.7.0_80/include/win32" com_narcano_jni_CaffParser.cpp -o com_narcano_jni_CaffParser.o
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

# Generate libnative.so library
g++ -shared -fPIC -o libnative.dll Block.o CaffAnim.o CaffCredits.o CaffHeader.o CAFFparser.o CIFF.o CIFFparser.o RGBpixel.o com_narcano_jni_CaffParser.o  -lc