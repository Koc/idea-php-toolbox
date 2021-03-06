package de.espend.idea.php.toolbox.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.twig.TwigFileType;
import com.jetbrains.twig.TwigTokenTypes;
import de.espend.idea.php.toolbox.extension.PhpToolboxProviderInterface;
import de.espend.idea.php.toolbox.navigation.dict.PhpToolboxDeclarationHandlerParameter;
import de.espend.idea.php.toolbox.utils.RegistrarMatchUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class PhpToolboxGotoDeclarationHandler implements GotoDeclarationHandler {

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int i, Editor editor) {

        FileType fileType = psiElement.getContainingFile().getFileType();
        if(!(fileType instanceof PhpFileType) && !(fileType instanceof TwigFileType)) {
            return new PsiElement[0];
        }

        Collection<PhpToolboxProviderInterface> providers = RegistrarMatchUtil.getProviders(psiElement);
        if(providers.size() == 0) {
            return new PsiElement[0];
        }

        Collection<PsiElement> targets = new ArrayList<PsiElement>();
        for (PhpToolboxProviderInterface provider : providers) {

            String selectedItem = null;
            if(psiElement.getNode().getElementType() == TwigTokenTypes.STRING_TEXT) {
                // twig language
                selectedItem = psiElement.getText();
            } else {
                // php language

                PsiElement stringLiteral = psiElement.getParent();
                if(stringLiteral instanceof StringLiteralExpression) {
                    selectedItem = ((StringLiteralExpression) stringLiteral).getContents();
                }
            }

            if(selectedItem != null && StringUtils.isNotBlank(selectedItem)) {
                targets.addAll(provider.getPsiTargets(new PhpToolboxDeclarationHandlerParameter(psiElement, selectedItem)));
            }

        }

        return targets.toArray(new PsiElement[targets.size()]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }

}
