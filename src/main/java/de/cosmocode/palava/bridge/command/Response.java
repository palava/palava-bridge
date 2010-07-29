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

package de.cosmocode.palava.bridge.command;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.ipc.IpcCommand;

/**
 * Legacy content wrapper.
 * 
 * @deprecated use {@link IpcCommand} and fill result map
 *
 * @author Willi Schoenborn
 */
@Deprecated
public interface Response {

    /**
     * Set the content of this response.
     * 
     * @param content the content
     */
    void setContent(Content content);
    
    /**
     * Provides the content of this response.
     * 
     * @return the current content or null if there is no content
     */
    Content getContent();
    
    /**
     * Check content state.
     * 
     * @return true if this response contains content
     */
    boolean hasContent();
    
}
