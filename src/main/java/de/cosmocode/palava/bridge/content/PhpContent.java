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

import de.cosmocode.palava.bridge.MimeType;

/**
 * Content which uses PHP notation.
 * 
 * @deprecated without substitution
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 */
@Deprecated
public class PhpContent extends AbstractContent {
    
    public static final PhpContent OK;
    public static final PhpContent NOT_FOUND;
    
    static {
        try {
            OK = new PhpContent("ok");
            NOT_FOUND = new PhpContent("not_found");
        } catch (ConversionException e) {
            throw new ExceptionInInitializerError(e);
        }
    };
    
    private final byte[] bytes;
    
    public PhpContent(Object object) throws ConversionException {
        super(MimeType.PHP);
        final PHPConverter converter = new PHPConverter();
        final StringBuilder buf = new StringBuilder();
        converter.convert(buf, object);
        bytes = buf.toString().getBytes(CHARSET);
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
