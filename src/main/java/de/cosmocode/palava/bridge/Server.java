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

package de.cosmocode.palava.bridge;

import com.google.inject.Inject;

import de.cosmocode.palava.core.Service;

/**
 * Legacy central palava framework instance.
 *
 * @author Willi Schoenborn
 */
public interface Server extends Service {

    /**
     * Provides access to the service manager.
     * 
     * @deprecated use {@link Inject} to get a reference
     * 
     * @return the service manager
     */
    @Deprecated
    ServiceManager getServiceManager();

    /**
     * Lookups a service by its specification.
     * 
     * @deprecated legacy way to retrieve a Service instance Use the {@link Inject}
     * annotation instead.
     * 
     * @param <T> the generic interface type
     * @param spec the spec class literal
     * @return the found Service
     * @throws NullPointerException if spec is null
     */
    @Deprecated
    <T> T lookup(Class<T> spec);
    
}
