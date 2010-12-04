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

package de.cosmocode.palava.jobs.session;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.ConversionException;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * Get a session data entry.
 * 
 * @deprecated use {@link Get}
 * @author Detlef Hüttemann
 */
@Deprecated
@Singleton
/* CHECKSTYLE:OFF */
public class get implements Job {
/* CHECKSTYLE:ON */
    
    @Override
    public void process(Call call, Response response, HttpSession session, Server server, Map<String, Object> caddy) 
        throws ConversionException {
        final Arguments arguments = call.getArguments();
        arguments.require("key");
        
        final Object key = arguments.get("key");
        Preconditions.checkNotNull(session, "Session");
        
        final Object value = session.get(key);
        
        response.setContent(new PhpContent(value));
    }

}
