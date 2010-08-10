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

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * Abstract base class for Hibernate related entity management.
 * 
 * @deprecated use {@link Command} and an injected {@link Provider} for {@link Session}s 
 *
 * @author Willi Schoenborn
 */
@Deprecated
public abstract class HibernateJob implements Job {
    
    @Inject
    private Provider<Session> provider;
    
    @Override
    public final void process(Call call, Response response, HttpSession httpSession, Server server,
            Map<String, Object> caddy) throws Exception {

        final Session session = provider.get();
        process(call, response, httpSession, server, caddy, session);
        session.flush();
    }
    
    /**
     * Delegates the call to sub classes, including the Hibernate {@link Session}.
     * 
     * @param request the request
     * @param response the {@link Response}
     * @param httpSession the {@link HttpSession}
     * @param server the {@link Server}
     * @param caddy the caddy
     * @param session the {@link Session}
     * @throws Exception if processing failed
     */
    public abstract void process(Call request, Response response, HttpSession httpSession, Server server,
        Map<String, Object> caddy, Session session) throws Exception;

}
