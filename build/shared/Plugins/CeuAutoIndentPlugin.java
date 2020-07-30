import processing.app.Plugin;
import processing.app.PluginAPI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CeuAutoIndentPlugin implements Plugin {
    Pattern pattern;
    private static final String REGEX = "^(?:[\\s]*|.*[\\s]+)((?:do|with|if|then|else)(?=/|[\\s]|$))(?!.*(?: end(?:$|[\\s])|;)).*$";
    
    public CeuAutoIndentPlugin() {
        pattern = Pattern.compile(REGEX);
    }

    @Override
    public void onNewLine(PluginAPI pluginAPI) {
        if (!pluginAPI.getProjectType().contains("ceu")) {
            return;
        }

        String text = pluginAPI.getCurrentTabText();
        int position = pluginAPI.getCaretPosition();
        int start = -1, end = position;
        for (int i = position - 1; i >= 0; i--) {
            if (text.charAt(i) == '\n') {
                start = i;
                break;
            }
        }
        
        String lastline = "";
        try {
            lastline = text.substring(start + 1, end);
        } catch (Exception ex) {
            return;
        }

        if (pattern.matcher(lastline).matches()) {
            pluginAPI.indentNextLine();
        }
    }
}