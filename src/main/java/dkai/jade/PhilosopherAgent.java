package dkai.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class PhilosopherAgent extends Agent {

    private enum State {THINKING, WAITING, EATING}

    private int id;
    private State state = State.THINKING;
    private final AID manager = new AID("manager", AID.ISLOCALNAME);

    @Override
    protected void setup() {
        id = (int) getArguments()[0];

        addBehaviour(new TickerBehaviour(this, 500) {
            @Override
            protected void onTick() {
                switch (state) {
                    case THINKING -> requestForks();
                    case WAITING -> checkReply();
                    case EATING -> releaseForks();
                }
            }
        });
    }

    private void requestForks() {
        System.out.println("P" + id + " thinks");
        sendToManager(ACLMessage.REQUEST);
        state = State.WAITING;
    }

    private void checkReply() {
        ACLMessage reply = receive();
        if (reply == null) {
            return;
        }

        if (reply.getPerformative() == ACLMessage.AGREE) {
            System.out.println("P" + id + " eats");
            state = State.EATING;
        } else {
            state = State.THINKING;
        }
    }

    private void releaseForks() {
        sendToManager(ACLMessage.INFORM);
        state = State.THINKING;
    }

    private void sendToManager(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.addReceiver(manager);
        message.setContent(String.valueOf(id));
        send(message);
    }
}
