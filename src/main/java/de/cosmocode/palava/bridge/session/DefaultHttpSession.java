/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.bridge.session;

import java.text.Collator;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.cosmocode.json.JSONRenderer;
import de.cosmocode.palava.ipc.AbstractIpcSession;

/**
 * Default implementation of the {@link HttpSession} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultHttpSession extends AbstractIpcSession implements HttpSession {
    
    public static final Logger LOG = LoggerFactory.getLogger(DefaultHttpSession.class);

    private String sessionId;

    private final Map<Object, Object> context = Maps.newHashMap();
    
    private Locale locale;
    
    private NumberFormat format;
    
    private Collator collator;
    
    public DefaultHttpSession(String sessionId) {
        this.sessionId = Preconditions.checkNotNull(sessionId, "SessionId");
    }

    @Override
    protected Map<Object, Object> context() {
        return context;
    }
    
    @Override
    public Locale getLocale() {
        touch();
        final Object langValue = get(LANGUAGE);
        if (locale == null || !locale.getLanguage().equals(langValue)) {
            
            format = null;
            collator = null;
            
            final Object countryValue = get(COUNTRY);
            
            if (langValue instanceof String && StringUtils.isNotBlank(String.class.cast(langValue))) {
                if (countryValue instanceof String && StringUtils.isNotBlank(String.class.cast(countryValue))) {
                    locale = new Locale(String.class.cast(langValue), String.class.cast(countryValue)); 
                } else {
                    locale = new Locale(String.class.cast(langValue));
                }
            } else {
                throw new IllegalStateException("No language found in session");
            }
        }
        return locale;
    }

    @Override
    public NumberFormat getNumberFormat() {
        touch();
        if (format == null) {
            format = NumberFormat.getInstance(getLocale());
        }
        return format;
    }

    @Override
    public Collator getCollator() {
        touch();
        if (collator == null) {
            collator = Collator.getInstance(getLocale());
        }
        return collator;
    }

    @Override
    public void updateAccessTime() {
        touch();
    }

    @Override
    public Date getAccessTime() {
        return lastAccessTime();
    }
    
    @Override
    public String getSessionId() {
        return sessionId;
    }
    
    @Override
    public JSONRenderer renderAsMap(JSONRenderer renderer) {
        return renderer.
            key("id").value(sessionId).
            key("accesstime").value(lastAccessTime()).
            key("data").object(context);
    }

}
