//
// Created by narcano on 2021. 09. 29..
//

#include <istream>
#include <fstream>
#include "CIFFparser.h"

CIFF CIFFparser::parser(const std::string& filename) {
    std::fstream file(filename, std::ios::in | std::ios::out | std::ios::binary);

    if (file) {

        file.seekg(0,std::fstream::end);
        long len = file.tellg();
        file.seekg(0,std::fstream::beg);
        char *buff = new char[len];
        if(len < 32){
            throw "File too short"; //TODO: error for this
        }
        //Magic
        file.read(buff, 4);

        if(std::string("CIFF")!=buff){
            throw "File wrong format"; //TODO: error for this, also errorokat a vegere kene rakni
        }
        //Header size
        size_t fileSize;
        file.read((char *) & fileSize,8);
        if(len<fileSize || fileSize<0){
            throw "File wrong format";
        }
        


    }


    return 0;
}
