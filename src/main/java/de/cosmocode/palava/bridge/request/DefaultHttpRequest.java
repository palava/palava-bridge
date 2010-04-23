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

package de.cosmocode.palava.bridge.request;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.ipc.IpcSession;
import de.cosmocode.palava.scope.AbstractScopeContext;

/**
 * Default implementation of the {@link HttpRequest}.
 *
 * @author Willi Schoenborn
 */
final class DefaultHttpRequest extends AbstractScopeContext implements HttpRequest {
    
    private static final String REQUEST_URI = "REQUEST_URI";
    private static final String HTTP_REFERER = "HTTP_REFERER";
    private static final String REMOTE_ADDR = "REMOTE_ADDR";
    private static final String HTTP_USER_AGENT = "HTTP_USER_AGENT";

    private final HttpSession httpSession;
    
    private final Map<Object, Object> context = Maps.newHashMap();
    
    private final Map<String, String> serverVariable = Maps.newHashMap();
    
    public DefaultHttpRequest(HttpSession httpSession, Map<String, String> serverVariable) {
        this.httpSession = httpSession;
        Preconditions.checkNotNull(serverVariable, "ServerVariable");
        this.serverVariable.putAll(serverVariable);
    }

    @Override
    public URI getRequestUri() {
        final String uri = serverVariable.get(REQUEST_URI);
        Preconditions.checkState(StringUtils.isNotBlank(uri), "%s not found", REQUEST_URI);
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    @Override
    public URL getReferer() {
        final String referer = serverVariable.get(HTTP_REFERER);
        if (StringUtils.isBlank(referer)) {
            return null;
        } else {
            try {
                return new URL(referer);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
    
    @Override
    public String getRemoteAddress() {
        final String address = serverVariable.get(REMOTE_ADDR);
        Preconditions.checkState(StringUtils.isNotBlank(address), "%s not found", REMOTE_ADDR);
        return address;
    }

    @Override
    public String getUserAgent() {
        return serverVariable.get(HTTP_USER_AGENT);
    }

    @Override
    public HttpSession getHttpSession() {
        return httpSession;
    }
    
    @Override
    public IpcSession getSession() {
        return getHttpSession();
    }

    @Override
    protected Map<Object, Object> context() {
        return context;
    }
    
}
