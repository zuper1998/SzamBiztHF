//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFCREDITS_H
#define SZAMITOGEBIZTONSAG_CAFFCREDITS_H


#include <string>
#include "Block.h"

class CaffCredits : public Block {
public:
    //Creation:
    int date, month, day, hour, minute;
    std::string creator;

    CaffCredits(int id,  int _date, int _month, int _day, int _hour, int _minute, std::string _creator) : Block(
            id), date(_date), month(_month), day(_day), hour(_hour), minute(_minute) {

        creator = std::move(_creator);
    }
};


#endif //SZAMITOGEBIZTONSAG_CAFFCREDITS_H
