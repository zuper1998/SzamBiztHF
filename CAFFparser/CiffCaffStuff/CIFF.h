//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CIFF_H
#define SZAMITOGEBIZTONSAG_CIFF_H


#include <string>
#include <vector>
#include "RGBpixel.h"

class CIFF {
public:
    //Header:
    long long width,height;
    std::string caption;
    std::vector<std::string> tags;
    //content
    std::vector<RGBpixel> pixels; //Note: Jobbrol Balra; Fentrol Lefele

    CIFF(int _w,int _h, std::string _caption, std::vector<std::string> _tag, std::vector<RGBpixel> px);

    void printRGB();
};




#endif //SZAMITOGEBIZTONSAG_CIFF_H
