/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2004-06 Ben Fry and Casey Reas
  Copyright (c) 2001-04 Massachusetts Institute of Technology

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation,
  Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package processing.app;

import processing.app.helpers.OSUtils;
import cc.arduino.ConsoleOutputStream;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.IOException;

import static processing.app.Theme.scale;
import processing.app.helpers.Keys;

/**
 * Message console that sits below the editing area.
 */
public class EditorConsole extends JScrollPane {

  private static ConsoleOutputStream out;
  private static ConsoleOutputStream err;
  private static OutputStream procInput;

  private static synchronized void init(SimpleAttributeSet outStyle, PrintStream outStream, SimpleAttributeSet errStyle, PrintStream errStream) {
    if (out != null) {
      return;
    }

    out = new ConsoleOutputStream(outStyle, outStream);
    System.setOut(new PrintStream(out, true));

    err = new ConsoleOutputStream(errStyle, errStream);
    System.setErr(new PrintStream(err, true));
  }

  public static void setCurrentEditorConsole(EditorConsole console) {
    out.setCurrentEditorConsole(console);
    err.setCurrentEditorConsole(console);
  }

  public static void setProcInputStream(OutputStream stream) {
    procInput = stream;
  }

  private final DefaultStyledDocument document;
  private final JTextPane consoleTextPane;

  private FocusListener focusListener;
  private KeyListener keyListener;
  private CaretListener caretListener;
  private int fixedTextOffset;
  private String userInput;

  private SimpleAttributeSet stdOutStyle;
  private SimpleAttributeSet stdErrStyle;


  public EditorConsole(Base base) {
    document = new DefaultStyledDocument();

    consoleTextPane = new JTextPane(document);
    consoleTextPane.setEditable(false);
    DefaultCaret caret = (DefaultCaret) consoleTextPane.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    consoleTextPane.setFocusTraversalKeysEnabled(false);

    Color backgroundColour = Theme.getColor("console.color");
    consoleTextPane.setBackground(backgroundColour);
    consoleTextPane.setCaretColor(Theme.getColor("console.output.color"));

    Font consoleFont = Theme.getFont("console.font");
    Font editorFont = PreferencesData.getFont("editor.font");
    Font actualFont = new Font(consoleFont.getName(), consoleFont.getStyle(), scale(editorFont.getSize()));

    stdOutStyle = new SimpleAttributeSet();
    StyleConstants.setForeground(stdOutStyle, Theme.getColor("console.output.color"));
    StyleConstants.setBackground(stdOutStyle, backgroundColour);
    StyleConstants.setFontSize(stdOutStyle, actualFont.getSize());
    StyleConstants.setFontFamily(stdOutStyle, actualFont.getFamily());
    StyleConstants.setBold(stdOutStyle, actualFont.isBold());
    StyleConstants.setItalic(stdOutStyle, actualFont.isItalic());

    consoleTextPane.setParagraphAttributes(stdOutStyle, true);
    
    stdErrStyle = new SimpleAttributeSet();
    StyleConstants.setForeground(stdErrStyle, Theme.getColor("console.error.color"));
    StyleConstants.setBackground(stdErrStyle, backgroundColour);
    StyleConstants.setFontSize(stdErrStyle, actualFont.getSize());
    StyleConstants.setFontFamily(stdErrStyle, actualFont.getFamily());
    StyleConstants.setBold(stdErrStyle, actualFont.isBold());
    StyleConstants.setItalic(stdErrStyle, actualFont.isItalic());

    JPanel noWrapPanel = new JPanel(new BorderLayout());
    noWrapPanel.add(consoleTextPane);

    setViewportView(noWrapPanel);
    getVerticalScrollBar().setUnitIncrement(7);

    // calculate height of a line of text in pixels
    // and size window accordingly
    FontMetrics metrics = getFontMetrics(actualFont);
    int height = metrics.getAscent() + metrics.getDescent();
    int lines = PreferencesData.getInteger("console.lines");
    setPreferredSize(new Dimension(100, (height * lines)));
    setMinimumSize(new Dimension(100, (height * lines)));

    EditorConsole.init(stdOutStyle, System.out, stdErrStyle, System.err);

    fixedTextOffset = 0;
    userInput = "";
    initializeListeners();

    // Add font size adjustment listeners.
    base.addEditorFontResizeListeners(consoleTextPane);
  }

  private void initializeListeners() {
    focusListener = new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        consoleTextPane.getCaret().setVisible(true);
      }
      
      @Override
      public void focusLost(FocusEvent e) {
        consoleTextPane.getCaret().setVisible(false);
      }
    };
    
    Keys.killBinding(consoleTextPane, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
    // TODO: change keyListener to key bindings
    keyListener = new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          try {
            userInput = document.getText(fixedTextOffset, document.getLength() - fixedTextOffset) + "\n";
            // Remove user input as it will be echoed by the process in windows
            if (OSUtils.isWindows()) {
              document.remove(fixedTextOffset, userInput.length() - 1);
            } else {
              insertString("\n", stdOutStyle);
            }
            // Send user input to the active process
            if (procInput != null) {
              procInput.write(userInput.getBytes());
            }
          }
          catch (BadLocationException ex) {
            // Ignore
          }
          catch (IOException ioexp) {
            // Failed to write, Ignore
          }
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
          int position = consoleTextPane.getCaretPosition();
          if (position == fixedTextOffset) {
            try {
              // Insert dummy character to be deleted
              document.insertString(position, "A", stdOutStyle);
            } catch (BadLocationException ex) {
              // Ignore
            }
          }
        }
      }
    };

    caretListener = new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        consoleTextPane.setEditable(e.getDot() >= fixedTextOffset);
      }
    };
  }

  public void applyPreferences() {

    // Update the console text pane font from the preferences.
    Font consoleFont = Theme.getFont("console.font");
    Font editorFont = PreferencesData.getFont("editor.font");
    Font actualFont = new Font(consoleFont.getName(), consoleFont.getStyle(), scale(editorFont.getSize()));

    AttributeSet stdOutStyleOld = stdOutStyle.copyAttributes();
    AttributeSet stdErrStyleOld = stdErrStyle.copyAttributes();
    StyleConstants.setFontSize(stdOutStyle, actualFont.getSize());
    StyleConstants.setFontSize(stdErrStyle, actualFont.getSize());

    // Re-insert console text with the new preferences if there were changes.
    // This assumes that the document has single-child paragraphs (default).
    if (!stdOutStyle.isEqual(stdOutStyleOld) || !stdErrStyle.isEqual(stdOutStyleOld)) {
      out.setAttibutes(stdOutStyle);
      err.setAttibutes(stdErrStyle);

      int start;
      for (int end = document.getLength() - 1; end >= 0; end = start - 1) {
        Element elem = document.getParagraphElement(end);
        start = elem.getStartOffset();
        AttributeSet attrs = elem.getElement(0).getAttributes();
        AttributeSet newAttrs;
        if (attrs.isEqual(stdErrStyleOld)) {
          newAttrs = stdErrStyle;
        } else if (attrs.isEqual(stdOutStyleOld)) {
          newAttrs = stdOutStyle;
        } else {
          continue;
        }
        try {
          String text = document.getText(start, end - start);
          document.remove(start, end - start);
          document.insertString(start, text, newAttrs);
        } catch (BadLocationException e) {
          // Should only happen when text is async removed (through clear()).
          // Accept this case, but throw an error when text could mess up.
          if (document.getLength() != 0) {
            throw new Error(e);
          }
        }
      }
    }
  }

  public void clear() {
    try {
      document.remove(0, document.getLength());
      fixedTextOffset = 0;
    } catch (BadLocationException e) {
      // ignore the error otherwise this will cause an infinite loop
      // maybe not a good idea in the long run?
    }
  }

  public void scrollDown() {
    getHorizontalScrollBar().setValue(0);
    getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum());
  }

  public boolean isEmpty() {
    return document.getLength() == 0;
  }

  public void insertString(String line, SimpleAttributeSet attributes) throws BadLocationException {
    line = line.replace("\r\n", "\n").replace("\r", "\n");
    int offset = document.getLength();
    document.insertString(offset, line, attributes);
    fixedTextOffset = document.getLength();
    consoleTextPane.setCaretPosition(document.getLength());
  }

  public String getText() {
    return consoleTextPane.getText().trim();
  }
  
  public void enableUserInput() {
    consoleTextPane.addFocusListener(focusListener);
    consoleTextPane.addCaretListener(caretListener);
    consoleTextPane.addKeyListener(keyListener);
    consoleTextPane.requestFocusInWindow();
    
  }
  
  public void disableUserInput() {
    consoleTextPane.removeFocusListener(focusListener);
    consoleTextPane.removeCaretListener(caretListener);
    consoleTextPane.removeKeyListener(keyListener);
    consoleTextPane.setEditable(false);
    // Remove focus from text pane to hide caret
    this.requestFocusInWindow();
  }

}
