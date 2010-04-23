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

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * @deprecated use Cache annotation
 *
 * @author Willi Schoenborn
 */
@Deprecated
public abstract class CachableDataJob extends CachableJob {
    
    private Map<String, String> args;

    @Override
    @SuppressWarnings("unchecked")
    public final void process(Call request, Response response, Server server, HttpSession session, 
        Map<String, Object> caddy) throws ConnectionLostException, Exception {
        
        DataCall dataRequest = (DataCall) request;
        args = dataRequest.getStringedArguments();
        
        process(args, response, session, server, caddy);
    }
    
    protected abstract void process(Map<String, String> args, Response response, HttpSession session, Server server, 
        Map<String, Object> caddy) throws ConnectionLostException, Exception;
    
    
    
    // methods implemented from UtilityJob

    @Override
    public String getMandatory(String key) throws MissingArgumentException {
        if (args.containsKey(key)) return args.get(key);
        else throw new MissingArgumentException(this, key);
    }

    @Override
    public String getMandatory(String key, String argumentType) throws MissingArgumentException {
        if (args.containsKey(key)) return args.get(key);
        else throw new MissingArgumentException(this, key, argumentType);
    }

    @Override
    public String getOptional(String key) {
        return args.get(key);
    }

    @Override
    public String getOptional(String key, String defaultValue) {
        if (args.containsKey(key)) return args.get(key);
        else return defaultValue;
    }

    @Override
    public boolean hasArgument(String key) {
        return args.containsKey(key);
    }
    
}