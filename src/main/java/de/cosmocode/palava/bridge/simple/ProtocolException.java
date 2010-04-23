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

/**
 * Indicates an error during protocol parsing.
 *
 * @author Willi Schoenborn
 */
public final class ProtocolException extends RuntimeException {

    private static final long serialVersionUID = -6944164800204330453L;

    public ProtocolException(char expected, char actual) {
        super(String.format("Expected '%s', but was '%s'", expected, actual));
    }
    
    public ProtocolException(String message) {
        super(message);
    }
    
    public ProtocolException(Throwable throwable) {
        super(throwable);
    }
    
    public ProtocolException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
