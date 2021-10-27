//
// Created by narcano on 2021. 09. 29..
//

#ifndef SZAMITOGEBIZTONSAG_CIFFPARSER_H
#define SZAMITOGEBIZTONSAG_CIFFPARSER_H
#include "../CiffCaffStuff/CIFF.h"
#include <vector>
class CIFFparser {
public:
    static CIFF parser (std::fstream&);

};


#endif //SZAMITOGEBIZTONSAG_CIFFPARSER_H
