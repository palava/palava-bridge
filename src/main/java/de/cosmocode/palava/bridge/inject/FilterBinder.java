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

package de.cosmocode.palava.bridge.inject;

import com.google.inject.Key;

import de.cosmocode.palava.bridge.call.filter.Filter;
import de.cosmocode.palava.bridge.call.filter.definition.FilterDefinition;

/**
 * Binds a filter to a key/class/instance.
 * 
 * @deprecated dont use anymore
 * 
 * @author Willi Schoenborn
 */
@Deprecated
public interface FilterBinder {

    /**
     * Binds the associated predicate and the specified class
     * to a new {@link FilterDefinition}.
     * 
     * @param filterKey the key
     */
    void through(Class<? extends Filter> filterKey);

    /**
     * Binds the associated predicate and the specified key
     * to a new {@link FilterDefinition}.
     * 
     * @param filterKey the key
     */
    void through(Key<? extends Filter> filterKey);
    
}
