package viewer;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   centerY

import java.awt.*;
import java.awt.image.*;

public class ProducingGraphicsBuffer implements ImageProducer, ImageObserver {

    public int pixels[];
    public int width;
    public int height;
    public ColorModel colorModel;
    public ImageConsumer imageConsumer;
    public Image image;

    public ProducingGraphicsBuffer(int width, int height, Component component) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        colorModel = new DirectColorModel(32, 0xff0000, 65280, 255);
        image = component.createImage(this);
        drawPixels();
        component.prepareImage(image, this);
        drawPixels();
        component.prepareImage(image, this);
        drawPixels();
        component.prepareImage(image, this);
        createRasterizer();
    }

    public final void createRasterizer() {
        Graphics2D.createRasterizer(pixels, width, height);
    }

    public final void drawGraphics(Graphics graphics, int i, int j) {
        drawPixels();
        graphics.drawImage(image, i, j, this);
    }

    public final synchronized void drawPixels() {
        if (imageConsumer != null) {
            imageConsumer.setPixels(0, 0, width, height, colorModel, pixels, 0, width);
            imageConsumer.imageComplete(2);
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }

    @Override
    public void addConsumer(ImageConsumer ic) {
        imageConsumer = ic;
        imageConsumer.setDimensions(width, height);
        imageConsumer.setProperties(null);
        imageConsumer.setColorModel(colorModel);
        imageConsumer.setHints(14);
    }

    @Override
    public boolean isConsumer(ImageConsumer ic) {
        return imageConsumer == ic;
    }

    @Override
    public void removeConsumer(ImageConsumer ic) {
        if (imageConsumer == ic)
            imageConsumer = null;
    }

    @Override
    public void startProduction(ImageConsumer ic) {
        addConsumer(ic);
    }

    @Override
    public void requestTopDownLeftRightResend(ImageConsumer ic) {
        System.out.println(IResourceLoader.I(699));
    }
}
