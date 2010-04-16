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
