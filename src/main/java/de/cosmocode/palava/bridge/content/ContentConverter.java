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

import java.util.Date;
import java.util.Map;

import de.cosmocode.rendering.Renderer;

/**
 * abstract, implements the convert() function.
 * 
 * @deprecated use {@link Renderer}
 * @author Detlef Hüttemann
 */
@Deprecated
/* CHECKSTYLE:OFF */
public abstract class ContentConverter {

    public void convert(StringBuilder buf, Object object) throws ConversionException {
        if (object == null) {
            convertNull(buf);
        } else if (object instanceof String) {
            convertString(buf, (String) object);
        } else if (object instanceof Date) {
            convertDate(buf, (Date) object);
        } else if (object instanceof Number) {
            convertNumber(buf, (Number) object);
        } else if (object instanceof Convertible) {
            ((Convertible) object).convert(buf, this);
        } else if (object instanceof Iterable<?>) {
            convertList(buf, (Iterable<?>) object);
        } else if (object instanceof Map<?, ?>) {
            convertMap(buf, Map.class.cast(object));
        } else {
            convertDefault(buf, object);
        }
    }

    public abstract void convertNull(StringBuilder buf) throws ConversionException;
    
    public abstract void convertString(StringBuilder buf, String object) throws ConversionException;
    
    public abstract void convertDate(StringBuilder buf, Date object) throws ConversionException;
    
    public abstract void convertNumber(StringBuilder buf, Number object) throws ConversionException;
    
    public abstract void convertList(StringBuilder buf, Iterable<?> object) throws ConversionException;
    
    public abstract void convertMap(StringBuilder buf, Map<?, ?> object) throws ConversionException;
    
    public abstract void convertDefault(StringBuilder buf, Object object) throws ConversionException;
    
    public abstract void convertKeyValue(StringBuilder buf, String key, Object value, KeyValueState state) throws ConversionException;

}

/* CHECKSTYLE:ON */
