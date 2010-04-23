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

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.CommandException;
import de.cosmocode.palava.bridge.content.ConversionException;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * get a session data entry
 * @author Detlef Hüttemann
 */
@Singleton
public class get implements Command {
    
    @Override
    public Content execute(Call call) throws CommandException {
        final Arguments arguments = call.getArguments();
        arguments.require("key");
        
        final Object key = arguments.get("key");
        final HttpSession session = call.getHttpRequest().getHttpSession();
        Preconditions.checkNotNull(session, "Session");
        
        final Object value = session.get(key);
        
        try {
            return new PhpContent(value);
        } catch (ConversionException e) {
            throw new CommandException(e);
        }
    }

}
