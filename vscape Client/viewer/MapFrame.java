package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   centerX

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

class WindowEventHandler extends WindowAdapter {
	  public void windowClosing(WindowEvent evt) {
	    //System.exit(0);
	  }
	}

public class MapFrame extends JFrame {

    public MapShell mapShell;

    public MapFrame(MapShell mapShell, int width, int height) {
        this.mapShell = mapShell;

        this.addWindowListener(new WindowEventHandler());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle(IResourceLoader.I(704));
        this.setResizable(false);
        this.setVisible(true);
        
        //this.toFront();
        this.setSize(width + 8, height + 28);
    }

    @Override
    public final Graphics getGraphics() {
        Graphics graphics = super.getGraphics();
        graphics.translate(4, 24);
        return graphics;
    }

    @Override
    public final void update(Graphics graphics) {
        mapShell.update(graphics);
    }

    @Override
    public final void paint(Graphics graphics) {
        mapShell.paint(graphics);
    }
}
