#include <iostream>
#include "Parsers/CAFFparser.h"

int main() {
    std::cout << "Hello, World!" << std::endl;
    CAFF c = CAFFparser::parser("TestFiles/3.caff");
    //c.print();
    std::cout << "HELL YEA!" << std::endl;

    return 0;
}
