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

import java.net.URI;
import java.net.URL;

import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.ipc.IpcConnection;

/**
 * Encapsulates http related data access and scoping issues.
 *
 * @deprecated use {@link IpcConnection}
 * @author Willi Schoenborn
 */
@Deprecated
public interface HttpRequest extends IpcConnection {

    /**
     * Provide the requested {@link URI}.
     * 
     * @return the requested uri
     * @throws IllegalStateException if there is no request uri information
     *         available in this request
     * @throws IllegalArgumentException if the request uri is no valid {@link URI}
     */
    URI getRequestUri();
    
    /**
     * Provide the requested referer.
     * 
     * @return the referer of this request or null if there
     *         was no information about the referer available
     * @throws IllegalArgumentException if the referer is no valid {@link URL}
     */
    URL getReferer();
    
    /**
     * Provide the remote address of the user.
     * 
     * @return the remote address
     * @throws IllegalStateException if there is no remote address
     *         information available in this request
     */
    String getRemoteAddress();
    
    /**
     * Provide the user agent.
     * 
     * @return the user agent of this request or null if there
     *         was no information about the user agent available
     */
    String getUserAgent();
    
    /**
     * Provide the associated session.
     * 
     * @return this request's session
     */
    HttpSession getHttpSession();
    
    /**
     * Sets an attribute in this request.
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param key the key
     * @param value the value
     */
    <K, V> void set(K key, V value);
    
    /**
     * Checks for the presence of a binding to the specified
     * key.
     * 
     * @param <K> the key type
     * @param key the key
     * @return true of this request contains a binding to the specified
     *         key
     */
    <K> boolean contains(K key);
    
    /**
     * Retrieves the value bound the specified key. 
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param key the key
     * @return the value associated to the specified key or null if
     *         there is no such binding
     */
    <K, V> V get(K key);
    
}
