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
import de.cosmocode.palava.bridge.scope.Scopes;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * {@link DataCall} based {@link HibernateJob}.
 * 
 * @deprecated without substitution
 * @author Willi Schoenborn
 */
@Deprecated
public abstract class DataHibJob extends HibernateJob {

    @Override
    public final void process(Call request, Response response, HttpSession s, Server server, 
        Map<String, Object> caddy, Session session) throws Exception {

        final DataCall dataRequest = (DataCall) request;
        final Map<String, String> args = dataRequest.getStringedArguments();
        
        process(args, response, s, server, caddy, session);
    }
    
    /**
     * Valides the given arguments.
     * 
     * @param keys the argument names
     * @throws MissingArgumentException if any argument is missing
     */
    protected final void validate(String... keys) throws MissingArgumentException {
        Scopes.getCurrentCall().getArguments().require(keys);
    }
    
    /**
     * Process method for sub classes.
     * 
     * @param args arguments
     * @param response response
     * @param s http session
     * @param server server
     * @param caddy caddy
     * @param session hibernate session
     * @throws Exception if anything went wrong
     */
    protected abstract void process(Map<String, String> args, Response response, HttpSession s, Server server,
        Map<String, Object> caddy, Session session) throws Exception;
    
}
