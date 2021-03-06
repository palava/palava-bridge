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

import de.cosmocode.palava.bridge.call.MissingArgumentException;

/**
 * Utility interface offering useful validation methods.
 *
 * @deprecated without substitution
 * @author Willi Schoenborn
 */
@Deprecated
/* CHECKSTYLE:OFF */
public interface UtilityJob {
    
    /**
     * Checks whether certain arguments were given to this job and throws a MissingArgumentException if not.
     * @param keys the arguments to check (keys in key-value-mapping)
     * @throws MissingArgumentException if at least one of the keys were not given as arguments to this job
     */
    void require(String... keys) throws Exception;
    
    /**
     * Checks whether at least one of certain arguments were given to this job
     * and throws a MissingArgumentException if not. 
     * @param keys the arguments to check (keys in key-value-mapping)
     * @throws MissingArgumentException if none of the keys were given as arguments to this job
     */
    void requireOneOf(String... keys) throws Exception;
    
    /**
     * Returns true if this job was given a parameter named `key`.
     * 
     * @param key the name of the parameter (key in key-value-mapping)
     * @return true if key exists, false otherwise
     */
    boolean hasArgument(String key);
    
    /** 
     * Looks up a necessary argument named `key`.
     * 
     * @param key the name of the argument (key in key-value-mapping)
     * @throws MissingArgumentException if no mapping for argument `key` exists
     * @return value
     */
    String getMandatory(String key) throws Exception;
    
    /** 
     * lookup a necessary argument.
     * 
     * @param key the name of the argument (key in key-value-mapping)
     * @param argumentType the type of the given argument. Only relevant for the thrown MissingArgumentException.
     * @return value
     */
    String getMandatory(String key, String argumentType) throws Exception;
    
    /**
     * lookup an optional argument. Returns null if no mapping for argument `key` exists.
     * 
     * @return null if no value exists
     */
    String getOptional(String key);

    /**
     * lookup an optional argument. Returns defaultValue if no mapping for argument `key` exists.
     * 
     * @return value
     */
    String getOptional(String key, String defaultValue);

    /** 
     * lookup a necessary argument and parse it to bool.
     * <br>Parsing works like this: 
     * <table>
     * <tr><td>"false"</td>          <td>false</td></tr>
     * <tr><td>null</td>             <td>false</td></tr>
     * <tr><td>"", " ", ...</td>     <td>false</td></tr>
     * <tr><td>(everything else)</td><td>true</td></tr>
     * </table>
     * @throws MissingArgumentException if no mapping for argument `key` exists
     * @return the found boolean
     */
    boolean getBool(String key) throws Exception;
    
    
    /** lookup an optional boolean job-argument.
     * Returns false if no mapping for argument `key` exists.
     * <br>Parsing works like this:
     * <table>
     * <tr><td>"false"</td>          <td>false</td></tr>
     * <tr><td>null</td>             <td>false</td></tr>
     * <tr><td>"", " ", ...</td>     <td>false</td></tr>
     * <tr><td>(everything else)</td><td>true</td></tr>
     * </table>
     */
    public boolean getOptBool (String key);
    
    /** lookup an optional boolean job-argument.
     * Returns `defaultValue` if no mapping for argument `key` exists.
     * <br>Parsing works like this:
     * <table>
     * <tr><td>"false"</td>          <td>false</td></tr>
     * <tr><td>null</td>             <td>false</td></tr>
     * <tr><td>"", " ", ...</td>     <td>false</td></tr>
     * <tr><td>(everything else)</td><td>true</td></tr>
     * </table>
     */
    public boolean getOptBool (String key, boolean defaultValue);
    
    /**
     * lookup a necessary argument of type int
     * @param key the name of the argument
     * @return the value of the argument
     * @throws MissingArgumentException if the argument wasn't given to this job
     * @throws NumberFormatException if the given argument is not a number
     */
    public int getInt (String key) throws MissingArgumentException, NumberFormatException, Exception;
    
    /** 
     * lookup an optional argument of type int. 
     * Returns the parsed int of the mapped value to the argument `key`
     * if it exists and is a number, 0 otherwise.
     */
    public int getOptInt (String key);

    /** 
     * lookup an optional argument of type int. 
     * Returns the parsed int of the mapped value to the argument `key`
     * if it exists and is a number, defaultValue otherwise.
     */
    public int getOptInt (String key, int defaultValue);
    
    
    /**
     * lookup a necessary argument of type long
     * @param key the name of the argument
     * @return the value of the argument
     * @throws MissingArgumentException if the argument wasn't given to this job
     */
    public long getLong (String key) throws MissingArgumentException, Exception;
    
    /** lookup an optional argument of type long. If hasArgument(key)==false then 0 is returned */
    public long getOptLong (String key);

    /** lookup an optional argument of type long. If hasArgument(key)==false then 0 is returned */
    public long getOptLong (String key, long defaultValue);
    
    /** 
     * lookup a necessary parameter.
     * 
     * @throws MissingArgumentException if the necessary parameter was omitted
     * @deprecated use getMandatory(String) instead
     */
    @Deprecated
    public String lookup (String key) throws MissingArgumentException, Exception;
    
    /** 
     * lookup a necessary parameter.
     * 
     * @throws MissingArgumentException if the necessary parameter was omitted
     * @deprecated use getMandatory(String, String) instead
     */
    @Deprecated
    public String lookup (String key, String argumentType) throws MissingArgumentException, Exception;
    
    /**
     * lookup an optional parameter (equivalent to args.get(key))
     * 
     * @deprecated use getOptional(String) instead
     */
    @Deprecated
    public String lookupOptional (String key);

    /**
     * lookup an optional parameter. if it doesn't exist, defaultValue is returned.
     * @deprecated use getOptional(String, String) instead
     */
    @Deprecated
    public String lookupOptional (String key, String defaultValue);
    
    /** 
     * lookup a necessary parameter and parse it to bool
     * <br>Parsing works like this: 
     * <table>
     * <tr><td>"false"</td>          <td>false</td></tr>
     * <tr><td>null</td>             <td>false</td></tr>
     * <tr><td>"", " ", ...</td>     <td>false</td></tr>
     * <tr><td>(everything else)</td><td>true</td></tr>
     * </table>
     * @throws MissingArgumentException if the necessary parameter was omitted
     * @deprecated use {@link #getBool(String)} instead
     */
    @Deprecated
    public boolean lookupBool (String key) throws MissingArgumentException, Exception;
    
    /** lookup an optional bool.
     * <br>Parsing works like this: 
     * <table>
     * <tr><td>"false"</td>          <td>false</td></tr>
     * <tr><td>null</td>             <td>false</td></tr>
     * <tr><td>"", " ", ...</td>     <td>false</td></tr>
     * <tr><td>(everything else)</td><td>true</td></tr>
     * </table>
     * @deprecated use {@link #getOptBool(String)} instead
     */
    @Deprecated
    public boolean lookupOptionalBool (String key);
    
    /**
     * lookup a necessary parameter of type int
     * @param key the name of the parameter
     * @return the value of the parameter
     * @throws MissingArgumentException if the parameter wasn't given to this job
     * @deprecated use {@link #getInt(String)} instead
     */
    @Deprecated
    public int lookupInt (String key) throws MissingArgumentException, Exception;
    
    /** 
     * lookup an optional int. If the int isn't set, 0 is returned
     * @deprecated use {@link #getOptInt(String)} instead
     */
    @Deprecated
    public int lookupOptionalInt (String key);

    /**
     * lookup an optional int. Returns defaultValue if it isn't given
     * @deprecated use {@link #getOptInt(String, int)} instead
     */
    @Deprecated
    public int lookupOptionalInt (String key, int defaultValue);

}
/* CHECKSTYLE:OFF */
