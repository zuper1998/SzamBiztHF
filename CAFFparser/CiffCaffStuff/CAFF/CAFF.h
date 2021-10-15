//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFF_H
#define SZAMITOGEBIZTONSAG_CAFF_H


#include <vector>
#include "Block.h"

class CAFF {
public:
    std::vector<Block> blocks;
    CAFF(){
        blocks=std::vector<Block>();
    }
};


#endif //SZAMITOGEBIZTONSAG_CAFF_H
