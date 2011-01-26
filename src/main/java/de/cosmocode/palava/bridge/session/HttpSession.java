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

import java.text.Collator;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import de.cosmocode.palava.ipc.IpcSession;
import de.cosmocode.rendering.Renderable;
import de.cosmocode.rendering.Renderer;
import de.cosmocode.rendering.RenderingException;
import de.cosmocode.rendering.RenderingLevel;

/**
 * A {@link HttpSession} represents an browser session,
 * lasting for a specific period of time.
 *
 * @deprecated use {@link IpcSession}
 * @author Willi Schoenborn
 */
@Deprecated
public interface HttpSession extends IpcSession, Renderable {
    
    String LANGUAGE = "lang";
    
    String COUNTRY = "country";

    /**
     * Provide the session id of this session.
     * 
     * @return the session id
     */
    @Override
    String getSessionId();
    
    /**
     * Provides the date of the last access.
     * 
     * @return the last access date
     */
    Date getAccessTime();
    
    /**
     * Update the access time and set it to the current
     * time stamp.
     */
    void updateAccessTime();
    
    /**
     * Provide the user's locale.
     * 
     * @return the user's locale
     * @throws IllegalStateException if there is no locale information
     *         stored in this session
     */
    Locale getLocale();
    
    /**
     * Provide a locale aware {@link NumberFormat} bound to the 
     * locale of this session as specified by {@link HttpSession#getLocale()}.
     * 
     * @return a {@link NumberFormat} instance of the given user locale
     */
    NumberFormat getNumberFormat();
    
    /**
     * Provide a locale aware {@link Collator} bound to the
     * locale of this session as specified by {@link HttpSession#getLocale()}.
     * 
     * @return a {@link Collator} instance of the given user locale
     */
    Collator getCollator();
    
    /**
     * {@inheritDoc}
     * 
     * An {@link HttpSession} must have the following JSON structure:
     * <pre>
     *  {
     *      id: <id>,
     *      accesstime: <accessTime>,
     *      data: {
     *          key: value,
     *          ...
     *      }
     *  }
     * </pre>
     */
    @Override
    void render(Renderer renderer, RenderingLevel level) throws RenderingException;
    
}
