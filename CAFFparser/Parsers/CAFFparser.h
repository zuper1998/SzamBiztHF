//
// Created by narcano on 2021. 10. 27..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFPARSER_H
#define SZAMITOGEBIZTONSAG_CAFFPARSER_H


#include <string>
#include "../CiffCaffStuff/CAFF/CAFF.h"

class CAFFparser {
public:
    [[nodiscard]] static CAFF parser(const std::string &filename);
};


#endif //SZAMITOGEBIZTONSAG_CAFFPARSER_H
