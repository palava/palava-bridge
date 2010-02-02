package de.cosmocode.palava.bridge.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ExceptionContent extends JsonContent {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionContent.class);

    public <K, V> ExceptionContent(Map<K, V> map) {
        super(map);
    }
}
