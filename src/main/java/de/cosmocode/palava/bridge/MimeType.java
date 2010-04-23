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

import com.google.common.base.Preconditions;

import de.cosmocode.patterns.Immutable;

/**
 * MimeTypes define a content type.
 * 
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 */
@Immutable
public class MimeType {
    
    public static final MimeType ERROR = new MimeType("application/error");
    public static final MimeType PHP = new MimeType("application/x-httpd-php");
    public static final MimeType JSON = new MimeType("application/json");
    public static final MimeType TEXT = new MimeType("text/plain");
    public static final MimeType XML = new MimeType("application/xml");
    public static final MimeType HTML = new MimeType("text/html");
    public static final MimeType IMAGE = new MimeType("image/*");
    public static final MimeType JPEG = new MimeType("image/jpeg");
    
    private final String type;

    public MimeType(String type) {
        this.type = Preconditions.checkNotNull(type, "MimeType");
        final int slash = type.indexOf("/");
        Preconditions.checkArgument(slash >= 0, "Type contains no /");
        Preconditions.checkArgument(slash != type.length() - 1, "Type ends with /");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MimeType)) {
            return false;
        }
        final MimeType other = (MimeType) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the part after the '/'.
     * 
     * @return the minor part
     */
    public String getMinor() {
        return type.substring(type.indexOf("/") + 1);
    }
    
    @Override
    public String toString() {
        return type;
    }

}
