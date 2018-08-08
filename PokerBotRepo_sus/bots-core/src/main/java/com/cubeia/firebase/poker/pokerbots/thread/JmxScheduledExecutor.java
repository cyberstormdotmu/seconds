/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cubeia.firebase.poker.pokerbots.thread;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

import com.cubeia.firebase.util.NamedThreadFactory;

/**
 * The JMX Executor is an MBean monitoring wrapper for a Thread pool.
 * 
 * The underlying executor will not support scheduling and uses an
 * unbound blocking queue. If you want to support a different queue etc.
 * you need to subclass this class.
 * 
 * @author fredrik.johansson
 *
 */
public class JmxScheduledExecutor implements JmxScheduledExecutorMBean {

    /** The logger */
    private Logger log = Logger.getLogger(JmxScheduledExecutor.class);
    
    /** The underlying thread pool for executing work */
    private ScheduledThreadPoolExecutor executor;    
    
    /** Keep a reference to the thread name for logging and JMX purposes */
    private String name;
    
    /**
     * Create the WorkQueue.
     * 
     * @param coreSize
     * @param maxSize
     * @param name, name of the created threads.
     */
    public JmxScheduledExecutor(int coreSize, String name) {
        this.name = name;
        executor = new ScheduledThreadPoolExecutor(coreSize, new NamedThreadFactory(name));
        initJmx();
    }
    
    /**
     * Submit a task for execution.
     */
    public void submit(Runnable task) {
        executor.submit(task);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    	return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long delay, TimeUnit unit) {
    	return executor.scheduleAtFixedRate(command, initialDelay, delay, unit);
    }
    
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return executor.schedule(command, delay, unit);
	}
    
    /**
     * Submit a task for execution.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void submit(Callable task) {
        executor.submit(task);
    }
    
    public long getQueueSize() {
        return executor.getQueue().size();
    }

    public long getActiveThreadCount() {
        return executor.getActiveCount();
    }
    
    public long getThreadCount() {
        return executor.getPoolSize();
    }
    
    public long getMaxThreads() {
    	return executor.getMaximumPoolSize();
    }
    
    public String getStateDescription() {
        return executor.isShutdown() ? "Shutdown" : "Running";
    }

    /**
     * Currently does nothing. 
     * The executor is initialized in the constructor.
     */
    public void start() {}

    /**
     * Shuts down the executor.
     * Currently this is a one way stop, it is not
     * possible to re-start the service.
     * 
     */
    public void stop() {
        executor.shutdown();
    }


    /**
     * Add MBean info to JMX.
     * Will be called from the constructor.
     *
     */
    private void initJmx() {
        try {
            log.info("Binding JMX Executor '"+name+"' to JMX");
            MBeanServer mbs = getMBeanServer();
            ObjectName monitorName = new ObjectName("com.cubeia.executors:type="+name);
            mbs.registerMBean(this, monitorName);
        } catch(Exception e) {
            log.error("failed to start JMX for named threads: "+name, e);
        }
    }
    
    private MBeanServer getMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }

	

	


}
