package de.espend.idea.php.toolbox.provider;

import org.jetbrains.annotations.NotNull;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class ClassInterfaceProvider extends ClassProvider {

    @NotNull
    @Override
    public String getName() {
        return "class_interface";
    }

    protected boolean withInterfaces() {
        return true;
    }

}
