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

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.ipc.IpcCommand;

/**
 * Default implementation of the {@link CommandManager} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultCommandManager implements CommandManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCommandManager.class);

    private final Injector injector;
    
    private final Server server;
    
    private final Set<Alias> aliases;
    
    private final Map<String, Class<?>> cache = Maps.newHashMap();

    @Inject
    public DefaultCommandManager(Injector injector, Server server, Set<Alias> aliases) {
        // TODO make aliases "optional" (no error if non registered)
        // TODO checkNotNull required? guice will error out without the objects..?
        this.injector = Preconditions.checkNotNull(injector, "Injector");
        this.server = Preconditions.checkNotNull(server, "Server");
        this.aliases = Preconditions.checkNotNull(aliases, "Aliases");
        LOG.debug("Aliases {}", aliases);
    }
    
    @Override
    public Command forName(String aliasedName) {
        Preconditions.checkNotNull(aliasedName, "AliasedName");
        final String realName = realName(aliasedName);
        
        final Class<?> type;
        final Class<?> cached = cache.get(realName);
        if (cached == null) {
            try {
                type = Class.forName(realName);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
            cache.put(realName, type);
        } else {
            type = cached;
        }
        if (Job.class.isAssignableFrom(type)) {
            final Job job = injector.getInstance(type.asSubclass(Job.class));
            return Commands.adaptJob(job, server);
        } else if (IpcCommand.class.isAssignableFrom(type)) {
            final IpcCommand ipcCommand = injector.getInstance(type.asSubclass(IpcCommand.class));
            return Commands.adaptIpcCommand(ipcCommand);
        } else {
            return injector.getInstance(type.asSubclass(Command.class));
        }
    }

    private String realName(String aliasedName) {
        for (Alias alias : aliases) {
            if (aliasedName.startsWith(alias.getName())) {
                return alias.apply(aliasedName);
            }
        }
        return aliasedName;
    }
    
}
