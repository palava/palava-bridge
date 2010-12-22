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

package de.cosmocode.palava.legacy;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Job;

/**
 * Abstract implementation of {@link UtilityJob}. 
 *
 * @deprecated witout substitution
 * @author Willi Schoenborn
 */
@Deprecated
/* CHECKSTYLE:OFF */
public abstract class UtilityJobImpl implements Job, UtilityJob {
    
    @Override
    public void require(String... keys) throws MissingArgumentException {
        for (String key : keys) {
            if (!hasArgument(key)) throw new MissingArgumentException(key);
        }
    }
    
    @Override
    public void requireOneOf(String... keys) throws Exception {
        boolean anyArgumentGiven = false;
        for (String key : keys) {
            if (hasArgument(key)) anyArgumentGiven = true;
        }
        if (!anyArgumentGiven) throw new MissingArgumentException("need one of " + Arrays.toString(keys));
    }

    @Override
    public boolean getBool(String key) throws MissingArgumentException {
        if (hasArgument(key)) {
            return getOptBool(key);
        } else {
            throw new MissingArgumentException(this, key, "boolean");
        }
    }
    
    @Override
    public boolean getOptBool(String key) {
        final String param = getOptional(key);
        return StringUtils.isNotBlank(param) && !param.equalsIgnoreCase("false");
    }
    
    @Override
    public boolean getOptBool(String key, boolean defaultValue) {
        if (hasArgument(key)) {
            return getOptBool(key);
        } else {
            return defaultValue;
        }
    }
    

    
    @Override
    public int getInt(String key) throws MissingArgumentException {
        if (hasArgument(key)) {
            return Integer.parseInt(getOptional(key));
        } else {
            throw new MissingArgumentException(this, key, "int");
        }
    }
    
    @Override
    public int getOptInt(String key) {
        return getOptInt(key, 0);
    }
    
    @Override
    public int getOptInt(String key, int defaultValue) {
        if (hasArgument(key)) {
            final String val = getOptional(key);
            if (StringUtils.isNumeric(val)) {
                return Integer.parseInt(val);
            }
        }
        return defaultValue;
    }
    
    
    
    @Override
    public long getLong(String key) throws MissingArgumentException {
        if (hasArgument(key)) {
            return Integer.parseInt(getOptional(key));
        } else {
            throw new MissingArgumentException(this, key, "int");
        }
    }
    
    @Override
    public long getOptLong(String key) {
        return getOptLong(key, 0);
    }
    
    @Override
    public long getOptLong(String key, long defaultValue) {
        if (hasArgument(key)) {
            final String val = getOptional(key);
            if (StringUtils.isNumeric(val)) {
                return Long.parseLong(val);
            }
        }
        return defaultValue;
    }

    public double optDouble(String key, double defaultValue) {
        if (hasArgument(key)) {
            final String val = getOptional(key);
            try {
                return Double.parseDouble(val);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    
    // -----------------
    // alternative names

    public boolean getOptionalBool(String key) {
        return getOptBool(key);
    }
    public boolean getOptionalBool(String key, boolean defaultValue) {
        return getOptBool(key, defaultValue);
    }
    public int getOptionalInt(String key) {
        return getOptInt(key);
    }
    public int getOptionalInt(String key, int defaultValue) {
        return getOptInt(key, defaultValue);
    }
    public long getOptionalLong(String key) {
        return getOptLong(key);
    }
    public long getOptionalLong(String key, long defaultValue) {
        return getOptLong(key, defaultValue);
    }
    
    
    // -----------------
    // deprecated
    // -----------------
    
    @Deprecated
    @Override
    public String lookup (String key) throws MissingArgumentException, Exception {
        return getMandatory(key);
    }
    
    @Deprecated
    @Override
    public String lookup (String key, String argumentType) throws MissingArgumentException, Exception {
        return getMandatory(key, argumentType);
    }
    
    @Deprecated
    @Override
    public String lookupOptional (String key) {
        return getOptional(key);
    }
    
    @Deprecated
    @Override
    public String lookupOptional (String key, String defaultValue) {
        return getOptional(key, defaultValue);
    }
    
    @Deprecated
    @Override
    public boolean lookupBool (String key) throws MissingArgumentException {
        return getBool(key);
    }
    
    @Deprecated
    @Override
    public boolean lookupOptionalBool (String key) {
        return getOptBool(key);
    }
    
    @Deprecated
    @Override
    public int lookupInt (String key) throws MissingArgumentException {
        return getInt(key);
    }
    
    @Deprecated
    @Override
    public int lookupOptionalInt (String key) {
        return getOptInt(key);
    }
    
    @Deprecated
    @Override
    public int lookupOptionalInt (String key, int defaultValue) {
        return getOptInt(key, defaultValue);
    }


}
/* CHECKSTYLE:ON */
