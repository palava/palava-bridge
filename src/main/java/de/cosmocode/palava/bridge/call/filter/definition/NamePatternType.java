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

package de.cosmocode.palava.bridge.call.filter.definition;

import com.google.common.base.Predicate;

import de.cosmocode.palava.bridge.command.Command;

/**
 * An enum style factory which creates concrete {@link CommandMatcher}
 * instances based on their specific semantics.
 *
 * @author Willi Schoenborn
 */
enum NamePatternType {

    /**
     * Defines simple servlet style name pattern matching
     * using *s.
     */
    SIMPLE {
        
        @Override
        public Predicate<Command> matcher(String pattern) {
            return new SimpleCommandNameMatcher(pattern);
        }
        
    },
    
    /**
     * Defines regex style name pattern matching.
     */
    REGEX {
        
        @Override
        public Predicate<Command> matcher(String pattern) {
            return new RegexCommandNameMatcher(pattern);
        }
        
    };
    
    /**
     * Creates a new {@link Predicate<Command>} using the semantics
     * of this {@link NamePatternType}.
     * 
     * @param pattern the name pattern
     * @return a new {@link Predicate<Command>}
     */
    public abstract Predicate<Command> matcher(String pattern);
    
}
