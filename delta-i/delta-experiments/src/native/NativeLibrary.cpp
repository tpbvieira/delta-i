#include <emmintrin.h>
#include <xmmintrin.h>
#include <smmintrin.h>
#include <time.h>
#include <jni.h>
#include <vector>
#include <stdio.h>
#include <windows.h>

namespace jSIMD
{
	enum OPERATION 
	{
		ADD = 0, ADDS = 1,	ADDSUB = 2,	AND = 3,	AVG = 4,	BLEND = 5,	CEIL = 6,	EQUAL = 7,	
		FLOOR = 8,	GREATERTHAN = 9,	GREATERTHANOREQUAL = 10,	HORIZADD = 11,	LESSTHATOREQUAL = 12,	
		MACC = 13,	MAX = 14,	MIN = 15,	MULT = 16,	MULTH = 17,	MULTL = 18,	NAND = 19,	NOTEQUAL = 20,	
		OR = 21,	ORDERED = 22,	ROUND = 23,	SHUF = 24,	SHUFL = 25,	SHUFH = 26,	SLL = 27,	SQRT = 28,	
		SRA = 29,	SRL = 30,	SUB = 31,	SUBS = 32,	SUMABSDIFF = 33,	UNORDERED = 34,	XOR = 35,	
		DIV = 36,	RECIP = 37,	RECIPSQRT = 38,	LESSTHAN = 39,	HORIZSUB = 40,	POPCOUNT = 41,	
		INTERLEAVELOW = 42,	INTERLEAVEHIGH = 43
	};
	enum DATATYPE 
	{
		BYTE = 0, CHAR =1, DOUBLE =2, FLOAT=3, INT=4, LONG=5, SHORT=6, UNKNOWNN=7, 
		CONSTBYTE = 8, CONSTCHAR=9, CONSTDOUBLE=10,	CONSTFLOAT=11, CONSTINT=12, CONSTLONG=13, CONSTSHORT=14
	};

	class Names
	{	
	public: 
		static char* Vector_Class_Name(void){return "java/util/Vector";}
		static char* ExecutionList_Class_Name(void){return "jSIMD/ExecutionList";}
		static char* ExecutionItem_Class_Name(void){return "jSIMD/ExecutionItem";}
		static char* Vector_Field_Type(void){return "Ljava/util/Vector;";}
		static char* Items_Field_Name(void){return "Items";}
		static char* Opcode_Field_Name(void){return "Opcode";}
		static char* SourceA_Field_Name(void){return "SourceA";}
		static char* SourceB_Field_Name(void){return "SourceB";}
		static char* arg_Field_Name (void){return "arg";}
		static char* Destination_Field_Name (void){return "Destination";}
		static char* ObjectArrays_Field_Name (void){return "ObjectArrays";}
		static char* ObjectArrays_MaxSizeFieldName (void){return "ObjectArraysMaxSize";}
	};

	class Environment
	{
	public:
		JNIEnv *env;
		jclass cls;
		JavaVM cached_jvm;
		__m128i (*intFunction[42])(__m128i SrcA, __m128i SrcB, int arg, int typeA, int typeB, int typeD);

		bool functionArrayPopulated;
		bool jniError;
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
		jmethodID getVectorElement;
		jmethodID getVectorSize;

	private:
		void ExtractClassDefinitions(void);

	public:
		Environment()
		{
			jniError = true;
		}


		void ExtractClassDefintions()
		{
			if(env!=NULL)
			{
				vectorClass = 
					(*env).FindClass(jSIMD::Names::Vector_Class_Name());
				executionListClass =
					(*env).FindClass(jSIMD::Names::ExecutionList_Class_Name());
				executionItemClass =
					(*env).FindClass(jSIMD::Names::ExecutionItem_Class_Name());
			}
		}


		void  ExtractFieldPointers()
		{
			if(env!=NULL)
			{
				itemsField = (*env).GetFieldID(executionListClass,
					jSIMD::Names::Items_Field_Name(), jSIMD::Names::Vector_Field_Type());
				objectArraysField = (*env).GetFieldID(executionListClass,
					jSIMD::Names::ObjectArrays_Field_Name(),jSIMD::Names::Vector_Field_Type());
				objectArraysMaxSizeField = (*env).GetFieldID(
					executionListClass, "ObjectArraysMaxSize", "I");
				opcodeField = (*env).GetFieldID(executionItemClass,
					jSIMD::Names::Opcode_Field_Name(), "I");
				sourceAField = (*env).GetFieldID(executionItemClass,
					jSIMD::Names::SourceA_Field_Name(), "I");
				sourceBField = (*env).GetFieldID(executionItemClass,
					jSIMD::Names::SourceB_Field_Name(), "I");
				argumentField = (*env).GetFieldID(executionItemClass,
					jSIMD::Names::arg_Field_Name(), "I");
				destinationField = (*env).GetFieldID(executionItemClass,
					jSIMD::Names::Destination_Field_Name(), "I");
			}
		}

		void GetVectorMethods()
		{
			if(env!=NULL)
			{
				getVectorElement = (*env).GetMethodID(vectorClass,
					"elementAt", "(I)Ljava/lang/Object;");
				getVectorSize = (*env).GetMethodID(vectorClass, "size",
					"()I");
			}
		}


		bool CreateEnv(JNIEnv *environment)
		{
			jniError = false;
			env = environment;
			ExtractFieldPointers();
			GetVectorMethods();
			return jniError;
		}
		bool CreateEnv(JavaVM jvm)
		{
			cached_jvm = jvm; /* cache the JavaVM pointer */
			jniError = false;
			if (jvm.GetEnv((void **)&env, JNI_VERSION_1_2)) 
			{
				jniError=true; /* JNI version not supported */
			}
			if(!jniError)
			{
				ExtractClassDefintions();
				ExtractFieldPointers();
				GetVectorMethods();
			}
			return !jniError;
		}
	};
}
jSIMD::Environment curEnv; //one global environment variable
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	curEnv.CreateEnv(*jvm);
	if(curEnv.jniError)
	{
		return JNI_ERR; /* JNI version not supported */
	}
	else
	{
		return JNI_VERSION_1_2;
	}
}

JNIEXPORT void JNICALL Java_simdMapper_NativeLibrary_Execute(JNIEnv *env, jclass clazz, jobject executionList)
{
	DWORD dwError;
	dwError = SetPriorityClass(GetCurrentProcess(),REALTIME_PRIORITY_CLASS);
	fflush(stdout);
	jobject itemsVector,objectArrays;
	int elSize,numberOfObjectArrays;

	//make sure the environment we are using is up to date
	if(env==0)
	{
		curEnv.CreateEnv(env);
	}

	//extract the execution List.
	//jSIMD::ExecutionList el = jSIMD::ExecutionList(executionList); //extract the execution list

	//create the object arrays
	objectArrays = (curEnv.env)->GetObjectField(executionList,curEnv.objectArraysField);
	numberOfObjectArrays = (curEnv.env)->CallIntMethod(objectArrays,curEnv.getVectorSize);

	itemsVector =(curEnv.env)->GetObjectField(executionList, curEnv.itemsField);
	elSize = (int)(curEnv.env)->CallIntMethod(itemsVector,curEnv.getVectorSize);
	//create the execution list

	//create arrays for the execution List
	int* OpCode = new int[elSize];
	int* SrcA = new int[elSize];
	int* SrcB =new int[elSize];
	int* Dest = new int[elSize];
	int* Arg  = new int[elSize];

	for(int i=0;i<elSize;i++)
	{
		jobject curItem = (curEnv.env)->CallObjectMethod(itemsVector, curEnv.getVectorElement,i);
		SrcA[i] = (curEnv.env)->GetIntField(curItem,curEnv.sourceAField),
			SrcB[i] = (curEnv.env)->GetIntField(curItem,curEnv.sourceBField),
			Dest[i] = (curEnv.env)->GetIntField(curItem,curEnv.destinationField),
			OpCode[i] = (curEnv.env)->GetIntField(curItem,curEnv.opcodeField),
			Arg[i] = (curEnv.env)->GetIntField(curItem,curEnv.argumentField);
	}

	//create arrays for the data and associations
	int*  objectArraySizes = new int[numberOfObjectArrays];
	jobject*  javaArrayObjects = new jobject[numberOfObjectArrays];
	jint** javaArrayData = new jint*[numberOfObjectArrays];
	int* vectorSizes = new int[numberOfObjectArrays];
	int* arrayType = new int[numberOfObjectArrays];
	bool* arraysCreated = new bool[numberOfObjectArrays];
	__m128i** arrayVector = new __m128i*[numberOfObjectArrays];
	bool* arrayModified = new bool[numberOfObjectArrays];

	//First pass, create object arrays
	for(int i=0;i<numberOfObjectArrays;i++)
	{
		//grab the java arrays and their data and find out the size of the vectors we need.
		int curArraySize;
		javaArrayObjects[i] = (curEnv.env)->CallObjectMethod(objectArrays,curEnv.getVectorElement, i);
		curArraySize = objectArraySizes[i] = (curEnv.env)->GetArrayLength((jarray)javaArrayObjects[i]);
		arraysCreated[i] = false;
		arrayModified[i] = false;
	}
	//Second pass, use the executionList to populate the java array data (do not pack __m128i arrays yet)
	for(int i=0;i<elSize;i++)
	{
		//populate the SrcA arrays
		int arrayWeArePopulating = SrcA[i];
		int arrayType = (OpCode[i]&0xF00) >> 8;
		jint* javaArray = javaArrayData[arrayWeArePopulating];
		int curArraySize = objectArraySizes[arrayWeArePopulating];
		if(arrayWeArePopulating == 4)
		{
			int breakpoint=0;
			breakpoint++;
		}
		if(!arraysCreated[arrayWeArePopulating])
		{
			//populate the arrays
			if(arrayType<8)
			{
				switch(arrayType)
				{
				case jSIMD::BYTE:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetByteArrayElements((jbyteArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/16 + ((curArraySize%16==0)?0:1);
					break;
				case jSIMD::CHAR:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetCharArrayElements((jcharArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/16 + ((curArraySize%16==0)?0:1);
					break;
				case jSIMD::FLOAT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetFloatArrayElements((jfloatArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				case jSIMD::DOUBLE: javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetDoubleArrayElements((jdoubleArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/2 + ((curArraySize%2==0)?0:1);
					break;
				case jSIMD::INT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetIntArrayElements((jintArray)javaArrayObjects[arrayWeArePopulating], 0);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				case jSIMD::LONG:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetLongArrayElements((jlongArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/2 + ((curArraySize%2==0)?0:1);
					break;
				case jSIMD::SHORT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetShortArrayElements((jshortArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/8 + ((curArraySize%8==0)?0:1);
					break;
				default:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetIntArrayElements((jintArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				}
				arraysCreated[arrayWeArePopulating] = true;
			}
		}

		//populate the SrcB arrays
		arrayWeArePopulating = SrcB[i];
		arrayType = (OpCode[i]&0xF0) >> 4;
		curArraySize = objectArraySizes[arrayWeArePopulating];

		if(!arraysCreated[arrayWeArePopulating])
		{
			//populate the arrays
			if(arrayType<8)
			{
				switch(arrayType)
				{
				case jSIMD::BYTE:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetByteArrayElements((jbyteArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/16 + ((curArraySize%16==0)?0:1);
					break;
				case jSIMD::CHAR:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetCharArrayElements((jcharArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/16 + ((curArraySize%16==0)?0:1);
					break;
				case jSIMD::FLOAT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetFloatArrayElements((jfloatArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				case jSIMD::DOUBLE: javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetDoubleArrayElements((jdoubleArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/2 + ((curArraySize%2==0)?0:1);
					break;
				case jSIMD::INT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetIntArrayElements((jintArray)javaArrayObjects[arrayWeArePopulating], 0);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				case jSIMD::LONG:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetLongArrayElements((jlongArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/2 + ((curArraySize%2==0)?0:1);
					break;
				case jSIMD::SHORT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetShortArrayElements((jshortArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/8 + ((curArraySize%8==0)?0:1);
					break;
				default:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetIntArrayElements((jintArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				}
				arraysCreated[arrayWeArePopulating] = true;
			}
		}
		//populate the Dest arrays
		arrayWeArePopulating = Dest[i];
		arrayType = (OpCode[i]&0x00F);
		javaArray = javaArrayData[arrayWeArePopulating];
		curArraySize = objectArraySizes[arrayWeArePopulating];

		if(!arraysCreated[arrayWeArePopulating])
		{
			//populate the arrays
			if(arrayType<8)
			{
				switch(arrayType)
				{
				case jSIMD::BYTE:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetByteArrayElements((jbyteArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/16 + ((curArraySize%16==0)?0:1);
					break;
				case jSIMD::CHAR:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetCharArrayElements((jcharArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/16 + ((curArraySize%16==0)?0:1);
					break;
				case jSIMD::FLOAT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetFloatArrayElements((jfloatArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				case jSIMD::DOUBLE: javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetDoubleArrayElements((jdoubleArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/2 + ((curArraySize%2==0)?0:1);
					break;
				case jSIMD::INT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetIntArrayElements((jintArray)javaArrayObjects[arrayWeArePopulating], 0);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				case jSIMD::LONG:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetLongArrayElements((jlongArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/2 + ((curArraySize%2==0)?0:1);
					break;
				case jSIMD::SHORT:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetShortArrayElements((jshortArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/8 + ((curArraySize%8==0)?0:1);
					break;
				default:	javaArrayData[arrayWeArePopulating] = (jint*)(curEnv.env)->GetIntArrayElements((jintArray)javaArrayObjects[arrayWeArePopulating], NULL);
					vectorSizes[arrayWeArePopulating] = curArraySize/4 + ((curArraySize%4==0)?0:1);
					break;
				}
				arraysCreated[arrayWeArePopulating] = true;
			}

		}
	}
	//Third Pass, create the __m128i vectors in contiguous space.
	for(int i=0;i<numberOfObjectArrays;i++)
	{
		arrayVector[i] = (__m128i*)_aligned_malloc(vectorSizes[i]*sizeof(__m128i),16);

	}

	//4th pass.. populate vector arrays with data
	for(int i=0;i<numberOfObjectArrays;i++)
	{
		for(int j=0;j<vectorSizes[i];j++)
		{
			(arrayVector[i])[j] = _mm_loadu_si128((__m128i*)(&(javaArrayData[i])[j*4]));
		}

	}

	int count =0;
	double startExecute,executeTime;
			startExecute = clock();


	dwError = SetThreadPriority(GetCurrentThread(), THREAD_PRIORITY_TIME_CRITICAL);

	for(int i=0;i<elSize;i++)
	{
		int Op = OpCode[i] >> 12;
		int SrcAType = (OpCode[i]&0xF00)>>8;
		int SrcBType = (OpCode[i]&0xF0)>>4;
		int DestType = OpCode[i]&0xF;
		int vectorSize = vectorSizes[SrcA[i]];
		int logicArgumentVector[4] = {Arg[i],Arg[i],Arg[i],Arg[i]};
		int shiftArgumentVector[4] = {Arg[i],0,0,0};
		for(int j=0;j<vectorSize;j++)
		{
			if(j%16==0)
			{
				for(int k=0;k<numberOfObjectArrays;k++)
				{
					_mm_prefetch((char*)(&(arrayVector[k][j])),_MM_HINT_T0); //load all arrays into the cache
				}
			}
			__m128i tmpVect = _mm_loadu_si128((__m128i*)shiftArgumentVector);
			switch(Op)
			{
			case jSIMD::ADD:
				switch(SrcAType)
				{
				case jSIMD::INT:
					tmpVect = _mm_add_epi32(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
					break;
				}
				break;
			case jSIMD::ADDS:
				//printf("adds\n");
				break;
			case jSIMD::ADDSUB:
				break;
			case jSIMD::AND:
				//printf("and\n");
				switch(SrcAType)
				{
				case jSIMD::INT:
					switch (SrcBType)
					{
					case jSIMD::INT:
						tmpVect = _mm_and_si128(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
						break;
					case jSIMD::CONSTINT:
						tmpVect = _mm_and_si128(arrayVector[SrcA[i]][j],_mm_loadu_si128((__m128i*)logicArgumentVector));
						break;
					}
				}
				break;
			case jSIMD::AVG:
				break;
			case jSIMD::BLEND:
				break;
			case jSIMD::CEIL:
				break;
			case jSIMD::EQUAL:
				break;
			case jSIMD::FLOOR:
				break;
			case jSIMD::GREATERTHAN:
				break;
			case jSIMD::GREATERTHANOREQUAL:
				break;
			case jSIMD::HORIZADD:
				break;
			case jSIMD::LESSTHATOREQUAL:
				break;
			case jSIMD::MACC:
				break;
			case jSIMD::MAX:
				break;
			case jSIMD::MIN:
				break;
			case jSIMD::MULT:
				break;
			case jSIMD::MULTH:
				break;
			case jSIMD::MULTL:
				switch(SrcAType)
				{
				case jSIMD::INT:
					tmpVect = _mm_mullo_epi32(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
					break;
				}
				break;
			case jSIMD::NAND:
				break;
			case jSIMD::NOTEQUAL:
				break;
			case jSIMD::OR:
				switch(SrcAType)
				{
				case jSIMD::INT:
					switch (SrcBType)
					{
					case jSIMD::INT:
						tmpVect = _mm_and_si128(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
						break;
					case jSIMD::CONSTINT:
						tmpVect = _mm_and_si128(arrayVector[SrcA[i]][j],_mm_loadu_si128((__m128i*)logicArgumentVector));
						break;
					}
				}
				break;
			case jSIMD::ORDERED:
				break;
			case jSIMD::ROUND:
				break;
			case jSIMD::SHUF:
				break;
			case jSIMD::SHUFH:
				break;
			case jSIMD::SHUFL:
				break;
			case jSIMD::SLL:
				switch(SrcAType)
				{
				case jSIMD::INT:
					switch(SrcBType)
					{
					case jSIMD::INT:
						tmpVect = _mm_sll_epi32(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
						break;
					case jSIMD::CONSTINT:
						tmpVect =  _mm_sll_epi32(arrayVector[SrcA[i]][j],_mm_loadu_si128((__m128i*)shiftArgumentVector));
						break;
					}
					break;
				}
				break;
			case jSIMD::SQRT:
				break;
			case jSIMD::SRA:
				switch(SrcAType)
				{
				case jSIMD::INT:
					switch(SrcBType)
					{
					case jSIMD::INT:
						tmpVect = _mm_sra_epi32(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
						break;
					case jSIMD::CONSTINT:
						tmpVect =  _mm_sra_epi32(arrayVector[SrcA[i]][j],_mm_loadu_si128((__m128i*)shiftArgumentVector));
						break;
					}
					break;
				}
				break;
			case jSIMD::SRL:
				switch(SrcAType)
				{
				case jSIMD::INT:
					switch(SrcBType)
					{
					case jSIMD::INT:
						tmpVect = _mm_srl_epi32(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
						break;
					case jSIMD::CONSTINT:
						tmpVect =  _mm_srl_epi32(arrayVector[SrcA[i]][j],_mm_loadu_si128((__m128i*)shiftArgumentVector));
						break;
					}
					break;
				}
				break;
			case jSIMD::SUB:
				//printf("sub\n");
				switch(SrcAType)
				{
				case jSIMD::INT:
					tmpVect = _mm_sub_epi32(arrayVector[SrcA[i]][j], arrayVector[SrcB[i]][j]);
					break;
				}
				break;
			case jSIMD::SUBS:
				break;
			case jSIMD::SUMABSDIFF:
				break;
			case jSIMD::UNORDERED:
				break;
			case jSIMD::XOR:
				break;
			case jSIMD::DIV:
				break;
			case jSIMD::RECIP:
				break;
			case jSIMD::RECIPSQRT:
				break;
			case jSIMD::LESSTHAN:
				break;
			case jSIMD::HORIZSUB:
				break;
			case jSIMD::POPCOUNT:
				break;
			case jSIMD::INTERLEAVELOW:
				break;
			case jSIMD::INTERLEAVEHIGH:
				break;
			}
			arrayVector[Dest[i]][j] = tmpVect;
			arrayModified[Dest[i]] = true;
			count++;

		}

	}
		executeTime = clock()-startExecute;
		if(executeTime > 0.01)
			printf("Execute time %f\n",executeTime);

	//el.Repack();  //repack the arrays
	double repackStart, repackTime;
	repackStart = clock();
	for(int i=0;i<numberOfObjectArrays;i++)
	{
		if(arrayModified[i])
		{
			__m128i* pointArr = (__m128i*)javaArrayData[i];
			__m128i* curVect = arrayVector[i];
			for(int j=0;j<vectorSizes[j];j++)
			{
				_mm_storeu_si128(pointArr,curVect[j]);
				pointArr+=4;
			}
			(curEnv.env)->ReleaseIntArrayElements((jintArray)javaArrayObjects[i],javaArrayData[i],0);
		}
	}
	repackTime = clock()-repackStart;
	if(repackTime>0.01)
		printf("Repack time %f\n",repackTime);

	//cleanup
	delete[] OpCode;
	delete[] SrcA;
	delete[] SrcB;
	delete[] Dest;
	delete[] Arg;
	delete[] objectArraySizes;
	delete[] javaArrayObjects;
	delete[] javaArrayData;
	delete[] vectorSizes;
	delete[] arraysCreated;
	delete[] arrayModified;
	for(int i=0;i<numberOfObjectArrays;i++)
	{
		_aligned_free(arrayVector[i]);
	}
	delete[] arrayVector;

}

