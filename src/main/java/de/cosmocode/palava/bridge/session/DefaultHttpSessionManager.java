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

package de.cosmocode.palava.bridge.session;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.cosmocode.palava.bridge.scope.Scopes;
import de.cosmocode.palava.core.lifecycle.Disposable;

/**
 * Manages all sessions.
 * 
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 */
final class DefaultHttpSessionManager implements HttpSessionManager, Disposable {
    
    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpSessionManager.class);
    
    private final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

    @Override
    public HttpSession get(String sessionId) {
        synchronized (sessions) {
            return sessions.get(sessionId);
        }
    }

    @Override
    public void destroyAll() {
        destroy(0, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void destroy(long period, TimeUnit periodUnit) {
        final long then = System.currentTimeMillis() - periodUnit.toMillis(period);
        final Date date = new Date(then);

        final Predicate<HttpSession> predicate = new Predicate<HttpSession>() {
            
            @Override
            public boolean apply(HttpSession input) {
                return input.getAccessTime().before(date);
            }
            
        };
        
        synchronized (sessions) {
            final Collection<HttpSession> oldSessions = Collections2.filter(sessions.values(), predicate);
            LOG.info("Destroying {} sessions", oldSessions.size());
            for (HttpSession session : oldSessions) {
                session.clear();
            }
            sessions.values().removeAll(oldSessions);
        }
    }

    @Override
    public HttpSession get() {
        final HttpSession cached = Scopes.getCurrentSession();
        if (cached == null) {
            final String sessionId = UUID.randomUUID().toString();
            LOG.debug("Creating new session with id {}", sessionId);
            final HttpSession session = new DefaultHttpSession(sessionId);
            
            synchronized (sessions) {
                sessions.put(sessionId, session);
            }
            
            LOG.debug("{} sessions currently in progess", sessions.size());
            return session;
        } else {
            LOG.debug("Found old session");
            return cached;
        }
    }
    
    @Override
    public void dispose() {
        destroyAll();
    }
    
}
