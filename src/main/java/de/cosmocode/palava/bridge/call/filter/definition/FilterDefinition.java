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

package de.cosmocode.palava.bridge.call.filter.definition;

import com.google.inject.Key;

import de.cosmocode.palava.bridge.call.filter.Filter;
import de.cosmocode.palava.bridge.command.Command;

/**
 * A filter definition is the combination of a key exactly defining a
 * filter and the abbility to decide whether the associated filter
 * should run on a specified command.
 *
 * @author Willi Schoenborn
 */
public interface FilterDefinition {

    /**
     * The {@link Key} of the associated filter.
     * 
     * @return the key
     */
    Key<? extends Filter> getKey();
    
    /**
     * Checks whether this filter should run on the specified command.
     *
     * <p>
     *   <strong>Note</strong>: Implementations should be as fast as
     *   possible. Long running operations will affect server performance
     *   and throughput dramatically.
     * </p>
     * 
     * @param command the requested command
     * @return true if this filter should be applied to the incoming call
     *         requesting the specified command
     */
    boolean appliesTo(Command command);
    
}
