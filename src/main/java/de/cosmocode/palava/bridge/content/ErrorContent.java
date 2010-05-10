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

package de.cosmocode.palava.bridge.content;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.google.common.base.Preconditions;

import de.cosmocode.palava.bridge.MimeType;


/**
 * An error occured, this ships it to the browser.
 * 
 * @author Tobias Sarnowski
 * @author Willi Schoenborn
 */
public class ErrorContent extends AbstractContent {
    
    private final byte [] bytes;

    private ErrorContent(Throwable e) {
        super(MimeType.ERROR);
        
        // FIXME php is good enough to create the html itself, it's ugly here
        final StringBuilder builder = new StringBuilder();
        builder.append("<div style='background-color: #ff0000 !important; margin: 20px; font-family: Lucida Grande, " +
            "Verdana, Sans-serif; font-size: 12px; color: #ffffff !important; border-color: #000000 !important; " +
            "border-width: 2px; border-style: solid; padding: 10px; text-align: left'>");
        builder.append("<span style='font-weight: bold; font-size: 16px; background-color: #ff0000 !important'>" +
            "Palava Error:  ");
        builder.append(htmlspecialchars(e.toString()));
        builder.append("</span>");
        builder.append("<pre style='background-color: #ff0000 !important'>");
        final StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        builder.append(htmlspecialchars(writer.toString()));
        builder.append("</pre>");
        builder.append("</div>");

        bytes = builder.toString().getBytes(CHARSET);
    }

    private String htmlspecialchars(String text) {
        return text.replace("<", "&lt;").replace(">", "&gt;");
    }
    
    @Override
    public long getLength() {
        return bytes.length;
    }
    
    @Override
    public void write(OutputStream out) throws IOException {
        out.write(bytes, 0, bytes.length);
    }
    
    public static ErrorContent create(Throwable exception) {
        Preconditions.checkNotNull(exception, "Exception");
        return new ErrorContent(exception);
    }

}


