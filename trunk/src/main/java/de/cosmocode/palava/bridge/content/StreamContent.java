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
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.io.LimitInputStream;

import de.cosmocode.palava.bridge.MimeType;

/**
 * General content to return streams.
 * 
 * @deprecated without substitution
 * @author Tobias Sarnowski
 * @author Willi Schoenborn
 */
@Deprecated
public class StreamContent extends AbstractContent {
    
    private final InputStream stream;
    private final int length;
    
    public StreamContent(InputStream stream, long length, MimeType mime) {
        super(mime);
        this.stream = stream;
        this.length = (int) length;
    }
    
    /** 
     * Returns the underlying inputstream.
     * 
     * note that if you read from the input stream manually, 
     * the write StreamContent.write method will fail
     *
     * @return InputStream
     */
    public InputStream getInputStream() {
        return stream;
    }
    
    @Override
    public long getLength() {
        return length;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        IOUtils.copy(new LimitInputStream(stream, length), out);
    }
    
}
