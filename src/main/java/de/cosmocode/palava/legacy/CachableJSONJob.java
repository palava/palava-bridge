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

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.JsonCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * 
 * @deprecated use Cache annotation
 *
 * @author Willi Schoenborn
 */
@Deprecated
public abstract class CachableJSONJob extends CachableJob {
    
    private JSONObject json;

    @Override
    public final void process(Call request, Response response, Server server, HttpSession session, 
        Map<String, Object> caddy) throws ConnectionLostException, Exception {

        JsonCall jRequest = (JsonCall) request;
        json = jRequest.getJSONObject();
        
        process(json, response, session, server, caddy);
    }
    
    protected abstract void process(JSONObject json, Response response, HttpSession session, Server server, 
        Map<String, Object> caddy) throws ConnectionLostException, Exception;
    
    @Override
    public final void require(String... keys) throws MissingArgumentException {
        require(json, keys);
    }
    
    protected final void require(JSONObject json, String... keys) throws MissingArgumentException {
        for (String key : keys) {
            if (!json.has(key)) throw new MissingArgumentException(key);
        }
    }
    

    // methods implemented from UtilityJob

    @Override
    public String getMandatory(String key) throws MissingArgumentException, JSONException {
        if (json.has(key)) return json.getString(key);
        else throw new MissingArgumentException(this, key);
    }

    @Override
    public String getMandatory(String key, String argumentType) throws MissingArgumentException, JSONException {
        if (json.has(key)) return json.getString(key);
        else throw new MissingArgumentException(this, key, argumentType);
    }

    @Override
    public String getOptional(String key) {
        if (json.has(key)) return json.optString(key);
        else return null;
    }

    @Override
    public String getOptional(String key, String defaultValue) {
        if (json.has(key)) return json.optString(key);
        else return defaultValue;
    }

    @Override
    public boolean hasArgument(String key) {
        return json.has(key);
    }

}
