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

package de.cosmocode.palava.jobs.system;

import java.io.StringWriter;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.TextCall;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.TextContent;
import de.cosmocode.palava.bridge.session.HttpSession;

/**
 * Offers a javascript enviroment in the server.
 * 
 * @author Tobias Sarnowski
 */
@Singleton
public class console implements Job {
    
    private static final Logger LOG = LoggerFactory.getLogger(console.class);

    public void process(Call request, Response response, HttpSession session, Server server, Map<String,Object> caddy) throws ConnectionLostException, Exception
    {
        // get the code
        String jscode = ((TextCall)request).getText();

        // initialize our world
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();

        // give him all our important objects
        StringWriter sout = new StringWriter();
        Object jsOut = Context.javaToJS(sout, scope);
        ScriptableObject.putProperty(scope, "out", jsOut);

        Object jsRequest = Context.javaToJS(request, scope);
        ScriptableObject.putProperty(scope, "request", jsRequest);

        Object jsResponse = Context.javaToJS(response, scope);
        ScriptableObject.putProperty(scope, "response", jsResponse);

        Object jsSession = Context.javaToJS(session, scope);
        ScriptableObject.putProperty(scope, "session", jsSession);

        Object jsServer = Context.javaToJS(server, scope);
        ScriptableObject.putProperty(scope, "server", jsServer);

        // prepare the code
        Script script = context.compileString(jscode, "console", 1, null);

        // execute it!
        // FIXME use a real ScriptReturnObject to transmit the results
        //       this is also ugly, let php or the binclient do the formating
        try {
            final Object returned = script.exec(context, scope);
            if (returned != null && returned.toString().length() > 0 && ! (returned instanceof Undefined))
                sout.write("\n%%GREEN+%%" + returned.toString() + "%%+GREEN%%");
            sout.write("\n%%GREY+%%Script successful executed.%%+GREY%%");
        } catch (Exception e) {
            LOG.error("Scripting error", e);
            String msg = e.getMessage();
            if (msg == null)
                msg = e.toString();
            sout.write("\n%%RED+%%" + htmlspecialchars(msg) + "%%+RED%%");
        } finally {
            context.exit();
        }

        // send the output
        if (!response.hasContent())
            response.setContent(new TextContent(sout.toString()));
    }

    private String htmlspecialchars(String text)
    {
        text = str_replace(text, "<", "&lt;");
        text = str_replace(text, ">", "&gt;");

        return text;
    }

    private String str_replace(String text, String search, String replace)
    {
        int pos = text.indexOf(search);
        while (pos >= 0) {
            String start = text.substring(0, pos);
            String end = text.substring(pos + search.length());
            text = start + replace + end;
            pos = text.indexOf(search);
        }

        return text;
    }
}
