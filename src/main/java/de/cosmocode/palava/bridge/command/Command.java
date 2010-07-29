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

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.ipc.IpcCommand;

/**
 * A {@link Command} is very similiar to a {@link Servlet}. It represents
 * a unit of work which will handle an incoming call.
 *
 * @deprecated use {@link IpcCommand}
 * @author Willi Schoenborn
 */
@Deprecated
public interface Command {

    /**
     * Executes an incoming call.
     * 
     * @param call the incoming call
     * @return the produced content
     * @throws CommandException if an error occurs during execution
     */
    Content execute(Call call) throws CommandException;
    
}
