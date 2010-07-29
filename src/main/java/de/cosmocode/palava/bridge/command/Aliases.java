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

import com.google.common.base.Preconditions;

/**
 * Static factory/utility class for {@link Alias}es.
 *
 * @deprecated without substitution
 * @author Willi Schoenborn
 */
@Deprecated
public final class Aliases {

    private Aliases() {
        
    }
    
    /**
     * Construct a new {@link Alias}.
     * 
     * @param name the name of the alias 
     * @param packageName the package name
     * @return a new {@link Alias}
     * @throws NullPointerException if name or packageName is null
     */
    public static Alias of(String name, String packageName) {
        return new DefaultAlias(name, packageName);
    }
    
    /**
     * Simple default implementation of the {@link Alias} interface.
     *
     * @author Willi Schoenborn
     */
    private static class DefaultAlias implements Alias {

        private final String name;
        
        private final String packageName;
        
        public DefaultAlias(String name, String packageName) {
            this.name = Preconditions.checkNotNull(name, "Name");
            this.packageName = Preconditions.checkNotNull(packageName, "PackageName");
        }

        @Override
        public String apply(String from) {
            return from.startsWith(name) ? from.replace(name, packageName) : from;
        }

        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getPackageName() {
            return packageName;
        }
        
        @Override
        public String toString() {
            return name + "=" + packageName;
        }
        
    }
    
}
