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

package de.cosmocode.palava.jobs.assets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.cosmocode.palava.bridge.MimeType;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.services.media.Asset;

public class upload0 implements Job {

    @Override
    public void process(Call call, Response response, HttpSession session,
            Server server, Map<String, Object> caddy) throws Exception {
        
        final Arguments map = call.getArguments();
        
        map.require("mimetype");
        
        final String mimetype = map.getString("mimetype");

        final Asset asset = new Asset();
        asset.setName(map.getString("name"));
        map.remove("name");
        asset.setDescription(map.getString("description"));
        map.remove("description");
        asset.setTitle(map.getString("title"));
        map.remove("title");
        asset.setExpiresNever(map.containsKey("expires"));
        map.remove("expires");
        
        final String exDate = map.getString("expirationDate", null);
        map.remove("expirationDate");
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date date = null;
        
        if (StringUtils.isNotBlank(exDate)) {
            try {
                date = format.parse(exDate);
            } catch (ParseException e) {
                //TODO handle format exception
            }
        }
        
        asset.setExpirationDate(date);
        asset.fillMetaData(map);
                
        call.getHttpRequest().set("asset", asset);
        call.getHttpRequest().set("mimetype", new MimeType(mimetype));

        response.setContent(PhpContent.OK);
    }

}
