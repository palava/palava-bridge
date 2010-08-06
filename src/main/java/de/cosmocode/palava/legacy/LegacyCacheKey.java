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
public class LegacyCacheKey implements CacheKey {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LegacyCacheKey that = (LegacyCacheKey) o;

        if (arguments != null ? !arguments.equals(that.arguments) : that.arguments != null) return false;
        if (command != null ? !command.equals(that.command) : that.command != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = command != null ? command.hashCode() : 0;
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}
