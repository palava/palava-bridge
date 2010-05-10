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

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import de.cosmocode.palava.bridge.command.Alias;
import de.cosmocode.palava.bridge.command.Aliases;

/**
 * Abstract module for applications.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractApplication extends AbstractModule {
    
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
