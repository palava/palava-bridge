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

package de.cosmocode.palava.bridge.call;

import de.cosmocode.palava.ipc.IpcArguments;

/**
 * Argument map providing useful methods for type checking
 * and converting.
 *
 * @deprecated use {@link IpcArguments}
 * @author Willi Schoenborn
 */
@Deprecated
public interface Arguments extends IpcArguments {

    /**
     * Checks whether all specified keys exist in these arguments.
     * 
     * @param keys the required keys
     * @throws MissingArgumentException if any key is missing
     */
    void require(String... keys) throws MissingArgumentException;
    
}
