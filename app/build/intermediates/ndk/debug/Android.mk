LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_SRC_FILES := \
	C:\Users\Luigi\Desktop\android-serialport-api\app\src\main\jni\Android.mk \
	C:\Users\Luigi\Desktop\android-serialport-api\app\src\main\jni\Application.mk \
	C:\Users\Luigi\Desktop\android-serialport-api\app\src\main\jni\gen_SerialPort_h.sh \
	C:\Users\Luigi\Desktop\android-serialport-api\app\src\main\jni\SerialPort.c \

LOCAL_C_INCLUDES += C:\Users\Luigi\Desktop\android-serialport-api\app\src\main\jni
LOCAL_C_INCLUDES += C:\Users\Luigi\Desktop\android-serialport-api\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
