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

package de.cosmocode.palava.bridge.call;

import java.util.List;

/**
 * Indicates a missing argument when invoking a command.
 *
 * @author Willi Schoenborn
 */
public class MissingArgumentException extends RuntimeException {

    private static final long serialVersionUID = -4016565094430756701L;
    
    public MissingArgumentException(String argument) {
        super(argument);
    }
    
    public MissingArgumentException(Throwable cause) {
        super(cause);
    }
    
    public MissingArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingArgumentException(Object ignored, String missingArgument) {
        this(missingArgument);
    }
    
    public MissingArgumentException(Object ignored, String missingArgument, String type) {
        super(missingArgument + " (" + type + ")");
    }
    
    public MissingArgumentException(Object ignored, List<String> missingArguments) {
        this(missingArguments.toString());
    }
    
}
