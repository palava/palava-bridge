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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * abstract, implements the convert() function.
 * 
 * @author Detlef Hüttemann
 * @deprecated use JSONRenderer instead
 */
@Deprecated
public abstract class ContentConverter {

    public void convert(StringBuffer buf, Object object) throws ConversionException {
        if (object == null) {
            convertNull(buf);
        } else if (object instanceof String) {
            convertString(buf, (String) object);
        } else if (object instanceof java.util.Date) {
            convertDate(buf, (java.util.Date) object);
        } else if (object instanceof Number) {
            convertNumber(buf, (Number) object);
        } else if (object instanceof List<?>) {
            convertList(buf, List.class.cast(object));
        } else if (object instanceof Set<?>) {
            convertList(buf, new ArrayList<Object>((Set<?>) object));
        } else if (object instanceof Map<?, ?>) {
            convertMap(buf, Map.class.cast(object));
        } else if (object instanceof Convertible) {
            ((Convertible) object).convert(buf, this);
        } else {
            convertDefault(buf, object);
        }
    }

    public abstract void convertNull(StringBuffer buf) throws ConversionException;
    
    public abstract void convertString(StringBuffer buf, String object) throws ConversionException;
    
    public abstract void convertDate(StringBuffer buf, java.util.Date object) throws ConversionException;
    
    public abstract void convertNumber(StringBuffer buf, Number object) throws ConversionException;
    
    public abstract void convertList(StringBuffer buf, List<?> object) throws ConversionException;
    
    public abstract void convertMap(StringBuffer buf, Map<?, ?> object) throws ConversionException;
    
    public abstract void convertDefault(StringBuffer buf, Object object) throws ConversionException;
    
    public abstract void convertKeyValue(StringBuffer buf, String key, Object value, KeyValueState state) throws ConversionException;

}
