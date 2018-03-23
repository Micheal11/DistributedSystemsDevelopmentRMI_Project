// package com.company;

import java.io.Serializable;

import java.net.*;

/**
 * Created by IVAN   FRANK on 3/20/2018.
 */
public class SequencerJoinInfo implements Serializable {
    public InetAddress addr;
    public long sequence;

    public SequencerJoinInfo(InetAddress addr, long sequence)
    {
        this.addr = addr;
        this.sequence = sequence;
    }
}

