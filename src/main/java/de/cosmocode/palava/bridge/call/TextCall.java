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

/**
 * {@link Call} which uses a plain UTF-8 string as arguments. 
 *
 * @deprecated without substitution
 * @author Willi Schoenborn
 */
@Deprecated
public interface TextCall extends Call {
 
    /**
     * Returns the given text parameter.
     * 
     * @return the parameter text
     */
    String getText();
    
}
