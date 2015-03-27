package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   virtualBottomX

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class MapShell extends Applet
        implements Runnable, MouseListener, MouseMotionListener, KeyListener, FocusListener, WindowListener {

    public final void stop() {
        if (addFocusListener >= 0)
            addFocusListener = 4000 / addKeyListener;
    }

    public void D() {
    }

    public final void I(int i, String s) {
        while (FI == null) {
            FI = J().getGraphics();
            try {
                J().repaint();
            } catch (Exception exception) {
            }
            try {
                Thread.sleep(1000L);
            } catch (Exception exception1) {
            }
        }
        Font font = new Font(IResourceLoader.I(1241), 1, 13);
        FontMetrics fontmetrics = J().getFontMetrics(font);
        Font font1 = new Font(IResourceLoader.I(1241), 0, 13);
        FontMetrics fontmetrics1 = J().getFontMetrics(font1);
        if (exit) {
            FI.setColor(Color.black);
            FI.fillRect(0, 0, destroy, drawRect);
            exit = false;
        }
        Color color = new Color(140, 17, 17);
        int j = drawRect / 2 - 18;
        FI.setColor(color);
        FI.drawRect(destroy / 2 - 152, j, 304, 34);
        FI.fillRect(destroy / 2 - 150, j + 2, i * 3, 30);
        FI.setColor(Color.black);
        FI.fillRect((destroy / 2 - 150) + i * 3, j + 2, 300 - i * 3, 30);
        FI.setFont(font);
        FI.setColor(Color.white);
        FI.drawString(s, (destroy - fontmetrics.stringWidth(s)) / 2, j + 22);
    }

    public final void mouseReleased(MouseEvent mouseevent) {
        getFontMetrics = 0;
        AI = 0;
    }

    public final void keyPressed(KeyEvent keyevent) {
        getFontMetrics = 0;
        int i = keyevent.getKeyCode();
        int j = keyevent.getKeyChar();
        if (j < 30)
            j = 0;
        if (i == 37)
            j = 1;
        if (i == 39)
            j = 2;
        if (i == 38)
            j = 3;
        if (i == 40)
            j = 4;
        if (i == 17)
            j = 5;
        if (i == 8)
            j = 8;
        if (i == 127)
            j = 8;
        if (i == 9)
            j = 9;
        if (i == 10)
            j = 10;
        if (i >= 112 && i <= 123)
            j = (1008 + i) - 112;
        if (i == 36)
            j = 1000;
        if (i == 35)
            j = 1001;
        if (i == 33)
            j = 1002;
        if (i == 34)
            j = 1003;
        if (j > 0 && j < 128)
            MI[j] = 1;
        if (j > 4) {
            isMetaDown[setColor] = j;
            setColor = setColor + 1 & 0x7f;
        }
    }

    public final void addFocusListener(Runnable runnable, int i) {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(i);
    }

    public final void windowClosing(WindowEvent windowevent) {
    	//System.exit(0);
    	
        SI.dispose();
    }

	public final void addKeyListener() {
        addFocusListener = -2;
        Z();
        if (SI != null) {
            try {
                Thread.sleep(1000L);
            } catch (Exception exception) {
            }
            try {
                System.exit(0);
            } catch (Throwable throwable) {
            }
        }
    }

    public final void update(Graphics g) {
        if (FI == null)
            FI = g;
        exit = true;
        D();
    }

    public final void mouseEntered(MouseEvent mouseevent) {
    }

    public final void mouseExited(MouseEvent mouseevent) {
        getFontMetrics = 0;
        EI = -1;
        GI = -1;
    }

    public final void windowOpened(WindowEvent windowevent) {
    }

    public final void windowDeiconified(WindowEvent windowevent) {
    }

    public final void windowActivated(WindowEvent windowevent) {
    }

    public void I() {
    }

    public final void start() {
        if (addFocusListener >= 0)
            addFocusListener = 0;
    }

    public final void I(int i, int j) {
        destroy = i;
        drawRect = j;
        SI = new MapFrame(this, destroy, drawRect);
        FI = J().getGraphics();
        JI = new ProducingGraphicsBuffer(destroy, drawRect, J());
        addFocusListener(this, 1);
    }

    public final int F() {
        int i = -1;
        if (setColor != repaint) {
            i = isMetaDown[repaint];
            repaint = repaint + 1 & 0x7f;
        }
        return i;
    }

    public void B() {
    }

    public final Component J() {
        if (SI != null)
            return SI;
        else
            return this;
    }

    public final void mouseClicked(MouseEvent mouseevent) {
    }

    public final void mousePressed(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        if (SI != null) {
            i -= 4;
            j -= 22;
        }
        getFontMetrics = 0;
        getKeyChar = i;
        getKeyCode = j;
        getX = System.currentTimeMillis();
        if (mouseevent.isMetaDown()) {
            getGraphics = 2;
            AI = 2;
        } else {
            getGraphics = 1;
            AI = 1;
        }
    }

    public final void mouseDragged(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        if (SI != null) {
            i -= 4;
            j -= 22;
        }
        getFontMetrics = 0;
        EI = i;
        GI = j;
    }

    public final void Z(int i, int j) {
        destroy = i;
        drawRect = j;
        FI = J().getGraphics();
        JI = new ProducingGraphicsBuffer(destroy, drawRect, J());
        addFocusListener(this, 1);
    }

    public final void mouseMoved(MouseEvent mouseevent) {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        if (SI != null) {
            i -= 4;
            j -= 22;
        }
        getFontMetrics = 0;
        EI = i;
        GI = j;
    }

    public final void keyTyped(KeyEvent keyevent) {
    }

    public final void windowDeactivated(WindowEvent windowevent) {
    }

    public final void paint(Graphics g) {
        if (FI == null)
            FI = g;
        exit = true;
        D();
    }

	public final void destroy() {
        addFocusListener = -1;
        try {
            //Thread.sleep(5000L);
        } catch (Exception exception) {
        }
        if (addFocusListener == -1)
            addKeyListener();
    }

    public void Z() {
    }

    public MapShell() {
        addFocusListener = 0;
        addKeyListener = 20;
        addMouseListener = 1;
        addMouseMotionListener = new long[10];
        black = 0;
        currentTimeMillis = false;
        drawString = new B[6];
        exit = true;
        fillRect = true;
        getFontMetrics = 0;
        AI = 0;
        EI = 0;
        GI = 0;
        getGraphics = 0;
        getKeyChar = 0;
        getKeyCode = 0;
        getX = 0L;
        HI = 0;
        KI = 0;
        LI = 0;
        getY = 0L;
        MI = new int[128];
        isMetaDown = new int[128];
        repaint = 0;
        setColor = 0;
    }

    public void C() {
    }

    public final void focusLost(FocusEvent focusevent) {
        fillRect = false;
        for (int i = 0; i < 128; i++)
            MI[i] = 0;

    }

    public final void keyReleased(KeyEvent keyevent) {
        getFontMetrics = 0;
        int i = keyevent.getKeyCode();
        char c = keyevent.getKeyChar();
        if (c < '\036')
            c = '\0';
        if (i == 37)
            c = '\001';
        if (i == 39)
            c = '\002';
        if (i == 38)
            c = '\003';
        if (i == 40)
            c = '\004';
        if (i == 17)
            c = '\005';
        if (i == 8)
            c = '\b';
        if (i == 127)
            c = '\b';
        if (i == 9)
            c = '\t';
        if (i == 10)
            c = '\n';
        if (c > 0 && c < '\200')
            MI[c] = 0;
    }

    public final void windowClosed(WindowEvent windowevent) {
    }

    public final void run() {
        J().addMouseListener(this);
        J().addMouseMotionListener(this);
        J().addKeyListener(this);
        J().addFocusListener(this);
        if (SI != null)
            SI.addWindowListener(this);
        I(0, IResourceLoader.I(730));
        I();
        int i = 0;
        int j = 256;
        int k = 1;
        int l = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < 10; j1++)
            addMouseMotionListener[j1] = System.currentTimeMillis();

        do {
            if (addFocusListener < 0)
                break;
            if (addFocusListener > 0) {
                addFocusListener--;
                if (addFocusListener == 0) {
                    addKeyListener();
                    return;
                }
            }
            int k1 = j;
            int l1 = k;
            j = 300;
            k = 1;
            long l2 = System.currentTimeMillis();
            if (addMouseMotionListener[i] == 0L) {
                j = k1;
                k = l1;
            } else if (l2 > addMouseMotionListener[i])
                j = (int) ((long) (2560 * addKeyListener) / (l2 - addMouseMotionListener[i]));
            if (j < 25)
                j = 25;
            if (j > 256) {
                j = 256;
                k = (int) ((long) addKeyListener - (l2 - addMouseMotionListener[i]) / 10L);
            }
            if (k > addKeyListener)
                k = addKeyListener;
            addMouseMotionListener[i] = l2;
            i = (i + 1) % 10;
            if (k > 1) {
                for (int i2 = 0; i2 < 10; i2++)
                    if (addMouseMotionListener[i2] != 0L)
                        addMouseMotionListener[i2] += k;

            }
            if (k < addMouseListener)
                k = addMouseListener;
            try {
                Thread.sleep(k);
            } catch (InterruptedException interruptedexception) {
                i1++;
            }
            for (; l < 256; l += j) {
                HI = getGraphics;
                KI = getKeyChar;
                LI = getKeyCode;
                getY = getX;
                getGraphics = 0;
                C();
                repaint = setColor;
            }

            l &= 0xff;
            if (addKeyListener > 0)
                black = (1000 * j) / (addKeyListener * 256);
            B();
            if (currentTimeMillis) {
                System.out.println((new StringBuilder()).append(IResourceLoader.I(741)).append(l2).toString());
                for (int j2 = 0; j2 < 10; j2++) {
                    int k2 = ((i - j2 - 1) + 20) % 10;
                    System.out.println((new StringBuilder()).append(IResourceLoader.I(748)).append(k2).append(IResourceLoader.I(753)).append(addMouseMotionListener[k2]).toString());
                }

                System.out.println((new StringBuilder()).append(IResourceLoader.I(755)).append(black).append(IResourceLoader.I(760)).append(j).append(IResourceLoader.I(768)).append(l).toString());
                System.out.println((new StringBuilder()).append(IResourceLoader.I(776)).append(k).append(IResourceLoader.I(781)).append(addKeyListener).append(IResourceLoader.I(791)).append(addMouseListener).toString());
                System.out.println((new StringBuilder()).append(IResourceLoader.I(800)).append(i1).append(IResourceLoader.I(807)).append(i).toString());
                currentTimeMillis = false;
                i1 = 0;
            }
        } while (true);
        if (addFocusListener == -1)
            addKeyListener();
    }

    public final void focusGained(FocusEvent focusevent) {
        fillRect = true;
        exit = true;
        D();
    }

    public final void windowIconified(WindowEvent windowevent) {
    }

    public int addFocusListener;
    public int addKeyListener;
    public int addMouseListener;
    public long addMouseMotionListener[];
    public int black;
    public boolean currentTimeMillis;
    public int destroy;
    public int drawRect;
    public Graphics FI;
    public ProducingGraphicsBuffer JI;
    public B drawString[];
    public MapFrame SI;
    public boolean exit;
    public boolean fillRect;
    public int getFontMetrics;
    public int AI;
    public int EI;
    public int GI;
    public int getGraphics;
    public int getKeyChar;
    public int getKeyCode;
    public long getX;
    public int HI;
    public int KI;
    public int LI;
    public long getY;
    public int MI[];
    public int isMetaDown[];
    public int repaint;
    public int setColor;
}
