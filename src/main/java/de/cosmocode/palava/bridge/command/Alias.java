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

import com.google.common.base.Function;

import de.cosmocode.patterns.Immutable;

/**
 * An {@link Alias} is a short name for a package.
 *
 * @author Willi Schoenborn
 */
@Immutable
public interface Alias extends Function<String, String> {

    /**
     * Provides the names of this alias.
     * 
     * @return the name
     */
    String getName();
    
    /**
     * Provides the package name this alias stands for.
     * 
     * @return the package name
     */
    String getPackageName();
    
}
