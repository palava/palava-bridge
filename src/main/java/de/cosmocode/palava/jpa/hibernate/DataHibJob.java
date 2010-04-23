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

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;

public abstract class DataHibJob extends HibernateJob {

    private Map<String, String> args;
    
    @Override
    public final void process(Call request, Response response, de.cosmocode.palava.bridge.session.HttpSession s, Server server, 
        Map<String, Object> caddy, Session session) throws Exception {

        DataCall dataRequest = (DataCall) request;
        args = dataRequest.getStringedArguments();
        
        process(args, response, s, server, caddy, session);
    }
    
    protected final void validate(String... keys) throws MissingArgumentException {
        for (String key : keys) {
            if (!args.containsKey(key)) throw new MissingArgumentException(key);
        }
    }
    
    protected abstract void process(Map<String, String> args, Response response, de.cosmocode.palava.bridge.session.HttpSession s, Server server,
        Map<String, Object> caddy, Session session) throws Exception;
    
}