package com.gdeer.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeToCn extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取编辑器model
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);

        final Project project = e.getProject();
        if (null == mEditor || project == null) {
            return;
        }

        // 获取选中区域model
        final SelectionModel selectionModel = mEditor.getSelectionModel();

        selectionModel.selectLineAtCaret();
        String selectedText = selectionModel.getSelectedText();
        selectionModel.removeSelection();

        System.out.println(selectedText);
        if (selectedText == null || selectedText.equals("")) {
            System.out.println("selectedText is null");
            return;
        }

        Pattern p = Pattern.compile("((\\\\u|\\\\\\\\u)(\\p{XDigit}){4})+");
        Matcher m = p.matcher(selectedText);//获取 matcher 对象

        if (!m.find()) {
            System.out.println("no unicode");
            return;
        }

        selectedText = m.group(0);
        System.out.println(selectedText);
        String chinese = Util.unicodeToCn(selectedText);
        System.out.println(chinese);
        showPopupBalloon(mEditor, chinese);
    }

    private void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                System.out.println("6");
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null,
                        new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }
}
