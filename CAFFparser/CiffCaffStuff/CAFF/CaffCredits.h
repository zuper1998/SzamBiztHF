//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFCREDITS_H
#define SZAMITOGEBIZTONSAG_CAFFCREDITS_H


#include <string>
#include "Block.h"

class CaffCredits : Block{
public:
    //Creation:
    int date,month,day,hour,minute;
    std::string creator;
};


#endif //SZAMITOGEBIZTONSAG_CAFFCREDITS_H
