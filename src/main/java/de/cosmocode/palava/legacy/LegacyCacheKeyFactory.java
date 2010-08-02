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
import de.cosmocode.palava.ipc.IpcCall;
import de.cosmocode.palava.ipc.IpcCommand;
import de.cosmocode.palava.ipc.IpcSession;
import de.cosmocode.palava.ipc.cache.CacheKey;
import de.cosmocode.palava.ipc.cache.CacheKeyFactory;

/**
 * Legacy compatible {@link CacheKeyFactory} which uses
 * the arguments and the language stored in the session.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
public final class LegacyCacheKeyFactory implements CacheKeyFactory {

    @Override
    public CacheKey create(IpcCall call, IpcCommand command) {

        final IpcArguments arguments = call.getArguments();
        final IpcSession session = call.getConnection().getSession();
        final String language = session.get("lang");

        return new LegacyCacheKey(command.getClass(), arguments, language);
    }

}
