package com.wyj.test;

import java.util.concurrent.*;

public class Homework03 {

    private static int result = 0;

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池
        // 异步执行 下面方法

        // 1、通过join方法等待结果执行完后，返回主线程
        Thread task = new Thread(() -> result = sum());
        task.start();
        task.join();

        // 2、FutureTask
        FutureTask<Integer> futureTask = new FutureTask(() -> sum());
        futureTask.run();
        result = futureTask.get();

        // 3、线程池
        Future<Integer> submit = executor.submit(() -> sum());
        result = submit.get();

        // 4、创建一个长度为1的CountDownLatch，在线程中将计数-1
        new Thread(() -> {
            result = sum();
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();


        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
        // 关闭线程池
        executor.shutdown();
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}