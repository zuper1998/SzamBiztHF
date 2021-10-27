//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFF_H
#define SZAMITOGEBIZTONSAG_CAFF_H


#include <vector>
#include "Block.h"
#include "CaffHeader.h"
#include "CaffCredits.h"
#include "CaffAnim.h"

class CAFF {
public:
    std::vector<Block> blocks = std::vector<Block>();
    CAFF()=default;
    void addHeader (const CaffHeader& caffHeader) {blocks.push_back(caffHeader);}
    void addCredits(const CaffCredits& caffCredits){blocks.push_back(caffCredits);}
    void addAnim(const CaffAnim& caffAnim){blocks.push_back(caffAnim);}
};


#endif //SZAMITOGEBIZTONSAG_CAFF_H
