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
        int left = id;
        int right = (id + 1) % forks.length;
        boolean available = forks[left] && forks[right];

        if (available) {
            forks[left] = false;
            forks[right] = false;
            System.out.println("P" + id + " took forks " + left + " and " + right);
        }

        ACLMessage reply = request.createReply();
        reply.setPerformative(available ? ACLMessage.AGREE : ACLMessage.REFUSE);
        send(reply);
    }

    private void release(int id) {
        int left = id;
        int right = (id + 1) % forks.length;
        forks[left] = true;
        forks[right] = true;
        System.out.println("P" + id + " released forks " + left + " and " + right);
    }
}
