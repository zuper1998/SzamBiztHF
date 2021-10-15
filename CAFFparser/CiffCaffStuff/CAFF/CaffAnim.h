//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFANIM_H
#define SZAMITOGEBIZTONSAG_CAFFANIM_H


#include "../CIFF.h"
#include "Block.h"

class CaffAnim : Block {
public:
    int duration;
    CIFF ciff;
    CaffAnim(int id, int duration,CIFF _ciff);
};


#endif //SZAMITOGEBIZTONSAG_CAFFANIM_H
