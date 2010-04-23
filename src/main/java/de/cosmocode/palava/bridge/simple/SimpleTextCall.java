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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.TextCall;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.request.HttpRequest;

/**
 * parse the request content as one big plain text.
 * 
 * @deprecated
 * 
 * @author Tobias Sarnowski
 */
@Deprecated
class SimpleTextCall extends AbstractCall implements TextCall {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleTextCall.class);
    
    private String text;

    SimpleTextCall(HttpRequest request, Command command, Header header, InputStream stream) {
        super(request, command, header, stream);
    }

    public String getText() throws ConnectionLostException {
        if (text == null) {
            final byte[] buffer = read();
            final ByteBuffer bb = ByteBuffer.wrap(buffer);
            final CharBuffer cb = CHARSET.decode(bb);
            text = cb.toString();
        }
        LOG.trace("Received text: {}", text);
        return text;
    }

}
