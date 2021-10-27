//
// Created by narcano on 2021. 09. 29..
//

#include <istream>
#include <fstream>
#include <string>
#include "CIFFparser.h"
#include <vector>
#include <stdexcept>

CIFF CIFFparser::parser(const std::string& filename) {

    if (std::fstream file(filename, std::ios::in | std::ios::out | std::ios::binary);file) {

        file.seekg(0,std::fstream::end);
        long len = file.tellg();
        file.seekg(0,std::fstream::beg);
        if(len < 32){
            throw std::invalid_argument( "File too short");
        }
        std::string magic;
        //Magic
        file.read((char*)&magic, 4);

        if(std::string("CIFF")!=magic){
            throw std::invalid_argument( "File wrong format");
        }
        //Header size
        size_t fileSize;
        file.read((char *) & fileSize,8);
        if(len<fileSize){
            throw std::invalid_argument(  "File wrong format");
        }
        //content size
        size_t contentSize=0;
        file.read((char*)&fileSize,8);

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
        do{
            file.read(&curt,1);
            if(curt=='\0'){
                tags.push_back(curtTag);
                curtTag="";
            }else{
                curtTag.push_back(curt);
            }

        }while((tags.size()+caption.length()+4+4*8+contentSize)!=len);

        //Ciff content
        char r=0;
        char g=0;
        char b=0;
        std::vector<RGBpixel> px;
        do{
            file.read(&r,4);
            file.read(&g,4);
            file.read(&b,4);
            px.emplace_back(r,g,b);

        }while(px.size()<contentSize);





        return {(int)width,(int)height,caption,tags,px};



    }
    return {};
}
