//
// Created by narcano on 2021. 10. 27..
//

#ifndef SZAMITOGEBIZTONSAG_CAFFPARSER_H
#define SZAMITOGEBIZTONSAG_CAFFPARSER_H


#include <string>
#include "../CiffCaffStuff/CAFF/CAFF.h"

class CAFFparser {
    [[nodiscard]] CAFF parser(const std::string &filename) const;
};


#endif //SZAMITOGEBIZTONSAG_CAFFPARSER_H
