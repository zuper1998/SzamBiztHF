//
// Created by narcano on 2021. 09. 29..
//

#include <iostream>
#include <cstdint>
#include <cstring>
#include "CaffAnim.h"

void CaffAnim::Visual(int counter) {
    //THIS SHOULD NEVER! SEE THE LIVE CODE only for testing
    /*
    int h = ciff.width;
    int w = ciff.height;
    FILE *f;
    unsigned char *img = NULL;
    int filesize = 54 + 3*w*h;  //w is your image width, h is image height, both int

    img = (unsigned char *)malloc(3*w*h);
    memset(img,0,3*w*h);
    int cnt = 0;
    for(int i=0; i<w; i++)
    {
        for(int j=0; j<h; j++)
        {
            int x=i; int y=(h-1)-j;
            RGBpixel px = ciff.pixels[cnt++];
            img[(x+y*w)*3+2] = (unsigned char)(px.R);
            img[(x+y*w)*3+1] = (unsigned char)(px.G);
            img[(x+y*w)*3+0] = (unsigned char)(px.B);
        }
    }

    unsigned char bmpfileheader[14] = {'B','M', 0,0,0,0, 0,0, 0,0, 54,0,0,0};
    unsigned char bmpinfoheader[40] = {40,0,0,0, 0,0,0,0, 0,0,0,0, 1,0, 24,0};
    unsigned char bmppad[3] = {0,0,0};

    bmpfileheader[ 2] = (unsigned char)(filesize    );
    bmpfileheader[ 3] = (unsigned char)(filesize>> 8);
    bmpfileheader[ 4] = (unsigned char)(filesize>>16);
    bmpfileheader[ 5] = (unsigned char)(filesize>>24);

    bmpinfoheader[ 4] = (unsigned char)(       w    );
    bmpinfoheader[ 5] = (unsigned char)(       w>> 8);
    bmpinfoheader[ 6] = (unsigned char)(       w>>16);
    bmpinfoheader[ 7] = (unsigned char)(       w>>24);
    bmpinfoheader[ 8] = (unsigned char)(       h    );
    bmpinfoheader[ 9] = (unsigned char)(       h>> 8);
    bmpinfoheader[10] = (unsigned char)(       h>>16);
    bmpinfoheader[11] = (unsigned char)(       h>>24);


    std::string filename("img"+std::to_string(counter)+".bmp"+'\0');

    f = fopen(filename.c_str(),"wb");
    fwrite(bmpfileheader,1,14,f);
    fwrite(bmpinfoheader,1,40,f);
    for(int i=0; i<h; i++)
    {
        fwrite(img+(w*(h-i-1)*3),3,w,f);
        fwrite(bmppad,1,(4-(w*3)%4)%4,f);
    }

    free(img);
    fclose(f);*/
}
