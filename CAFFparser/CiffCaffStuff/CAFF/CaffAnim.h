//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFANIM_H
#define SZAMITOGEBIZTONSAG_CAFFANIM_H


#include "../CIFF.h"
#include "Block.h"

class CaffAnim : public Block {
public:
    int duration;
    CIFF ciff;
    CaffAnim(int id, int duration,CIFF const &_ciff): Block(id),duration(duration),ciff(_ciff){

    }
    void Visual(int);
};


#endif //SZAMITOGEBIZTONSAG_CAFFANIM_H
