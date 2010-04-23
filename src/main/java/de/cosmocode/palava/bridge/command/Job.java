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

package de.cosmocode.palava.bridge.command;

import java.util.Map;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.session.HttpSession;


/**
 * Legacy {@linkplain Command command}-style interface.
 * 
 * @deprecated <strong>Use {@link Command}!</strong>
 * 
 * @author Tobias Sarnowski
 * @author Oliver Lorenz 
 * @author Willi Schoenborn
 */
@Deprecated
public interface Job {

    /**
     * Executes this job.
     * 
     * @deprecated <strong>Use {@link Command}!</strong>
     * 
     * @param call the request of this job. contains the invoking args
     * @param response the container for the results 
     * @param session a session (may be null). available across different frontent/http requests
     * @param server the server structure - basically for the components lookup
     * @param caddy a container available across all jobs of the <strong>same</strong> frontend/http request
     * @throws Exception if execution failed
     */
    @Deprecated
    void process(Call call, Response response, HttpSession session, Server server, 
        Map<String, Object> caddy) throws Exception;

}
