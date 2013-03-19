/*cygwin compliation string:
gcc -c -msse -I"C:\cygwin\usr\include\w32api" -I"C:\Program Files\Java\jdk1.6.0_03\include" -I"C:\Program Files\Java\jdk1.6.0_03\include\win32" -o SSELibrary.o SSELibrary.c &> error.log
Produces error.log which contains a list of compliation errors redirected from stderr
*/

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "..\NativeLibrary.h"

jbyte** byteArrays; //8-bit signed type missing :(
int nextbyteArray;
jchar** charArrays; 	//8-bit unsigned
int nextcharArray;
jdouble** doubleArrays; //64-bit floats
int nextdoubleArray;
jfloat** floatArrays;	//32-bit floats
int nextfloatArray;
jint** intArrays;		//32-bit ints
int nextintArray;
jshort** shortArrays;	//16-bit ints
int nextshortArray;
jlong** longArrays; 	//64-bit ints
int nextlongArray;

jsize sourceListALength;
jint* sourceListA;
jsize sourceListBLength;
jint* sourceListB;
jsize destinationListLength;
jint* destinationList;
jsize executionListLength;
jint* executionList;

JNIEXPORT void JNICALL Java_NativeLibrary_SendSourceAList
  (JNIEnv * env, jobject jObj, jintArray sourceListAArg)
  {
  	sourceListALength = (*env)->GetArrayLength(env, sourceListAArg);
  	sourceListA = (*env)->GetIntArrayElements(env,sourceListAArg, 0);
  }
JNIEXPORT void JNICALL Java_NativeLibrary_SendSourceBList
  (JNIEnv * env, jobject jObj, jintArray sourceListBArg)
  {
  	sourceListBLength = (*env)->GetArrayLength(env,sourceListBArg);
  	sourceListB = (*env)->GetIntArrayElements(env,sourceListBArg,0);
  }
JNIEXPORT void JNICALL Java_NativeLibrary_SendDestinationList
  (JNIEnv * env, jobject jObj, jintArray destinationListArg)
  {
  	destinationListLength = (*env)->GetArrayLength(env,destinationListArg);
  	destinationList = (*env)->GetIntArrayElements(env,destinationListArg,0);
  }
JNIEXPORT void JNICALL Java_NativeLibrary_SendExecutionList
  (JNIEnv * env, jobject jObj, jintArray executionListArg)
  {
  	executionListLength = (*env)->GetArrayLength(env,executionListArg);
	executionList = (*env)->GetIntArrayElements(env,executionListArg,0);

  }
JNIEXPORT void JNICALL Java_NativeLibrary_SendByteArray
  (JNIEnv * env, jobject jObj, jbyteArray Array, jint numberOfArrays)
  {
  	jboolean isCopy = JNI_FALSE;
  	if(byteArrays==NULL)
  	{
  		*byteArrays = malloc(numberOfArrays*sizeof(jbyte*));
  		nextbyteArray = 0;
  	}
  	byteArrays[nextbyteArray++] = (*env)->GetByteArrayElements(env, Array, &isCopy);
  }
  
JNIEXPORT void JNICALL Java_NativeLibrary_SendCharArray
  (JNIEnv * env, jobject jObj, jcharArray Array, jint numberOfArrays)
  {
  	jboolean isCopy = JNI_FALSE;
  	if(charArrays==NULL)
  	{
  		*charArrays = malloc(numberOfArrays*sizeof(jchar*));
  		nextcharArray = 0;
  	}
  	charArrays[nextcharArray++] = (*env)->GetCharArrayElements(env, Array, &isCopy);
  }
  
JNIEXPORT void JNICALL Java_NativeLibrary_SendDoubleArray
  (JNIEnv * env, jobject jObj, jdoubleArray Array, jint numberOfArrays)
  {
  	jboolean isCopy = JNI_FALSE;
  	if(doubleArrays==NULL)
  	{
  		*doubleArrays = (jdouble*)malloc(numberOfArrays*sizeof(jdouble*));
  		nextdoubleArray = 0;
  	}
  	doubleArrays[nextdoubleArray++] = (*env)->GetDoubleArrayElements(env, Array, &isCopy);
  }
 
JNIEXPORT void JNICALL Java_NativeLibrary_SendFloatArray
  (JNIEnv * env, jobject jObj, jfloatArray Array, jint numberOfArrays)
  {
  	jboolean isCopy = JNI_FALSE;
  	  	if(floatArrays==NULL)
  	{
  		*floatArrays = (jfloat*)malloc(numberOfArrays*sizeof(jfloat*));
  		nextfloatArray = 0;
  	}
  	floatArrays[nextfloatArray++] = (*env)->GetFloatArrayElements(env, Array, &isCopy);
  }
  
JNIEXPORT void JNICALL Java_NativeLibrary_SendIntArray
  (JNIEnv * env, jobject jObj, jintArray Array, jint numberOfArrays)
    {
    	jboolean isCopy = JNI_FALSE;
  	  	if(intArrays==NULL)
  	{
  		*intArrays = malloc(numberOfArrays*sizeof(jint*));
  		nextintArray = 0;
  	}
  	intArrays[nextintArray++] = (*env)->GetIntArrayElements(env, Array, &isCopy);
  }
JNIEXPORT void JNICALL Java_NativeLibrary_SendLongArray
  (JNIEnv * env, jobject jObj, jlongArray Array, jint numberOfArrays)
  {
  	jboolean isCopy = JNI_FALSE;
  	 if(longArrays==NULL)
  	{
  		*longArrays = (jlong*)malloc(numberOfArrays*sizeof(jlong*));
  		nextlongArray = 0;
  	}
  	longArrays[nextlongArray++] = (*env)->GetLongArrayElements(env, Array, &isCopy);
  }
JNIEXPORT void JNICALL Java_NativeLibrary_SendShortArray
  (JNIEnv * env, jobject jObj, jshortArray Array, jint numberOfArrays)
{
	jboolean isCopy = JNI_FALSE;  
    	  	if(shortArrays==NULL)
  	{
  		*shortArrays = (jshort*)malloc(numberOfArrays*sizeof(jshort*));
  		nextshortArray = 0;
  	}
  	shortArrays[nextshortArray++] = (*env)->GetShortArrayElements(env, Array, &isCopy);
}

JNIEXPORT void JNICALL Java_NativeLibrary_Execute (JNIEnv * env, jobject jObj)
{
	//Populate Arrays of Function Calls
	//Some windowing scheduling code would be nice here.
	//LOOP
		//Peel off bits of the arrays and pack them into SIMD registers
		//Call offset into array of functions
}

/*
 * 						MMXFunctions
 */

//	public void Add(PackedByte A, PackedByte B, PackedByte C);
	//mmxPADDB(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
	
//	public void Add(PackedInt A, PackedInt B, PackedInt C);
	//mmxPADDD(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Add(PackedShort A, PackedShort B, PackedShort C);
	//mmxPADDW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void AddS(PackedByte A, PackedByte B, PackedByte C);
	//mmxPADDSB(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void AddS(PackedShort A, PackedShort B, PackedShort C);
	//mmxPADDSW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void And(PackedLong A, PackedLong B, PackedLong C);
	//mmxPAND ((jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Equal(PackedByte A, PackedByte B, PackedByte C);
	//mmxPCMPEQB(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Equal(PackedInt A, PackedInt B, PackedInt C);
	//mmxPCMPEQD(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Equal(PackedShort A, PackedShort B, PackedShort C);
	//mmxPCMPEQW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void GreaterThan(PackedByte A, PackedByte B, PackedByte C);
	//mmxPCMPGTB(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void GreaterThan(PackedInt A, PackedInt B, PackedInt C);
	//mmxPCMPGTD(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void GreaterThan(PackedShort A, PackedShort B, PackedShort C);
	//mmxPCMPGTW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void MAcc(PackedShort A, PackedShort B, PackedInt C);
	//mmxPMADDWD(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void MultH(PackedShort A, PackedShort B, PackedShort C);
	//mmxPMULHW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void MultL(PackedShort A, PackedShort B, PackedShort C);
	//mmxPMULLW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Nand(PackedLong A, PackedLong B, PackedLong C);
	//mmxPANDN (jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Or(PackedLong A, PackedLong B, PackedLong C);
	//mmxPOR (jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void SLL(PackedInt A, int count, PackedInt C);
	//mmxPSLLD(jobjectArray A,  jobject count, jobjectArray returnValue)

//	public void SLL(PackedLong A, int count, PackedLong C);
	//mmxPSLLQ(jobjectArray A,  jobject count, jobjectArray returnValue)

//	public void SLL(PackedShort A, int count, PackedShort C);
	//mmxPSLLW(jobjectArray A,  jobject count, jobjectArray returnValue)

//	public void SRA(PackedInt A, int count, PackedInt C);
	//mmxPSRAW(jobjectArray A,  jobject count, jobjectArray returnValue)

//	public void SRA(PackedLong A, int count, PackedLong C);
	//mmxPSRAD(jobjectArray A,  jobject count, jobjectArray returnValue)
	
//	public void SRL(PackedInt A, int count, PackedInt C);
	//mmxPSRLD(objectArray A,  jobject count, jobjectArray returnValue)
	
//	public void SRL(PackedLong A, int count, PackedLong C);
	//mmxPSRLQ(objectArray A,  jobject count, jobjectArray returnValue)
	
//	public void SRL(PackedShort A, int count, PackedShort C);
	//mmxPSRLW(jobjectArray A,  jobject count, jobjectArray returnValue)
	
//	public void Sub(PackedByte A, PackedByte B, PackedByte C);
	//mmxPSUBB(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void Sub(PackedInt A, PackedInt B, PackedInt C);
	//mmxPSUBD(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	 
//	public void Sub(PackedShort A, PackedShort B, PackedShort C);
	//mmxPSUBW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void SubS(PackedByte A, PackedByte B, PackedByte C);
	//mmxPSUBSB(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void SubS(PackedChar A, PackedChar B, PackedChar C);
	//mmxPSUBUSW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void SubS(PackedShort A, PackedShort B, PackedShort C);
	//mmxPSUBSW(jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
//	public void XOr(PackedLong A, PackedLong B, PackedLong C);
	//mmxPXOR (jobjectArray A, jobjectArray B, jobjectArray returnValue)
	
/*
 *						SSE Functions
 */
//	public void Add(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void And(PackedFloat A, PackedFloat B, PackedFloat C);
//	public char Avg(PackedChar A);
//	public void Divide(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Equal(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void GreaterThan(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void GreaterThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void LessThan(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void LessThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Max(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Max(PackedShort A, PackedShort B, PackedShort C);
//	public void Min(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Min(PackedShort A, PackedShort B, PackedShort C);
//	public void Mult(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Nand(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void NotEqual(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Or(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Ordered(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Recip(PackedFloat A, PackedFloat C);
//	public void RecipSqrt(PackedFloat A, PackedFloat C);
//	public void Shuf(PackedFloat A, PackedFloat B, int mask, PackedFloat C);
//	public void Sqrt(PackedFloat A, PackedFloat C);
//	public void Sub(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Unordered(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void XOr(PackedFloat A, PackedFloat B, PackedFloat C);

 /*
  *						SSE2 Functions
  */
//	public void Add(PackedByte A, PackedByte B, PackedByte C);
//	public void Add(PackedInt A, PackedInt B, PackedInt C);
//	public void Add(PackedShort A, PackedShort B, PackedShort C);
//	public void AddS(PackedByte A, PackedByte B, PackedByte C);
//	public void AddS(PackedShort A, PackedShort B, PackedShort C);
//	public void And(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void Equal(PackedByte A, PackedByte B, PackedByte C);
//	public void Equal(PackedInt A, PackedInt B, PackedInt C);
//	public void Equal(PackedShort A, PackedShort B, PackedShort C);
//	public void GreaterThan(PackedByte A, PackedByte B, PackedByte C);
//	public void GreaterThan(PackedInt A, PackedInt B, PackedInt C);
//	public void GreaterThan(PackedShort A, PackedShort B, PackedShort C);
//	public void LessThan(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void Max(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Max(PackedShort A, PackedShort B, PackedShort C);
//	public void Min(PackedShort A, PackedShort B, PackedShort C);
//	public void Mult(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void Mult(PackedInt A, PackedInt B, PackedLong C);
//	public void MultH(PackedShort A, PackedShort B, PackedShort C);
//	public void MultL(PackedShort A, PackedShort B, PackedShort C);
//	public void Or(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void SLL(PackedInt A, int count, PackedInt C);
//	public void Sqrt(PackedDouble A, PackedDouble C);
//	public void SRA(PackedInt A, int count, PackedInt C);
//	public void Sub(PackedByte A, PackedByte B, PackedByte C);
//	public void Sub(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void Sub(PackedInt A, PackedInt B, PackedInt C);
//	public void Sub(PackedShort A, PackedShort B, PackedShort C);
//	public void SubS(PackedByte A, PackedByte B, PackedByte C);
//	public void SubS(PackedChar A, PackedChar B, PackedChar C);
//	public void SubS(PackedShort A, PackedShort B, PackedShort C);
// 	public void XOr(PackedDouble A, PackedDouble B, PackedDouble C);

 
 /*
  *						SSE3 Functions
  */
 
//	public void AddSub(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void AddSub(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void HAdd(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void HAdd(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void HSub(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void HSub(PackedFloat A, PackedFloat B, PackedFloat C);
 
 /*
  *						SSE4.1 Functions
  */
  
//	public void Blend(PackedByte A, PackedByte B, PackedByte Mask, PackedByte C);
//	public void Blend(PackedDouble A, PackedDouble B, PackedDouble Mask, PackedDouble C);
//	public void Blend(PackedFloat A, PackedFloat B, PackedFloat Mask, PackedFloat C);
//	public void Blend(PackedInt A, PackedInt B, PackedInt Mask, PackedInt C);
//	public void Ceil(PackedDouble A, PackedDouble C);
//	public void Ceil(PackedFloat A, PackedFloat C);
//	public void Equal(PackedLong A, PackedLong B, PackedLong C);
//	public void Floor(PackedDouble A, PackedDouble C);
//	public void Floor(PackedFloat A, PackedFloat C);
//	public void GreaterThan(PackedLong A, PackedLong B, PackedLong C);
//	public void MAcc(PackedDouble A, PackedDouble B, PackedDouble C);
//	public void MAcc(PackedFloat A, PackedFloat B, PackedFloat C);
//	public void Max(PackedByte A, PackedByte B, PackedByte C);
//	public void Max(PackedInt A, PackedInt B, PackedInt C);
//	public void Min(PackedByte A, PackedByte B, PackedByte C);
//	public void Min(PackedInt A, PackedInt B, PackedInt C);
  
 /*
  *						SSE4.2 Functions
  */
  
// 	public int PopCount(int A);
//	public int PopCount(long A);