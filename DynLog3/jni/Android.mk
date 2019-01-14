LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_C_INCLUDES += $(LOCAL_PATH)/src

LOCAL_SRC_FILES := \
	jni/com_android_inputmethoddl_latin_BinaryDictionary.cpp \
	src/dictionary.cpp

#LOCAL_C_INCLUDES += \
#    external/icu4c/common \
#	$(JNI_H_INCLUDE)

LOCAL_LDLIBS := -lm

LOCAL_PRELINK_MODULE := false

LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libcutils \
    libutils \
    libicuuc

LOCAL_MODULE := libjni_latinimedl

LOCAL_MODULE_TAGS := user

#LOCAL_LDLIBS += -Lbuild/platforms/android-1.5/arch-arm/usr/lib
#LOCAL_LDLIBS += -llog 

ANDROID_HOME ?= /opt/android-sdk-linux

LOCAL_CPPFLAGS	+= -I$(ANDROID_HOME)/frameworks/base/include
LOCAL_CPPFLAGS  += -I$(ANDROID_HOME)/dalvik/libnativehelper/include
LOCAL_CPPFLAGS	+= -I$(ANDROID_HOME)/system/core/include
LOCAL_CPPFLAGS	+= -I$(ANDROID_HOME)/./external/icu4c/common
LOCAL_LDLIBS	+= -L$(ANDROID_HOME)/out/target/product/generic/system/lib 
LOCAL_LDLIBS	+= -landroid_runtime -lcutils -lutils -licuuc

LOCAL_INCLUDES += $(ANDROID_HOME)/frameworks/base/include

include $(BUILD_SHARED_LIBRARY)

