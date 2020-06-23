/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  Part of the Processing project - http://processing.org

  Copyright (c) 2004-08 Ben Fry and Casey Reas
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

import processing.app.helpers.Keys;
import processing.app.helpers.OSUtils;
import processing.app.helpers.SimpleAction;
import processing.app.tools.MenuScroller;
import static processing.app.I18n.tr;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

import static processing.app.Theme.scale;

/**
 * Sketch tabs at the top of the editor window.
 */
@SuppressWarnings("serial")
public class SideBarHeader extends JComponent {
  static Color backgroundColor;
  static Color textColor[] = new Color[2];

  Editor editor;
  ArrayList<String> tabNames;

  int tabTop[];
  int tabBottom[];

  Font font;
  FontMetrics metrics;
  int fontAscent;

  JMenu menu;
  JPopupMenu popup;

  int menuTop;
  int menuBottom;

  //

  static final String STATUS[] = { "unsel", "sel" };
  static final int UNSELECTED = 0;
  static final int SELECTED = 1;

  static final String WHERE[] = { "left", "mid", "right" };
  static final int LEFT = 0;
  static final int MIDDLE = 1;
  static final int RIGHT = 2;

  static final int PIECE_WIDTH = scale(33);
  static final int PIECE_HEIGHT = scale(4);

  // value for the size bars, buttons, etc
  // TODO: Should be a Theme value?
  static final int GRID_SIZE = 33;

  static Image[][] pieces;
  static Image menuButtons[];

  Image offscreen;
  int sizeW, sizeH;
  int imageW, imageH;

  public class Actions {
    public final Action prevTab = new SimpleAction(tr("Previous Tab"),
        Keys.ctrlAlt(KeyEvent.VK_LEFT), () -> editor.selectPrevSideTab());

    public final Action nextTab = new SimpleAction(tr("Next Tab"),
        Keys.ctrlAlt(KeyEvent.VK_RIGHT), () -> editor.selectNextSideTab());

    Actions() {
      // Explicitly bind keybindings for the actions with accelerators above
      // Normally, this happens automatically for any actions bound to menu
      // items, but only for menus attached to a window, not for popup menus.
      Keys.bind(SideBarHeader.this, prevTab);
      Keys.bind(SideBarHeader.this, nextTab);

      // Add alternative keybindings to switch tabs
      Keys.bind(SideBarHeader.this, prevTab, Keys.ctrlShift(KeyEvent.VK_TAB));
      Keys.bind(SideBarHeader.this, nextTab, Keys.ctrl(KeyEvent.VK_TAB));
    }
  }
  public Actions actions = new Actions();

  /**
   * Called whenever we, or any of our ancestors, is added to a container.
   */
  public void addNotify() {
    super.addNotify();
    /*
     * Once we get added to a window, remove Ctrl-Tab and Ctrl-Shift-Tab from
     * the keys used for focus traversal (so our bindings for these keys will
     * work). All components inherit from the window eventually, so this should
     * work whenever the focus is inside our window. Some components (notably
     * JTextPane / JEditorPane) keep their own focus traversal keys, though, and
     * have to be treated individually (either the same as below, or by
     * disabling focus traversal entirely).
     */
    Window window = SwingUtilities.getWindowAncestor(this);
    if (window != null) {
      Keys.killFocusTraversalBinding(window, Keys.ctrl(KeyEvent.VK_TAB));
      Keys.killFocusTraversalBinding(window, Keys.ctrlShift(KeyEvent.VK_TAB));
    }
  }

  public SideBarHeader(Editor ed, ArrayList<String> _tabNames) {
    this.editor = ed; // weird name for listener
    this.tabNames = _tabNames;

    if (pieces == null) {
      pieces = new Image[STATUS.length][WHERE.length];
      menuButtons = new Image[STATUS.length];
      for (int i = 0; i < STATUS.length; i++) {
        for (int j = 0; j < WHERE.length; j++) {
          String path = "tab-" + STATUS[i] + "-" + WHERE[j];
          pieces[i][j] = Theme.getThemeImage(path, this, PIECE_WIDTH,
                                             PIECE_HEIGHT);
        }
        String path = "tab-" + STATUS[i] + "-menu";
        menuButtons[i] = Theme.getThemeImage(path, this, PIECE_HEIGHT,
                                             PIECE_HEIGHT);
      }
    }

    if (backgroundColor == null) {
      backgroundColor =
        Theme.getColor("header.bgcolor");
      textColor[SELECTED] =
        Theme.getColor("header.text.selected.color");
      textColor[UNSELECTED] =
        Theme.getColor("header.text.unselected.color");
    }

    addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          int x = e.getX();
          int y = e.getY();

          if ((y > menuTop) && (y < menuBottom)) {
            popup.show(SideBarHeader.this, x, y);

          } else {
            int numTabs = tabNames.size();
            for (int i = 0; i < numTabs; i++) {
              if ((y > tabTop[i]) && (y < tabBottom[i])) {
                editor.selectSideTab(i);
                repaint();
              }
            }
          }
        }
      });
  }


  public void paintComponent(Graphics screen) {
    if (screen == null) return;

    SketchController sketch = editor.getSketchController();
    if (sketch == null) return;  // ??

    Dimension size = getSize();
    if ((size.width != sizeW) || (size.height != sizeH)) {
      // component has been resized

      if ((size.width > imageW) || (size.height > imageH)) {
        // nix the image and recreate, it's too small
        offscreen = null;

      } else {
        // who cares, just resize
        sizeW = size.width;
        sizeH = size.height;
      }
    }

    if (offscreen == null) {
      sizeW = size.width;
      sizeH = size.height;
      imageW = sizeW;
      imageH = sizeH;
      offscreen = createImage(imageW, imageH);
    }

    Graphics2D g = Theme.setupGraphics2D(offscreen.getGraphics());
    if (font == null) {
      font = Theme.getFont("header.text.font");
    }
    g.setFont(font);  // need to set this each time through
    metrics = g.getFontMetrics();
    fontAscent = metrics.getAscent();

    // set the background for the offscreen
    g.setColor(backgroundColor);
    g.fillRect(0, 0, imageW, imageH);

    int codeCount = tabNames.size();
    if ((tabTop == null) || (tabTop.length < codeCount)) {
      tabTop = new int[codeCount];
      tabBottom = new int[codeCount];
    }

    int y = scale(6); // offset from Top edge of the component
    int i = 0;
    for (String tabName : tabNames) {
      
      String text = "  " + tabName;

      int textWidth = (int)
        font.getStringBounds(text, g.getFontRenderContext()).getWidth();

      int pieceCount = 2 + (textWidth / PIECE_HEIGHT);
      int pieceHeight = pieceCount * PIECE_HEIGHT;

      int state = (i == editor.getCurrentSideTabIndex()) ? SELECTED : UNSELECTED;
      g.drawImage(pieces[state][LEFT], 0, y, null);
      y += PIECE_HEIGHT;

      int contentTop = y;
      tabTop[i] = y;
      for (int j = 0; j < pieceCount; j++) {
        g.drawImage(pieces[state][MIDDLE], 0, y, null);
        y += PIECE_HEIGHT;
      }
      tabBottom[i] = y;
      int textTop = contentTop + (pieceHeight - textWidth) / 2;

      g.setColor(textColor[state]);
      int baseline = (sizeW + fontAscent) / 2;
      //g.drawString(sketch.code[i].name, textLeft, baseline);
      g.drawString(text, baseline, textTop);

      g.drawImage(pieces[state][RIGHT], 0, y, null);
      y += PIECE_HEIGHT - 1;  // overlap by 1 pixel
      i++;
    }

    menuTop = sizeH - (16 + menuButtons[0].getHeight(this));
    menuBottom = sizeH - 16;
    // draw the dropdown menu target
    g.drawImage(menuButtons[popup.isVisible() ? SELECTED : UNSELECTED],
                0, menuTop, null);

    screen.drawImage(offscreen, 0, 0, null);
  }


  /**
   * Called when a new sketch is opened.
   */
  public void rebuild() {
    //System.out.println("rebuilding editor header");
    rebuildMenu();
    repaint();
    Toolkit.getDefaultToolkit().sync();
  }


  public void rebuildMenu() {
    if (menu != null) {
      menu.removeAll();

    } else {
      menu = new JMenu();
      MenuScroller.setScrollerFor(menu);
      popup = menu.getPopupMenu();
      popup.setLightWeightPopupEnabled(true);
    }
    JMenuItem item;

    menu.add(new JMenuItem(actions.prevTab));
    menu.add(new JMenuItem(actions.nextTab));

    if (tabNames != null) {
      menu.addSeparator();

      int i = 0;
      for (String tab : tabNames) {
        final int index = i++;
        item = new JMenuItem(tab);
        item.addActionListener((ActionEvent e) -> {
          editor.selectSideTab(index);
        });
        menu.add(item);
      }
    }
  }


  public Dimension getPreferredSize() {
    return getMinimumSize();
  }


  public Dimension getMinimumSize() {
    Dimension size = scale(new Dimension(GRID_SIZE, 300));
    if (OSUtils.isMacOS())
      size.width--;
    return size;
  }


  public Dimension getMaximumSize() {
    Dimension size = scale(new Dimension(GRID_SIZE, 30000));
    if (OSUtils.isMacOS())
      size.width--;
    return size;
  }
}
