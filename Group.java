// package com.company;

import java.net.*;
import java.io.*;
import java.rmi.*;

public class Group implements Runnable {

    MsgHandler mHandler;
    InetAddress address;
    MulticastSocket socket;
    SequencerImpl sequencer;
    String sender;
    String senderName;
      //...............................constructor for Group.java class
    public Group(String host, MsgHandler handler, String senderName) throws GroupException {
        try {

            sender = host;
            mHandler = handler;
            this.senderName = senderName;
            socket = new MulticastSocket(6789);
            address = InetAddress.getByName(TestSequencer.theGroupIpAddress);
            //socket.joinGroup(address);

            sequencer = new SequencerImpl();
            sequencer.join(host);

            send((senderName +" JOINED").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SequencerException e) {
            e.printStackTrace();
        }
        // contact Sequencer on "host" -to join group,
        // create Multi-castSocket and thread to listen on it,
        // perform other initialisations
    }

    /*..........send method that sends  the sender IP, message in bytes, calls  getNextMessageId() and  getLastMessageId()
    methods in SequencerImpl class  .................................................
     */
    public void send(byte[] msg) throws GroupException {
        try {
            String newMsg  = "";
            String oldMsg = new String(msg);

            newMsg = senderName+" => "+oldMsg;


            sequencer.send(sender, newMsg.getBytes(), sequencer.getNextMessageId(), sequencer.getLastMessageId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /* ................alerts other members that a specific user has left the  group chat and calls the leave method
    in SequencerImpl by passing the username of the host leaving
     */
    public void leave() {
        try {

            send( (senderName+" LEFT").getBytes() );
            sequencer.leave(senderName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (GroupException e) {
            e.printStackTrace();
        }


    }

/* repeatedly: listen to MulticastSocket created in constructor, and on receipt  of
a datagram call "handle" on the instance of Group.MsgHandler which was supplied to
 the constructor   */
    public void run() {
        try {
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                sequencer.socket.receive(messageIn);
                String receivedMessage = new String(messageIn.getData());
                sequencer.history.addMessage(receivedMessage.trim());
                mHandler.handle(4,receivedMessage.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public interface MsgHandler {
        public void handle(int count, byte[] msg);

    }

    public class GroupException extends Exception {
        public GroupException(String s) {
            super(s);
        }
    }

    public class HeartBeater extends Thread {
        // This thread sends heartbeat messages when required
    }
}
