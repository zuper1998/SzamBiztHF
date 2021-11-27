//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFHEADER_H
#define SZAMITOGEBIZTONSAG_CAFFHEADER_H


#include "Block.h"

class CaffHeader : public Block {
public:
    int num_anim;
    CaffHeader(int id,int num_anim): Block(id),num_anim(num_anim){}
};


#endif //SZAMITOGEBIZTONSAG_CAFFHEADER_H
