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
@SuppressWarnings("deprecation")
/* CHECKSTYLE:OFF */
public class console implements Job {
/* CHECKSTYLE:ON */
    
    private static final Logger LOG = LoggerFactory.getLogger(console.class);

    @Override
    public void process(Call call, Response response, HttpSession session, Server server, Map<String, Object> caddy) {
        // get the code
        final String jscode = ((TextCall) call).getText();

        // initialize our world
        final Context context = Context.enter();
        final Scriptable scope = context.initStandardObjects();

        // give him all our important objects
        final StringWriter sout = new StringWriter();
        final Object jsOut = Context.javaToJS(sout, scope);
        ScriptableObject.putProperty(scope, "out", jsOut);

        final Object jsRequest = Context.javaToJS(call, scope);
        ScriptableObject.putProperty(scope, "request", jsRequest);

        final Object jsResponse = Context.javaToJS(response, scope);
        ScriptableObject.putProperty(scope, "response", jsResponse);

        final Object jsSession = Context.javaToJS(session, scope);
        ScriptableObject.putProperty(scope, "session", jsSession);

        final Object jsServer = Context.javaToJS(server, scope);
        ScriptableObject.putProperty(scope, "server", jsServer);

        // prepare the code
        final Script script = context.compileString(jscode, "console", 1, null);

        // execute it!
        try {
            final Object returned = script.exec(context, scope);
            if (returned != null && returned.toString().length() > 0 && ! (returned instanceof Undefined)) {
                sout.write("\n%%GREEN+%%" + returned.toString() + "%%+GREEN%%");
            }
            sout.write("\n%%GREY+%%Script successful executed.%%+GREY%%");
        /* CHECKSTYLE:OFF */
        } catch (Exception e) {
        /* CHECKSTYLE:ON */
            LOG.error("Scripting error", e);
            String msg = e.getMessage();
            if (msg == null)
                msg = e.toString();
            sout.write("\n%%RED+%%" + htmlspecialchars(msg) + "%%+RED%%");
        } finally {
            Context.exit();
        }

        // send the output
        if (!response.hasContent()) {
            response.setContent(new TextContent(sout.toString()));
        }
    }

    private String htmlspecialchars(String text) {
        return text.replace("<", "&lt;").replace(">", "&gt;");
    }

}
