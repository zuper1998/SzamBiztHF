#javac -h . CaffParser.java CIFF.java 
x86_64-w64-mingw32-gcc -c Block.cpp -fPIC -o Block.o
x86_64-w64-mingw32-gcc -c CaffAnim.cpp -fPIC -o CaffAnim.o
x86_64-w64-mingw32-gcc -c CAFF.cpp -fPIC -o CAFF.o
x86_64-w64-mingw32-gcc -c CaffCredits.cpp -fPIC -o CaffCredits.o
x86_64-w64-mingw32-gcc -c CaffHeader.cpp -fPIC -o CaffHeader.o
x86_64-w64-mingw32-gcc -c CAFFparser.cpp -fPIC -o CAFFparser.o
x86_64-w64-mingw32-gcc -c CIFF.cpp -fPIC -o CIFF.o
x86_64-w64-mingw32-gcc -c CIFFparser.cpp -fPIC -o CIFFparser.o
x86_64-w64-mingw32-gcc -c RGBpixel.cpp -fPIC -o RGBpixel.o
x86_64-w64-mingw32-g++ -fPIC   -c -I"C:/Program Files/Java/jdk1.8.0_251/include" -I"C:/Program Files/Java/jdk1.8.0_251/include/win32" com_narcano_jni_CaffParser.cpp -o com_narcano_jni_CaffParser.o
echo "Gen lib"
x86_64-w64-mingw32-g++ -shared -o libnative.dll Block.o CaffAnim.o CaffCredits.o CaffHeader.o CAFFparser.o CIFF.o CIFFparser.o RGBpixel.o com_narcano_jni_CaffParser.o -static-libgcc -static-libstdc++