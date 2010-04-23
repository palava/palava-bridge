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

package de.cosmocode.palava.services.store;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.MimeType;
import de.cosmocode.palava.bridge.content.FileContent;
import de.cosmocode.palava.bridge.content.StreamContent;

/**
 * File system base {@link ContentStore} implementation.
 *
 * @author Willi Schoenborn
 */
public final class FSContentStore implements ContentStore {
    
    private static final Logger LOG = LoggerFactory.getLogger(FSContentStore.class);
    
    private final File root;
    
    @Inject
    public FSContentStore(@Named("contentstore.root") File root) {
        this.root = Preconditions.checkNotNull(root, "Root");
        LOG.info("Configured file system content store: {}", root);
    }

    private String generateFilename(MimeType mimeType) {
        return DigestUtils.md5Hex(Long.toString(System.currentTimeMillis())) + "." + mimeType.getMinor();
    }

    private File createFile(String name) {
        return new File(root, name);
    }

    @Override
    public StreamContent load(String key) throws Exception {
        final File file = createFile(key);

        if (!file.exists()) return null;
        return new FileContent(file);
    }
    
    @Override
    public String store(Content content) throws Exception {
        
        final String name = generateFilename(content.getMimeType());
        final File file = createFile(name);
        
        final FileOutputStream out = new FileOutputStream(file);
        content.write(out);
        out.flush();
        out.close();
        return name;
    }

    @Override
    public void remove(String key) {
        final File file = createFile(key);
        if (file.delete()) {
            LOG.info("Deleted file {}", file.getAbsoluteFile());
        } else {
            LOG.error("Failed to delete {}", file.getAbsolutePath());
        }
    }

}
