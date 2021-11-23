#include <iostream>
#include "com_narcano_jni_CaffParser.h"
#include "CAFFparser.h"

///https://cppcodetips.wordpress.com/2014/02/25/returning-array-of-user-defined-objects-in-jni/

typedef struct _JNI_POSREC {
  jclass cls;
    jmethodID constructortorID;
    jfieldID nameID; 
}JNI_POSREC;


//The stuff we are returning
JNI_POSREC * jniPosRec = NULL;

/*Fill JNI details*/
void LoadJniPosRec(JNIEnv * env) {
 
    if (jniPosRec != NULL)
        return;
 
    jniPosRec = new JNI_POSREC;
 
    jniPosRec->cls = env->FindClass("com/narcano/jni/CAFF");

    //Class reference
    if(jniPosRec->cls != NULL)
        printf("sucessfully created class \n");
    //Ctor reference
    jniPosRec->constructortorID = env->GetMethodID(jniPosRec->cls, "<init>", "()V");
    if(jniPosRec->constructortorID != NULL){
        printf("sucessfully created ctorID \n");
    }
 
    jniPosRec->nameID = env->GetFieldID(jniPosRec->cls, "uName", "Ljava/lang/String;");

}

void FillJNIOjbectValues(JNIEnv * env, jobject jPosRec, CAFF c) {
    env->SetObjectField(jPosRec, jniPosRec->nameID, env->NewStringUTF(c.cc.creator.c_str()));
}


//This is called from java
JNIEXPORT jobject JNICALL Java_com_narcano_jni_CaffParser_CallParser
  (JNIEnv *env, jobject thisObject, jstring filename){
    const char* fileN = env->GetStringUTFChars(filename, NULL);
    jniPosRec = NULL;
    LoadJniPosRec(env);
    CAFF caff = CAFFparser::parser(fileN);

    jobject JO = env->NewObject(jniPosRec->cls, jniPosRec->constructortorID);
    FillJNIOjbectValues(env,JO,caff);
    return JO;
  }

