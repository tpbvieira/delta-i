/*cygwin compliation string:
gcc -c -msse -I"C:\cygwin\usr\include\w32api" -I"C:\Program Files\Java\jdk1.6.0_03\include" -I"C:\Program Files\Java\jdk1.6.0_03\include\win32" -o SSELibrary.o SSELibrary.c &> error.log
Produces error.log which contains a list of compliation errors redirected from stderr
*/

#define TEST 0
#if TEST!=1
#include <jni.h>
#endif

#include <emmintrin.h>
#include <xmmintrin.h>
#include <smmintrin.h>
#include <assert.h>
#include <stdio.h>
#include <time.h>
#include <pthread.h>
#include <sys/time.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>

#if !TEST
#include "NativeLibrary.h"
#endif

char* Vector_Class_Name = "java/util/Vector";
char* ExecutionList_Class_Name = "jSIMD/ExecutionList";
char* ExecutionItem_Class_Name = "jSIMD/ExecutionItem";
char* Vector_Field_Type = "Ljava/util/Vector;";
char* Items_Field_Name = "Items";
char* Opcode_Field_Name = "Opcode";
char* SourceA_Field_Name = "SourceA";
char* SourceB_Field_Name = "SourceB";
char* arg_Field_Name = "arg";
char* Destination_Field_Name = "Destination";
char* ObjectArrays_Field_Name = "ObjectArrays";
char* ObjectArrays_MaxSizeFieldName = "ObjectArraysMaxSize";

#if TEST
#include <stdio.h>
#include <stdarg.h>
#include <string.h>

int IntegerArray0[10] = {1,1,1,1,1,1,1,1,1,1};
int IntegerArray1[10] = {1,1,1,1,1,1,1,1,1,1};
int IntegerArray2[10] = {1,1,1,1,1,1,1,1,1,1};

#define JNICALL          __stdcall
typedef unsigned char	jboolean;
typedef unsigned short	jchar;
typedef short		jshort;
typedef float		jfloat;
typedef double		jdouble;
typedef int 		jint;
typedef char		jbyte;
typedef long		jlong;
typedef jint            jsize;


struct _jobject
{
	char* name;
};

typedef struct _jobject *jobject;
typedef jobject jclass;
typedef jobject jstring;
typedef jobject jarray;
typedef jarray jbooleanArray;
typedef jarray jbyteArray;
typedef jarray jcharArray;
typedef jarray jshortArray;
typedef jarray jintArray;
typedef jarray jlongArray;
typedef jarray jfloatArray;
typedef jarray jfloatArray;
typedef jarray jdoubleArray;
typedef jarray jobjectArray;

struct _jfieldID
{
	char* name;
	char* type;
};
typedef struct _jfieldID *jfieldID;

struct _jmethodID
{
	char* name;
	char* type;
};
typedef struct _jmethodID *jmethodID;
#define JNI_FALSE 0
#define JNI_TRUE 1
typedef struct {
	char *name;
	char *signature;
	void *fnPtr;
} JNINativeMethod;

struct JNINativeInterface_;

struct JNIEnv_;

typedef const struct JNINativeInterface_ *JNIEnv;

struct JNINativeInterface_ {
	//TODO Populate Native Calls
	jclass (*FindClass)(JNIEnv *env, char* string);
	jfieldID (*GetFieldID)(JNIEnv *env, jclass class, char* str1, char* str2);
	jint (*GetIntField)(JNIEnv *env, jobject executionList, jfieldID maxSizeField);
	jobject (*GetObjectField)(JNIEnv *env, jobject executionList, jfieldID arrayField);
	jmethodID (*GetMethodID)(JNIEnv *env, jclass cla, char* str1, char* str2);
	jint (*CallIntMethod)(JNIEnv *env, jobject obj, jmethodID meth);
	jint (*GetArrayLength)(JNIEnv *env, jintArray vect);
	jobject (*CallObjectMethod)(JNIEnv *env, jobject itemsVector,jmethodID getVectorElement,jint iteration);
	jbyte* (*GetByteArrayElements)(JNIEnv *env, jbyteArray intVectA,jboolean IsCopy);
	jchar* (*GetCharArrayElements)(JNIEnv *env, jcharArray intVectA, jboolean IsCopy);
	jdouble* (*GetDoubleArrayElements)(JNIEnv *env, jdoubleArray intVectA, jboolean IsCopy);
	jfloat* (*GetFloatArrayElements)(JNIEnv *env, jfloatArray intVectA, jboolean IsCopy);
	jint* (*GetIntArrayElements) (JNIEnv *env,jintArray intVectA, jboolean IsCopy);
	jshort* (*GetShortArrayElements)(JNIEnv *env, jshortArray intVectA, jboolean IsCopy);
	jlong* (*GetLongArrayElements)(JNIEnv *env, jlongArray intVectA, jboolean IsCopy);
	void (*ReleaseIntArrayElements)(JNIEnv *env,jintArray priorArray,jint* curIntArray,int IsCopy);
};

#endif
struct UsedArrays{
	int type;
	int number;
	int* curArrayPointer;
	__m128i*curVector;
	int vectorSize;
	jintArray javaArray;
	int arraySize;
	int isNew;
};
struct UsedArrays tmpArray;

struct ExecutionElement{
	int srcA;
	int srcB;
	int dest;
};

enum OPERATION {
	ADD = 0,
	ADDS = 1,
	ADDSUB = 2,
	AND = 3,
	AVG = 4,
	BLEND = 5,
	CEIL = 6,
	EQUAL = 7,
	FLOOR = 8,
	GREATERTHAN = 9,
	GREATERTHANOREQUAL = 10,
	HORIZADD = 11,
	LESSTHATOREQUAL = 12,
	MACC = 13,
	MAX = 14,
	MIN = 15,
	MULT = 16,
	MULTH = 17,
	MULTL = 18,
	NAND = 19,
	NOTEQUAL = 20,
	OR = 21,
	ORDERED = 22,
	ROUND = 23,
	SHUF = 24,
	SHUFL = 25,
	SHUFH = 26,
	SLL = 27,
	SQRT = 28,
	SRA = 29,
	SRL = 30,
	SUB = 31,
	SUBS = 32,
	SUMABSDIFF = 33,
	UNORDERED = 34,
	XOR = 35,
	DIV = 36,
	RECIP = 37,
	RECIPSQRT = 38,
	LESSTHAN = 39,
	HORIZSUB = 40,
	POPCOUNT = 41,
	INTERLEAVELOW = 42,
	INTERLEAVEHIGH = 43
};

enum DATATYPE {
	BYTE = 0,
	CHAR,
	DOUBLE,
	FLOAT,
	INT,
	LONG,
	SHORT,
	UNKNOWNN,
	CONSTBYTE,
	CONSTCHAR,
	CONSTDOUBLE,
	CONSTFLOAT,
	CONSTINT,
	CONSTLONG,
	CONSTSHORT
};

enum OPERATION getOperation(int opcode) {
	return opcode >> 12;
}

#if !TEST
JavaVM cached_jvm;
#endif
//TYPO JavaVM not javaVM;



//Function Prototypes
struct ExecutionElement CreateExecutionItem(JNIEnv*,int);
void ExtractClassDefintions(JNIEnv*);
void ExtractFieldPointers(JNIEnv *env);
int ExtractArrays(JNIEnv* env,jobject executionList);
void GetVectorMethods(JNIEnv* env);
void ExtractItemsVector(JNIEnv * env, jobject executionList);
struct ExecutionElement CreateExecutionItem(JNIEnv* env,	int iteration);
struct UsedArrays ProcessArray(JNIEnv *env, int Source, __m128i * pSrcA, int* size, int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize, int type);
struct UsedArrays ProcessIntArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessByteArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessCharArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessDoubleArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessFloatArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessLongArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessShortArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
struct UsedArrays ProcessConstIntArray(JNIEnv *env, int Arg, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize);
//void Java_simdMapper_NativeLibrary_Execute(JNIEnv *env,jclass clazz, jobject executionList);
 

#if TEST
jclass FindClassFunction(JNIEnv *env, char* string);
jfieldID GetFieldIDFunction(JNIEnv *env, jclass class, char* str1, char* str2);
jint GetIntFieldFunction(JNIEnv *env, jobject executionList, jfieldID maxSizeField);
jobject GetObjectFieldFunction(JNIEnv *env, jobject executionList, jfieldID arrayField);
jmethodID GetMethodIDFunction(JNIEnv *env, jclass cla, char* str1, char* str2);
jint CallIntMethodFunction(JNIEnv *env, jobject obj, jmethodID meth);
jint GetArrayLengthFunction(JNIEnv *env, jintArray vect);
jobject CallObjectMethodFunction(JNIEnv *env, jobject itemsVector,jmethodID getVectorElement,jint iteration);
jbyte* GetByteArrayElementsFunction(JNIEnv *env, jbyteArray intVectA,jboolean IsCopy);
jchar* GetCharArrayElementsFunction(JNIEnv *env, jcharArray intVectA, jboolean IsCopy);
jdouble* GetDoubleArrayElementsFunction(JNIEnv *env, jdoubleArray intVectA, jboolean IsCopy);
jfloat* GetFloatArrayElementsFunction(JNIEnv *env, jfloatArray intVectA, jboolean IsCopy);
jint* GetIntArrayElementsFunction(JNIEnv *env,jintArray intVectA, jboolean IsCopy);
jshort* GetShortArrayElementsFunction(JNIEnv *env, jshortArray intVectA, jboolean IsCopy);
jlong* GetLongArrayElementsFunction(JNIEnv *env, jlongArray intVectA, jboolean IsCopy);
void ReleaseIntArrayElementsFunction(JNIEnv *env,jintArray priorArray,jint* curIntArray,int IsCopy);

jclass FindClassFunction(JNIEnv *env, char* string)
{

	jclass retVal;
	retVal = (jclass)malloc(sizeof(struct _jobject));
	retVal->name = string;
	return retVal;
}

jfieldID GetFieldIDFunction(JNIEnv *env, jclass class, char* str1, char* str2)
{
	jfieldID retVal;
	retVal = (jfieldID)malloc(sizeof(struct _jfieldID));
	retVal->name = str1;
	retVal->type = str2;
	return retVal;
}
jint GetIntFieldFunction(JNIEnv *env, jobject executionList, jfieldID maxSizeField)
{
	jint retVal = 0;
	if(strcmp(maxSizeField->name,"ObjectArraysMaxSize")==0)
	{
		retVal = 10;
	}
	else if(strcmp(executionList->name,"ExecutionItem0")==0)
	{
		//add A + B = C; C contains (2,2,2,2...)
		if(strcmp(maxSizeField->name,"Opcode")==0)
			retVal = (ADD<<12)|0x444;
		else if(strcmp(maxSizeField->name,"SourceB")==0)
			retVal = 1;
		else if(strcmp(maxSizeField->name,"SourceA")==0)
			retVal = 0;
		else if(strcmp(maxSizeField->name,"Destination")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"arg")==0)
			retVal = 0;
	}
	else if(strcmp(executionList->name,"ExecutionItem1")==0)
	{
		//add C + B = A; A contains (3,3,3,...)
		if(strcmp(maxSizeField->name,"Opcode")==0)
			retVal = (ADD<<12)|0x444;
		else if(strcmp(maxSizeField->name,"SourceB")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"SourceA")==0)
			retVal = 1;
		else if(strcmp(maxSizeField->name,"Destination")==0)
			retVal = 0;
		else if(strcmp(maxSizeField->name,"arg")==0)
			retVal = 0;
	}
	else if(strcmp(executionList->name,"ExecutionItem2")==0)
	{
		//add C + C = B; B contains(4,4,4,...)
		if(strcmp(maxSizeField->name,"Opcode")==0)
			retVal = (ADD<<12)|0x444;
		else if(strcmp(maxSizeField->name,"SourceB")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"SourceA")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"Destination")==0)
			retVal = 1;
		else if(strcmp(maxSizeField->name,"arg")==0)
			retVal = 0;
	}
	else if(strcmp(executionList->name,"ExecutionItem4")==0)
	{
		//add B + A = C; C contains(7,7,7,...)
		if(strcmp(maxSizeField->name,"Opcode")==0)
			retVal = (ADD<<12)|0x444;
		else if(strcmp(maxSizeField->name,"SourceB")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"SourceA")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"Destination")==0)
			retVal = 1;
		else if(strcmp(maxSizeField->name,"arg")==0)
			retVal = 0;
	}
	else if(strcmp(executionList->name,"ExecutionItem5")==0)
	{
		//add C + A = B; B contains(9,9,9,...)
		if(strcmp(maxSizeField->name,"Opcode")==0)
			retVal = (ADD<<12)|0x444;
		else if(strcmp(maxSizeField->name,"SourceB")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"SourceA")==0)
			retVal = 2;
		else if(strcmp(maxSizeField->name,"Destination")==0)
			retVal = 1;
		else if(strcmp(maxSizeField->name,"arg")==0)
			retVal = 0;
	}
	return retVal;
}
jobject GetObjectFieldFunction(JNIEnv *env, jobject executionList, jfieldID arrayField)
{
	jobject retVal;
	retVal = (jclass)malloc(sizeof(struct _jobject));
	if(strcmp(arrayField->name,"ObjectArrays")==0)
	{
		retVal->name = "ObjectArray1";
	}
	else if(strcmp(arrayField->name,"Items")==0)
	{
		retVal->name = "ItemsArray1";
	}
	return retVal;
}

jmethodID GetMethodIDFunction(JNIEnv *env, jclass cla, char* str1, char* str2)
{
	jmethodID retVal;
	retVal = (jmethodID)malloc(sizeof(struct _jmethodID));
	retVal->name = str1;
	retVal->type = str2;
	return retVal;
}

jint CallIntMethodFunction(JNIEnv *env, jobject obj, jmethodID meth)
{
	jint retVal;
	if(strcmp(meth->name,"size")==0)
		retVal =5;
	return retVal;
}
jint GetArrayLengthFunction(JNIEnv *env, jintArray vect)
{
	jint retVal = 10;
	return retVal;
}

jobject CallObjectMethodFunction(JNIEnv *env, jobject itemsVector,jmethodID getVectorElement,jint iteration)
{
	jobject retVal;
	retVal = (jobject)malloc(sizeof(struct _jobject));
	//ItemsArray1
	if(strcmp(itemsVector->name,"ItemsArray1")==0)
	{

		switch(iteration)
		{	
		case 0:
			retVal->name = "ExecutionItem0";
			break;
		case 1:
			retVal->name = "ExecutionItem1";
			break;
		case 2:
			retVal->name = "ExecutionItem2";
			break;
		case 3:
			retVal->name = "ExecutionItem3";
			break;
		case 4:
			retVal->name = "ExecutionItem4";
			break;
		case 5:
			retVal->name = "ExecutionItem5";
		default:
			break;
		}
	}
	//ObjectArray1
	if(strcmp(itemsVector->name,"ObjectArray1")==0)
	{
		switch (iteration)
		{
		case 0:
			retVal->name = "IntegerArray0";
			break;
		case 1:
			retVal->name = "IntegerArray1";
			break;
		case 2:
			retVal->name = "IntegerArray2";
			break;
		case 3:
			retVal->name = "IntegerArray3";
			break;
		case 4:
			retVal->name = "IntegerArray4";
			break;
		case 5:
			retVal->name = "IntegerArray5";
			break;
		}
	}

	return retVal;
}
jbyte* GetByteArrayElementsFunction(JNIEnv *env, jbyteArray intVectA,jboolean IsCopy)
{
	jbyte* retVal = 0;
	return retVal;
}
jchar* GetCharArrayElementsFunction(JNIEnv *env, jcharArray intVectA, jboolean IsCopy)
{
	jchar* retVal = 0;
	return retVal;
}
jdouble* GetDoubleArrayElementsFunction(JNIEnv *env, jdoubleArray intVectA, jboolean IsCopy)
{
	jdouble* retVal = 0;
	return retVal;
}
jfloat* GetFloatArrayElementsFunction(JNIEnv *env, jfloatArray intVectA, jboolean IsCopy)
{
	jfloat* retVal = 0;
	return retVal;
}
jint* GetIntArrayElementsFunction(JNIEnv *env,jintArray intVectA, jboolean IsCopy)
{
	jint* retVal;
	if(strcmp(intVectA->name,"IntegerArray0")==0)
		retVal = IntegerArray0;
	else if(strcmp(intVectA->name,"IntegerArray1")==0)
		retVal = IntegerArray1;
	else if(strcmp(intVectA->name,"IntegerArray2")==0)
		retVal = IntegerArray2;
	return retVal;
}
jshort* GetShortArrayElementsFunction(JNIEnv *env, jshortArray intVectA, jboolean IsCopy)
{
	jshort* retVal = 0;
	return retVal;
}
jlong* GetLongArrayElementsFunction(JNIEnv *env, jlongArray intVectA, jboolean IsCopy)
{
	jlong* retVal = 0;
	return retVal;
}
void ReleaseIntArrayElementsFunction(JNIEnv *env,jintArray priorArray,jint* curIntArray,int IsCopy)
{

}

#endif



__m128i (*intFunction[42])(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD);
int functionArrayPopulated = 0;


//Globals
jclass vectorClass;
jclass executionListClass;
jclass executionItemClass;
jfieldID itemsField;
jfieldID objectArraysField;
jfieldID objectArraysMaxSizeField;
jfieldID opcodeField;
jfieldID sourceAField;
jfieldID sourceBField;
jfieldID argumentField;
jfieldID destinationField;
jobject objectArrays;
jmethodID getVectorElement;
jmethodID getVectorSize;
jobject itemsVector;
jint executionListSize;

#if TEST
void main()
{
	//set up the environment so that we have valid function calls.
	JNIEnv envir;

	jclass clazz = 0;
	jobject executionList= 0;

	struct JNINativeInterface_ jni;
	//ok.. so create an environment, populate some arrays and feed them through the algorithm
	jni.CallIntMethod= CallIntMethodFunction;
	jni.CallObjectMethod = CallObjectMethodFunction;
	jni.FindClass = FindClassFunction;
	jni.GetArrayLength = GetArrayLengthFunction;
	jni.GetByteArrayElements=GetByteArrayElementsFunction;
	jni.GetCharArrayElements=GetCharArrayElementsFunction;
	jni.GetDoubleArrayElements=GetDoubleArrayElementsFunction;
	jni.GetFieldID=GetFieldIDFunction;
	jni.GetFloatArrayElements=GetFloatArrayElementsFunction;
	jni.GetIntArrayElements=GetIntArrayElementsFunction;
	jni.GetIntField=GetIntFieldFunction;
	jni.GetLongArrayElements= GetLongArrayElementsFunction;
	jni.GetMethodID= GetMethodIDFunction;
	jni.GetObjectField = GetObjectFieldFunction;
	jni.GetShortArrayElements=GetShortArrayElementsFunction;
	jni.ReleaseIntArrayElements= ReleaseIntArrayElementsFunction;
	envir = &jni;

	Java_simdMapper_NativeLibrary_Execute(&envir,clazz,executionList);
}

#endif

void ExtractClassDefintions(JNIEnv *env)
{


	vectorClass = (*env)->FindClass(env, Vector_Class_Name);
	executionListClass =
		(*env)->FindClass(env, ExecutionList_Class_Name);
	executionItemClass =
		(*env)->FindClass(env, ExecutionItem_Class_Name);
}


void  ExtractFieldPointers(JNIEnv *env)
{
	itemsField = (*env)->GetFieldID(env, executionListClass,
		Items_Field_Name, Vector_Field_Type);
	objectArraysField = (*env)->GetFieldID(env, executionListClass,
		ObjectArrays_Field_Name, Vector_Field_Type);
	objectArraysMaxSizeField = (*env)->GetFieldID(env,
		executionListClass, "ObjectArraysMaxSize", "I");
	opcodeField = (*env)->GetFieldID(env, executionItemClass,
		Opcode_Field_Name, "I");
	sourceAField = (*env)->GetFieldID(env, executionItemClass,
		SourceA_Field_Name, "I");
	sourceBField = (*env)->GetFieldID(env, executionItemClass,
		SourceB_Field_Name, "I");
	argumentField = (*env)->GetFieldID(env, executionItemClass,
		arg_Field_Name, "I");
	destinationField = (*env)->GetFieldID(env, executionItemClass,
		Destination_Field_Name, "I");
}

//I HAD TO CHANGE THIS JP. Please review since assert was causing compiler error on final linking.
int ExtractArrays(JNIEnv* env,jobject executionList)
{
	int maxSize;
	maxSize =(*env)->GetIntField(env, executionList,objectArraysMaxSizeField);
	//assert(maxSize>0);
	objectArrays =  (*env)->GetObjectField(env, executionList, objectArraysField);
	//assert(objectArrays);
	return maxSize;
}


void GetVectorMethods(JNIEnv* env)
{
	getVectorElement = (*env)->GetMethodID(env, vectorClass,
		"elementAt", "(I)Ljava/lang/Object;");
	getVectorSize = (*env)->GetMethodID(env, vectorClass, "size",
		"()I");
}


void ExtractItemsVector(JNIEnv * env, jobject executionList)
{
	itemsVector =
		(*env)->GetObjectField(env, executionList, itemsField);
	executionListSize = (*env)->CallIntMethod(env,
		itemsVector,
		getVectorSize);
}

struct ExecutionElement tmpElement;

struct UsedArrays ProcessArray(JNIEnv *env, int Source, __m128i * pSrcA, int* size, int iteration, int pointerCount,struct UsedArrays* priorArrays, int maxSize, int type)
{
	int hasPointer = 0;
	int i=0;
	jobject intVectA;
	jint curArraySize;
	jint* curArray;

	for (;i<pointerCount;i++)
	{
		if((priorArrays[i]).number == Source)
		{
			pSrcA = (priorArrays[i].curVector);
			hasPointer = 1;
			size[iteration]= priorArrays[i].vectorSize;
			priorArrays[i].isNew = 0;
			tmpArray = priorArrays[i];
		}
	}
	if(!hasPointer)
	{
		switch(type){
	case BYTE:
		intVectA = (jbyteArray) (*env)->CallObjectMethod(env, objectArrays,
			getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (jint*)(*env)->GetByteArrayElements(env, intVectA,
			JNI_FALSE);
		break;
	case CHAR:
		intVectA = (jcharArray) (*env)->CallObjectMethod(env, objectArrays,
			getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (jint*)(*env)->GetCharArrayElements(env, intVectA,
			JNI_FALSE);
		break;
	case INT:
		intVectA = (jintArray) (*env)->CallObjectMethod(env, objectArrays, getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (*env)->GetIntArrayElements(env, intVectA,JNI_FALSE);
		break;
	case DOUBLE:
		intVectA = (jintArray) (*env)->CallObjectMethod(env, objectArrays,
			getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (jint*) (*env)->GetDoubleArrayElements(env, intVectA,
			JNI_FALSE);
		break;
	case FLOAT:
		intVectA = (jintArray) (*env)->CallObjectMethod(env, objectArrays,
			getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (jint*) (*env)->GetFloatArrayElements(env, intVectA,
			JNI_FALSE);
		break;
	case LONG:
		intVectA = (jintArray) (*env)->CallObjectMethod(env, objectArrays,
			getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (jint*) (*env)->GetLongArrayElements(env, intVectA,
			JNI_FALSE);
		break;
	case SHORT:
				intVectA = (jintArray) (*env)->CallObjectMethod(env, objectArrays,
			getVectorElement, Source);
		curArraySize = (*env)->GetArrayLength(env, intVectA);
		curArray = (jint*) (*env)->GetShortArrayElements(env, intVectA,
			JNI_FALSE);
		break;
		}
		size[iteration]=0;

		//all arrays have been cast to int... so they should load properly using this
		for(i = 0;i<(curArraySize/4+1);i++)
		{
			pSrcA[i] =_mm_loadu_si128((__m128i *) (&curArray[i*4]));
			size[iteration]=size[iteration]+1;
		}
		tmpArray.curArrayPointer = (int*)curArray;
		tmpArray.curVector = pSrcA;
		tmpArray.number = Source;
		tmpArray.type = type;
		tmpArray.javaArray = intVectA;
		tmpArray.arraySize = curArraySize;
		tmpArray.vectorSize = size[iteration];
		tmpArray.isNew = 1;
		
	}
	return tmpArray;

}
struct UsedArrays ProcessIntArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return (ProcessArray(env, Source, pSrcA, size, iteration, pointerCount, priorArrays, maxSize, INT));
}
struct UsedArrays ProcessByteArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return ProcessArray(env, Source, pSrcA, size,iteration, pointerCount, priorArrays, maxSize, BYTE);
}
struct UsedArrays ProcessCharArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return ProcessArray(env, Source, pSrcA, size,iteration, pointerCount, priorArrays, maxSize, CHAR);
}
struct UsedArrays ProcessDoubleArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return ProcessArray(env, Source, pSrcA, size,iteration, pointerCount, priorArrays, maxSize, DOUBLE);
}
struct UsedArrays ProcessFloatArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return ProcessArray(env, Source, pSrcA, size,iteration, pointerCount, priorArrays, maxSize, FLOAT);
}
struct UsedArrays ProcessLongArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return (ProcessArray(env, Source, pSrcA, size, iteration, pointerCount, priorArrays, maxSize, LONG));
}
struct UsedArrays ProcessShortArray(JNIEnv *env, int Source, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	return (ProcessArray(env, Source, pSrcA, size, iteration, pointerCount, priorArrays, maxSize, SHORT));
}
struct UsedArrays ProcessConstIntArray(JNIEnv *env, int Arg, __m128i* pSrcA, int* size,int iteration, int pointerCount, struct UsedArrays* priorArrays, int maxSize)
{
	struct UsedArrays retVal;
	int i=0;
	int constintArray[4];
	jint *pResulti;
	for (;i<4;i++)
	{
		constintArray[i] = Arg;
	}
	pResulti = (jint*) _aligned_malloc(maxSize * sizeof(jint), 16);
	pSrcA = (__m128i *) pResulti;

	size[iteration]=0;
	for(i = 0;i<(maxSize/4+((maxSize%4==0)?0:1));i++)
	{
		pSrcA[i] =_mm_loadu_si128((const __m128i *) (constintArray));
		size[iteration]=size[iteration]+1;
	}
	retVal.curVector = pSrcA;
	retVal.type = CONSTINT;
	return retVal;
}
//Casting __m128i to __m128d or __m128 is done by these intrinsics
//extern __m128  _mm_castpd_ps(__m128d in);
//extern __m128i _mm_castpd_si128(__m128d in);
//extern __m128d _mm_castps_pd(__m128 in);
//extern __m128i _mm_castps_si128(__m128 in);
//extern __m128  _mm_castsi128_ps(__m128i in);
//extern __m128d _mm_castsi128_pd(__m128i in);


__m128i Add(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD)
{
	//__m128i retVal;
	//__m128 A, B;
	int array1[4]={0,0,0,0};
	switch(typeA)
	{
	case BYTE:
	case CHAR:
		return _mm_add_epi8(SrcA, SrcB);
		break;
	case DOUBLE:
		return _mm_castpd_si128(_mm_add_pd(_mm_castsi128_pd(SrcA), _mm_castsi128_pd(SrcB)));
		break;
	case FLOAT:
		//A = _mm_castsi128_ps(SrcA);
		//B = _mm_castsi128_ps(SrcB);
		return _mm_castps_si128(_mm_add_ps(_mm_castsi128_ps(SrcA),_mm_castsi128_ps(SrcB)));
		break;
	case INT:
		return _mm_add_epi32(SrcA, SrcB);
		break;
	case LONG:
		return _mm_add_epi64(SrcA, SrcB);
		break;
	case SHORT:
		return _mm_add_epi16(SrcA, SrcB);
		break;
	default:
		break;
	}
	return  _mm_loadu_si128((__m128i *) (&array1[0]));
}
__m128i And(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD)
{	
	return _mm_and_si128(SrcA, SrcB);
}


__m128i Multlo(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD)
{
	int array1[4]={0,0,0,0};
	switch(typeA)
	{
	case INT:
		return _mm_mullo_epi32(SrcA, SrcB);
		break;
	case SHORT:
		return _mm_mullo_epi16(SrcA, SrcB);
		break;
	default:
		break;
	}
	return _mm_loadu_si128((__m128i *) (&array1[0]));
}
__m128i Or(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD)
{	
	return _mm_or_si128(SrcA, SrcB);
}
__m128i ShiftRightArith (__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD)
{
	int array1[4]={0,0,0,0};
	switch(typeA)
	{
	case INT:
		return _mm_sra_epi32(SrcA, SrcB);
		break;
	case SHORT:
		return _mm_sra_epi16(SrcA, SrcB);
		break;
	default:
		break;
	}
	return _mm_loadu_si128((__m128i *) (&array1[0]));

}
__m128i Sub(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD)
{
	int array1[4] = {0,0,0,0};
	switch(typeA)
	{
	case BYTE:
	case CHAR:
		return _mm_sub_epi8(SrcA, SrcB);
	case DOUBLE:
		return  _mm_castpd_si128(_mm_sub_pd(_mm_castsi128_pd(SrcA), _mm_castsi128_pd(SrcB)));
	case FLOAT:
		return _mm_castps_si128(_mm_sub_ps(_mm_castsi128_ps(SrcA),_mm_castsi128_ps(SrcB)));
	case INT:
		return _mm_sub_epi32(SrcA, SrcB);
	case LONG:
		return _mm_sub_epi64(SrcA, SrcB);
	case SHORT:
		return _mm_sub_epi16(SrcA, SrcB);
	default:
		return _mm_loadu_si128((__m128i *) (&array1[0]));
	}
}

void PopulateFunctionArray()
{
	intFunction[ADD] = Add;
	intFunction[AND] = And;
	intFunction[MULTL] = Multlo;
	intFunction[OR] = Or;
	intFunction[SRA] = ShiftRightArith;
	intFunction[SUB] = Sub;
	functionArrayPopulated = 1;
}
#if TEST
void Java_simdMapper_NativeLibrary_Execute(JNIEnv *env,jclass clazz, jobject executionList)
#else
JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_Execute(JNIEnv *env, jclass clazz, jobject executionList) 
#endif
{
	int i;
	jint curArraySize;
	int maxSize;
	int executionListIterator = executionListSize;
	__m128i** pSrcA;
	__m128i** pSrcB;
	__m128i** pDest;
	int* size;
	int* OperationList;
	int* ArgumentList;
	struct UsedArrays *priorArrays;
	int pointerCount = 0;
	//int constintArray[4];
	int iteration = 0;
	int j;
	int repopIterator;
	int breakout = 0;
	int curOp;
	int copyIterator;
	struct UsedArrays priorArray;
	jint* curIntArray;
	jint* resultIntVector;
	__m128i* tmpSrcA;
	struct ExecutionElement curExecutionItem;

#if TEST
	//otherwise these go in OnLoad
	//get important classes from which we can pull field identifiers and class methods
	ExtractClassDefintions(env);
	//get Execution List class fields
	ExtractFieldPointers(env);
	//we need some vector methods (get and size)
	GetVectorMethods(env);
		if(!functionArrayPopulated)
	{
		PopulateFunctionArray();
	}
#endif

	//better free these later.
	pSrcA = (__m128i**)malloc(executionListSize*sizeof(__m128i*));
	pSrcB = (__m128i**)malloc(executionListSize*sizeof(__m128i*));
	pDest = (__m128i**)malloc(executionListSize*sizeof(__m128i*));
	size = (int*)malloc(executionListSize);
	OperationList =(int*)malloc(executionListSize);
	ArgumentList = (int*)malloc(executionListSize);
	priorArrays = (struct UsedArrays *)malloc(3*executionListSize*sizeof(struct UsedArrays)); 

	//get the arrays from the ExecutionList and set max size
	maxSize = ExtractArrays(env,executionList);
	ExtractItemsVector(env, executionList);
	
	while (executionListSize - iteration > 0) //there are items left to do
	{
		jobject executionItem;
		executionItem = (*env)->CallObjectMethod(env,itemsVector,getVectorElement,iteration);
		curExecutionItem.srcA = (*env)->GetIntField(env,executionItem,sourceAField);
		curExecutionItem.srcB = (*env)->GetIntField(env,executionItem,sourceBField);
		curExecutionItem.dest = (*env)->GetIntField(env,executionItem,destinationField);
		ArgumentList[iteration] = (*env)->GetIntField(env,executionItem,argumentField);
		OperationList[iteration] = (*env)->GetIntField(env,executionItem,opcodeField);
		tmpSrcA = (__m128i *)  _aligned_malloc(maxSize * sizeof(jint), 16);
		priorArray = ProcessArray(env, curExecutionItem.srcA, tmpSrcA ,size,iteration, pointerCount, priorArrays, maxSize,OperationList[iteration]&0xF00>>8);
		if(priorArray.isNew)
		{
			priorArrays[pointerCount].arraySize	= priorArray.arraySize;
			priorArrays[pointerCount].curArrayPointer = priorArray.curArrayPointer;
			priorArrays[pointerCount].curVector = priorArray.curVector;
			priorArrays[pointerCount].isNew = priorArray.isNew;
			priorArrays[pointerCount].javaArray = priorArray.javaArray;
			priorArrays[pointerCount].number = priorArray.number;
			priorArrays[pointerCount].type = priorArray.type;
			priorArrays[pointerCount].vectorSize = priorArray.vectorSize;
			pSrcA[iteration] = tmpSrcA;
			pointerCount++;
		}
		else
		{
			_aligned_free(tmpSrcA);
		}
		pSrcA[iteration] = priorArray.curVector;
		
		tmpSrcA = (__m128i *)  _aligned_malloc(maxSize * sizeof(jint), 16);
		priorArray = ProcessArray(env,curExecutionItem.srcB,tmpSrcA,size,iteration, pointerCount, priorArrays, maxSize, OperationList[iteration]&0x0F0>>4);
		if(priorArray.isNew)
		{
			priorArrays[pointerCount].arraySize	= priorArray.arraySize;
			priorArrays[pointerCount].curArrayPointer = priorArray.curArrayPointer;
			priorArrays[pointerCount].curVector = priorArray.curVector;
			priorArrays[pointerCount].isNew = priorArray.isNew;
			priorArrays[pointerCount].javaArray = priorArray.javaArray;
			priorArrays[pointerCount].number = priorArray.number;
			priorArrays[pointerCount].type = priorArray.type;
			priorArrays[pointerCount].vectorSize = priorArray.vectorSize;
			pSrcB[iteration]=tmpSrcA;
			pointerCount++;
		}
		else
		{
			_aligned_free(tmpSrcA);
		}
		pSrcB[iteration] = priorArray.curVector;

		tmpSrcA = (__m128i *)  _aligned_malloc(maxSize * sizeof(jint), 16);
		priorArray = ProcessArray(env,curExecutionItem.dest,tmpSrcA,size,iteration, pointerCount, priorArrays, maxSize, OperationList[iteration]&0x0F);
		if(priorArray.isNew)
		{
			priorArrays[pointerCount].arraySize	= priorArray.arraySize;
			priorArrays[pointerCount].curArrayPointer = priorArray.curArrayPointer;
			priorArrays[pointerCount].curVector = priorArray.curVector;
			priorArrays[pointerCount].isNew = priorArray.isNew;
			priorArrays[pointerCount].javaArray = priorArray.javaArray;
			priorArrays[pointerCount].number = priorArray.number;
			priorArrays[pointerCount].type = priorArray.type;
			priorArrays[pointerCount].vectorSize = priorArray.vectorSize;
			pDest[iteration] = tmpSrcA;
			pointerCount++;
		}
		else
		{
			_aligned_free(tmpSrcA);
		}
		pDest[iteration] = priorArray.curVector;
		iteration++;
	}


	
	free(priorArrays);
	free(ArgumentList);
	free(OperationList);
	free(size);
	free(pDest);
	free(pSrcB);
	free(pSrcA);
	
#if TEST
	free(getVectorSize);
	free(getVectorElement);
	free(destinationField);
	free(argumentField);
	free(sourceBField);
	free(sourceAField);
	free(opcodeField);
	free(objectArraysMaxSizeField);
	free(objectArraysField);
	free(itemsField);
	free(executionItemClass);
	free(executionListClass);
	free(vectorClass);
#endif
	
}
#if !TEST
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	JNIEnv *env;
	jclass cls;
	cached_jvm = jvm; /* cache the JavaVM pointer */
	if ((*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2)) 
	{
		return JNI_ERR; /* JNI version not supported */
	}
	if(!functionArrayPopulated)
	{


		PopulateFunctionArray();
	}
	//TODO: Put a large amount of error checking on all these FINDS/GET commands
	//TODO: Or, pretty much line containing (*env)->

	//get important classes from which we can pull field identifiers and class methods
	ExtractClassDefintions(env);
	//get Execution List class fields
	ExtractFieldPointers(env);
	//we need some vector methods (get and size)
	GetVectorMethods(env);
	return JNI_VERSION_1_2;
}
#endif


JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_voidNativeCall(JNIEnv *env,jclass clazz){}


JNIEXPORT jint JNICALL Java_simdMapper_NativeLibrary_intNativeCall(JNIEnv *env,jclass clazz){
	return 0;
}


void *print_message_function( void *ptr ){
	char *message;
	message = (char *) ptr;
	int i = 0;

	for(;i<10000000;i++)
		printf("%s %d\n", message,i);
}


typedef struct _addArray{
	int* left;
	int* right;
	int begin;
	int end;
	int* done;
} StrcAddArray;


void *addArray( void *ptr ){
	StrcAddArray *comando;
	comando = (StrcAddArray *) ptr;
	int i;

	for (i = comando->begin; i <= comando->end; i++) {
		comando->left[i] = comando->left[i] + comando->right[i];
	}
}

// Only test the call with object as parameter
JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_nativeReferenceCopyTest1(JNIEnv *env,jclass clazz, jobject obj){
}

// Test the call and get array from object
JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_nativeReferenceCopyTest2(JNIEnv *env,jclass clazz, jobject obj){
	jclass cls = (*env)->GetObjectClass(env,obj);
	jfieldID fid = (*env)->GetFieldID(env, cls, "varInteger","[Ljava/lang/Integer;");
	jobjectArray objArray = (*env)->GetObjectField(env, obj, fid);
// 	jobject GetObjectArrayElement(JNIEnv *env, jobjectArray array, jsize index);
}

// Test iteration on array
JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_nativeReferenceCopyTest3(JNIEnv *env,jclass clazz, jobject obj){
	jclass cls = (*env)->GetObjectClass(env,obj);
	jfieldID fid = (*env)->GetFieldID(env, cls, "varInteger","[Ljava/lang/Integer;");
	jobjectArray objArray = (*env)->GetObjectField(env, obj, fid);

	jobject integerElement;
	jint i = 0;
	jint arraySize = (*env)->GetArrayLength(env, objArray);

	for(;i<arraySize;i++){
		integerElement = (*env)->GetObjectArrayElement(env, objArray, i);
	}
 	
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_nativeReferenceCopyCompareReferenceTest(JNIEnv *env,jclass clazz, jobject obj1, jobject obj2){
	if((*env)->IsSameObject(env, obj1, obj2))
		printf("OK - Comparação por Referência\n");
	else
		printf("Error - Comparação não é por Referência\n");
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_nativeReferenceCopyCompareCloneTest(JNIEnv *env,jclass clazz, jobject obj1, jobject obj2){
	if((*env)->IsSameObject(env, obj1, obj2))
		printf("OK - Comparação por Conteúdo\n");
	else
		printf("Error - Comparação não é por Conteúdo\n");
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_soma(JNIEnv *env,jclass clazz, jintArray srcA, jintArray srcB, jint numThreads) {
	int i,sizePerThread,sizeLastThread,indexArray;
	int arraySize = (*env)->GetArrayLength(env, srcA);

	sizePerThread = arraySize/numThreads;
	sizeLastThread = arraySize%numThreads;
	if(sizeLastThread)
		sizeLastThread = sizeLastThread + sizePerThread;

	int* pSrcA = (int*)(*env)->GetPrimitiveArrayCritical(env,srcA,0);
	int* pSrcB = (int*)(*env)->GetPrimitiveArrayCritical(env,srcB,0);

	//Prepare commands
	StrcAddArray * command = (StrcAddArray *)_mm_malloc (sizeof(StrcAddArray) * numThreads, 16);
	for (i = 0, indexArray = 0; i < (numThreads - 1); i++) {
		command[i].left = pSrcA;
		command[i].right = pSrcB;
		command[i].begin = indexArray;
		indexArray = indexArray + sizePerThread;
		command[i].end = indexArray - 1;
	}
	command[i].left = pSrcA;
	command[i].right = pSrcB;
	command[i].begin = indexArray;
	if(sizeLastThread){//If the arraySize is not divisible by number of threads
		command[i].end = indexArray + sizeLastThread - 1;		
	}else{
		command[i].end = indexArray + sizePerThread - 1;
	}

	/* Create threads */
	pthread_t * thread = (pthread_t *)_mm_malloc (sizeof(pthread_t) * numThreads, 16);
	for (i = 0; i < numThreads; i++) {	
		pthread_create( &thread[i], NULL, addArray, (void*) &command[i]);
	}

	for (i = 0; i < numThreads; i++) {	
		pthread_join( thread[i], NULL);
	}

	(*env)->ReleasePrimitiveArrayCritical(env,srcA, pSrcA, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,srcB, pSrcB, 0);
	_mm_free(command);
	_mm_free(thread);

	return;
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_addIntegerArrayCThreads2(JNIEnv *env,jclass clazz, jintArray srcA, jintArray srcB, jint numThreads) {

	int i,sizePerThread,sizeLastThread,indexArray;
	int arraySize = (*env)->GetArrayLength(env, srcA);

	int* pSrcA = (int*)(*env)->GetPrimitiveArrayCritical(env,srcA,0);
	int* pSrcB = (int*)(*env)->GetPrimitiveArrayCritical(env,srcB,0);

	for (i = 0; i <= arraySize; i++) {
		pSrcA[i] = pSrcA[i] + pSrcB[i];
	}

	(*env)->ReleasePrimitiveArrayCritical(env,srcA, pSrcA, 0);
	(*env)->ReleasePrimitiveArrayCritical(env,srcB, pSrcB, 0);

	return;
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_addIntegerSIMD(JNIEnv *env,jclass clazz, jint arraySize) {
	int i, sseSize,restSize;

	__m128i *x;
	__m128i *y;
	__m128i *z;

	jint * a;
	jint * b;
	jint * c;

	a = (jint *)_mm_malloc (sizeof(jint) * arraySize, 16);
	b = (jint *)_mm_malloc (sizeof(jint) * arraySize, 16);
	c = (jint *)_mm_malloc (sizeof(jint) * arraySize, 16);

	//Variaveis para manipulação vetorial
	x = (__m128i*) a;
	y = (__m128i*) b;	
	z = (__m128i*) c;

	sseSize = arraySize / 4;
	restSize = arraySize % 4;

	//Atribuição
	for (i = 0; i < sseSize; i++) {	
		x[i] = _mm_set1_epi32(6);
		y[i] = _mm_set1_epi32(7);
	}

	if(restSize){
		a[i*4] = 6;
		b[i*4] = 7;
		if(restSize == 3){
			a[(i*4)+1] = 6;
			b[(i*4)+1] = 7;
			a[(i*4)+2] = 6;
			b[(i*4)+2] = 7;
		}else {
              if(restSize == 2) {
			a[(i*4)+1] = 6;
			b[(i*4)+1] = 7;
		}
    }
	}

	//Soma	
	for (i = 0; i < sseSize; i++) {
		z[i] = _mm_add_epi32(x[i], y[i]);
	}

	if(restSize){
		c[i*4] = a[i*4] + b[i*4];
		if(restSize == 3){
			c[(i*4)+1] = a[(i*4)+1] + b[(i*4)+1];
			c[(i*4)+2] = a[(i*4)+2] + b[(i*4)+2];
		}else{
              if(restSize == 2) {
			c[(i*4)+1] = a[(i*4)+1] + b[(i*4)+1];
		}
    }
	}

	_mm_free (a);
	_mm_free (b);
	_mm_free (c);

	return;
 }


JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_addIntegerCTotal(JNIEnv *env,jclass clazz, jint arraySize) {
	int i;

	int * a;
	int * b;
	int * c;

	a = (int *)_mm_malloc (sizeof(int) * arraySize, 16);
	b = (int *)_mm_malloc (sizeof(int) * arraySize, 16);
	c = (int *)_mm_malloc (sizeof(int) * arraySize, 16);

	//Atribuição
	for (i = 0; i < arraySize; i++) {	
		a[i] = 6;
		b[i] = 7;
	}

	//Soma	
	for (i = 0; i < arraySize; i++) {
		c[i] = a[i] + b[i];
	}

	_mm_free (a);
	_mm_free (b);
	_mm_free (c);

	return;
 }


JNIEXPORT jintArray JNICALL Java_simdMapper_NativeLibrary_addInteger(JNIEnv *env,jclass clazz, jintArray a1, jintArray a2) {
	jsize arraySize = (*env)->GetArrayLength(env, a1);
	int i, sseSize = arraySize/4;
	int restSize = arraySize % 4;
	jint *body1 = (*env)->GetIntArrayElements(env,a1,0);
	jint *body2 = (*env)->GetIntArrayElements(env,a2,0);

	jintArray returnArray = (*env)->NewIntArray(env,arraySize);
	jint *result = (*env)->GetIntArrayElements(env, returnArray, 0);
	__m128i x, y;
	__m128i *resultSSE = (__m128i*) result;	

	for (i = 0; i < sseSize; i++) {	
      	x = _mm_set_epi32(body1[(i*4)],body1[(i*4)+1],body1[(i*4)+2],body1[(i*4)+3]);      	
		y = _mm_set_epi32(body2[(i*4)],body2[(i*4)+1],body2[(i*4)+2],body2[(i*4)+3]);
	
    	printf("%d\n",body1[(i*4)]);
      	printf("%d\n",body1[(i*4)+1]);
      	printf("%d\n",body1[(i*4)+2]);
      	printf("%d\n",body1[(i*4)+3]);  
      	printf("%d\n",body2[(i*4)]);
      	printf("%d\n",body2[(i*4)+1]);
      	printf("%d\n",body2[(i*4)+2]);
      	printf("%d\n",body2[(i*4)+3]);  
	
		resultSSE[i] = _mm_add_epi32(x, y);	
		printf("%s\n", "executei a soma");
        }
 
	if(restSize){
		result[i*4] = body1[(i*4)] + body2[(i*4)];
		if(restSize == 3){
            result[(i*4)+1] = body1[(i*4)+1] + body2[(i*4)+1];
			result[(i*4)+2] = body1[(i*4)+2] + body2[(i*4)+2];
		}else {
            if(restSize == 2){
               result[(i*4)+1] = body1[(i*4)+1] + body2[(i*4)+1];
            }
         }
	}
	(*env)->ReleaseIntArrayElements(env, returnArray, result, 0);
	(*env)->ReleaseIntArrayElements(env, a1, body1, 0);
	(*env)->ReleaseIntArrayElements(env, a2, body2, 0);

	return returnArray;

 }



JNIEXPORT jintArray JNICALL Java_simdMapper_NativeLibrary_addIntegerC(JNIEnv *env,jclass clazz, jintArray a1, jintArray a2) {
	int i;
	jsize arraySize = (*env)->GetArrayLength(env, a1);
	jint *body1 = (*env)->GetIntArrayElements(env,a1,0);
	jint *body2 = (*env)->GetIntArrayElements(env,a2,0);

	jintArray returnArray = (*env)->NewIntArray(env,arraySize);
	jint *result = (*env)->GetIntArrayElements(env, returnArray, 0);
	
	for (i = 0; i < arraySize; i++) {
		result[i] = body1[i] + body2[1];
	}

	(*env)->ReleaseIntArrayElements(env, returnArray, result, 0);
	(*env)->ReleaseIntArrayElements(env, a1, body1, 0);
	(*env)->ReleaseIntArrayElements(env, a2, body2, 0);

	return returnArray;
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_addConstArrayC(JNIEnv *env,jclass clazz, jint a1, jintArray a2) {
	int i;
	jsize arraySize = (*env)->GetArrayLength(env, a2);
	jint *body2 = (*env)->GetIntArrayElements(env,a2,0);

	for (i = 0; i < arraySize; i++) {
		body2[i] = a1 + body2[i];
	}

	(*env)->ReleaseIntArrayElements(env, a2, body2, 0);
}

/*
JNIEXPORT jintArray JNICALL Java_simdMapper_NativeLibrary_addIntegerCTime(JNIEnv *env,jclass clazz, jintArray a1, jintArray a2, jint time) {
	int i, tmp;
	timespec ts;
   	tmp = clock_gettime(CLOCK_REALTIME, &ts);

	jsize arraySize = (*env)->GetArrayLength(env, a1);
	jint *body1 = (*env)->GetIntArrayElements(env,a1,0);
	jint *body2 = (*env)->GetIntArrayElements(env,a2,0);

	jintArray returnArray = (*env)->NewIntArray(env,arraySize);
	jint *result = (*env)->GetIntArrayElements(env, returnArray, 0);
	
	for (i = 0; i < arraySize; i++) {
		result[i] = body1[i] + body2[1];
	}

	(*env)->ReleaseIntArrayElements(env, returnArray, result, 0);
	(*env)->ReleaseIntArrayElements(env, a1, body1, 0);
	(*env)->ReleaseIntArrayElements(env, a2, body2, 0);

	time = clock_gettime(CLOCK_REALTIME, &ts) - tmp;

	return returnArray;
}

JNIEXPORT jshortArray JNICALL Java_simdMapper_NativeLibrary_addShort(JNIEnv *env,jclass clazz, jshortArray a1, jshortArray a2) {
    jsize arraySize = (*env)->GetArrayLength(env, a1);
    int i, sseSize = arraySize/8;    
    jshort *body1 = (*env)->GetShortArrayElements(env,a1,0);
    jshort *body2 = (*env)->GetShortArrayElements(env,a2,0);
	
	jshortArray returnArray = (*env)->NewShortArray(env,arraySize);
	jshort *result = (*env)->GetShortArrayElements(env, returnArray, 0);
	__m128i x, y;
	__m128i *resultSSE = (__m128i*) result;	
	
    for (i = 0; i < sseSize; i++) {	
    	x = _mm_set_epi16(body1[(i*8)],body1[(i*8)+1],body1[(i*8)+2],body1[(i*8)+3],body1[(i*8)+4],body1[(i*8)+5],body1[(i*8)+6],body1[(i*8)+7]);
    	y = _mm_set_epi16(body2[(i*8)],body2[(i*8)+1],body2[(i*8)+2],body2[(i*8)+3],body2[(i*8)+4],body2[(i*8)+5],body2[(i*8)+6],body2[(i*8)+7]);
        resultSSE[i] = _mm_add_epi16(x, y);		
    }

	(*env)->ReleaseShortArrayElements(env, returnArray, result, 0);
	(*env)->ReleaseShortArrayElements(env, a1, body1, 0);
	(*env)->ReleaseShortArrayElements(env, a2, body2, 0);

	return returnArray;
 }


JNIEXPORT jbyteArray JNICALL Java_simdMapper_NativeLibrary_addByte(JNIEnv *env,jclass clazz, jbyteArray a1, jbyteArray a2) {
    jsize arraySize = (*env)->GetArrayLength(env, a1);
    int i, sseSize = arraySize/16;
    jbyte *body1 = (*env)->GetByteArrayElements(env,a1,0);
    jbyte *body2 = (*env)->GetByteArrayElements(env,a2,0);
	
	jbyteArray returnArray = (*env)->NewByteArray(env,arraySize);
	jbyte *result = (*env)->GetByteArrayElements(env, returnArray, 0);
	__m128i x, y;
	__m128i *resultSSE = (__m128i*) result;	
	
    for (i = 0; i < sseSize; i++) {
    	x = _mm_set_epi8(body1[(i*8)],body1[(i*8)+1],body1[(i*8)+2],body1[(i*8)+3],body1[(i*8)+4],body1[(i*8)+5],body1[(i*8)+6],body1[(i*8)+7],body1[(i*8)+8],body1[(i*8)+9],body1[(i*8)+10],body1[(i*8)+11],body1[(i*8)+12],body1[(i*8)+13],body1[(i*8)+14],body1[(i*8)+15]);
    	y = _mm_set_epi8(body2[(i*8)],body2[(i*8)+1],body2[(i*8)+2],body2[(i*8)+3],body2[(i*8)+4],body2[(i*8)+5],body2[(i*8)+6],body2[(i*8)+7],body2[(i*8)+8],body2[(i*8)+9],body2[(i*8)+10],body2[(i*8)+11],body2[(i*8)+12],body2[(i*8)+13],body2[(i*8)+14],body2[(i*8)+15]);
        resultSSE[i] = _mm_add_epi8(x, y);		
    }

	(*env)->ReleaseByteArrayElements(env, returnArray, result, 0);
	(*env)->ReleaseByteArrayElements(env, a1, body1, 0);
	(*env)->ReleaseByteArrayElements(env, a2, body2, 0);

	return returnArray;
 }*/

// GetDirectBufferAddress
// GetPrimitiveArrayCritical
// ReleasePrimitiveArrayCritical
