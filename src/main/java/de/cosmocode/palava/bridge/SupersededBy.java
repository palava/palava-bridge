package de.cosmocode.palava.bridge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.cosmocode.palava.bridge.command.Job;
import de.cosmocode.palava.ipc.IpcCommand;

/**
 * {@link Job}s annotated with this annotation are superseded
 * by a newer version with the same api.
 *
 * @since 
 * @author Willi Schoenborn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("deprecation")
public @interface SupersededBy {

    Class<? extends IpcCommand> value();
    
}
