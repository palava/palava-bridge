/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.bridge.inject;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import de.cosmocode.palava.bridge.request.RequestFilter;
import de.cosmocode.palava.core.ServiceModule;

/**
 * Abstract module for applications.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractApplication extends ServiceModule {
    
    private final List<FilterDefinition> filterDefinitions = Lists.newArrayList();
    private final Map<Key<Filter>, Filter> filterInstances = Maps.newLinkedHashMap();
    
    @Override
    protected final void configure() {
        configureApplication();
        
        for (Map.Entry<Key<Filter>, Filter> entry : filterInstances.entrySet()) {
            bind(entry.getKey()).toInstance(entry.getValue());
        }
        
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
     * @param matcher the matcher the filter uses
     * @return a {@link FilterBinder}
     */
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
     * Binds a request filter.
     * 
     * @param type the filter's class literal
     */
    protected final void filterRequestsWith(Class<? extends RequestFilter> type) {
        Multibinder.newSetBinder(binder(), RequestFilter.class).addBinding().to(type);
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
        
        @Override
        public void through(Filter filter) {
            Preconditions.checkNotNull(filter, "Filter");
            final Key<Filter> key = Key.get(Filter.class, UniqueAnnotations.create());
            filterInstances.put(key, filter);
            through(key);
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
