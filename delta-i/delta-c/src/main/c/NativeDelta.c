#include <jni.h>
#include <smmintrin.h>
#include <assert.h>
#include <stdio.h>
#include <time.h>
#include <sys/time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <stdarg.h>
#include <string.h>

enum AOP {DENSE_REPRESENTATION,
			SPARSE_REPRESENTATION,
            SHARED_SPARSE_REPRESENTATION,
            CONTIGUOUS_REPRESENTATION};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved){
	JNIEnv *env;

	if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2)){
		return JNI_ERR; /* JNI version not supported */
	}

	return JNI_VERSION_1_2;
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_EQIntArray(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	jint i,j;

	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] == pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_EQIntArrayConst(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] == arg2;
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_EQIntConstArray(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = arg1 == pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_LTIntArray(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	jint i,j;

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] < pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_LTIntArrayConst(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] < arg2;
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_LTIntConstArray(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;
	
	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = arg1 < pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_GTIntArray(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] > pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_GTIntArrayConst(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] > arg2;
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_GTIntConstArray(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jbooleanArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = arg1 > pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
	
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_ADDIntArray(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jintArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jint* pResult = (jint*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] + pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_ADDIntArrayConst(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jintArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jint* pResult = (jint*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = pArg1[i] + arg2;
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_ADDIntConstArray(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jintArray result,
	jintArray mask,
	jint index,
	jint arraySize,
	jint maskType) {

	jint i,j;

	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jint* pResult = (jint*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	//iterating
	if(maskType == SHARED_SPARSE_REPRESENTATION){
		j = index;
		arraySize = j + arraySize;
	}else{
		j=0;
		arraySize = (*env)->GetArrayLength(env, mask);
	}

	jint* pMask = (jint*)(*env)->GetPrimitiveArrayCritical(env,mask,0);
	for(;j < arraySize; j++){
		i = pMask[j];
		pResult[i] = arg1 + pArg2[i];
	}
	(*env)->ReleasePrimitiveArrayCritical(env,mask, pMask, 0);
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_EQIntArrayContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jbooleanArray result) {

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	jint i,j=0;

	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] == pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_EQIntArrayConstContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jbooleanArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] == arg2;
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_EQIntConstArrayContigousMask(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jbooleanArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg2);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = arg1 == pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_LTIntArrayContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jbooleanArray result) {

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);

	jint i,j=0;

	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] < pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_LTIntArrayConstContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jbooleanArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] < arg2;
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_LTIntConstArrayContigousMask(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jbooleanArray result) {

	jint i,j=0;
	
	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg2);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = arg1 < pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_GTIntArrayContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jbooleanArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] > pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_GTIntArrayConstContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jbooleanArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] > arg2;
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_GTIntConstArrayContigousMask(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jbooleanArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jboolean* pResult = (jboolean*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg2);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = arg1 > pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_ADDIntArrayContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jintArray arg2,
	jintArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jint* pResult = (jint*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] + pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_ADDIntArrayConstContigousMask(JNIEnv *env,jclass clazz, 
	jintArray arg1, 
	jint arg2,
	jintArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg1 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg1,0);	
	jint* pResult = (jint*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg1);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = pArg1[i] + arg2;
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg1, pArg1, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}


JNIEXPORT void JNICALL Java_nativedelta_NativeDelta_ADDIntConstArrayContigousMask(JNIEnv *env,jclass clazz, 
	jint arg1, 
	jintArray arg2,
	jintArray result) {

	jint i,j=0;

	//getting arrays
	jint* pArg2 = (jint*)(*env)->GetPrimitiveArrayCritical(env,arg2,0);	
	jint* pResult = (jint*)(*env)->GetPrimitiveArrayCritical(env,result,0);


	//iterating
	jsize arraySize = (*env)->GetArrayLength(env, arg2);
	for(; j < arraySize; j++){
		i = j;
		pResult[i] = arg1 + pArg2[i];
	}
    
	//releasing arrays
	(*env)->ReleasePrimitiveArrayCritical(env,arg2, pArg2, 0);	
	(*env)->ReleasePrimitiveArrayCritical(env,result, pResult, 0);
}
