/*
 * This C++ source file was generated by the Gradle 'init' task.
 */

#include <iostream>
#include <stdlib.h>
#include "app.h"

std::string CAFFparser::Greeter::greeting() {
    return std::string("Hello, World!");
}

int main () {
    CAFFparser::Greeter greeter;
    std::cout << greeter.greeting() << std::endl;
    return 0;
}