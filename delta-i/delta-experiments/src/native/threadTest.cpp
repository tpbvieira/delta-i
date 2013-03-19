#include <time.h>
#include <jni.h>
#include <vector>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>



void *print_message_function( void *ptr ){
     char *message;
     message = (char *) ptr;
     printf("%s \n", message);
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved){
	printf("OnLoad");
	return JNI_VERSION_1_2;
}


JNIEXPORT void JNICALL Java_simdMapper_Threads_addIntegerSIMDThread(JNIEnv *env,jclass clazz) {
	printf("Call_SIMD_Thread_Function");
	return;
}


JNIEXPORT void JNICALL Java_simdMapper_Threads_addIntegerCThread(JNIEnv *env,jclass clazz) {printf("Call_C_Thread_Function");	

	pthread_t thread1, thread2, thread3, thread4;

	char *message1 = "Thread 1";
	char *message2 = "Thread 2";
	char *message3 = "Thread 1";
	char *message4 = "Thread 2";

	int  iret1, iret2, iret3, iret4;

	/* Create independent threads each of which will execute function */
	iret1 = pthread_create( &thread1, NULL, print_message_function, (void*) message1);
	iret2 = pthread_create( &thread2, NULL, print_message_function, (void*) message2);
	iret3 = pthread_create( &thread3, NULL, print_message_function, (void*) message3);
	iret4 = pthread_create( &thread4, NULL, print_message_function, (void*) message4);

	/* Wait till threads are complete before main continues. Unless we  */
	/* wait we run the risk of executing an exit which will terminate   */
	/* the process and all threads before the threads have completed.   */
	pthread_join( thread1, NULL);
	pthread_join( thread2, NULL);
	pthread_join( thread3, NULL);
	pthread_join( thread4, NULL);

	printf("Thread 1 returns: %d\n",iret1);
	printf("Thread 2 returns: %d\n",iret2);
	printf("Thread 3 returns: %d\n",iret3);
	printf("Thread 4 returns: %d\n",iret4);

	return;
}
