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

package de.cosmocode.palava.jobs.form;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.CommandException;
import de.cosmocode.palava.bridge.content.JsonContent;
import de.cosmocode.rendering.Renderer;

@Singleton
public class parseDate implements Command {

    private static final Logger log = LoggerFactory.getLogger(parseDate.class); 
    
    @Override
    public Content execute(Call call) throws CommandException {
        final Arguments arguments = call.getArguments();
        arguments.require("pattern", "source");
        
        final String pattern = arguments.getString("pattern");
        final String source = arguments.getString("source");
        
        final DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        
        final Renderer out = JSON.newRenderer();
        
        Object returnValue = Boolean.FALSE;
        
        try {
            final Date date = dateFormat.parse(source);
            returnValue = date.getTime() / 1000;
        } catch (ParseException e) {
            log.info("Parsing date " + source + " with pattern " + pattern + " failed.");
        }
        
        out.map().
                key("date").value(returnValue).
            endMap();
        
        return new JsonContent(out);
    }

}
