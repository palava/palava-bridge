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

package de.cosmocode.palava.bridge.content;

import java.io.IOException;
import java.io.OutputStream;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.MimeType;

/**
 * Plain text content.
 *
 * @deprecated without substitution
 * @author Detlef Hüttemann
 */
@Deprecated
public class TextContent implements Content {

    private final byte[] bytes;

    public TextContent(String text) {
        bytes = text == null ? "null".getBytes(CHARSET) : text.getBytes(CHARSET);
    }

    @Override
    public MimeType getMimeType() {
        return MimeType.TEXT;
    }
    
    @Override
    public long getLength() {
        return bytes.length;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(bytes, 0, bytes.length);
    }
    
    @Override
    public String toString() {
        return new String(bytes, CHARSET);
    }

}
