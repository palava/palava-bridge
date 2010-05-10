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

import java.nio.ByteBuffer;

import com.google.common.base.Preconditions;

import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.CallType;

/**
 * Default implementation of the the {@link Header} interface.
 *
 * @author Willi Schoenborn
 */
final class SimpleHeader implements Header {

    private final CallType requestType;
    private final String aliasedName;
    private final String sessionId;
    private final int contentLength;
    
    public SimpleHeader(CallType requestType, String aliasedName, String sessionId, int contentLength) {
        this.requestType = Preconditions.checkNotNull(requestType, "RequestType");
        this.aliasedName = Preconditions.checkNotNull(aliasedName, "AliasedName");
        this.sessionId = Preconditions.checkNotNull(sessionId, "SessionId");
        this.contentLength = contentLength;
    }

    @Override
    public CallType getCallType() {
        return requestType;
    }

    @Override
    public String getAliasedName() {
        return aliasedName;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public int getContentLength() {
        return contentLength;
    }

    @Override
    public ByteBuffer getContent() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return String.format("SimpleHeader [aliasedName=%s, contentLength=%s, requestType=%s, sessionId=%s]",
            aliasedName, contentLength, requestType, sessionId);
    }

}
