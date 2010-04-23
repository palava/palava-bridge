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

package de.cosmocode.palava.bridge.call.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.call.Call;

/**
 * Default implementation of the {@link FilterChain} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultFilterChain implements FilterChain {

    private static final Logger log = LoggerFactory.getLogger(DefaultFilterChain.class);

    private final List<Filter> filters;
    
    private final FilterChain proceedingChain;
    
    private int index = -1;
    
    public DefaultFilterChain(List<Filter> filters, FilterChain proceedingChain) {
        this.filters = Preconditions.checkNotNull(filters, "Filter");
        this.proceedingChain = Preconditions.checkNotNull(proceedingChain, "ProceedingChain");
    }

    @Override
    public Content filter(Call call) throws FilterException {
        index++;
        if (index < filters.size()) {
            final Filter filter = filters.get(index);
            log.debug("Running filter {}", filter);
            return filter.filter(call, this);
        } else {
            return proceedingChain.filter(call);
        }
    }

}
