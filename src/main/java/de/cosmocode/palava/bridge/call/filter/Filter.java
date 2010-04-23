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

package de.cosmocode.palava.bridge.call.filter;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.command.Command;

/**
 * A filter can be configured to get executed on specified call, 
 * usually requesting specified {@link Command}s and runs before, after or instead
 * of the requested command.
 *
 * @author Willi Schoenborn
 */
public interface Filter {

    /**
     * Execute this filter. This may result in proceeding the given chain or in returning 
     * a probably cached content.
     * 
     * @param call the incoming call
     * @param chain the proceeding chain
     * @return the generated content
     * @throws FilterException if filtering failed
     */
    Content filter(Call call, FilterChain chain) throws FilterException;
    
}
