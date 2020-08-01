import processing.app.Plugin;
import processing.app.PluginAPI;
import java.util.regex.Pattern;

public class CeuAutoFormatPlugin implements Plugin {

    static final int START = 0, END = 1, MIDDLE = 2, NORMAL = 3;

    public CeuAutoFormatPlugin() {
    }

    @Override
    public void onFormat(PluginAPI pluginAPI) {
        if (!pluginAPI.getProjectType().contains("ceu")) {
            return;
        }

        String text = pluginAPI.getCurrentTabText();
        // TODO: grab tab size from preferences
        String newText = "";
        String line = "";
        String indentation = "    ";
        int level = 0;
        for (int index = 0; index <= text.length(); index++) {
            if (index < text.length() && text.charAt(index) != '\n') {
                line += text.charAt(index);
            } else {
                line = line.replaceAll("^\\s+", "");
                String code = reformatLine(line);
                // System.out.print(code + "  ");
                // System.out.println(checkType(code));
                switch (checkType(code)) {
                    case START:
                        newText += makeLine(line, level, indentation);
                        level++;
                        break;
                    case END:
                        level = level > 0 ? level - 1 : 0;
                        newText += makeLine(line, level, indentation);
                        break;
                    case MIDDLE:
                        int middleLevel = level > 0 ? level - 1 : 0;
                        newText += makeLine(line, middleLevel, indentation);
                        break;
                    case NORMAL:
                    default:
                        newText += makeLine(line, level, indentation);
                        break;
                }

                line = "";
            }
        }

        pluginAPI.setCurrentTabText(newText);
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

    private String makeLine(String line, int level, String indentation) {
        String newLine = "";
        for (int i = 0; i < level; i++) {
            newLine += indentation;
        }
        newLine += line + '\n';
        return newLine;
    }
}