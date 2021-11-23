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
    if(jniPosRec->cls != NULL)
        printf("sucessfully created class \n");
    //Ctor reference
    jniPosRec->constructortorID = env->GetMethodID(jniPosRec->cls, "<init>", "()V");
    if(jniPosRec->constructortorID != NULL){
        printf("sucessfully created ctorID \n");
    }

    jniPosRec->arrayID = env->GetFieldID(jniPosRec->cls, "rgb_values", "[I");
    jniPosRec->wID = env->GetFieldID(jniPosRec->cls, "width", "I");
    jniPosRec->hID = env->GetFieldID(jniPosRec->cls, "height", "I");
}

void FillJNIOjbectValues(JNIEnv * env, jobject jPosRec, CIFF c) {
    env->SetIntField(jPosRec, jniPosRec->hID, env->NewStringUTF(c.height));
    env->SetIntField(jPosRec, jniPosRec->wID, env->NewStringUTF(c.width));
}


//This is called from java
JNIEXPORT jobjectArray JNICALL Java_com_narcano_jni_CaffParser_CallParser
  (JNIEnv *env, jobject thisObject, jstring filename){
    const char* fileN = env->GetStringUTFChars(filename, NULL);
    jniPosRec = NULL;
    LoadJniPosRec(env);
    CAFF caff = CAFFparser::parser(fileN);
    jobjectArray jarr = env->NewObjectArray(caff.blocks.size(), jniPosRec->cls, NULL);
    for(int i=0;i<caff.blocks.size();i++){
      jobject JO = env->NewObject(jniPosRec->cls, jniPosRec->constructortorID);
      FillJNIOjbectValues(env,JO,caff.blocks[i]);
       env->SetObjectArrayElement(jarr, i, JO);
    }
    
    return jarr;
  }

