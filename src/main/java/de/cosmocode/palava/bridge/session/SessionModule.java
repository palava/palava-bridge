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

package de.cosmocode.palava.bridge.session;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

/**
 * A {@link Module} for the {@link de.cosmocode.palava.bridge.session} package.
 *
 * @author Willi Schoenborn
 */
public final class SessionModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(HttpSessionManager.class).to(DefaultHttpSessionManager.class).in(Singleton.class);
        binder.bind(HttpSession.class).toProvider(HttpSessionManager.class);
    }

}
