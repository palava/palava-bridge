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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * convert java objects into php.
 * 
 * @deprecated without substitution
 * @author Detlef Hüttemann
 */
@Deprecated
/* CHECKSTYLE:OFF */
public class PHPConverter extends ContentConverter {

    public void convertMap( StringBuffer buf, Map<?, ?> map) throws ConversionException {
        buf.append("array(");
        
        for (Entry<?, ?> e : map.entrySet()) {
            final Object key = e.getKey();
            final Object value = e.getValue();
            convertKeyValue(buf, key.toString(), value, KeyValueState.INSIDE);
        }
        
        buf.append(")");
    }

    public void convertKeyValue(StringBuffer buf, String key, Object value, KeyValueState state) throws ConversionException {
        if (state == KeyValueState.ZERO) {
            buf.append("array()");
            return;
        } else if (state == KeyValueState.SINGLE || state == KeyValueState.START) {
            buf.append("array(");
        }

        buf.append(key);
        buf.append("=>");
        convert(buf, value);

        if (state != KeyValueState.LAST) {
            buf.append(",");
        }
        
        if (state == KeyValueState.LAST || state == KeyValueState.SINGLE) {
            buf.append(")");
        }
    }

    public void convertNull( StringBuffer buf ) throws ConversionException {
        buf.append("null");
    }

    public void convertString(StringBuffer buf, String s) throws ConversionException {
        final String buffer = s.replace("\\", "\\\\").replace("'", "\\'");
        buf.append("'").append(buffer).append("'");
    }

    public void convertDate( StringBuffer buf, java.util.Date object ) throws ConversionException {
        // java timestamp:  number of milliseconds since January 1, 1970, 00:00:00 GMT
        // php timestamp:   number of seconds since January 1, 1970, 00:00:00 GMT
        buf.append(object.getTime() / 1000);
    }

    public static Date getJavaDate(String phpdate) {
        if (phpdate == null) return null;
        final long phptime = Long.parseLong(phpdate);
        return new Date(phptime * 1000);
    }

    public void convertNumber(StringBuffer buf, Number object) throws ConversionException {
        buf.append(object);
    }

    public void convertList( StringBuffer buf, List<?> object) throws ConversionException {
        buf.append("array(");
        final Iterator<?> i = object.iterator();
        
        while (i.hasNext()) {
            final Object key = i.next();
            convert(buf, key);
            if (i.hasNext()) {
                buf.append(",");
            }
        }
        buf.append(")");
    }

    public void convertDefault(StringBuffer buf, Object object) throws ConversionException {
        convertString(buf, object.toString());
    }

}
/* CHECKSTYLE:ON */
