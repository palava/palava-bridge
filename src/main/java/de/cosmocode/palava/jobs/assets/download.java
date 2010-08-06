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

import java.util.Map;

import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.jpa.hibernate.HibernateJob;
import de.cosmocode.palava.services.media.Asset;
import de.cosmocode.palava.services.media.ImageManager;
import de.cosmocode.palava.services.media.ImageStore;

@Singleton
public class download extends HibernateJob {

    @Override
    public void process(Call request, Response response, HttpSession session,
            Server server, Map<String, Object> caddy, org.hibernate.Session hibSession)
            throws ConnectionLostException, Exception {
        
        ImageStore ist = server.getServiceManager().lookup(ImageStore.class);

        DataCall req = (DataCall) request;
        final Map<String, String> map = req.getStringedArguments();

        String filterName = map.get("filter");
        Long id = null;
        try {
            id = Long.parseLong(map.get("id"));
        } catch ( Exception e ) {
        }
        if ( id == null ) throw new MissingArgumentException(this,"id");

        ImageManager im = ist.createImageManager(hibSession);

        Asset asset = null;

        asset = im.getImage(id,filterName);

        if ( asset != null )
            response.setContent( asset.getContent());
        else
            response.setContent( PhpContent.NOT_FOUND );
        


    }

}
