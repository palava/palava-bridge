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

package de.cosmocode.palava.bridge.simple;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.inject.internal.Maps;

import de.cosmocode.commons.io.UnclosableInputStream;
import de.cosmocode.palava.bridge.ConnectionLostException;
import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.Arguments;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.request.HttpRequest;
import de.cosmocode.palava.ipc.IpcConnection;
import de.cosmocode.palava.scope.AbstractScopeContext;

/**
 * Abstract implementation of the {@link Call} interface.
 * 
 * @author Tobias Sarnowski
 * @author Willi Schoenborn
 */
abstract class AbstractCall extends AbstractScopeContext implements Call {
    
    protected static final Charset CHARSET = Charset.forName("UTF-8");
    
    private final HttpRequest request;

    private final Header header;
    
    private final InputStream stream;
    
    private int totalBytesRead;
    
    private final Map<Object, Object> context = Maps.newHashMap();

    AbstractCall(HttpRequest request, Header header, InputStream stream) {
        // TODO we need nullchecks here!
//        this.request = Preconditions.checkNotNull(request, "Request");
//        this.command = Preconditions.checkNotNull(command, "Command");
        this.request = request;
        this.stream = new UnclosableInputStream(Preconditions.checkNotNull(stream, "Stream"));
        this.header = Preconditions.checkNotNull(header, "Header");
    }
    
    @Override
    public Arguments getArguments() {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support arguments");
    }
    
    @Override
    public HttpRequest getHttpRequest() {
        return request;
    }
    
    @Override
    public IpcConnection getConnection() {
        return getHttpRequest();
    }
    
    @Override
    protected Map<Object, Object> context() {
        return context;
    }
    
    @Override
    public InputStream getInputStream() {
        return new FilterInputStream(stream) {
            
            @Override
            public int read() throws IOException {
                final int read = super.read();
                totalBytesRead += read;
                return read;
            }
            
            @Override
            public int read(byte[] b) throws IOException {
                final int read = super.read(b);
                totalBytesRead += read;
                return read;
            }
            
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                final int read = super.read(b, off, len);
                totalBytesRead += read;
                return read;
            }
            
        };
    }
    
    @Override
    public Header getHeader() {
        return header;
    }

    /**
     * 
     * @param data
     * @return
     * @throws ConnectionLostException
     */
    protected final byte[] read() throws ConnectionLostException {
        final long max = header.getContentLength();
        final byte[] data = new byte[header.getContentLength()];

        if (totalBytesRead >= max) {
            throw new ConnectionLostException("not allowed to read enough bytes, content-length reached");
        }

        while (totalBytesRead < max) {
            final int read;
            try {
                read = stream.read(data, totalBytesRead, data.length - totalBytesRead);
            } catch (IOException ioe) {
                throw new ConnectionLostException();
            }
            if (read == -1) {
                throw new ConnectionLostException();
            }
            totalBytesRead += read;
        }

        return data;
    }

    @Override
    public final void discard() throws ConnectionLostException, IOException {
        if (totalBytesRead < header.getContentLength()) {
            read();
        }
    }
    
}
