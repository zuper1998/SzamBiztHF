//
// Created by narcano on 2021. 10. 27..
//

#include <fstream>
#include <memory>
#include "CAFFparser.h"
#include "CAFF.h"
#include "CIFFparser.h"


CAFF CAFFparser::parser(const std::string &filename) {
    CAFF main;

    //if (std::fstream file(filename, std::ios::in | std::ios::out | std::ios::binary);file) {
    std::ifstream file(filename,std::ios::in | std::ios::out | std::ios::binary);
    if (!file.good()){
        throw std::length_error("File not found");
    }
     while (!file.eof()) {
        //This will be loop
        int ID=0; //		0x1 - header 0x2 - credits 0x3 - animation
        file.read((char *) &ID, 1);
        //printf("%d\n", ID);

        //length --- DATA LENGTH not TOTAL LENGTH
        size_t len;
        file.read((char *) &len, 8);
        auto magic = std::make_unique<char[]>(5);

        //credit

        size_t year;
        size_t month;
        size_t day;
        size_t hour;
        size_t minute;
        size_t creator_len;
        auto creator = std::unique_ptr<char[]>();
        //Anim
        size_t duration;
        if(file.eof()) return main;
        switch (ID) {
            case 1: //Header
                file.read(magic.get(), 4);
                magic.get()[4]='\0';
                if (std::string("CAFF") != magic.get()) {
                    throw std::invalid_argument("File wrong format expected CAFF");
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

                file.read((char *) &creator_len, 8);
                if ((len + 9 + 14) < creator_len) {
                    throw std::invalid_argument("creator len to big");
                }
                creator = std::make_unique<char[]>((int) creator_len);
                file.read(creator.get(), (int) creator_len);


                main.addCredits(CaffCredits(ID, (int) year, (int) month, (int) day, (int) hour, (int) minute,
                                            std::string(creator.get())));

                break;
            case 3: //Anim

                if(main.toManyAnim()){//not gonna read ahead if there is nothing there :D
                    file.close();
                    return main;
                }
                file.read((char *) &duration, 8);
                main.addAnim(CaffAnim(ID, (int) duration, CIFFparser::parser(file)));

                break;
            default:
                throw std::invalid_argument("Unrecognized CAFF ID");
        }
    }

    return main;
}