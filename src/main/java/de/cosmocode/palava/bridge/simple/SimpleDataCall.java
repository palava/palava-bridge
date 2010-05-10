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

package de.cosmocode.palava.bridge.simple;

import java.io.InputStream;

import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.DataCall;
import de.cosmocode.palava.bridge.call.JsonCall;
import de.cosmocode.palava.bridge.request.HttpRequest;

/**
 * parses the content of a datarequest into a map.
 * 
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 * @deprecated use {@link JsonCall} instead
 */
@Deprecated
final class SimpleDataCall extends SimpleJsonCall implements DataCall {
    
    SimpleDataCall(HttpRequest request, Header header, InputStream stream) {
        super(request, header, stream);
    }

}
