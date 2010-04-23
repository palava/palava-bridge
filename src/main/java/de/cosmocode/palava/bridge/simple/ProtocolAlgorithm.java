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
import java.util.Map;

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.Header;

public interface ProtocolAlgorithm {
    
    Map<String, String> open(Header header, InputStream input, OutputStream output);

    /**
     * Parses the palava protocol:
     * 
     * <p>
     *   {@code <type>://<aliasedName>/<sessionId>/(<contentLength>)?<content>}
     * </p>
     * 
     * <p>
     *   After the header has been parsed, the next byte read from the stream
     *   is the first byte of the actual content.
     * </p>
     * 
     * @param input the input to read from
     * @return a parsed Header
     * @throws ProtocolException if the supplied stream does not contain a valid input
     * @throws ConnectionLostException if connection to the socket broke up during reading
     */
    Header read(InputStream input);
    
    void sendTo(Content content, OutputStream output) throws IOException;
    
}
