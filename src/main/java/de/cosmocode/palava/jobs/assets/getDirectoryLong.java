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

import java.util.List;
import java.util.Map;

import org.json.extension.JSONConstructor;

import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.JsonContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.jpa.hibernate.HibernateJob;
import de.cosmocode.palava.services.media.Asset;
import de.cosmocode.palava.services.media.Directory;
import de.cosmocode.palava.services.media.ImageManager;
import de.cosmocode.palava.services.media.ImageStore;

public class getDirectoryLong extends HibernateJob {

    @Override
    public void process(Call req, Response response, HttpSession session, Server server, 
            Map<String, Object> caddy, org.hibernate.Session hibSession) throws Exception {
        
        ImageStore store = server.getServiceManager().lookup(ImageStore.class);

        DataCall request = (DataCall) req;
        final Map<String, String> map = request.getStringedArguments();

        String dirID = map.get("id");
        if(dirID == null) throw new MissingArgumentException(this, "id");

        ImageManager manager = store.createImageManager(hibSession);

        Directory directory = manager.getDirectory(Long.parseLong(dirID));

        directory.sort(Asset.ByCreationDateComparator.INSTANCE);
        
        final JSONConstructor json = JSON.newConstructor();

        List<Asset> assets = directory.getAssets();
        
        json.array();
        for(Asset asset : assets) {            
            json.
                object();
                    asset.encodeJSON(json);           
                    json.key("directories").
                    value(manager.getDirectoryIdsForAsset(asset.getId())).
                endObject();            
        }
        json.endArray();
        
        response.setContent(new JsonContent(json));
    }
}