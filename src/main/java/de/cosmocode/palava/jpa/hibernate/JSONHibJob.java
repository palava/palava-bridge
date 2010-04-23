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

package de.cosmocode.palava.jpa.hibernate;

import java.util.Map;

import org.hibernate.Session;
import org.json.JSONObject;

import com.google.inject.Provider;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.JsonCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * @deprecated use {@link Command} and an injected {@link Provider} for {@link Session}s 
 *
 * @author Willi Schoenborn
 */
@Deprecated
public abstract class JSONHibJob extends HibernateJob {
    
    private JSONObject json;

    @Override
    public final void process(Call request, Response response, HttpSession httpSession, Server server, 
        Map<String, Object> caddy, org.hibernate.Session session) throws Exception {

        final JsonCall jRequest = (JsonCall) request;
        json = jRequest.getJSONObject();
        
        process(json, response, httpSession, server, caddy, session);
    }
    
    protected final void require(String... keys) throws MissingArgumentException {
        for (String key : keys) {
            if (!json.has(key)) throw new MissingArgumentException(key);
        }
    }
    
    /**
     * 
     * @param json
     * @param response
     * @param httpSession
     * @param server
     * @param caddy
     * @param session
     * @throws Exception
     */
    protected abstract void process(JSONObject json, Response response, HttpSession httpSession, Server server,
        Map<String, Object> caddy, org.hibernate.Session session) throws Exception;

}