package com.hugoviallon.opta.window.testbed.gui;

import com.hugoviallon.opta.model.Parameters;
import com.hugoviallon.opta.model.Planification;
import com.hugoviallon.opta.model.Task;
import com.hugoviallon.opta.model.TaskGroup;
import com.hugoviallon.opta.window.testbed.gui.bubble.BulleOF;
import com.hugoviallon.opta.window.testbed.gui.bubble.TestBedBulle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.JPanel;

public class OptaPlannerTestBedPlanningPanel<T extends Task, Solution extends Planification> extends JPanel {

    private Map<Integer, List<TestBedBulle>> bullesByMachine = new HashMap<>();
    private TestBedBulle selectedBulle = null;

    private int width = 1200;
    private int height = 600;

    private TaskBoardScale<T> scale;
    private Parameters parametrization;

    public OptaPlannerTestBedPlanningPanel() {
        setPreferredSize(new Dimension(width, height));
    }

    public void setParametrization(Parameters parametrization, Consumer<String> onSelectedTaskChange) {
        this.parametrization = parametrization;
        this.scale = new TaskBoardScale<>(parametrization.getStart(), parametrization.getEnd(), width);

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                TestBedBulle bulleHovered = getBulleAtLocation(e.getX(), e.getY());
                if (bulleHovered != null && selectedBulle != bulleHovered) {
                    onSelectedTaskChange.accept(bulleHovered.toString());
                    selectedBulle = bulleHovered;
                    setDirty();
                }
            }
        });
    }

    private TestBedBulle getBulleAtLocation(int x, int y) {
        Integer machine = y / scale.getBubbleHeight();

        if (bullesByMachine.containsKey(machine)) {
            List<TestBedBulle> tasks = bullesByMachine.get(machine);
            for (TestBedBulle bulle : tasks) {
                if (bulle.getX() > x) {
                    break;
                } else if (x <= bulle.getX() + bulle.getWidth()) {
                    return bulle;
                }
            }
        }
        return null;
    }

    public void setNewBestSolution(Solution bestSolution) {
        List<Task> tasks = new ArrayList<Task>(bestSolution.getTasks());
        Map<Integer, List<TestBedBulle>> newBullesByMachine = tasks.stream()
                .filter(Task::isPlanified)
                .map(task -> scale.buildBubble(task))
                .collect(Collectors.groupingBy(bubble -> bubble.getEquipement().getId(), Collectors.toList()));

        for (Map.Entry<Integer, List<TestBedBulle>> entry : newBullesByMachine.entrySet()) {
            entry.getValue().sort(Comparator.comparing(TestBedBulle::getX));
        }

        bullesByMachine = newBullesByMachine;
        selectedBulle = null;
        setDirty();
    }

    private void setDirty() {
        repaint();
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bullesByMachine != null) {
            draw(g);
        }
    }

    private void draw(Graphics g) {
        List<TestBedBulle> highlight = new ArrayList<>();
        TaskGroup selectedGroup = selectedBulle instanceof BulleOF ? ((BulleOF) selectedBulle).getTask().getGroup() : null;
        if (selectedGroup != null) {
            int y = (this.parametrization.getMachines().size() + 2) * scale.getBubbleHeight();
            ((BulleOF) selectedBulle).drawVerticalLine(g, scale.calculateX(selectedGroup.getDueDate()), 4, Color.BLACK, selectedGroup.getDueDate(), y);
        }

        for (Integer machine : bullesByMachine.keySet()) {
            for (TestBedBulle bubble : bullesByMachine.get(machine)) {
                bubble.draw(g);

                if (bubble instanceof BulleOF) {
                    BulleOF bof = (BulleOF) bubble;
                    if (selectedGroup != null && bof.getTask().getGroup().getId() == selectedGroup.getId()) {
                        highlight.add(bubble);
                    }
                }
            }

            drawAxe(g);
        }

        for (TestBedBulle bulle : highlight) {
            bulle.drawBorder(g, 3, Color.black);
        }
    }

    private void drawAxe(Graphics g) {
        int y = (this.parametrization.getMachines().size() + 3) * scale.getBubbleHeight();
        g.setColor(Color.BLACK);
        g.drawRect(0, y, this.width, 2);
        Instant sDate = this.parametrization.getStart();
        Instant eDate = this.parametrization.getEnd();
        Duration step = Duration.between(sDate, eDate).dividedBy(15);
        Instant currDate = sDate;
        while (currDate.isBefore(this.parametrization.getEnd())) {
            int x = scale.calculateX(currDate);
            g.drawRect(x, y, 1, 5);
            g.drawString(TestBedConstant.getYYMMDDFormat().format(Date.from(currDate)), x, y + TestBedConstant.DELTA_Y_TEXT);
            g.drawString(TestBedConstant.getHHMMSSFormat().format(Date.from(currDate)), x, y + 2 * TestBedConstant.DELTA_Y_TEXT);
            currDate = currDate.plus(step);
        }
    }
}
