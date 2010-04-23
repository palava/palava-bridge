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

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.UniqueAnnotations;
import com.google.inject.multibindings.Multibinder;

import de.cosmocode.palava.bridge.call.filter.Filter;
import de.cosmocode.palava.bridge.call.filter.definition.FilterDefinition;
import de.cosmocode.palava.bridge.command.Alias;
import de.cosmocode.palava.bridge.command.Aliases;
import de.cosmocode.palava.bridge.command.Command;

/**
 * Abstract module for applications.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractApplication extends AbstractModule {
    
    private final List<FilterDefinition> filterDefinitions = Lists.newArrayList();
    
    @Override
    protected final void configure() {
        configureApplication();
        
        final TypeLiteral<List<FilterDefinition>> literal = new TypeLiteral<List<FilterDefinition>>() { };
        bind(Key.get(literal, UniqueAnnotations.create())).toInstance(filterDefinitions);
    }
    
    /**
     * Configures the applications.
     * 
     * <p>
     *   It required to install implementations for all
     *   core interfaces, e.g. by installing {@link CoreModule}.
     * </p>
     */
    protected abstract void configureApplication();

    /**
     * Binds a filter.
     * 
     * @deprecated do not use
     * 
     * @param matcher the matcher the filter uses
     * @return a {@link FilterBinder}
     */
    @Deprecated
    protected final FilterBinder filter(Predicate<Command> matcher) {
        return new InternalFilterBinder(matcher);
    }
    
    /**
     * Binds an alias.
     * 
     * @param packageName the package name the alias stands for
     * @return an {@link AliasBinder}
     */
    protected final AliasBinder alias(String packageName) {
        return new InternalAliasBinder(packageName);
    }
    
    /**
     * Private implementation of the {@link FilterBinder} interface
     * which holds a reference to the enclosing {@link Module}.
     *
     * @author Willi Schoenborn
     * @param <F> the generic filter type
     */
    private final class InternalFilterBinder implements FilterBinder {
        
        private final Predicate<Command> matcher;
        
        public InternalFilterBinder(Predicate<Command> matcher) {
            this.matcher = Preconditions.checkNotNull(matcher, "Matcher");
        }
        
        @Override
        public void through(Class<? extends Filter> type) {
            through(Key.get(type));
        }
        
        @Override
        public void through(final Key<? extends Filter> filterKey) {
            filterDefinitions.add(new FilterDefinition() {
                
                @Override
                public Key<? extends Filter> getKey() {
                    return filterKey;
                }
                
                @Override
                public boolean appliesTo(Command command) {
                    return matcher.apply(command);
                }
            });
        }
        
    }
    
    /**
     * Private implementation of the {@link AliasBinder} interface.
     *
     * @author Willi Schoenborn
     */
    private final class InternalAliasBinder implements AliasBinder {
        
        private final String packageName;
        
        public InternalAliasBinder(String packageName) {
            this.packageName = Preconditions.checkNotNull(packageName, "PackageName");
        }
        
        @Override
        public void as(String alias) {
            Multibinder.newSetBinder(binder(), Alias.class).addBinding().toInstance(
                Aliases.of(alias, packageName)
            );
        }
        
    }
    
}
