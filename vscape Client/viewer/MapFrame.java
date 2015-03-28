package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   centerX

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MapFrame extends JFrame implements ComponentListener {

    public MapShell mapShell;
    
	int newwidth = 0;
	int newheight = 0;
	
	public void setInitSize(int x, int y)
	{
		newwidth = x;
		newheight = y;
	}

    public MapFrame(MapShell mapShell, int width, int height) {
        this.mapShell = mapShell;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(IResourceLoader.I(704));
        this.setResizable(false);
        this.setVisible(true);
        this.toFront();
        this.setSize(width + 8, height + 28);
        addComponentListener(this);
    }

    public void componentResized(ComponentEvent event)
    {
        /*JOptionPane.showMessageDialog(this,
    			    "JFrame has been resized!",
    			    "JFrame Resize",
    			    JOptionPane.INFORMATION_MESSAGE);*/
    	//System.out.println((this.size().width-28)+","+(this.size().height-8));
    	
    	newheight = (this.size().height-28);
    };
    
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

	@Override
	public void componentMoved(ComponentEvent paramComponentEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent paramComponentEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent paramComponentEvent) {
		// TODO Auto-generated method stub
		
	}
}
