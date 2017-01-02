package tw.com.geminihsu.app01.utils;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.util.Log;

public class ThreadPoolUtil {
    private static final String TAG = ThreadPoolUtil.class.getSimpleName();




    /*for command"順序"很重要的情況*/
    private static Executor threadPoolExecutor = Executors.newSingleThreadExecutor();

    /*for command 過程不重要,只care最後一次的command是啥的情況 ,這邊限定 Connect, resume or Pause*/
    private static Executor sendPauseOrResumeToDevice_threadPoolExecutor = Executors.newFixedThreadPool(1);
    private static Executor[] threadPoolExecutor_ ;;

    /*for command"順序"很重要的情況*/
    public static Executor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }










}
