//
// Created by narcano on 2021. 09. 29..
//

#include "CIFF.h"

#include <utility>

CIFF::CIFF(int _w, int _h, std::string _caption, std::vector<std::string> _tag, std::vector<RGBpixel> px) : width(_w),
                                                                                                            height(_h) {
    caption = std::move(_caption);
    tags = std::move(_tag);
    pixels = std::move(px);
}

void CIFF::printRGB() {
    for (int hi = 0; hi < height; hi++) {
        for (int wi = 0; wi < width; wi++) {
            RGBpixel px = pixels.at(wi+hi);
            printf("PX at %d:%d %d %d %d",wi,hi,px.R,px.G,px.B);
        }
    }
}
