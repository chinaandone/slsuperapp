package com.cleverm.smartpen.util;

import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiong,An android project Engineer,on 2016/2/23.
 * Data:2016-02-23  10:29
 * Base on clever-m.com(JAVA Service)
 * Describe: 一个基础到极致的自定义线程池,用于一些非常小的事物处理方面管理
 *
 * ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workBlockingQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) .

 *       corePoolSize - 池中所保存的线程数，包括空闲线程。
 *       maximumPoolSize-池中允许的最大线程数。
 *       keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
 *       unit - keepAliveTime 参数的时间单位。
 *       workBlockingQueue - 执行前用于保持任务的队列。此队列仅保持由 execute方法提交的 Runnable任务。
 *       threadFactory - 执行程序创建新线程时使用的工厂。
 *       handler - 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序。
 *
 * Version:1.0
 * Open source
 */
public class SimplePoolThread {

    private static SimplePoolThread INSTANCE =new SimplePoolThread();

    public static SimplePoolThread getInstance(){
        return INSTANCE;
    }

    private SimplePoolThread(){

    }

    //CPU的核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    //任务缓冲队列
    private final Queue<Runnable> queue = new LinkedList<Runnable>();

    // 任务调度周期
    private static final int PERIOD_TASK_QOS = 1000;

    /**
     * 核心池的大小:
     * 在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，
     * 除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，
     * 从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。
     * 默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，
     * 当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
     */
    private static final int CORE_POOL_SIZE=CPU_COUNT+1;

    /**
     * 线程池最大线程数:
     * 它表示在线程池中最多能创建多少个线程
     */
    private static final int MAX_POOL_SIZE=CPU_COUNT * 2+1;

    /**
     * 表示线程没有任务执行时最多保持多久时间会终止:
     * 默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用.
     *直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，
     * 如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。
     * 但是如果调用了allowCoreThreadTimeOut(boolean)方法，
     * 在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0.
     */
    private static final long KEEP_ALIVE_TIME = 5;

    /**
     * 参数keepAliveTime的时间单位，有7种取值
     * TimeUnit.DAYS;               //天
     * TimeUnit.HOURS;             //小时
     * TimeUnit.MINUTES;           //分钟
     * TimeUnit.SECONDS;           //秒
     * TimeUnit.MILLISECONDS;      //毫秒
     * TimeUnit.MICROSECONDS;      //微妙
     * TimeUnit.NANOSECONDS;       //纳秒
     */
    private static final TimeUnit UNIT = TimeUnit.SECONDS;

    /**
     * 个阻塞队列，用来存储等待执行的任务：
     * 阻塞队列有以下几种选择
     * ArrayBlockingQueue:有界队列策略--->有界阻塞队列,此队列按 FIFO（先进先出）原则对元素进行排序。队列的头部是在队列中存在时间最长的元素
     * LinkedBlockingQueue:无界队列策略--->无界,队列的大小,默认是Integer.MAX_VALUE,也是FIFO机制
     * SynchronousQueue:直接提交策略--->没有数据缓冲,生产者线程对其的插入操作put必须等待消费者的移除操作take.
     */
    private static final ArrayBlockingQueue BLOCKING_QUEUE = new ArrayBlockingQueue(128);

    /**
     * 线程工厂，主要用来创建线程
     */
    private static final ThreadFactory Thread_Factory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "SimplePoolThread #" + mCount.getAndIncrement());
        }
    };

    /**
     * 表示当拒绝处理任务时的策略
     * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
     * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
     */
    private  final RejectedExecutionHandler mHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            //这里对拒绝执行的任务实现的自定义策略是：将任务放入一个集合中,等待再运行
            queue.offer(runnable);
        }
    };

    /**
     * 将缓冲队列中的任务重新加载到线程池
     */
    private final Runnable mAccessBufferThread = new Runnable(){
        @Override
        public void run() {
            if(!isBufferQueueEmpty()){
                threadPoolExecutor.execute(queue.poll());
            }
        }
    };

    /**
     * 创建一个调度线程池
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * 通过调度线程周期性的执行缓冲队列中任务
     */
    protected final ScheduledFuture<?> mTaskHandler = scheduler.scheduleAtFixedRate(mAccessBufferThread,0,PERIOD_TASK_QOS,TimeUnit.SECONDS);


    public  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor
                                                                    (CORE_POOL_SIZE,
                                                                            MAX_POOL_SIZE,
                                                                            KEEP_ALIVE_TIME,
                                                                            UNIT,
                                                                            BLOCKING_QUEUE,
                                                                            Thread_Factory,
                                                                            mHandler);

    /**
     * 运行run
     * @param task
     */
    public void run(Runnable task){
        if(task !=null){
            threadPoolExecutor.execute(task);
        }
    }

    public void prepare(){
        if(threadPoolExecutor.isShutdown()&&!threadPoolExecutor.prestartCoreThread()){
            int start_core_num = threadPoolExecutor.prestartAllCoreThreads();
        }
    }

    public  boolean isPoolEmpty(){
        if(threadPoolExecutor.getActiveCount()==0){
            return true;
        }else{
            return false;
        }
    }

    public boolean isBufferQueueEmpty(){
        return queue.isEmpty();
    }

    public void shutdown(){
        queue.clear();
        threadPoolExecutor.shutdown();
    }



}
