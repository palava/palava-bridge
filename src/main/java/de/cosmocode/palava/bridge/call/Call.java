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

package de.cosmocode.palava.bridge.call;

import java.io.IOException;
import java.io.InputStream;

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.request.HttpRequest;
import de.cosmocode.palava.ipc.IpcCall;

/**
 * A Call represents one single access.
 *
 * @author Willi Schoenborn
 */
public interface Call extends IpcCall {

    /**
     * Provide the arguments of this call.
     * 
     * @return the arguments
     * @throws UnsupportedOperationException if this call does not support
     *         Arguments
     */
    Arguments getArguments();
    
    /**
     * Provide the surrounding {@link HttpRequest}.
     * 
     * @return the request
     */
    HttpRequest getHttpRequest();
    
    /**
     * Provides an optional binary inputstream.
     * 
     * @return the inputstream
     * @throws UnsupportedOperationException if this call does not provide
     *         a binary stream
     */
    InputStream getInputStream();
    
    /**
     * Provide the header, this call has been build upon.
     * 
     * @return the header
     */
    Header getHeader();
    
    /**
     * Discard all bytes left in the stream without parsing them.
     * 
     * @deprecated don't use
     * 
     * @throws ConnectionLostException if stream has been closed
     * @throws IOException if an error occurs during read
     */
    @Deprecated
    void discard() throws ConnectionLostException, IOException;
    
}
