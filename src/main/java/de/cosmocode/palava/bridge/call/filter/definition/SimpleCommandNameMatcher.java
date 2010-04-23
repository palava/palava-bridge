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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.Commands;

/**
 * A {@link CommandMatcher} which uses a servlet style like
 * matching algorithm.
 *
 * @author Willi Schoenborn
 */
final class SimpleCommandNameMatcher implements Predicate<Command> {

    /**
     * A {@link PatternType} describes the kind of a simple name pattern.
     *
     * @author Willi Schoenborn
     */
    private static enum PatternType {
        
        PREFIX {
            
            @Override
            public boolean matches(String pattern, String name) {
                return name.endsWith(pattern);
            }
            
        },
        
        SUFFIX {
            
            @Override
            public boolean matches(String pattern, String name) {
                return name.startsWith(pattern);
            }
            
        },
        
        LITERAL {
          
            @Override
            public boolean matches(String pattern, String name) {
                return pattern.equals(name);
            }
            
        };
        
        public abstract boolean matches(String pattern, String name);
        
    }

    private final String pattern;
    
    private final PatternType patternType;
    
    public SimpleCommandNameMatcher(String pattern) {
        Preconditions.checkNotNull(pattern, "Pattern");
        
        if (pattern.startsWith("*")) {
            this.pattern = pattern.substring(1);
            this.patternType = PatternType.PREFIX;
        } else if (pattern.endsWith("*")) {
            this.pattern = pattern.substring(0, pattern.length() - 1);
            this.patternType = PatternType.SUFFIX;
        } else {
            this.pattern = pattern;
            this.patternType = PatternType.LITERAL;
        }
    }
    
    @Override
    public boolean apply(Command command) {
        return patternType.matches(pattern, Commands.getClass(command).getName());
    }

}
