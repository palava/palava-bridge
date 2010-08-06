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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Singleton;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.MissingArgumentException;
import de.cosmocode.palava.bridge.command.Response;
import de.cosmocode.palava.bridge.content.PhpContent;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.jpa.hibernate.HibernateJob;
import de.cosmocode.palava.legacy.UpdateResult;
import de.cosmocode.palava.services.media.Asset;
import de.cosmocode.palava.services.media.ImageManager;
import de.cosmocode.palava.services.media.ImageStore;

@Singleton
public class editAsset extends HibernateJob {

    @SuppressWarnings("unchecked")
    @Override
    public void process(Call req, Response resp, HttpSession session,
            Server server, Map<String, Object> caddy,
            org.hibernate.Session hibSession) throws Exception {
        
        ImageStore as = server.getServiceManager().lookup(ImageStore.class);

        DataCall request = (DataCall) req;
        Map<String, String> map = request.getStringedArguments();

        String assetId = map.get("assetId");
        if ( assetId == null ) throw new MissingArgumentException(this,"assetId");
        map.remove("assetId");

        String title = map.get("title");
        if ( title == null ) throw new MissingArgumentException(this,"title");
        map.remove("title");

        String description = map.get("description");
        if ( description == null ) throw new MissingArgumentException(this,"description");
        map.remove("description");
        
        String exDate = map.get("expirationDate"); 
        map.remove("expirationDate");

        ImageManager am = as.createImageManager(hibSession);

        UpdateResult ur = new UpdateResult();
        try {
            Asset asset = am.getAsset( Long.parseLong(assetId), false ) ;

            if ( asset == null ) throw new NullPointerException("asset") ;

            asset.setTitle( title ) ;
            asset.setDescription( description );
                   
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            Date date = null;
            
            try {
                format.setLenient(false);
                if (!exDate.equals("")) date = format.parse(exDate);
            } catch (Exception e) {
                ur.addError(UpdateResult.ERR_FORMAT, "expirationDate");
            }
        
            asset.setExpirationDate(date);
            asset.setExpiresNever(map.containsKey("expiresNever") && map.get("expiresNever").equals("true"));
            map.remove("expiresNever");

            asset.getMetaData().putAll(map);

            if (ur.isError()) {
                ur.setResult(asset);
                resp.setContent(new PhpContent(ur));
                return;
            }
            
            am.updateAsset(asset);
            ur.setResult(asset);
        } catch ( Exception e ) {
            ur.setException(e);
        }
        resp.setContent(new PhpContent(ur));
    }
}