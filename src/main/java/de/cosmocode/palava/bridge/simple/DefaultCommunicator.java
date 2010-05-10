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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import de.cosmocode.palava.bridge.Content;
import de.cosmocode.palava.bridge.Header;
import de.cosmocode.palava.bridge.call.Call;
import de.cosmocode.palava.bridge.call.CallType;
import de.cosmocode.palava.bridge.command.Command;
import de.cosmocode.palava.bridge.command.CommandException;
import de.cosmocode.palava.bridge.command.CommandManager;
import de.cosmocode.palava.bridge.content.ErrorContent;
import de.cosmocode.palava.bridge.request.HttpRequest;
import de.cosmocode.palava.bridge.request.HttpRequestFactory;
import de.cosmocode.palava.bridge.scope.Scopes;
import de.cosmocode.palava.bridge.session.HttpSession;
import de.cosmocode.palava.bridge.session.HttpSessionManager;
import de.cosmocode.palava.ipc.IpcCallScope;

/**
 * Default implementation of the {@link Communicator} interface.
 *
 * @author Oliver Lorenz
 * @author Willi Schoenborn
 * @author Tilo Baller
 */
final class DefaultCommunicator implements Communicator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCommunicator.class);
    
    private final ProtocolAlgorithm algorithm;
    
    private final HttpSessionManager sessionManager;
    
    private final HttpRequestFactory requestFactory;
    
    private final CommandManager commandManager;
    
    private final IpcCallScope callScope;
    
    @Inject
    public DefaultCommunicator(ProtocolAlgorithm algorithm, HttpSessionManager sessionManager,
        HttpRequestFactory requestFactory, CommandManager commandManager,
        IpcCallScope callScope) {
        this.algorithm = Preconditions.checkNotNull(algorithm, "Algorithm");
        this.sessionManager = Preconditions.checkNotNull(sessionManager, "SessionManager");
        this.requestFactory = Preconditions.checkNotNull(requestFactory, "RequestFactory");
        this.commandManager = Preconditions.checkNotNull(commandManager, "CommandManager");
        this.callScope = Preconditions.checkNotNull(callScope, "CallScope");
    }
    
    private HttpRequest open(InputStream input, OutputStream output) {
        final Header header = algorithm.read(input);
        if (header.getCallType() == CallType.OPEN) {
            final String sessionId = header.getSessionId();
            HttpSession session = sessionManager.get(sessionId);
            LOG.debug("Session found for id {}: {}", sessionId, session);
            if (session == null) {
                session = sessionManager.get();
            } else {
                LOG.debug("Updating access time for {}", sessionId);
                session.updateAccessTime();
            }
            
            final Map<String, String> serverVariable = algorithm.open(header, input, output);
            return requestFactory.create(session, serverVariable);
        } else {
            throw new ProtocolException("First call must be of type OPEN");
        }
    }
    
    @Override
    public void communicate(InputStream input, OutputStream output) {
        final HttpRequest request = open(input, output);
        try {
            while (true) {
                final Header header = algorithm.read(input);
                LOG.debug("Reading header {}", header);
                LOG.debug("Incoming call {}", header.getCallType());
                if (header.getCallType() == CallType.CLOSE) break;
                
                final Content content = process(request, header, input);
                
                try {
                    algorithm.sendTo(content, output);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        } finally {
            request.clear();
        }
    }
    
    private Content process(HttpRequest request, Header header, InputStream input) {
        final String aliasedName = header.getAliasedName();
        
        final Command command;
        
        try {
            command = commandManager.forName(aliasedName);
        /*CHECKSTYLE:OFF*/
        } catch (RuntimeException e) {
        /*CHECKSTYLE:ON*/
            return ErrorContent.create(e);
        }
        
        final Call call;
        
        switch (header.getCallType()) {
            case DATA: {
                call = new SimpleDataCall(request, header, input);
                break;
            }
            case TEXT: {
                call = new SimpleTextCall(request, header, input);
                break;
            }
            case JSON: {
                call = new SimpleJsonCall(request, header, input);
                break;
            }
            case BINARY: {
                call = new SimpleBinaryCall(request, header, input);
                break;
            }
            case OPEN: {
                call = new SimpleJsonCall(request, header, input);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
        
        return filterAndExecute(command, call);
    }
    
    private Content filterAndExecute(final Command command, Call call) {
        callScope.enter(call);
        Scopes.setCurrentCall(call);
        try {
            return command.execute(call);
        /*CHECKSTYLE:OFF*/
        } catch (RuntimeException e) {
        /*CHECKSTYLE:ON*/ 
            LOG.error("Command execution failed", e);
            return ErrorContent.create(e);
        } catch (CommandException e) {
            LOG.error("Command execution failed", e);
            return ErrorContent.create(e);
        } finally {
            try {
                call.discard();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            } finally {
                callScope.exit();
                Scopes.clean();
            }
        }
    }
    
}
