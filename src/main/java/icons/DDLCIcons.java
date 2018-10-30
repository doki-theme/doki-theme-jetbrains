package icons;

import com.chrisrm.idea.icons.tinted.TintedIconsService;
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


    public static class Actions {
        public static Icon CloseHovered = TintedIconsService.getAccentIcon("/icons/ddlc/actions/closeHovered.svg");
        public static Icon CloseNewHovered = TintedIconsService.getAccentIcon("/icons/ddlc/actions/closeNewHovered.svg");
    }

    public static class Ide {
        public static Icon Rating = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating.svg");
        public static Icon Rating1 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating1.svg");
        public static Icon Rating2 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating2.svg");
        public static Icon Rating3 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating3.svg");
        public static Icon Rating4 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating4.svg");
    }
}
