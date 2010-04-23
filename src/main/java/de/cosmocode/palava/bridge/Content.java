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

package de.cosmocode.palava.bridge;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * A content can be seen as any kind
 * of data that can be "shipped over the wire"
 * to PHP.
 *
 * @author Willi Schoenborn
 */
public interface Content {
    
    Charset CHARSET = Charset.forName("UTF-8");
    
    /**
     * Provide the mimetype.
     * 
     * @return the mimetype of this content
     */
    MimeType getMimeType();
    
    /**
     * Provide the length of the content.
     * 
     * @return the length
     */
    long getLength();
    
    /**
     * Writes this content to the given {@link OutputStream}.
     * 
     * @param out the target
     * @throws IOException if an error occurs 
     */
    void write(OutputStream out) throws IOException;
    
}
