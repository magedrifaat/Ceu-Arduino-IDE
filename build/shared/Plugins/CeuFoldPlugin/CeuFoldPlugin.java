import processing.app.Plugin;
import processing.app.PluginAPI;

import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldType;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;

public class CeuFoldPlugin implements Plugin {
    public CeuFoldPlugin() {
    }

    @Override
    public void onSide(PluginAPI pluginAPI) {
        if (!pluginAPI.getProjectType().contains("ceu")) {
            return;
        }

        pluginAPI.addFoldParser(new CeuFoldParser());
    }
}

class CeuFoldParser implements FoldParser {
    static final int START = 0, END = 1, MIDDLE = 2, NORMAL = 3;

    public CeuFoldParser() {
    }

    @Override
    public List<Fold> getFolds(RSyntaxTextArea textArea) {
        ArrayList<Fold> folds = new ArrayList<>();
        ArrayList<Fold> tempFolds = new ArrayList<>();
        String text = textArea.getText();
        String line = "";
        for (int index = 0; index <= text.length(); index++) {
            if (index < text.length() && text.charAt(index) != '\n') {
                line += text.charAt(index);
            } else {
                String code = reformatLine(line);
                switch (checkType(code)) {
                    case START:
                        try {
                            if (tempFolds.size() > 0) {
                                Fold parentFold = tempFolds.get(tempFolds.size() - 1);
                                tempFolds.add(parentFold.createChild(FoldType.CODE, index - 1));
                            } else {
                                tempFolds.add(new Fold(FoldType.CODE, textArea, index - 1));
                            }
                        } catch (BadLocationException ex) {
                            // Ignore
                        }
                        break;
                    case END:
                        try {
                            if (tempFolds.size() > 0) {
                                if (tempFolds.size() == 1) {
                                    Fold highLevelFold = tempFolds.get(0);
                                    highLevelFold.setEndOffset(index - line.length() - 1);
                                    folds.add(highLevelFold);
                                    tempFolds.remove(highLevelFold);
                                } else {
                                    Fold fold = tempFolds.get(tempFolds.size() - 1);
                                    fold.setEndOffset(index - line.length() - 1);
                                    tempFolds.remove(tempFolds.size() - 1);
                                }
                            }
                        } catch (BadLocationException ex) {
                            // Ignore
                        }
                        break;
                    case MIDDLE:
                        try {
                            if (tempFolds.size() > 0) {
                                if (tempFolds.size() == 1) {
                                    Fold highLevelFold = tempFolds.get(0);
                                    highLevelFold.setEndOffset(index - line.length() - 1);
                                    folds.add(highLevelFold);
                                    tempFolds.remove(highLevelFold);
                                } else {
                                    Fold fold = tempFolds.get(tempFolds.size() - 1);
                                    fold.setEndOffset(index - line.length() - 1);
                                    tempFolds.remove(tempFolds.size() - 1);
                                }
                            }
                            if (tempFolds.size() > 0) {
                                Fold parentFold = tempFolds.get(tempFolds.size() - 1);
                                tempFolds.add(parentFold.createChild(FoldType.CODE, index - 1));
                            } else {
                                tempFolds.add(new Fold(FoldType.CODE, textArea, index - 1));
                            }
                        } catch (BadLocationException ex) {
                            // Ignore
                        }
                        break;
                    case NORMAL:
                    default:
                        break;
                }

                line = "";
            }
        }

        return folds;
    }

    private String reformatLine(String line) {
        int commentIndex = line.indexOf("//");
        if (commentIndex != -1) {
            return line.substring(0, commentIndex);
        } else {
            return line;
        }
    }

    private int checkType(String line) {
        int type = NORMAL;
        if (checkMiddle(line, "do", "with", "end")
                || checkMiddle(line, "then", "else", "end")
                || checkMiddle(line, "if", "then", "end")) {
            type = MIDDLE;
        } else if (checkStart(line, "do", "end") || checkStart(line, "if", "end")) {
            type = START;
        } else if (checkEnd(line, "do", "end")) {
            type = END;
        }
        
        return type;
    }

    private boolean checkStart(String line, String start, String end) {
        String pattern = "^(?:[\\s]*|.*[\\s]+)(?:" + start + ")(?=/|[\\s]|$)(?!.*(?: " + end + "(?:;|$|[\\s]))).*$";
        return Pattern.compile(pattern).matcher(line).matches();
    }

    private boolean checkMiddle(String line, String start, String middle, String end) {
        String pattern = "^(?:[\\s]*|.*[\\s]+)" + start + "(?:/|[\\s]).* " + middle + "(?=/|[\\s]|$).*$";
        return checkStart(line, middle, end) && !Pattern.compile(pattern).matcher(line).matches();
    }

    private boolean checkEnd(String line, String start, String end) {
        String patternExists = "^(?:[\\s]*|.*[\\s]+)" + end + "(?=;|[\\s]|$).*$";
        String patternPreceded = "^(?:[\\s]*|.*[\\s]+)" + start + "(?:/|[\\s]).* " + end + "(?=;|[\\s]|$).*$";
        return Pattern.compile(patternExists).matcher(line).matches()
                && !Pattern.compile(patternPreceded).matcher(line).matches();
    }
}