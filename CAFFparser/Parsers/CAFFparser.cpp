//
// Created by narcano on 2021. 10. 27..
//

#include <fstream>
#include "CAFFparser.h"
#include "../CiffCaffStuff/CAFF/CAFF.h"

CAFF CAFFparser::parser(const std::string &filename) const {
    if (std::fstream file(filename, std::ios::in | std::ios::out | std::ios::binary);file) {
        int ID; //		0x1 - header 0x2 - credits 0x3 - animation
        file.read((char *)&ID,1);


        //length --- DATA LENGTH not TOTAL LENGTH
        size_t len;
        file.read((char *)&len,8);


        switch (ID) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                throw std::invalid_argument("Unrecognized CAFF ID");
                break;
        }
    }
}