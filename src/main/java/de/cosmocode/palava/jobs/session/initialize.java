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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.TextContent;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * Checks whether a session exists and creates new if necessary.
 * 
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 */
@Singleton
public final class initialize implements Job {

    private final Provider<HttpSession> provider;
    
    @Inject
    public initialize(Provider<HttpSession> provider) {
        this.provider = provider;
    }

    @Override
    public void process(Call call, Response response, HttpSession session, Server server, Map<String, Object> caddy) {
        response.setContent(new TextContent(provider.get().getSessionId()));
    }
    
}
