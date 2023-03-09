/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import java.security.Security;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author vanth
 */
public class ThreadPoolManagement {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ThreadPoolManagement.class.getName());
    final private static int SCALING_FACTOR = 8;

    final private static ThreadPoolExecutor threadPoolExecutorForEverything = (ThreadPoolExecutor) Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * SCALING_FACTOR);

    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
        log.info("Number of CPU: " + Runtime.getRuntime().availableProcessors());
    }

    public static void release() {
        log.info("Release all of thread pools");
        threadPoolExecutorForEverything.shutdown();
        try {
            if (!threadPoolExecutorForEverything.awaitTermination(2, TimeUnit.DAYS)) {
                threadPoolExecutorForEverything.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPoolExecutorForEverything.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static ThreadPoolExecutor getThreadPoolExecutorForEverything() {
        return threadPoolExecutorForEverything;
    }
}
