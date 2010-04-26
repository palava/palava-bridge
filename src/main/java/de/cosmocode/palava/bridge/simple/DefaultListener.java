/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.bridge.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.commons.State;
import de.cosmocode.palava.bridge.BridgeConfig;
import de.cosmocode.palava.core.Registry;
import de.cosmocode.palava.core.event.PostFrameworkStart;
import de.cosmocode.palava.core.event.PreFrameworkStop;
import de.cosmocode.palava.core.lifecycle.LifecycleException;

/**
 * Default implementation of the {@link Listener} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultListener implements Listener, PostFrameworkStart, PreFrameworkStop, 
    ThreadFactory, UncaughtExceptionHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListener.class);
    
    private final Communicator communicator;
    
    private final int port;
    
    private final long socketTimeout;

    private final TimeUnit socketTimeoutUnit;
    
    private final ExecutorService service;
    
    private State state = State.NEW;
    
    @Inject
    public DefaultListener(
        Registry registry,
        Communicator communicator,
        @Named(BridgeConfig.PORT) int port,
        @Named(SimpleBridgeConfig.SOCKET_TIMEOUT) long socketTimeout,
        @Named(SimpleBridgeConfig.SOCKET_TIMEOUT_UNIT) TimeUnit socketTimeoutUnit,
        @WorkerThreads ExecutorService service) {
        
        Preconditions.checkNotNull(registry, "Registry");
        
        this.communicator = Preconditions.checkNotNull(communicator, "Communicator");
        
        Preconditions.checkArgument(port >= 0, "Port must be positive, but was %s", port);
        this.port = port;
        
        this.socketTimeout = socketTimeout;
        this.socketTimeoutUnit = socketTimeoutUnit;

        this.service = service;
        
        registry.register(PostFrameworkStart.class, this);
        registry.register(PreFrameworkStop.class, this);
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(this);
        return thread;
    }
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOG.error("Uncaught exception during connection", e);
    }
    
    @Override
    public void eventPostFrameworkStart() {
        final Thread thread = newThread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    DefaultListener.this.run();
                } catch (IOException e) {
                    throw new LifecycleException(e);
                }
            }
            
        });
        thread.setName("Simple Bridger Listener");
        thread.start();
    }
    
    @Override
    public void run() throws IOException {
        LOG.debug("Starting {}", this);
        state = State.STARTING;
        LOG.info("Creating socket on port {}", port);
        final ServerSocket socket = new ServerSocket(port);
        LOG.info("Setting socket timeout to {} {}", socketTimeout, socketTimeoutUnit.name().toLowerCase());
        final int timeout = (int) socketTimeoutUnit.toMillis(socketTimeout);
        socket.setSoTimeout(timeout);
        state = State.RUNNING;
        LOG.debug("{} is running", this);
        
        while (state == State.RUNNING) {
            
            final Socket client;
            
            try {
                client = socket.accept();
            } catch (SocketTimeoutException e) {
                continue;
            }

            service.execute(new Runnable() {
                
                @Override
                public void run() {
                    final InputStream input;
                    final OutputStream output;
                    
                    try {
                        input = client.getInputStream();
                        output = client.getOutputStream();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }

                    try {
                        communicator.communicate(input, output);
                    } finally {
                        try {
                            client.shutdownInput();
                            client.shutdownOutput();
                            client.close();
                        } catch (IOException e) {
                            LOG.warn("Closing socket failed", e);
                        }
                    }
                }
                
            });
            
        }
        
        socket.close();
    }

    @Override
    public void eventPreFrameworkStop() {
        stop();
    }
    
    @Override
    public void stop() {
        LOG.debug("Stopping {}", this);
        state = State.STOPPING;
    }
    
}
