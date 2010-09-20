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

package de.cosmocode.palava.legacy;

import com.google.inject.internal.Objects;

import de.cosmocode.palava.ipc.IpcArguments;
import de.cosmocode.palava.ipc.IpcCommand;
import de.cosmocode.palava.ipc.cache.CacheKey;

/**
 * Legacy compatible {@link CacheKey} which uses
 * the arguments and the language stored in the session.
 *
 * @since 2.1
 * @author Oliver Lorenz
 */
final class LegacyCacheKey implements CacheKey {

    private static final long serialVersionUID = 8404456285214048054L;
    
    private Class<? extends IpcCommand> command;
    private IpcArguments arguments;
    private String language;

    public LegacyCacheKey(Class<? extends IpcCommand> command, IpcArguments arguments, String language) {
        this.command = command;
        this.arguments = arguments;
        this.language = language;
    }

    @Override
    public Class<? extends IpcCommand> getCommand() {
        return command;
    }

    @Override
    public IpcArguments getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof LegacyCacheKey) {
            final LegacyCacheKey other = LegacyCacheKey.class.cast(that);
            return Objects.equal(command, other.command) &&
                Objects.equal(arguments, other.arguments) &&
                Objects.equal(language, other.language);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(command, arguments, language);
    }
}
