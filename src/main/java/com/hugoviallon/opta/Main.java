package com.hugoviallon.opta;

import com.hugoviallon.opta.model.Machine;
import com.hugoviallon.opta.model.Parameters;
import com.hugoviallon.opta.model.Planification;
import com.hugoviallon.opta.model.Task;
import com.hugoviallon.opta.services.TaskGeneratorService;
import com.hugoviallon.opta.window.testbed.gui.OptaPlannerTestBedGui;
import com.hugoviallon.opta.window.testbed.gui.OptaPlannerTestBedPlanningPanel;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.optaplanner.core.api.solver.SolverFactory;

public class Main {

    public static void main(String[] argv) {
        SolverFactory<Planification> solverFactory = SolverFactory.createFromXmlResource("temporalModelSolverConfig.xml");

        Planification planification = new Planification(Instant.now(), Instant.now().plusSeconds(60*60*24*30));
        Parameters parameters = new Parameters();
        parameters.setStart(planification.getStart());
        parameters.setEnd(planification.getEnd());

        Machine mach0 = new Machine(0, "Mach0");
        Machine mach1 = new Machine(1, "Mach1");
        Machine mach2 = new Machine(2, "Mach2");
        Machine mach3 = new Machine(3, "Mach3");

        List<Machine> machines = Arrays.asList(mach0, mach1, mach2, mach3);


        planification.setMachines(machines);
        parameters.setMachines(machines);
        planification.setParameters(parameters);

        List<Task> tasks = TaskGeneratorService.generateTasks(machines, 5, 7, planification.getStart(), planification.getEnd());
        planification.setTasks(tasks);

        OptaPlannerTestBedPlanningPanel panel = new OptaPlannerTestBedPlanningPanel();
        OptaPlannerTestBedGui<Task, Planification> optaPlannerTestBedGui = new OptaPlannerTestBedGui<>(panel);
        optaPlannerTestBedGui.solve(solverFactory, planification);
    }
}
