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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.JsonContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.rendering.Renderer;

/**
 * Parses a date.
 * 
 * @deprecated with replacement
 * @author Willi Schoenborn
 */
@Deprecated
@Singleton
/* CHECKSTYLE:OFF */
public class parseDate implements Job {
/* CHECKSTYLE:ON */

    private static final Logger LOG = LoggerFactory.getLogger(parseDate.class); 
    
    @Override
    public void process(Call call, Response response, HttpSession session, Server server, Map<String, Object> caddy) {
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
            LOG.info("Parsing date " + source + " with pattern " + pattern + " failed.");
        }
        
        out.map();
        out.key("date").value(returnValue);
        out.endMap();
        
        response.setContent(new JsonContent(out));
    }

}
