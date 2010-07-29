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

package de.cosmocode.palava.bridge.scope;

import com.google.inject.Provider;

import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.request.HttpRequest;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.ipc.IpcCall;
import de.cosmocode.palava.ipc.IpcConnection;
import de.cosmocode.palava.ipc.IpcSession;

/**
 * Keeps track of thread bound {@link Call}s, {@link HttpRequest}s and {@link HttpSession}s.
 *
 * @deprecated inject {@link Provider} for {@link IpcCall}, {@link IpcConnection} or {@link IpcSession}
 * @author Willi Schoenborn
 */
@Deprecated
public final class Scopes {
    
    private static final ThreadLocal<Call> CALL = new ThreadLocal<Call>();

    private Scopes() {
        
    }
    
    /**
     * Sets the current call.
     * <strong>USE WITH CAUTION</strong>
     * 
     * @param call the new current call
     */
    public static void setCurrentCall(Call call) {
        CALL.set(call);
    }
    
    /**
     * Cleans up the scopes.
     */
    public static void clean() {
        CALL.remove();
    }
    
    /**
     * Provides access to the current call.
     * 
     * @return the current call.
     */
    public static Call getCurrentCall() {
        return CALL.get();
    }
    
    /**
     * Provides access to the current request.
     * 
     * @return the current request
     */
    public static HttpRequest getCurrentRequest() {
        final Call call = getCurrentCall();
        if (call == null) return null;
        return call.getHttpRequest();
    }
    
    /**
     * Provides access to the current session.
     * 
     * @return the current session
     */
    public static HttpSession getCurrentSession() {
        final HttpRequest request = getCurrentRequest();
        if (request == null) return null;
        return request.getHttpSession();
    }
    
}
