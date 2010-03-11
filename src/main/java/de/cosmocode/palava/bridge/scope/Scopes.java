/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.bridge.scope;

import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.request.HttpRequest;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * Keeps track of thread bound {@link Call}s, {@link HttpRequest}s and {@link HttpSession}s.
 *
 * @author Willi Schoenborn
 */
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
