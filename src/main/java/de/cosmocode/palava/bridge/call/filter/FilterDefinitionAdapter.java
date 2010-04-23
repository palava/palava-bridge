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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.filter.definition.FilterDefinition;
import de.cosmocode.patterns.Adapter;

/**
 * Adapts a {@link FilterDefinition} to the {@link Filter} interface. 
 *
 * @author Willi Schoenborn
 */
@Adapter(Filter.class)
final class FilterDefinitionAdapter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(FilterDefinitionAdapter.class);

    private final Filter filter;
    
    private final FilterDefinition definition;

    public FilterDefinitionAdapter(Injector injector, FilterDefinition definition) {
        Preconditions.checkNotNull(injector, "Injector");
        this.filter = injector.getInstance(definition.getKey());
        this.definition = Preconditions.checkNotNull(definition, "Definition");
    }

    @Override
    public Content filter(Call call, FilterChain chain) throws FilterException {
        if (definition.appliesTo(call.getCommand())) {
            log.debug("Filtering {} using {}", call, filter);
            return filter.filter(call, chain);
        } else {
            log.debug("Skipping filter executing of {} for {}", filter, call);
            return chain.filter(call);
        }
    }

    @Override
    public String toString() {
        return filter.toString();
    }
    
}
