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

import java.io.InputStream;
import java.io.OutputStream;

import de.cosmocode.palava.core.Service;

/**
 * A communicator handles exactly one incoming http request,
 * which itself results in one socket connections.
 *
 * @author Willi Schoenborn
 */
public interface Communicator extends Service {

    /**
     * Communicate with the php frontend using the given input and
     * output streams. A communication usually consists of multiple
     * call/response cycles.
     * 
     * @param input the socket input stream
     * @param output the socket output stream
     */
    void communicate(InputStream input, OutputStream output);
    
}
