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

import de.cosmocode.palava.bridge.Server;
import de.cosmocode.palava.ipc.IpcCommand;

/**
 * Static utility class for working with {@link Command}s.
 *
 * @author Willi Schoenborn
 */
public final class Commands {
    
    private static final Function<Command, Class<?>> GET_CLASS = new Function<Command, Class<?>>() {
        
        @Override
        public Class<?> apply(Command command) {
            if (command instanceof JobCommand) {
                return JobCommand.class.cast(command).getConcreteClass();
            } else if (command instanceof IpcCommandCommand) {
                return IpcCommandCommand.class.cast(command).getConcreteClass();
            } else {
                return command.getClass();
            }
        }
        
    };
    
    private Commands() {
        
    }

    /**
     * Returns the real class of a {@link Command}, which may differ from
     * {@link Object#getClass()} in case the given {@link Command} is
     * a {@link JobCommand}.
     * 
     * @param command the command
     * @return the underlying class
     */
    public static Class<?> getClass(Command command) {
        return GET_CLASS.apply(command);
    }
    
    /**
     * Creates a {@link Command}-adapter for a Job, using the given server.
     * @param job the job to adapt from
     * @param server the server to use for the job's process method
     * @return a Command that adapts the given Job
     */
    @SuppressWarnings("deprecation")
    public static Command adaptJob(final Job job, final Server server) {
        return new JobCommand(server, job);
    }

    public static Command adaptIpcCommand(IpcCommand ipcCommand) {
        return new IpcCommandCommand(ipcCommand);
    }
}
