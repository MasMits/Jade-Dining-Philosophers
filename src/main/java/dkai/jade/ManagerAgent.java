package dkai.jade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

public class ManagerAgent extends Agent {

    private boolean[] forks;

    @Override
    protected void setup() {
        forks = new boolean[(int) getArguments()[0]];
        Arrays.fill(forks, true);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive();
                if (message == null) {
                    block();
                    return;
                }

                int id = Integer.parseInt(message.getContent());
                if (message.getPerformative() == ACLMessage.REQUEST) {
                    take(message, id);
                } else if (message.getPerformative() == ACLMessage.INFORM) {
                    release(id);
                }
            }
        });
    }

    private void take(ACLMessage request, int id) {
        int left = (id + forks.length - 1) % forks.length;
        int right = id;
        boolean available = forks[left] && forks[right];

        if (available) {
            forks[left] = false;
            forks[right] = false;
            // --- Visualisations
            TableView.setFork(left, false);
            TableView.setFork(right, false);
            // ---
            System.out.println("P" + id + " took forks " + left + " and " + right);
        }

        ACLMessage reply = request.createReply();
        reply.setPerformative(available ? ACLMessage.AGREE : ACLMessage.REFUSE);
        send(reply);
    }

    private void release(int id) {
        int left = (id + forks.length - 1) % forks.length;
        int right = id;
        forks[left] = true;
        forks[right] = true;
        // --- Visualisations
        TableView.setFork(left, true);
        TableView.setFork(right, true);
        //
        System.out.println("P" + id + " released forks " + left + " and " + right);
    }
}
