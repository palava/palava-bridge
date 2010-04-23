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

package de.cosmocode.palava.bridge.content;

/**
 * marks an object to be usable with a converter.
 * 
 * @author Detlef Hüttemann
 * @deprecated use JSONMapable instead
 */
@Deprecated
public interface Convertible {
    
    /**
     * Converts this object using the given converter.
     * 
     * @param buf the target buffer
     * @param converter the {@linkplain ContentConverter content converter} used to convert this object
     * @throws ConversionException if conversion failed
     */
    void convert(StringBuffer buf, ContentConverter converter) throws ConversionException;
    
}
