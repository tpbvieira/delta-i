all:
#Build the NativeDelta library
	javah -classpath bin/ -jni -o src/main/java/nativedelta/NativeDelta.h nativedelta.NativeDelta
	gcc -O3 -ffast-math -march=native -mtune=native -mfpmath=both -fPIC -c -I "$(JAVA_HOME)/include" -I "$(JAVA_HOME)/include/linux" -o lib/NativeDelta.o src/main/c/NativeDelta.c
	gcc -O3 -ffast-math -march=native -mtune=native -mfpmath=both -fPIC -shared -o lib/NativeDelta.so lib/NativeDelta.o