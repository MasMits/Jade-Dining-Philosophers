package dkai.jade;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class JadeDiningPhilosophers {

    private static final int DEFAULT_COUNT = 5;

    public static void main(String[] args) throws Exception {
        String port = args.length > 0 ? args[0] : "1100";
        int count = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_COUNT;

        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.LOCAL_PORT, port);
        ContainerController container = Runtime.instance().createMainContainer(profile);

        TableView.open(count);

        AgentController manager = container.createNewAgent(
                "manager",
                ManagerAgent.class.getName(),
                new Object[]{count}
        );
        manager.start();

        for (int id = 0; id < count; id++) {
            AgentController philosopher = container.createNewAgent(
                    "philosopher-" + id,
                    PhilosopherAgent.class.getName(),
                    new Object[]{id}
            );
            philosopher.start();
        }
    }
}
