#include <jni.h>

JNIEXPORT jdouble JNICALL
com_example_myapplication_activities_FlashCardsActivity_calculatePercentage(JNIEnv* env, jobject obj, jint correctAnswers, jint totalCards) {
    if (totalCards == 0) return 0.0; // Защита от деления на ноль
    return ((double)correctAnswers / totalCards) * 100.0;
}
