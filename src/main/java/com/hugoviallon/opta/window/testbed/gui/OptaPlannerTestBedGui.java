package com.hugoviallon.opta.window.testbed.gui;

import com.hugoviallon.opta.model.Planification;
import com.hugoviallon.opta.model.Task;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.Comparator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

public class OptaPlannerTestBedGui<T extends Task, Solution extends Planification> extends JFrame {

    private Solver<Solution> solver;
    private Solution planification;
    private OptaPlannerTestBedPlanningPanel<T, Solution> planningPanel;

    private JButton solveButton;
    private JTextArea infoPanel;
    private JEditorPane detailsPanel;

    public OptaPlannerTestBedGui(OptaPlannerTestBedPlanningPanel optaPlannerTestBedPlanningPanel) throws HeadlessException {
        super("AquiOrdo OptaPlanner Sandbox");

        this.planningPanel = optaPlannerTestBedPlanningPanel;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Container contentPane = getContentPane();
        this.infoPanel = new JTextArea();
        this.infoPanel.setPreferredSize(new Dimension(200, 200));
        contentPane.add(this.infoPanel, BorderLayout.WEST);
        contentPane.add(this.planningPanel);
        contentPane.add(getButtonPanel(), BorderLayout.NORTH);
        this.detailsPanel = new JEditorPane("text/html", "");
        this.detailsPanel.setEditable(false);
        this.detailsPanel.setPreferredSize(new Dimension(1200, 2000));
        JScrollPane dsp = new JScrollPane(this.detailsPanel);
        dsp.setPreferredSize(new Dimension(1200, 400));
        contentPane.add(dsp, BorderLayout.SOUTH);

        pack();
        setLocationByPlatform(true);
    }

    public OptaPlannerTestBedGui() {

    }

    public void solve(SolverFactory<Solution> solverFactory, Solution problem) throws HeadlessException {
        this.planningPanel.setParametrization(problem.getParameters(), this::updateDetailsPanelText);
        this.solver = solverFactory.buildSolver();
        setPlanification(problem);
        this.solver.addEventListener(bestSolutionChangedEvent -> {
            System.out.println("New best solution (score: " + bestSolutionChangedEvent.getNewBestScore() + ")");
            setPlanification(bestSolutionChangedEvent.getNewBestSolution());
        });

        setVisible(true);
    }

    private JPanel getButtonPanel() {
        JPanel p = new JPanel();
        solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> new Thread(this::toggleSolving).start());
        p.add(solveButton);
        return p;
    }

    private void toggleSolving() {
        if (solver == null) return;
        if (solver.isSolving()) {
            solveButton.setText("Solve");
            solver.terminateEarly();
        } else {
            solveButton.setText("Stop");
            new Thread(() -> {
                solver.solve(planification);
                Planification solvedPlanification = solver.getBestSolution();
                // print planif
                List<Task> finalSolutionTasks = solvedPlanification.getTasks();
                finalSolutionTasks.sort(Comparator.comparing(t -> t.getMachine().getId()));
                finalSolutionTasks.forEach(t -> System.out.println(t.toString()));
                System.out.println(solvedPlanification.getScore());
            }).start();
        }
    }

    private void setPlanification(Solution problem) {
        this.planification = problem;
        planningPanel.setNewBestSolution(planification);
        updateInfoPanel();
    }

    private void updateInfoPanel() {
        String s = planification.getTasks().size() + " tâches"
                + "\n" + planification.getTasks().stream().filter(Task::isPlanified).count() + " tâches planifiées";
        infoPanel.setText(s);
    }

    private void updateDetailsPanelText(String selectedTask) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(selectedTask).append("</b><br/>");

        java.util.List<Task> theTasks = planification.getTasks();
        theTasks.sort((t1, t2) -> new CompareToBuilder()
                .append(t1.getMachine() != null ? t1.getMachine().getId() : Integer.MAX_VALUE,
                        t2.getMachine() != null ? t2.getMachine().getId() : Integer.MAX_VALUE)
                .append(t1.getStart(), t2.getStart())
                .toComparison());

        for (Task taskTmp : theTasks) {
            sb.append(taskTmp.toString()).append("<br/>");
        }
        detailsPanel.setText(sb.toString());
        detailsPanel.setCaretPosition(0);
    }
}
