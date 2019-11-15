package com.hugoviallon.opta.window.testbed.gui;

import com.hugoviallon.opta.model.Task;
import com.hugoviallon.opta.window.testbed.gui.bubble.BulleOF;
import java.time.Duration;
import java.time.Instant;

public class TaskBoardScale<T extends Task> {

    private static final int BULLE_HEIGHT = 25;

    private Instant dateStart;
    private Instant dateEnd;
    private double ratioX = 0d;

    /**
     * Créer un objet échelle.
     * @param dateStart Date start de la planification
     * @param dateEnd Date end de la planification
     * @param width La largeur de la fenêtre
     */
    TaskBoardScale(Instant dateStart, Instant dateEnd, int width){
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.ratioX = (double) width / Duration.between(dateStart, dateEnd).toNanos();
    }

    /**
     * Créer une TestBedBulle depuis une T
     * @param task
     * @return
     */
    BulleOF<Task> buildBubble(Task task) {
        return new BulleOF<>(
                calculateX(task.getStart()),
                Math.toIntExact(task.getMachine().getId()) * getBubbleHeight(),
                calculateX(task.getStart(), task.getEnd()),
                getBubbleHeight(),
                task);
        /*
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;*/
    }

    /**
     * Calculer la largeur en pixels d'une durée
     * @param position1
     * @param position2
     * @return
     */
    private int calculateX(Instant position1, Instant position2) {
        return (int) (Duration.between(position1, position2).toNanos() * ratioX);
    }

    /**
     * Calculer la position X en pixels d'un instant
     * @param position
     * @return
     */
    int calculateX(Instant position) {
        return (int) (Duration.between(dateStart, position).toNanos() * ratioX);
    }

    int getBubbleHeight(){
        return BULLE_HEIGHT;
    }

}
