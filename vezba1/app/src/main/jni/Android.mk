LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := crypt
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := crypt.cpp

include $(BUILD_SHARED_LIBRARY)