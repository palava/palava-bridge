/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.bridge;

import de.cosmocode.palava.bridge.call.CallModule;
import de.cosmocode.palava.bridge.call.filter.FilterModule;
import de.cosmocode.palava.bridge.command.CommandManager;
import de.cosmocode.palava.bridge.command.CommandModule;
import de.cosmocode.palava.bridge.command.DefaultCommandManager;
import de.cosmocode.palava.bridge.request.RequestModule;
import de.cosmocode.palava.bridge.scope.ScopeModule;
import de.cosmocode.palava.bridge.session.SessionModule;
import de.cosmocode.palava.core.ServiceModule;

/**
 * 
 *
 * @author Willi Schoenborn
 */
public class BridgeModule  extends ServiceModule {

    @Override
    protected void configure() {
        install(new CallModule());
        install(new FilterModule());
        install(new CommandModule());
        install(new RequestModule());
        install(new ScopeModule());
        install(new SessionModule());
        install(new ServerModule());
    }
}
