// package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

/**
 * Created by Micheal Paul on 3/18/2018.
 */
// ....................Class that implements  the interface Sequencer.....................................
public class SequencerImpl implements Sequencer {

    MulticastSocket socket = null;
    History history;
    InetAddress group;

    int lastMessageId;
    //.........Constructor for SequencerImpl.java...................................................
    public SequencerImpl() {
        history = new History();

        if (history.messages.size() == 0)
            lastMessageId = 0;
        else
            lastMessageId = history.messages.size() - 1;
    }

    @Override
    //.........................method for joining a Multicast socket....................................................
    public SequencerJoinInfo join(String sender) throws RemoteException, SequencerException {
        try {
            group = InetAddress.getByName(TestSequencer.theGroupIpAddress);
            socket = new MulticastSocket(6789);

            InetAddress senderIp = InetAddress.getByName(sender);
            long seqNo = history.messages.size();
            SequencerJoinInfo sq = new SequencerJoinInfo(senderIp, seqNo);
            socket.joinGroup(group);

            return sq;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    //.............................method receives a message in bytes from Group.java and sends a datagram to TestSequencer ......
    public void send(String sender, byte[] msg, long msgID, long lastSequenceReceived) throws RemoteException {
        try {
            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, 6789);
            socket.send(messageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    //...................method for leaving a group ....................................................
    public void leave(String sender) throws RemoteException {
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getMissing(String sender, long sequence) throws RemoteException, SequencerException {
        return new byte[0];
    }

    @Override
    public void heartbeat(String sender, long lastSequenceReceived) throws RemoteException {

    }

//................... method that returns the index of the messsage stored in the ArrayList found in History.java
    public int getLastMessageId() {
        return lastMessageId;
    }
//............method that returns the index of the next message to be stored........................
    public int getNextMessageId() {
        return history.messages.size();
    }


}

