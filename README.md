Fork of https://github.com/wuyouuuu/wytrace repository

Updates:
- cleaned up, for example, removed launcher icon
- updated dependencies

Simple how-to:
- compile library as AAR
- add it to your project (google how to add AAR library)
- call ArtMethodTrace.fix14debugApp(this) in your Application.onCreate()

================

### 介绍
起初这是一个android 平台排查方法耗时的库
后面我用他来代替systrace使用了。
支持环境
Android 9 ～ Android 14 debug包
Android 13已经默认切到Nterp了，13后需要切到switch解释执行才能监控到. 这边hook art::interpreter::CanRuntimeUseNterp()让方法走switch执行
Android 14 debugable包默认解释执行 bootImage方法也用的解释执行，这会导致debugable包变的卡顿，这边利用art::instrumentation::Instrumentation::UpdateEntrypointsForDebuggable() 临时解决

### 背景
排查一个方法耗时时，经常需要在内部打印各种时间戳,比如排查下面onCreate方法耗时
```java
   @Override
protected void onCreate(Bundle savedInstanceState) {
    //步骤1
    super.onCreate(savedInstanceState);
    //步骤2
    setContentView(R.layout.activity_main);
    //步骤3
    test();
}

```
### wytrace
这个库就是解决上面的繁琐问题


```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy

android {
    buildFeatures {
        prefab true
    }
}

dependencies {
    implementation 'com.bytedance.android:shadowhook:1.0.9'
    implementation 'com.github.wuyouuuu:wytrace:1.0.1'
}
```


```java
/**
 *
 * @param methodName 方法名
 * @param tid   需要抓取trace线程tid   -1表示抓取全部线程
 * @param depth 方法内部抓取层级
 * @param debug 是否打印方法耗时日志
 */

public static void methodHookStart(String methodName, int tid, int depth, boolean debug){}

```
### 示例
```java
        ArtMethodTrace.methodHookStart("com.wy.wytrace.MainActivity.onCreate", Process.myTid(),3,true);

```
日志输出
```text
2023-06-26 18:26:39.288 18782-18782/com.wy.wytrace E/wytrace: 3, androidx.activity.ComponentActivity.onCreate 28 ms
2023-06-26 18:26:39.288 18782-18782/com.wy.wytrace E/wytrace: 3, androidx.lifecycle.LifecycleRegistry.handleLifecycleEvent 0 ms
2023-06-26 18:26:39.288 18782-18782/com.wy.wytrace E/wytrace: 3, androidx.fragment.app.FragmentController.dispatchCreate 0 ms
2023-06-26 18:26:39.288 18782-18782/com.wy.wytrace E/wytrace: 2, androidx.fragment.app.FragmentActivity.onCreate 28 ms
2023-06-26 18:26:39.323 18782-18782/com.wy.wytrace E/wytrace: 3, androidx.appcompat.app.AppCompatActivity.initViewTreeOwners 35 ms
2023-06-26 18:26:39.323 18782-18782/com.wy.wytrace E/wytrace: 3, androidx.appcompat.app.AppCompatActivity.getDelegate 0 ms
2023-06-26 18:26:39.386 18782-18782/com.wy.wytrace E/wytrace: 3, androidx.appcompat.app.AppCompatDelegateImpl.setContentView 63 ms
2023-06-26 18:26:39.386 18782-18782/com.wy.wytrace E/wytrace: 2, androidx.appcompat.app.AppCompatActivity.setContentView 98 ms
2023-06-26 18:26:39.486 18782-18782/com.wy.wytrace E/wytrace: 3, android.os.SystemClock.sleep 100 ms
2023-06-26 18:26:39.486 18782-18782/com.wy.wytrace E/wytrace: 2, com.wy.wytrace.MainActivity.test 100 ms
2023-06-26 18:26:39.486 18782-18782/com.wy.wytrace E/wytrace: 1, com.wy.wytrace.MainActivity.onCreate 226 ms
```
使用perfetto
```shell
cd wytrace
chmod 777 record_android_trace.py
./record_android_trace.py -o trace_file.perfetto-trace -a com.wy.wytrace -t 5s sched -b 64mb
```
操作app 等待浏览器打开
![img.png](img.png)


