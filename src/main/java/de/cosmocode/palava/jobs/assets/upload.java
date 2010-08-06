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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.MimeType;
import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.content.StreamContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.jpa.hibernate.HibernateJob;
import de.cosmocode.palava.services.media.Asset;
import de.cosmocode.palava.services.media.ImageManager;
import de.cosmocode.palava.services.media.ImageStore;

@Singleton
public class upload extends HibernateJob {

    @Inject
    private ImageStore imageStore;
    
    @Override
    public void process(Call call, Response response, HttpSession session,
        Server server, Map<String, Object> caddy, org.hibernate.Session hibSession) throws Exception {
        
        final Asset asset = call.getHttpRequest().get("asset");
        Preconditions.checkNotNull(asset, "Asset");
        final MimeType mimetype = call.getHttpRequest().get("mimetype");

        // just use the request data as the content
        asset.setContent(new StreamContent(
            call.getInputStream(), call.getHeader().getContentLength(), mimetype
        ));

        final ImageManager im = imageStore.createImageManager(hibSession);

        im.createAsset(asset);

        response.setContent(new PhpContent(asset.getId()));
    }

}
