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

import java.io.InputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.extension.JSONConstructor;
import org.json.extension.JSONEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.gag.annotation.disclaimer.LegacySucks;
import com.google.inject.internal.Objects;
import com.google.inject.internal.Sets;

import de.cosmocode.collections.callback.Callback;
import de.cosmocode.collections.callback.Callbacks;
import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.MimeType;
import de.cosmocode.palava.bridge.content.ContentConverter;
import de.cosmocode.palava.bridge.content.ConversionException;
import de.cosmocode.palava.bridge.content.Convertible;
import de.cosmocode.palava.bridge.content.KeyValueState;
import de.cosmocode.palava.bridge.content.StreamContent;
import de.cosmocode.palava.media.asset.AbstractAsset;
import de.cosmocode.palava.media.asset.AssetBase;
import de.cosmocode.palava.model.base.Copyable;
import de.cosmocode.palava.model.base.EntityBase;
import de.cosmocode.rendering.Renderer;
import de.cosmocode.rendering.RenderingException;
import de.cosmocode.rendering.RenderingLevel;

/**
 * Legacy {@link AssetBase} implementation.
 * 
 * @deprecated use {@link AssetBase} or {@link AbstractAsset} instead
 * @author Willi Schoenborn
 */
@Deprecated
@LegacySucks
@Entity
public class Asset implements AssetBase, Copyable<Asset>, JSONEncoder, Convertible {
    
    private static final Logger LOG = LoggerFactory.getLogger(Asset.class);
    
    /**
     * A {@link Comparator} which sorts by creation date.
     *
     * @deprecated use {@link EntityBase#ORDER_BY_AGE}
     */
    @Deprecated
    public static class ByCreationDateComparator implements Comparator<Asset> {
        
        public static final ByCreationDateComparator INSTANCE = new ByCreationDateComparator();
        
        @Override
        public int compare(Asset a, Asset b) {
            // null is less than 
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;
            return - a.getCreatedAt().compareTo(b.getCreatedAt());
        }

    };

    @Id
    @GeneratedValue(generator = "entity_id_gen", strategy = GenerationType.TABLE)
    private Long id;
    
    @Column(name = "storeKey")
    private String storeIdentifier;
    
    private String mime;
    
    private long length;
    
    @Transient
    private transient StreamContent content;
    
    private String name;
    
    private String title;
    
    @Lob
    private String description;
    
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "modificationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt = new Date();

    @Column(name = "expirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;
    
    /**
     * Flag to indicate whether this asset should never expire.
     */
    @Column(nullable = false)
    private boolean expiresNever;

    /**
     * JSON Object.
     * {
     *    "key" : "value",
     *    "key2" : "value2",
     *    ..
     * }
     */
    private String metaData = "{}";
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public void setCreated() {
        setCreatedAt(new Date());
    }
    
    @Override
    public Date getModifiedAt() {
        return modifiedAt;
    }
    
    @Override
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    
    @Override
    public void setModified() {
        setModifiedAt(new Date());
    }
    
    @Override
    public Date getDeletedAt() {
        return null;
    }
    
    @Override
    public void setDeletedAt(Date deletedAt) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setDeleted() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getStoreIdentifier() {
        return storeIdentifier;
    }
    
    @Override
    public void setStoreIdentifier(String storeIdentifier) {
        this.storeIdentifier = storeIdentifier;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * This implementation is not designed to be threadsafe. Concurrent use of {@link #getMetaData()}
     * will almost always return in data corruption.
     * 
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public Map<String, String> getMetaData() {
        if (StringUtils.isBlank(metaData)) {
            this.metaData = "{}";
        }
        
        final JSONObject object;
        
        try {
            object = new JSONObject(metaData);
        } catch (JSONException e) {
            LOG.warn("Old value of metadata was no valid json: '{}'", metaData);
            this.metaData = "{}";
            return getMetaData();
        }
        
        // copies content of metaData to map
        final Map<String, String> map = Maps.newHashMap(Maps.transformValues(
            JSON.asMap(object), Functions.toStringFunction()));
        
        return Callbacks.compose(map, new Callback() {
            
            @Override
            public void poke() {
                Asset.this.metaData = new JSONObject(map).toString();
            }
            
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Directory> getDirectories() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Date getExpiresAt() {
        return expiresAt;
    }
    
    @Override
    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    @Override
    public boolean isExpirable() {
        return expiresAt != null;
    }
    
    @Override
    public boolean isExpired() {
        return expiresAt != null && expiresAt.getTime() < System.currentTimeMillis();
    }
    
    @Override
    public void setExpired(boolean expired) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isExpiring() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isUnexpiring() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Provides the creation date of this asset.
     * 
     * @deprecated use {@link #getCreatedAt()}
     * @return the creation date
     */
    @Deprecated
    public Date getCreationDate() {
        return getCreatedAt();
    }

    /**
     * Changes the creation date of this asset.
     * 
     * @deprecated use {@link #setCreatedAt(Date)}
     * @param creationDate the new creation date
     */
    @Deprecated
    public void setCreationDate(Date creationDate) {
        setCreatedAt(creationDate);
    }

    /**
     * Provides the modification date of this asset.
     * 
     * @deprecated use {@link #getModifiedAt()}
     * @return the modification date
     */
    @Deprecated
    public Date getModificationDate() {
        return getModifiedAt();
    }

    /**
     * Changes the modification date of this asset.
     * 
     * @deprecated use {@link #setModificationDate(Date)}
     * @param modificationDate the new modification date
     */
    @Deprecated
    public void setModificationDate(Date modificationDate) {
        setModifiedAt(modificationDate);
    }

    /**
     * Returns the associated content.
     * 
     * @deprecated don't rely on {@link Content}, use {@link #getStream()}
     * @return the content
     */
    @Deprecated
    public StreamContent getContent() {
        return content;
    }
    
    /**
     * Sets the content of this asset.
     * 
     * @deprecated use {@link #setStream(InputStream)}
     * @param content the new content
     */
    @Deprecated
    public void setContent(StreamContent content) {
        this.content = content;
        
        this.mime = content.getMimeType() == null ? MimeType.IMAGE.toString() : content.getMimeType().toString();
        this.length = content.getLength();
    }
    
    @Override
    public InputStream getStream() {
        return content.getInputStream();
    }
    
    @Override
    public void setStream(InputStream stream) {
        throw new UnsupportedOperationException();        
    }
    
    @Override
    public boolean hasStream() {
        return content != null && content.getInputStream() != null;
    }

    /**
     * Sets the id of this asset.

     * @deprecated the id should not be changed
     * @param id the new id
     */
    @Deprecated
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the store key.
     * 
     * @deprecated use {@link Asset#getStoreIdentifier()} instead
     * @return the current store key
     */
    @Deprecated
    public String getStoreKey() {
        return getStoreIdentifier();
    }

    /**
     * Sets the store key.
     * 
     * @deprecated use {@link Asset#setStoreIdentifier(String)} instead.
     * @param storeKey the new storeKey
     */
    @Deprecated
    public void setStoreKey(String storeKey) {
        setStoreIdentifier(storeKey);
    }

    /**
     * Provides the expiration date of this asset.
     * 
     * @deprecated use {@link #getExpiresAt()}
     * @return the expiration date
     */
    @Deprecated
    public Date getExpirationDate() {
        return getExpiresAt();
    }

    /**
     * Changes the expiration date of this asset.
     * 
     * @deprecated use {@link #setExpiresAt(Date)}
     * @param expirationDate the new expiration date
     */
    @Deprecated
    public void setExpirationDate(Date expirationDate) {
        setExpiresAt(expirationDate);
    }

    private boolean getExpiresNever() {
        return expiresNever;
    }

    public void setExpiresNever(boolean expiresNever) {
        this.expiresNever = expiresNever;
    }

    @Override
    public Asset copy() {
        final Asset asset = new Asset();
        
        asset.setCreatedAt(getCreatedAt());
        asset.setModifiedAt(getModifiedAt());
        asset.setExpiresAt(getExpiresAt());
        asset.setExpiresNever(getExpiresNever());
        asset.metaData = metaData;
        asset.setName(getName());
        asset.setTitle(getTitle());
        asset.setDescription(getDescription());
        asset.setContent(getContent());
        
        return asset;
    }

    @Override
    public void render(Renderer renderer, RenderingLevel level) throws RenderingException {
        renderer.
            key("id").value(getId()).
            key("storeKey").value(getStoreIdentifier()).
            key("creationDate").value(getCreatedAt()).
            key("modificationDate").value(getModifiedAt()).
            key("name").value(getName()).
            key("title").value(getTitle()).
            key("description").value(getDescription()).
            key("mimetype").value(mime).
            key("size").value(length).
            key("expirationDate").value(getExpiresAt()).
            key("expired").value(isExpired()).
            key("expiresNever").value(getExpiresNever()).
            key("metaData").value(getMetaData());
    }
    
    @Override
    public void encodeJSON(JSONConstructor json) throws JSONException {
        json.
            key("id").value(getId()).
            key("storeKey").value(getStoreIdentifier()).
            key("creationDate").value(getCreatedAt() == null ? null : getCreatedAt().getTime() / 1000).
            key("modificationDate").value(getModifiedAt() == null ? null : getModifiedAt().getTime() / 1000);
        
        if (name != null) json.key("name").value(getName());
        if (title != null) json.key("title").value(getTitle());
        if (description != null) json.key("description").value(getDescription());
        
        json.
            key("mimetype").value(mime).       
            key("size").value(length).
            key("expirationDate").value(getExpiresAt() == null ? null : getExpiresAt().getTime() / 1000).
            key("expired").value(isExpired()).
            key("expiresNever").value(getExpiresNever());
        
        if (metaData != null) json.key("metaData").plain(metaData);
    }

    @Override
    public void convert(StringBuilder buf, ContentConverter converter) throws ConversionException {
        converter.convertKeyValue(buf, "id", id, KeyValueState.START);
        converter.convertKeyValue(buf, "storeKey", getStoreIdentifier(), KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "creationDate", 
            getCreatedAt() == null ? null : getCreatedAt().getTime() / 1000, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "modificationDate", 
            getModifiedAt() == null ? null : getModifiedAt().getTime() / 1000, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "name", name, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "title", title, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "expirationDate", 
            getExpiresAt() == null ? null : getExpiresAt().getTime() / 1000, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "expired", isExpired(), KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "expiresNever", expiresNever, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "metaData", metaData, KeyValueState.INSIDE);
        converter.convertKeyValue(buf, "description", description, KeyValueState.LAST);
    }

    @Override
    public int hashCode() {
        return 31 * 1 + (id == null ? 0 : id.hashCode());
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Asset) {
            final Asset other = Asset.class.cast(that);
            return Objects.equal(id, other.id);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Asset[" + getId() + "] ";
    }

}
