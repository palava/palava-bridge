/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.jobs.captcha;

import java.io.ByteArrayInputStream;
import java.util.Map;

import com.google.inject.Inject;

import de.cosmocode.palava.bridge.MimeType;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.StreamContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.captcha.CaptchaService;
import de.cosmocode.palava.captcha.Create;

/**
 * Creates a captcha image and returns the binary image data
 * to the browser.
 * 
 * @deprecated use {@link Create} instead
 *
 * @author Willi Schoenborn
 */
@Deprecated
public class createCaptcha implements Job {

    @Inject
    private CaptchaService captcha;
    
    @Override
    public void process(Call request, Response response, HttpSession session,
            Server server, Map<String, Object> caddy) throws  Exception {

        final byte [] bytes = captcha.getCaptcha(session.getSessionId());
        
        final ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        final StreamContent content = new StreamContent(input, bytes.length, MimeType.JPEG);
        
        response.setContent(content);
    }

}
