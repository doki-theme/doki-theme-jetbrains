package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class DDLCIcons {

    public static final Icon EXCLUDED = load("/icons/ddlc/modules/ExcludedTreeOpen.svg");
    public static final Icon MODULE = load("/icons/ddlc/modules/ModuleOpen.svg");
    public static final Icon SOURCE = load("/icons/ddlc/modules/sourceRootOpen.svg");
    public static final Icon TEST = load("/icons/ddlc/modules/testRootOpen.svg");

    private static Icon load(final String path) {
        return IconLoader.findIcon(path);
    }
}
