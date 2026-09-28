package dkai.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class PhilosopherAgent extends Agent {

    private enum State {THINKING, WAITING, EATING}

    private static final int THINK_TICKS = 3;
    private static final int EAT_TICKS = 3;

    private int id;
    private State state = State.THINKING;
    private int ticks = 0;
    private final AID manager = new AID("manager", AID.ISLOCALNAME);

    @Override
    protected void setup() {
        id = (int) getArguments()[0];
        TableView.setState(id, TableView.State.THINKING);

        addBehaviour(new TickerBehaviour(this, 700) {
            @Override
            protected void onTick() {
                switch (state) {
                    case THINKING -> think();
                    case WAITING -> checkReply();
                    case EATING -> eat();
                }
            }
        });
    }

    private void think() {
        if (++ticks < THINK_TICKS) return;
        ticks = 0;
        System.out.println("P" + id + " is hungry");
        sendToManager(ACLMessage.REQUEST);
        state = State.WAITING;
        TableView.setState(id, TableView.State.WAITING);
    }

    private void checkReply() {
        ACLMessage reply = receive();
        if (reply == null) return;

        if (reply.getPerformative() == ACLMessage.AGREE) {
            System.out.println("P" + id + " eats");
            state = State.EATING;
            TableView.setState(id, TableView.State.EATING);
        } else {
            state = State.THINKING;
            TableView.setState(id, TableView.State.THINKING);
        }
    }

    private void eat() {
        if (++ticks < EAT_TICKS) return;
        ticks = 0;
        sendToManager(ACLMessage.INFORM);
        state = State.THINKING;
        TableView.setState(id, TableView.State.THINKING);
    }

    private void sendToManager(int performative) {
        ACLMessage message = new ACLMessage(performative);
        message.addReceiver(manager);
        message.setContent(String.valueOf(id));
        send(message);
    }
}