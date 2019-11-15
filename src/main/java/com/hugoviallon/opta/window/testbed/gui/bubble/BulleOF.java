package com.hugoviallon.opta.window.testbed.gui.bubble;

import com.hugoviallon.opta.model.Machine;
import com.hugoviallon.opta.model.Task;
import com.hugoviallon.opta.window.testbed.gui.TestBedConstant;
import java.awt.Color;
import java.awt.Graphics;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulleOF<T extends Task> extends TestBedBulle {

    private static Logger logger = LoggerFactory.getLogger(BulleOF.class);

    private final Color backgroundColor;
    private T task;

    public BulleOF(int x, int y, int width, int height, T task) {
        super(x, y, width, height);
        this.task = task;
        this.backgroundColor = colorByHashCode(task.getGroup().getId() + "");
    }

    public String getLabel() {
        return task.getGroup().getId() + task.getName();
    }

    @Override
    public String toString() {
        return task.toString();
    }

    @Override
    public int getNumberOfPixelsForRounding() {
        return TestBedConstant.ARC;
    }

    @Override
    public Color getColor() {
        return backgroundColor;
    }

    @Override
    public Machine getEquipement() {
        return task.getMachine();
    }

    public Color colorByHashCode(String value) {
        String hashed = null;
        try {
            hashed = new String(MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Method colorByHashCode failed. value=" + value, e);
        }
        int hash = hashed.hashCode();
        int h = (hash & 0xFF0000) >> 16;
        int s = (hash & 0x00FF00) >> 8;
        int v = new Double(new Double(hash & 0x0000FF) * 0.8d + (0.2d * 256.0d)).intValue();
        return Color.getHSBColor(h, s, v);
    }

    public Task getTask() {
        return task;
    }

    Color getBorderColor() {
        return Color.BLUE;
    }

    public void drawVerticalLine(Graphics g, int x, int width, Color color, Instant date, int y) {
        g.setColor(color);
        g.fillRect(x, 0, width, y);
        g.drawString(TestBedConstant.getYYMMDDFormat().format(Date.from(date)), x + TestBedConstant.DELTA_X_TEXT, (int) (y + 0.5 * TestBedConstant.DELTA_Y_TEXT));
        g.drawString(TestBedConstant.getHHMMSSFormat().format(Date.from(date)), x + TestBedConstant.DELTA_X_TEXT, (int) (y + 1.5 * TestBedConstant.DELTA_Y_TEXT));
    }
}
