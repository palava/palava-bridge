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

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.inject.Inject;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.JsonCall;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.cache.CacheService;

/**
 * Abstract base class for caching concerns.
 * 
 * @deprecated use Cache annotation
 *
 * @author Willi Schoenborn
 */
@Deprecated
public abstract class CachableJob extends UtilityJobImpl {
    
    private static final Logger LOG = LoggerFactory.getLogger(CachableJob.class);
    
    @Inject
    private CacheService service;
    
    /**
     * Process the job as it normally would be.
     * ATTENTION: parameters server and session are flipped in the signature
     * 
     * @since 1.0
     * @param request the incoming request
     * @param response the response being sent to the client
     * @param server the current server
     * @param session the current session
     * @param caddy the connection
     * @throws Exception if anything went wrong
     */
    public abstract void process(Call request, Response response, Server server, HttpSession session, 
        Map<String, Object> caddy) throws Exception;
    
    @Override
    public final void process(Call call, Response response, HttpSession session, Server server,
        Map<String, Object> caddy) throws Exception {

        if (isCachingEnabled()) {
            final Arguments arguments = supportsArguments(call) ? call.getArguments() : null;
            final String language = session.get("lang");
            
            final Builder<Object> builder = ImmutableSet.builder();
            
            builder.add(getClass());
            
            if (arguments != null && arguments.size() > 0) {
                builder.add(arguments);
            }
            
            if (StringUtils.isNotBlank(language)) {
                builder.add(language);
            }
            
            final Serializable key = builder.build();
            final Content cached = service.read(key);
            
            if (cached == null) {
                process(call, response, server, session, caddy);
                assert response.hasContent() : "Expected content to be set";
                final Content content = response.getContent();
                LOG.debug("Storing {} into cache", content);
                service.store(key, content);
            } else {
                LOG.debug("Retrieved content {} from cache", cached);
                response.setContent(cached);
            }
        } else {
            process(call, response, server, session, caddy);
        }
    }
    
    protected boolean isCachingEnabled() {
        return true;
    }
    
    private boolean supportsArguments(Call call) {
        return call instanceof DataCall || call instanceof JsonCall;
    }
    
    @Override
    public String getMandatory(String key) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getMandatory(String key, String argumentType) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getOptional(String key) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getOptional(String key, String defaultValue) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean hasArgument(String key) {
        throw new UnsupportedOperationException();
    }
    
}
