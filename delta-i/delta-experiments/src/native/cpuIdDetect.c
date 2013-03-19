#include <jni.h>
#include "cpuIdDetect.h"
#include <stdio.h>

#define cpuid(func,ax,bx,cx,dx)\
		__asm__ __volatile__ ("cpuid":\
		"=a" (ax), "=b" (bx), "=c" (cx), "=d" (dx) : "a" (func));


JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_mmxIsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(edx & 1<<23)
		return simdMapper_cpuIdDetect_CPU_FEATURE_MMX;
	return 0;
}

JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_sseIsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(edx & 1<<25)
		return simdMapper_cpuIdDetect_CPU_FEATURE_SSE;
	return 0;
}

/*
 */
JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_sse2IsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(edx & 1<<26)
		return simdMapper_cpuIdDetect__CPU_FEATURE_SSE2;
	return 0;
}

/*
 */
JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_sse3IsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(ecx & (1))
		return simdMapper_cpuIdDetect__CPU_FEATURE_SSE3;
	return 0;
}

/*
 */
JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_ssse3IsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(ecx & (1<<9))
		return simdMapper_cpuIdDetect__CPU_FEATURE_SSSE3;
	return 0;
}

/*
 */
JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_sse41IsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(ecx & (1<<19))
		return simdMapper_cpuIdDetect__CPU_FEATURE_SSE41;
	return 0;
}

/*
 */
JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_sse42IsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	if(ecx & (1<<20))
		return simdMapper_cpuIdDetect__CPU_FEATURE_SSE42;
	return 0;
}


JNIEXPORT jint JNICALL Java_simdMapper_cpuIdDetect_avxIsSupported
  (JNIEnv *env, jobject jobj)
{
	int eax,ebx,ecx,edx;
	cpuid(1,eax,ebx,ecx,edx);
	/*if(ecx & (1<<20))
		return simdMapper_cpuIdDetect__CPU_FEATURE_AVX;*/
	return 0;
}

