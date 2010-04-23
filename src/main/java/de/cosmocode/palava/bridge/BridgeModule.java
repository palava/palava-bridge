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

import com.google.inject.Binder;
import com.google.inject.Module;

import de.cosmocode.palava.bridge.call.filter.FilterModule;
import de.cosmocode.palava.bridge.command.CommandModule;
import de.cosmocode.palava.bridge.request.RequestModule;
import de.cosmocode.palava.bridge.session.SessionModule;
import de.cosmocode.palava.ipc.IpcModule;

/**
 * Binds the default bridge modules.
 *
 * @author Willi Schoenborn
 */
public final class BridgeModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.install(new FilterModule());
        binder.install(new CommandModule());
        binder.install(new RequestModule());
        binder.install(new IpcModule());
        binder.install(new SessionModule());
        binder.install(new ServerModule());
    }
}
