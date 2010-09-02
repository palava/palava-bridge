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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.extension.JSONConstructor;
import org.json.extension.JSONEncoder;

import de.cosmocode.json.JSON;
import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.MimeType;
import de.cosmocode.rendering.Renderer;

/**
 * use the JSONConverter to produce JSON output of java objects.
 * 
 * @deprecated without substitution
 * @author Detlef Hüttemann
 * @author Willi Schoenborn
 */
@Deprecated
@Immutable
public class JsonContent implements Content {

    public static final JsonContent EMPTY;
    
    private static final byte[] NULL = "null".getBytes(CHARSET);
    
    static {
        EMPTY = new JsonContent(new JSONObject());
    }
    
    private final byte[] bytes;
    
    public JsonContent(JSONObject object) {
        bytes = object == null ? NULL : object.toString().getBytes(CHARSET);
    }
    
    public JsonContent(JSONArray array) {
        bytes = array == null ? NULL : array.toString().getBytes(CHARSET);
    }
    
    public JsonContent(Renderer renderer) {
        bytes = renderer == null ? NULL : renderer.build().toString().getBytes(CHARSET);
    }
    
    public JsonContent(JSONConstructor constructor) {
        bytes = constructor == null ? NULL : constructor.toString().getBytes(CHARSET);
    }
    
    public JsonContent(JSONEncoder encoder) {
        bytes = encoder == null ? NULL : JSON.newRenderer().value(encoder).build().toString().getBytes(CHARSET);
    }
    
    public <E> JsonContent(Iterable<E> iterable) {
        bytes = iterable == null ? NULL : JSON.newRenderer().value(iterable).build().toString().getBytes(CHARSET);
    }
    
    public <K, V> JsonContent(Map<K, V> map) {
        bytes = map == null ? NULL : JSON.newRenderer().value(map).build().toString().getBytes(CHARSET);
    }
    
    @Override
    public MimeType getMimeType() {
        return MimeType.JSON;
    }
    
    @Override
    public long getLength() {
        return bytes.length;
    }
    
    @Override
    public void write(OutputStream out) throws IOException {
        out.write(bytes, 0, bytes.length);
    }
    
    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }
    
    @Override
    public String toString() {
        return new String(bytes, CHARSET);
    }
    
}
