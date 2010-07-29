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

import java.util.concurrent.TimeUnit;

import com.google.inject.Provider;

import de.cosmocode.palava.ipc.IpcSessionProvider;

/**
 * A manager for {@link HttpSession}s.
 *
 * @deprecated use {@link IpcSessionProvider}
 * @author Willi Schoenborn
 */
@Deprecated
public interface HttpSessionManager extends Provider<HttpSession> {

    /**
     * Destroys all sessions.
     */
    void destroyAll();
    
    /**
     * Destroys all sessions which were not
     * accessed in the specified period.
     * 
     * @param period the maximum time of inactivity
     * @param periodUnit the unit of period
     */
    void destroy(long period, TimeUnit periodUnit);
    
    /**
     * Retrieve an {@link HttpSession} for a given session id.
     * 
     * @param sessionId the session's id
     * @return the found {@link HttpSession} or null if there was no session
     *         with the given id
     */
    HttpSession get(String sessionId);
    
}
