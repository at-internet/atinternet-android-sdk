/*
This SDK is licensed under the MIT license (MIT)
Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.atinternet.tracker;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Store the hits to build
 */
class TrackerQueue extends LinkedBlockingQueue<Runnable> {

    /**
     * Boolean to know if the database content is present in queue
     */
    private static boolean ENABLED_FILL_QUEUE_FROM_DATABASE = true;

    /**
     * Builder queue instance
     */
    private static TrackerQueue instance;

    /**
     * Executor services for run tasks in the queue
     */
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Runnable to execute all tasks in queue
     */
    private final Runnable executeTaskRunnable;

    /**
     * Set enabled fill queue from database
     *
     * @param enabled boolean
     */
    static void setEnabledFillQueueFromDatabase(boolean enabled) {
        ENABLED_FILL_QUEUE_FROM_DATABASE = enabled;
    }

    /**
     * Get enabled fill queue from database
     *
     * @return boolean
     */
    static boolean getEnabledFillQueueFromDatabase() {
        return ENABLED_FILL_QUEUE_FROM_DATABASE;
    }

    /**
     * Init the builderQueue
     */
    private TrackerQueue() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        executeTaskRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isEmpty()) {
                        scheduledExecutorService.execute(take());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Get instance of the builder queue singleton
     *
     * @return BuilderQueue
     */
    static TrackerQueue getInstance() {
        if (instance == null) {
            instance = new TrackerQueue();
        }
        return instance;
    }

    /**
     * Put a new task and run it
     *
     * @param runnable Runnable
     */
    @Override
    public void put(Runnable runnable) {
        try {
            if (runnable != null) {
                super.put(runnable);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executeTaskRunnable.run();
    }
}
