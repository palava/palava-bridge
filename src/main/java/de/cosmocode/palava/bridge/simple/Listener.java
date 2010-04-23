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

import de.cosmocode.palava.core.Service;

/**
 * A {@link Listener} is able to listen
 * on one specific port and establish a socket
 * connection. 
 *
 * @author Willi Schoenborn
 */
public interface Listener extends Service {

    /**
     * Starts this socket connector, which
     * opens a server socket.
     * 
     * @throws IOException if socket creation failed.
     */
    void run() throws IOException;
    
    /**
     * Blocks until socket has been closed.
     */
    void stop();
    
}
