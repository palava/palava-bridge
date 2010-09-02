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

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Preconditions;

/**
 * MimeTypes define a content type.
 * 
 * @deprecated use {@link javax.activation.MimeType}
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 */
@Deprecated
@Immutable
public final class MimeType {
    
    public static final MimeType ERROR = new MimeType("application/error");
    public static final MimeType PHP = new MimeType("application/x-httpd-php");
    public static final MimeType JSON = new MimeType("application/json");
    public static final MimeType TEXT = new MimeType("text/plain");
    public static final MimeType XML = new MimeType("application/xml");
    public static final MimeType HTML = new MimeType("text/html");
    public static final MimeType IMAGE = new MimeType("image/*");
    public static final MimeType JPEG = new MimeType("image/jpeg");
    
    private final String name;

    public MimeType(String type) {
        this.name = Preconditions.checkNotNull(type, "MimeType");
        Preconditions.checkArgument(type.indexOf("/") >= 0, "Type contains no /");
        Preconditions.checkArgument(type.indexOf("/") != type.length() - 1, "Type ends with /");
    }

    @Override
    public int hashCode() {
        return 31 * 1 + name.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof MimeType) {
            final MimeType other = MimeType.class.cast(that);
            return name.equals(other.name);
        } else {
            return false;
        }
    }

    /**
     * Returns the part after the '/'.
     * 
     * @return the minor part
     */
    public String getMinor() {
        return name.substring(name.indexOf("/") + 1);
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @deprecated use {@link #getName()}
     */
    @Deprecated
    @Override
    public String toString() {
        return name;
    }

}
