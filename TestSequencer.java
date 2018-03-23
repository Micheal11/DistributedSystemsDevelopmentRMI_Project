// package com.company;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Scanner;

//.................................................Main class.........................................................
public class TestSequencer {

    public static String theGroupIpAddress = "";
    public static String userName = "Micheal Paul";

    public static void main(String[] args) {
        try {

            InetAddress address = InetAddress.getLocalHost();
            String myIP = (address.getHostAddress());

 //----------------------------------------GETTING RANDOM MULTI-CAST ADDRESS --------------------------------
            theGroupIpAddress = getRandomMultiCastAddress();

            System.out.println("CLIENT OF  "+theGroupIpAddress + " STARTED :");
            System.out.println("Type 'exit' to close ");


 //....................................GROUP ------------------------------------------------------------
            MessageHandler handler = new MessageHandler();
            Group group = new Group(myIP,handler,userName);
            Thread thread = new Thread(group);
            thread.start();

 //.---------------------------------GET MESSAGE FROM KEYBOARD -----------------------------------------------
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String multiMessage = scanner.nextLine();

                if (multiMessage.trim().equals("exit")) {
                    group.leave();
                    System.exit(1);
                }


                byte[] msgBytes = multiMessage.getBytes();

                group.send(msgBytes);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Group.GroupException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------Method for generating multicast group address...................
    public static String getRandomMultiCastAddress(){
        Calendar c = Calendar.getInstance();
        // double random = Math.random();
        int octet1 = 230;
        int octet2 = c.get(Calendar.DAY_OF_MONTH);
        int octet3 = c.get(Calendar.MONTH)+1;
        // int octet4 =  (int)(random*254);
        int octet4 =  10;

        String multiCastAddress = octet1+"."+octet2+"."+octet3+"."+octet4;
        return multiCastAddress;

    }
//..................................the class that implements the interface MsgHandler in Group.java....................
    // It also implements all abstract methods in the interface
    public static class  MessageHandler implements Group.MsgHandler{

        @Override
        public void handle(int count, byte[] msg) {
            System.out.println(new String(msg).trim());
        }
    }

}
