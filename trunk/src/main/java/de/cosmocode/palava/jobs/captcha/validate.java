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

package de.cosmocode.palava.jobs.captcha;

import com.google.inject.Inject;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.CommandException;
import de.cosmocode.palava.bridge.content.TextContent;
import de.cosmocode.palava.captcha.CaptchaService;
import de.cosmocode.palava.captcha.Validate;

/**
 * Validates the current captcha against the user input.
 *
 * @deprecated use {@link Validate} instead
 *
 * @author Willi Schoenborn
 */
@Deprecated
public class validate implements Command {

    @Inject
    private CaptchaService captcha;
    
    @Override
    public Content execute(Call call) throws CommandException {
        final String userInput = call.getArguments().getString("code");
        final boolean result = captcha.validate(call.getHttpRequest().getHttpSession().getSessionId(), userInput);
        
        return new TextContent(Boolean.toString(result));
    }

}
