package icons;

import io.acari.DDLC.icons.tinted.TintedIconsService;
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

    public static class Windows {
        public static Icon CloseHover = TintedIconsService.getAccentIcon("/icons/windows/closeHover.svg");
    }

    public static class Ide {
        public static Icon Rating = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating.svg");
        public static Icon Rating1 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating1.svg");
        public static Icon Rating2 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating2.svg");
        public static Icon Rating3 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating3.svg");
        public static Icon Rating4 = TintedIconsService.getAccentIcon("/icons/ddlc/ide/rating4.svg");
    }


    public static class Process {
        public static Icon Step1 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_1.svg");
        public static Icon Step2 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_2.svg");
        public static Icon Step3 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_3.svg");
        public static Icon Step4 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_4.svg");
        public static Icon Step5 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_5.svg");
        public static Icon Step6 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_6.svg");
        public static Icon Step7 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_7.svg");
        public static Icon Step8 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_8.svg");
        public static Icon Step9 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_9.svg");
        public static Icon Step10 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_10.svg");
        public static Icon Step11 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_11.svg");
        public static Icon Step12 = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_12.svg");
        public static Icon StepMask = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_mask.svg");
        public static Icon StepPassive = TintedIconsService.getAccentIcon("/icons/ddlc/process/step_passive.svg");

        public static Icon BigStep1 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_1.svg");
        public static Icon BigStep2 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_2.svg");
        public static Icon BigStep3 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_3.svg");
        public static Icon BigStep4 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_4.svg");
        public static Icon BigStep5 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_5.svg");
        public static Icon BigStep6 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_6.svg");
        public static Icon BigStep7 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_7.svg");
        public static Icon BigStep8 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_8.svg");
        public static Icon BigStep9 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_9.svg");
        public static Icon BigStep10 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_10.svg");
        public static Icon BigStep11 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_11.svg");
        public static Icon BigStep12 = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_12.svg");
        public static Icon BigStepMask = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_mask.svg");
        public static Icon BigStepPassive = TintedIconsService.getAccentIcon("/icons/ddlc/process/big/step_passive.svg");
    }

}
