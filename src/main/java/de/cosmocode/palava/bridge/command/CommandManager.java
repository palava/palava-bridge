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

package de.cosmocode.palava.bridge.command;

import de.cosmocode.palava.core.Service;

/**
 * A service which handles command management.
 *
 * @author Willi Schoenborn
 */
public interface CommandManager extends Service {

    /**
     * Creates a {@link Command} for a given name.
     * The name might be aliased.
     * 
     * @param aliasedName the aliased command name
     * @return the probably cached command instance
     */
    Command forName(String aliasedName);
    
}
