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

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.jpa.hibernate.HibernateJob;
import de.cosmocode.palava.services.media.ImageManager;
import de.cosmocode.palava.services.media.ImageStore;

@Singleton
public class removeFromDirectory extends HibernateJob {

    @Override
    public void process(Call req, Response resp, HttpSession session,
            Server server, Map<String, Object> caddy,
            org.hibernate.Session hibSession) throws Exception {
        
        ImageStore ist = server.getServiceManager().lookup(ImageStore.class);

        DataCall request = (DataCall) req;
        final Map<String, String> map = request.getStringedArguments();

        String dirId = map.get("dirId");
        if ( dirId == null ) throw new MissingArgumentException(this, "dirId");

        String assetId = map.get("assetId");
        if ( assetId == null ) throw new MissingArgumentException(this,"assetId");

        ImageManager im = ist.createImageManager(hibSession);

        Boolean done = im.removeAssetFromDirectory(Long.parseLong(dirId),Long.parseLong(assetId));

        resp.setContent( new PhpContent(done) );
    }

    

}
