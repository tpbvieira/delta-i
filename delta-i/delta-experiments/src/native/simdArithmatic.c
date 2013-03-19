#include <jni.h>
#include <emmintrin.h>
#include <xmmintrin.h>
#include "simdArithmatic.h"
#include <malloc.h>
#include <stdio.h>


/* need more work for array less than 4 element*/
//JNIEXPORT void JNICALL Java_simdMapper_simdArithmatic_addIntegers
JNIEXPORT void JNICALL Java_simdMapper_simdArithmatic_addIntegers
  (JNIEnv *env , jobject jobj, jintArray v01, jintArray v02, jintArray v03)
{
    jboolean isCopy = JNI_FALSE;
	jsize len = (*env)->GetArrayLength(env, v01);

	jint* op1 =(*env)->GetIntArrayElements(env, v01,&isCopy);
	jint* op2 =(*env)->GetIntArrayElements(env, v02,&isCopy);
	jint* vec3=(*env)->GetIntArrayElements(env, v03,&isCopy);

	int *pResult = (int*) _aligned_malloc(len * sizeof(int), 16);

	__m128i *pResultSSE = (__m128i*) pResult;
	__m128 x,y;
	__m128i	tmpData1,tmpData2,data1,data2,const_zero,data11,data12;
	__m128i	*result_data;

	const int iterationMax=len/4;
	//const int iterationMax=len;
	int count;
	int base=0;

    int j;
    for (j=0; j < len; j++){
	printf("C program: v1[%d] = %d  ---  v2[%d] = %d\n", j, op1[j], j, op2[j]);
    }
    printf("\n");	
 
    for(count=0;count<iterationMax;count++)
	{
		tmpData1 = _mm_loadu_si128((const __m128i *)(&op1[base])); 
        tmpData2 = _mm_loadu_si128((const __m128i *)(&op2[base]));
        pResultSSE[count] = _mm_add_epi32(tmpData1,tmpData2);
        base+=4;
		int* p = ((int*)(pResultSSE));
		p+=count;
		printf("zzzzzz p1=%d p2=%d p3=%d p4=%d\n", p[0], p[1], p[2], p[3]);
	}
    
	int leftOver=len-base;  
	printf("\nafter loop:::::: leftOver=%d len=%d base=%d count=%d\n", leftOver, len, base, count);

	if(leftOver<4 && leftOver>0)
	{
		int tmpArray1[4], tmpArray2[4];
		int index, opIndex=base;

		for(index=0; index<4; index++)
		{  
			if(index < leftOver){
				tmpArray1[index]=op1[opIndex];
				tmpArray2[index]=op2[opIndex];
				opIndex++;
			}
			else{
				tmpArray1[index]=0;
				tmpArray2[index]=0;
				opIndex++;
			}
		}

		tmpData1 = _mm_loadu_si128((const __m128i *)(&tmpArray1[0]));
        tmpData2 = _mm_loadu_si128((const __m128i *)(&tmpArray2[0]));
        pResultSSE[count] = _mm_add_epi32(tmpData1,tmpData2);
	}

	
	int i;
	for (i=0; i < len; i++)
		//vec3[i]=(jint)((int)pResult[i]);
        vec3[i]=pResult[i];
	//Unpin the arrays
	(*env)->ReleaseIntArrayElements(env, v01,op1, 0);
	(*env)->ReleaseIntArrayElements(env, v02, op2,0);
	(*env)->ReleaseIntArrayElements(env, v03, vec3,0);
	_aligned_free(pResult);
}

  
//JNIEXPORT void JNICALL Java_simdArithmatic_multIntegers
JNIEXPORT void JNICALL Java_simdMapper_simdArithmatic_multIntegers
  (JNIEnv *env, jobject jobj, jintArray v01, jintArray v02, jlongArray v03)
{
    jboolean isCopy = JNI_FALSE;
	jsize len = (*env)->GetArrayLength(env, v01);
	jint* op1 =(*env)->GetIntArrayElements(env, v01,&isCopy);
	jint* op2 =(*env)->GetIntArrayElements(env, v02,&isCopy);
	jlong* vec3=(*env)->GetLongArrayElements(env, v03,&isCopy);

	long long *pResult = (long long *) _aligned_malloc(len * sizeof(long long), 16);

	__m128i *pResultSSE = (__m128i*) pResult;
	__m128i x,y;
	__m128i	tmpData1,tmpData2,op1Data1,op1Data2,op2Data1, op2Data2,const_zero,data11,data12,data1,data2;
	__m128i	*result_data;

	//const int iterationMax=len/4;
	const int iterationMax=len/4;

	int count;
	int base=0;
	
int j;
for (j=0; j < len; j++){
        printf("v1[%d] = %d  ---  v2[%d] = %d\n", j, op1[j], j, op2[j]);
}
    //need more work for the array len
	for(count=0;count<iterationMax;(count+=2))
	{
		tmpData1 = _mm_loadu_si128((const __m128i *)(&op1[base])); 
        tmpData2 = _mm_loadu_si128((const __m128i *)(&op2[base]));

		data1 = _mm_unpacklo_epi32(tmpData1, const_zero);
		data2 = _mm_unpacklo_epi32(tmpData2,const_zero);

		x = _mm_mul_epu32(data1,data2);
		pResultSSE[count]=x;
				
		data1 = _mm_unpackhi_epi32(tmpData1, const_zero);
		data2 = _mm_unpackhi_epi32(tmpData2,const_zero);
        
        y = _mm_mul_epu32(data1,data2);
        pResultSSE[count+1]=y;

        base+=4;
	}
	
	int i;
	for (i=0; i < len; i++) {
		//vec3[i]=(jlong)(pResult[i]);
		vec3[i]=pResult[i];
		printf("vec3 = %d\n", vec3[i]);
	}

	//Unpin the arrays
	(*env)->ReleaseIntArrayElements(env, v01,op1, 0);
	(*env)->ReleaseIntArrayElements(env, v02, op2,0);
	(*env)->ReleaseLongArrayElements(env, v03, vec3,0);
	_aligned_free(pResult);
	
}
