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

package de.cosmocode.palava.bridge.simple;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import de.cosmocode.collections.utility.AbstractUtilityMap;
import de.cosmocode.collections.utility.UtilityMap;
import de.cosmocode.collections.utility.UtilitySet;
import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.JsonCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.request.HttpRequest;

/**
 * parse the request content as a json object.
 * 
 * @deprecated 
 * 
 * @author Detlef Huettemann
 */
@Deprecated
class SimpleJsonCall extends SimpleTextCall implements JsonCall {
    
    private JSONObject json;
    private UtilityMap<String, Object> map;
    private Arguments arguments;

    SimpleJsonCall(HttpRequest request, Command command, Header header, InputStream stream) {
        super(request, command, header, stream);
    }

    public final JSONObject getJSONObject() throws ConnectionLostException, JSONException {
        if (json == null) {
            json = new JSONObject(getText());
        }
        return json;
    }
    
    @Deprecated
    public final <K, V> UtilityMap<K, V> getArgs() {
        if (arguments == null) {
            arguments = new InternalArguments();
        }
        return (UtilityMap<K, V>) arguments;
    }
    
    public final Arguments getArguments() {
        if (arguments == null) {
            arguments = new InternalArguments();
        }
        return arguments;
    }
    
    private class InternalArguments extends AbstractUtilityMap<String, Object> implements Arguments {
        
        @Override
        public void require(String... keys) throws MissingArgumentException {
            final Set<String> keySet = keySet();
            for (String key : keys) {
                if (keySet.contains(key)) continue;
                throw new MissingArgumentException(key);
            }                
        }
        
        private void lazyLoad() {
            if (json == null) {
                try {
                    json = getJSONObject();
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            if (map == null) {
                map = JSON.asMap(json);
            }
        }
        
        @Override
        public UtilitySet<Map.Entry<String, Object>> entrySet() {
            lazyLoad();
            return map.entrySet();
        }
        
        @Override
        public Object put(String key, Object value) {
            lazyLoad();
            try {
                return json.put(key, value);
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        }
        
    }

    public final Map<String, String> getStringedArguments() throws ConnectionLostException {
        if (!getText().startsWith("{")) {
            // dirty hack to ignore everything that is not a JsonObject
            return new LinkedHashMap<String, String>();
        } else {
            final Map<String, String> stringed = new LinkedHashMap<String, String>();
            for (final Map.Entry<String, Object> entry : getArguments().entrySet()) {
                final Object value = entry.getValue();
                stringed.put(entry.getKey(), value == null ? null : value.toString());
            }
            return stringed;
        }
    }

}
