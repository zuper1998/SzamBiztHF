#include <iostream>
#include "com_narcano_jni_CaffParser.h"
#include "CAFFparser.h"

///https://cppcodetips.wordpress.com/2014/02/25/returning-array-of-user-defined-objects-in-jni/
//https://docs.oracle.com/javase/7/docs/technotes/guides/jni/spec/functions.html
// https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/types.html

typedef struct _JNI_POSREC {
  jclass cls;
    jmethodID constructortorID;
    jfieldID wID; 
    jfieldID hID;
    jfieldID arrayID;
    jfieldID durID;
}JNI_POSREC;


//The stuff we are returning
JNI_POSREC * jniPosRec = NULL;

/*Fill JNI details*/
void LoadJniPosRec(JNIEnv * env) {
 
    if (jniPosRec != NULL)
        return;
 
    jniPosRec = new JNI_POSREC;
 
    jniPosRec->cls = env->FindClass("com/narcano/jni/CIFF");

    //Class reference
    //if(jniPosRec->cls != NULL)
    //    printf("sucessfully created class \n");
    //Ctor reference
    jniPosRec->constructortorID = env->GetMethodID(jniPosRec->cls, "<init>", "([I)V");
    //if(jniPosRec->constructortorID != NULL){
    //    printf("sucessfully created ctorID \n");
    //}

    jniPosRec->arrayID = env->GetFieldID(jniPosRec->cls, "rgb_values", "[I");
    jniPosRec->wID = env->GetFieldID(jniPosRec->cls, "width", "I");
    jniPosRec->hID = env->GetFieldID(jniPosRec->cls, "height", "I");
    jniPosRec->durID = env->GetFieldID(jniPosRec->cls, "duration", "I");

}

jobject FillJNIOjbectValues(JNIEnv * env, CIFF c,CaffAnim ca) {
    
    int tlen = c.pixels.size()*3;
    //Call CTOR

    jintArray iarr =  env->NewIntArray(tlen);
    jint * temparr = new jint[tlen];              
    for(int i =0;i<tlen;i++){
      int tmp=0;
      int index = i/3;
      switch(i%3){
        case 0:
          tmp = c.pixels[index].R;
          break;
        case 1:
          tmp=c.pixels[index].G;
          break;
        case 2:
          tmp=c.pixels[index].B;
          break;
        temparr[i] = tmp;
      }
    }
    env->SetIntArrayRegion(iarr, 0, tlen, temparr);
    jobject result = env->NewObject(jniPosRec->cls, jniPosRec->constructortorID,iarr );
    
    env->SetIntField(result, jniPosRec->hID, (int)c.height);
    env->SetIntField(result, jniPosRec->wID, (int)c.width);
    env->SetIntField(result, jniPosRec->durID, (int)ca.duration);

    //https://edux.pjwstk.edu.pl/mat/268/lec/lect10/lecture10.html last part, using ctor for this
    return result;


    



}


//This is called from java
JNIEXPORT jobjectArray JNICALL Java_com_narcano_jni_CaffParser_CallParser
  (JNIEnv *env, jobject thisObject, jstring filename){
    const char* fileN = env->GetStringUTFChars(filename, NULL);
    jniPosRec = NULL;
    LoadJniPosRec(env);
    CAFF caff = CAFFparser::parser(fileN);
    //caff.print();
    jobjectArray jarr = env->NewObjectArray(caff.blocks.size(), jniPosRec->cls, NULL);
    for(int i=0;i<caff.blocks.size();i++){
      //jobject JO = env->NewObject(jniPosRec->cls, jniPosRec->constructortorID); we are calling the ctor in the next function
      jobject JO = FillJNIOjbectValues(env,caff.blocks[i].ciff,caff.blocks[i]);
      env->SetObjectArrayElement(jarr, i, JO);
    }
    
    return jarr;
  }

