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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.Commands;

/**
 * Static factory class for {@link CommandMatcher}s.
 *
 * @author Willi Schoenborn
 */
public final class Matchers {

    private static final Predicate<Command> ANY = new Predicate<Command>() {
        
        @Override
        public boolean apply(Command type) {
            return true;
        }
        
    };
    
    private Matchers() {
        
    }
    
    /**
     * Provides a {@link Predicate<Command>} which matches every command.
     * 
     * @return a {@link Predicate<Command>} which always return true
     */
    public static Predicate<Command> any() {
        return ANY;
    }
    
    /**
     * Provides a {@link Predicate<Command>} which uses the given predicate to decide
     * whether a given {@link Command} matches.
     * 
     * @param predicate the backing predicate
     * @return a {@link Predicate<Command>} backed by a {@link Predicate}
     * @throws NullPointerException if predicate is null
     */
    public static Predicate<Command> ofPredicate(final Predicate<? super Command> predicate) {
        Preconditions.checkNotNull(predicate, "Predicate");
        return new Predicate<Command>() {
            
            @Override
            public boolean apply(Command input) {
                return predicate.apply(input);
            }
            
        };
    }
    
    /**
     * Returns a predicate which checks whether a given {@link Command} matches any
     * of the given patterns by using a servlet style regex syntax.
     * 
     * <div>
     *   Supported are the following patterns:
     *   <ol>
     *     <li>*.list</li>
     *     <li>de.cosmocode.palava.*</li>
     *     <li>de.cosmocode.palava.jobs.assets.list.All</li>
     *   </ol>
     * </div>
     * 
     * @param pattern the required pattern
     * @param patterns optional patterns
     * @return a new {@link Predicate}
     * @throws NullPointerException if pattern is null, patterns is null or patterns contains null
     */
    public static Predicate<Command> named(String pattern, String... patterns) {
        Preconditions.checkNotNull(pattern, "Pattern");
        Preconditions.checkNotNull(patterns, "Patterns");
        if (Iterables.any(Sets.newHashSet(patterns), Predicates.isNull())) {
            throw new NullPointerException("Patterns contains null");
        }
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        final ImmutableList<String> list = builder.add(pattern).add(patterns).build();
        final List<Predicate<Command>> matchers = Lists.transform(list, new Function<String, Predicate<Command>>() {
           
            @Override
            public Predicate<Command> apply(String from) {
                return NamePatternType.SIMPLE.matcher(from);
            }
            
        });
        return Matchers.ofPredicate(Predicates.or(matchers));
    }
    
    /**
     * Returns a predicate which checks whether a given {@link Command} matches any
     * of the given patterns by using standard {@link Pattern} syntax.
     * 
     * @param pattern the required pattern
     * @param patterns optional patterns
     * @return a new {@link Predicate}
     * @throws NullPointerException if pattern is null, patterns is null or patterns contains null
     */
    public static Predicate<Command> regex(String pattern, String... patterns) {
        Preconditions.checkNotNull(pattern, "Pattern");
        Preconditions.checkNotNull(patterns, "Patterns");
        if (Iterables.any(Sets.newHashSet(patterns), Predicates.isNull())) {
            throw new NullPointerException("Patterns contains null");
        }
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        final ImmutableList<String> list = builder.add(pattern).add(patterns).build();
        final List<Predicate<Command>> matchers = Lists.transform(list, new Function<String, Predicate<Command>>() {
           
            @Override
            public Predicate<Command> apply(String from) {
                return NamePatternType.REGEX.matcher(from);
            }
            
        });
        return Matchers.ofPredicate(Predicates.or(matchers));
    }
    
    /**
     * Returns a {@link Predicate} which checks the given {@link Command} for the presence
     * of the specified {@link Annotation}.
     * 
     * @param annotation the required annotation
     * @return a new {@link Predicate}
     * @throws NullPointerException if annotation is null
     */
    public static Predicate<Command> annotatedWith(final Class<? extends Annotation> annotation) {
        Preconditions.checkNotNull(annotation, "Annotation");
        return new Predicate<Command>() {
            
            @Override
            public boolean apply(Command command) {
                return Commands.getClass(command).isAnnotationPresent(annotation);
            }
            
        };
    }
    
    /**
     * Returns a {@link Predicate} which checks whether the given {@link Command}
     * is a sub class or of the same class as specified class.
     * 
     * @param superClass the super class
     * @return a new {@link Predicate}
     * @throws NullPointerException if superClass is null
     */
    public static Predicate<Command> subClassesOf(final Class<?> superClass) {
        Preconditions.checkNotNull(superClass, "SuperClass");
        return new Predicate<Command>() {
            
            @Override
            public boolean apply(Command command) {
                return superClass.isAssignableFrom(Commands.getClass(command));
            }
            
        };
    }
    
}
