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

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import de.cosmocode.palava.bridge.call.filter.definition.FilterDefinition;

/**
 * Default implementation of the {@link FilterChainFactory} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultFilterChainFactory implements FilterChainFactory {

    private final ImmutableList<Filter> filters;
    
    @Inject
    public DefaultFilterChainFactory(final Injector injector) {
        Preconditions.checkNotNull(injector, "Injector");
        
        final ImmutableList.Builder<FilterDefinition> builder = ImmutableList.builder();

        for (Binding<?> entry : injector.findBindingsByType(new TypeLiteral<List<FilterDefinition>>() { })) {

            // guarded by findBindingsByType()
            @SuppressWarnings("unchecked")
            final Key<List<FilterDefinition>> key = (Key<List<FilterDefinition>>) entry.getKey();
            builder.addAll(injector.getInstance(key));
        }
        
        final List<Filter> transformed = Lists.transform(builder.build(), new Function<FilterDefinition, Filter>() {
            
            @Override
            public Filter apply(FilterDefinition from) {
                return new FilterDefinitionAdapter(injector, from);
            }
            
        });
        
        this.filters = ImmutableList.copyOf(transformed);
    }

    @Override
    public FilterChain create(FilterChain proceeding) {
        return new DefaultFilterChain(filters, proceeding);
    }

}
