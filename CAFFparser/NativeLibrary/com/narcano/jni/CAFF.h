//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CAFF_H
#define SZAMITOGEBIZTONSAG_CAFF_H


#include <vector>
#include <iostream>
#include "Block.h"
#include "CaffHeader.h"
#include "CaffCredits.h"
#include "CaffAnim.h"

class CAFF {
public:
    CaffHeader ch=CaffHeader(0,0);
    CaffCredits cc=CaffCredits(0,0,0,0,0,0,"");

    std::vector<CaffAnim> blocks = std::vector<CaffAnim>();
    CAFF()=default;
    void addHeader (const CaffHeader& caffHeader) {ch = caffHeader;}
    void addCredits(const CaffCredits& caffCredits){cc = caffCredits;}
    void addAnim(const CaffAnim& caffAnim){blocks.push_back(caffAnim);}

    void print() {
        std::cout << ch.num_anim <<std::endl;
        std::cout << cc.creator<<std::endl;

        int cnt=0;
        for(auto b : blocks){
            std::cout<<b.duration<<"\n";
            b.Visual(cnt++);
        }
    }

    bool toManyAnim() {
        return blocks.size()==ch.num_anim;
    }
};


#endif //SZAMITOGEBIZTONSAG_CAFF_H
