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

import com.google.inject.Inject;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.jpa.hibernate.HibernateJob;
import de.cosmocode.palava.services.media.ImageManager;
import de.cosmocode.palava.services.media.ImageStore;

/**
 * @deprecated legacy support
 *
 * @author Willi Schoenborn
 */
@Deprecated
public class addDirectory extends HibernateJob {

    @Inject
    private ImageStore imageStore;
    
    @Override
    public void process(Call call, Response resp, HttpSession session,
            Server server, Map<String, Object> caddy,
            org.hibernate.Session hibSession) throws Exception {
        
        final Arguments map = call.getArguments();
        map.require("id", "assetId");

        final String dirId = map.getString("id");
        final String name = map.getString("name", null);
        final String assetId = map.getString("assetId");

        final ImageManager manager = imageStore.createImageManager(hibSession);

        final Long resultId = manager.addAssetToDirectory(
            dirId == null ? null : Long.parseLong(dirId), name, Long.parseLong(assetId)
        ).getId();

        resp.setContent(new PhpContent(resultId));
    }

}
