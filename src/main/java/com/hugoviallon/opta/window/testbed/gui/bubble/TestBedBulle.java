package com.hugoviallon.opta.window.testbed.gui.bubble;

import com.hugoviallon.opta.model.Machine;
import com.hugoviallon.opta.window.testbed.gui.TestBedConstant;
import java.awt.Color;
import java.awt.Graphics;

public abstract class TestBedBulle {

    final int x;
    final int y;
    final int height;
    final int width;


    public TestBedBulle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public abstract String getLabel();

    public abstract int getNumberOfPixelsForRounding();

    public void draw(Graphics g) {
        String text = getLabel();

        g.setColor(getColor());
        g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), getNumberOfPixelsForRounding(), getNumberOfPixelsForRounding());
        if(getBorderWidth()>0) {
            g.setColor(Color.BLACK);
            g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), getNumberOfPixelsForRounding(), getNumberOfPixelsForRounding());
        }
        g.setColor(Color.BLACK);
        g.drawString(text, getX() + TestBedConstant.DELTA_X_TEXT, getY() + TestBedConstant.DELTA_Y_TEXT);
    }

    protected int getBorderWidth(){
       return 2;
    };

    public abstract Color getColor();

    public abstract Machine getEquipement();

    public void drawBorder(Graphics g, int width, Color color) {
        g.setColor(color);
        for (int i = 0; i < width; i++) {
            g.drawRoundRect(getX() + i, getY() + i, getWidth() - 2 * i, getHeight() - 2 * i, getNumberOfPixelsForRounding(), getNumberOfPixelsForRounding());
        }
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
