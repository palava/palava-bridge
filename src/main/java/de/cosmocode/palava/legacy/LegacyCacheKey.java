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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Legacy compatible {@link CacheKey} which uses
 * the arguments and the language stored in the session.
 *
 * @since 2.1
 * @author Oliver Lorenz
 */
public class LegacyCacheKey implements CacheKey {

    private static final Logger LOG = LoggerFactory.getLogger(LegacyCacheKey.class);

    private Class<? extends IpcCommand> ipcCommand;
    private IpcArguments ipcArguments;
    private String language;

    public LegacyCacheKey(Class<? extends IpcCommand> ipcCommand, IpcArguments ipcArguments, String language) {
        this.ipcCommand = ipcCommand;
        this.ipcArguments = ipcArguments;
        this.language = language;
    }

    public Class<? extends IpcCommand> getIpcCommand() {
        return null;
    }

    public IpcArguments getIpcArguments() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final LegacyCacheKey that = (LegacyCacheKey) o;

        if (ipcArguments != null ? !ipcArguments.equals(that.ipcArguments) : that.ipcArguments != null) return false;
        if (ipcCommand != null ? !ipcCommand.equals(that.ipcCommand) : that.ipcCommand != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ipcCommand != null ? ipcCommand.hashCode() : 0;
        result = 31 * result + (ipcArguments != null ? ipcArguments.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}
