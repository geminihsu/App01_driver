package tw.com.geminihsu.app01Client.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
