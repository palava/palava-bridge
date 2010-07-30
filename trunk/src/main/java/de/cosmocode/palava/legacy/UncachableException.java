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

package de.cosmocode.palava.legacy;

import com.google.gag.annotation.remark.OhNoYouDidnt;

/**
 * Used to indicate no caching is requested.

 * @deprecated why?!
 * @author Willi Schoenborn
 */
@Deprecated
@OhNoYouDidnt
public class UncachableException extends RuntimeException {
    
    private static final long serialVersionUID = -4123365865843473019L;

    public UncachableException() {
    }

    public UncachableException(String message) {
        super(message);
    }

    public UncachableException(Throwable cause) {
        super(cause);
    }

    public UncachableException(String message, Throwable cause) {
        super(message, cause);
    }

}
