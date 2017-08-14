package com.facebook.react.uiapp;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.facebook.react.animated.NativeAnimatedModule;
import com.facebook.react.bridge.AcJavaModuleWrapper;
import com.facebook.react.bridge.JSInstance;
import com.facebook.react.bridge.JavaModuleWrapper;
import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.NativeArray;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.modules.core.ReactChoreographer;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ransj on 2017/8/14.
 */
@RunWith(AndroidJUnit4.class)
public class NativeModuleBenchMarkTest {
  private static final String TAG = "NativeModuleBenchMarkTest";

  @Test
  public void getMethodDescriptors() {
    int count = 10000;
    UiThreadUtil.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        ReactChoreographer.initialize();
      }
    });
    System.gc();
    waitTime(5000);
    JSInstance jsInstance = new JSInstance() {
      @Override
      public void invokeCallback(int callbackID, NativeArray arguments) {

      }
    };
    ModuleHolder moduleHolder = new ModuleHolder(new NativeAnimatedModule(new ReactApplicationContext(InstrumentationRegistry.getTargetContext())));
    long start = System.currentTimeMillis();
    for (int i = 0; i < count; i++) {
      new JavaModuleWrapper(jsInstance, NativeAnimatedModule.class, moduleHolder).getMethodDescriptors();
    }
    Log.w(TAG, "testGetMethodDescriptors: old cost "+(System.currentTimeMillis() - start));
    System.gc();
    waitTime(5000);
    start = System.currentTimeMillis();
    for (int i = 0; i < count; i++) {
      new AcJavaModuleWrapper(jsInstance, NativeAnimatedModule.class, moduleHolder).getMethodDescriptors();
    }
    Log.w(TAG, "testGetMethodDescriptors: new cost "+(System.currentTimeMillis() - start));
  }

  void waitTime(long duration){
    for (long start = System.currentTimeMillis(); start + duration >= System.currentTimeMillis(); ) {
      Thread.yield();
    }
  }

}
