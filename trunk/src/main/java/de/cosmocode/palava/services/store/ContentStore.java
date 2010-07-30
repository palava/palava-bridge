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

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.content.StreamContent;
import de.cosmocode.palava.store.Store;

/**
 * Binary data storage.
 *
 * @deprecated use {@link Store}
 * @since long time ago
 * @author Willi Schoenborn
 */
@Deprecated
public interface ContentStore {

    /**
     * Saves content.
     * 
     * @param content the content being saved
     * @return string identifier
     * @throws Exception if saving failed
     */
    String store(Content content) throws Exception;
    
    /**
     * Reads content.
     * 
     * @param key the string identifier
     * @return content being read
     * @throws Exception if reading failed
     */
    StreamContent load(String key) throws Exception;

    /**
     * Removes content.
     * 
     * @param key the string identifier
     */
    void remove(String key);
    
}
