//
// Created by narcano on 2021. 10. 27..
//

#include <fstream>
#include <memory>
#include "CAFFparser.h"
#include "../CiffCaffStuff/CAFF/CAFF.h"
#include "CIFFparser.h"

CAFF CAFFparser::parser(const std::string &filename) const {
    CAFF main;

    if (std::fstream file(filename, std::ios::in | std::ios::out | std::ios::binary);file) {

        do {
            //This will be loop
            int ID; //		0x1 - header 0x2 - credits 0x3 - animation
            file.read((char *) &ID, 1);


            //length --- DATA LENGTH not TOTAL LENGTH
            size_t len;
            file.read((char *) &len, 8);
            std::string magic;
            //credit
            auto creator = std::make_unique<char[]>(8);
            size_t year;
            size_t month;
            size_t day;
            size_t hour;
            size_t minute;
            //Anim
            size_t duration;
            switch (ID) {
                case 1: //Header
                    file.read((char *) &magic, 4);
                    if (std::string("CAFF") != magic) {
                        throw std::invalid_argument("File wrong format");
                    }
                    //Header Size
                    size_t hSize;
                    file.read((char *) &hSize, 8);

                    if (hSize != (4 + 8 + 8))
                        throw std::invalid_argument("Wrong header size value");
                    //Ciff num
                    size_t CiffNum;
                    file.read((char *) &CiffNum, 8);
                    main.addHeader(CaffHeader(ID, (int) CiffNum));
                    break;
                case 2: //CAFF credit
                    file.read((char *) &year, 2);
                    file.read((char *) &month, 1);
                    file.read((char *) &day, 1);
                    file.read((char *) &hour, 1);
                    file.read((char *) &minute, 1);


                    file.read(creator.get(), 8);

                    main.addCredits(CaffCredits(ID, (int) year, (int) month, (int) day, (int) hour, (int) minute,
                                                std::string(creator.get())));

                    break;
                case 3: //Anim
                    file.read((char *) &duration, 8);
                    main.addAnim(CaffAnim(ID, duration, CIFFparser::parser(file)));

                    break;
                default:
                    throw std::invalid_argument("Unrecognized CAFF ID");
            }
        }while(file.eof());
    }
    return main;
}