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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * can parse the mimetype from a file extension.
 * 
 * @author Detlef Hüttemann
 */
public final class MimeTypes {

    public static final MimeTypes SINGLETON;
    
    static {
        try {
            // TODO fixme
            SINGLETON = new MimeTypes("/etc/mime.types");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    };

    private final Map<String, MimeType> mimeTypes;

    private MimeTypes(String file) throws IOException {
        mimeTypes = new HashMap<String, MimeType>();
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        final Pattern pattern = Pattern.compile("\\s+");

        while (null != (line = reader.readLine())) {
            if (line.length() > 0 && line.charAt(0) != '#') {
                final String [] results = pattern.split(line);
                if (results.length > 1) {
                    final MimeType mimeType = new MimeType(results[0]);
                    for (String name : results) {
                        mimeTypes.put(name, mimeType);
                    }
                }
            }
        }
    }

    /**
     * Get a {@link MimeType} by file extension.
     * 
     * @param ext the file extension
     * @return the found {@link MimeType} or null if there was no mimetype
     *         found for the given extension
     */
    public MimeType byExtension(String ext) {
        return mimeTypes.get(ext);
    }
    
    /**
     * Get a {@link MimeType} by name.
     * 
     * @param name the name
     * @return the found {@link MimeType} of null if there was no mimetype
     *         found for the given name
     */
    public MimeType byName(String name) {
        final int dot = name.lastIndexOf(".");
        if (dot != -1) 
            return byExtension(name.substring(dot + 1).toLowerCase());
        return null;
    }

}
