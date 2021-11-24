//
// Created by narcano on 2021. 09. 29..
//

#include <istream>
#include <fstream>
#include <string>
#include "CIFFparser.h"
#include <vector>
#include <stdexcept>
#include <memory>

CIFF CIFFparser::parser(std::ifstream& file) {
        auto magic = std::make_unique<char[]>(5);
        //Magic
        file.read(magic.get(), 4);
        magic.get()[4]='\0';
        if(std::string("CIFF")!=magic.get()){
            throw std::invalid_argument( "File wrong format");
        }
        //Header size
        size_t headerSize;
        file.read((char *) & headerSize, 8);
        //content size
        size_t contentSize=0;
        file.read((char*)&contentSize,8);

        //Width
        size_t width=0;
        file.read((char*)&width,8);


        //Height
        size_t height=0;
        file.read((char*)&height,8);
        if(contentSize!=width*height*3){
            throw std::invalid_argument("Nem megfelelo hossz");
        }

        //Caption :/
        // Erre kell keresni valami szebet
        char cur = 0;
        std::string caption;
        do {
            file.read(&cur, 1);//read the caption char by char
            caption.push_back(cur);
        } while (cur!='\n');


        //Tags
        std::vector<std::string> tags;
        std::string curtTag;
        char curt=0;
        int bytes_read=0;
        do{
            file.read(&curt,1);
            if(curt=='\0'){
                tags.push_back(curtTag);
                curtTag="";
            }else{
                curtTag.push_back(curt);
            }

        }while((++bytes_read+caption.size()+2*8+2*8+4+1)<=headerSize);

        //Ciff content
        char r=0;
        char g=0;
        char b=0;
        std::vector<RGBpixel> px;
        do{
            file.read(&r,1);
            file.read(&g,1);
            file.read(&b,1);
            px.emplace_back(r,g,b);

        }while((px.size()*3+3)<=contentSize);




        return {(int)width,(int)height,caption,tags,px};
}
