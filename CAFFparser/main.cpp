#include <iostream>
#include "Parsers/CAFFparser.h"

int main() {
    std::cout << "Loading TestFile 1." << std::endl;
    CAFF c1 = CAFFparser::parser("TestFiles/1.caff");
    std::cout << "Loading TestFile 2." << std::endl;
    CAFF c2 = CAFFparser::parser("TestFiles/2.caff");
    std::cout << "Loading TestFile 3." << std::endl;
    CAFF c3 = CAFFparser::parser("TestFiles/3.caff");
    //c.print(); -- Only use it to get the pictures out of the CAFF files for testing
    std::cout << "Succsessfully loaded all TestFiles!" << std::endl;

    return 0;
}
