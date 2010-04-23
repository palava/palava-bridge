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

package de.cosmocode.palava.services.media;

import java.io.File;

import org.hibernate.Session;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.palava.core.lifecycle.Initializable;
import de.cosmocode.palava.services.store.ContentStore;

/**
 * 
 *
 * @author Willi Schoenborn
 */
public final class DefaultImageStore implements ImageStore, Initializable {

    private final ContentStore store;
    
    private final File filterDirectory;
    
    @Inject
    public DefaultImageStore(
        @ImageContentStore ContentStore store,
        @Named("imagestore.filterDirectory") File filterDirectory) {
        this.store = Preconditions.checkNotNull(store, "Store");
        this.filterDirectory = Preconditions.checkNotNull(filterDirectory);
    }

    @Override
    public File getFile(String storeKey, String filterName) {
        return new File(filterDirectory, filterName + "/" + storeKey);
    }
    
    @Override
    public ImageManager createImageManager(Session session) {
        return new ImageManager(this, store, session);
    }
    
    @Override
    public void initialize() {
        filterDirectory.mkdirs();
    }


}
