cmake_minimum_required(VERSION 3.20)
project(SzamitogeBiztonsag)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})
set(CMAKE_CXX_STANDARD 20)

add_executable(SzamitogeBiztonsag main.cpp Parsers/CIFFparser.cpp Parsers/CIFFparser.h CiffCaffStuff/CIFF.cpp CiffCaffStuff/CIFF.h CiffCaffStuff/RGBpixel.cpp CiffCaffStuff/RGBpixel.h CiffCaffStuff/CAFF/CAFF.cpp CiffCaffStuff/CAFF/CAFF.h CiffCaffStuff/CAFF/Block.cpp CiffCaffStuff/CAFF/Block.h CiffCaffStuff/CAFF/CaffHeader.cpp CiffCaffStuff/CAFF/CaffHeader.h CiffCaffStuff/CAFF/CaffCredits.cpp CiffCaffStuff/CAFF/CaffCredits.h CiffCaffStuff/CAFF/CaffAnim.cpp CiffCaffStuff/CAFF/CaffAnim.h Parsers/CAFFparser.cpp Parsers/CAFFparser.h)
