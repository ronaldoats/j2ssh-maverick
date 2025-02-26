/**
 * Copyright 2003-2014 SSHTOOLS Limited. All Rights Reserved.
 *
 * For product documentation visit https://www.sshtools.com/
 *
 * This file is part of J2SSH Maverick.
 *
 * J2SSH Maverick is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * J2SSH Maverick is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with J2SSH Maverick.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is originally from the http://sourceforge.net/projects/jsocks/
 * released under the LGPL.
 */
package socks;
import java.io.*;
import java.net.*;

/** 
  SOCKS4 Reply/Request message.
*/

class Socks4Message extends ProxyMessage{

   private byte[] msgBytes;
   private int msgLength;

   /**
    * Server failed reply, cmd command for failed request
    */
   public Socks4Message(int cmd){
      super(cmd,null,0);
      this.user    = null;

      msgLength = 2;
      msgBytes = new byte[2];

      msgBytes[0] = (byte) 0;
      msgBytes[1] = (byte) command;
   }

   /** 
    *  Server successfull reply
    */
   public Socks4Message(int cmd,InetAddress ip,int port){
      this(0,cmd,ip,port,null);
   }

   /** 
    *  Client request
    */
   public Socks4Message(int cmd,InetAddress ip,int port,String user){
      this(SOCKS_VERSION,cmd,ip,port,user);
   }

   /**
    * Most general constructor
    */
   public Socks4Message(int version, int cmd,
                        InetAddress ip,int port,String user){
      super(cmd,ip,port);
      this.user    = user;
      this.version = version;

      msgLength = user == null?8:9+user.length();
      msgBytes = new byte[msgLength];

      msgBytes[0] = (byte) version;
      msgBytes[1] = (byte) command;
      msgBytes[2] = (byte) (port >> 8);
      msgBytes[3] = (byte) port;

      byte[] addr;

      if(ip != null)
        addr = ip.getAddress();
      else{
        addr = new byte[4];
        addr[0]=addr[1]=addr[2]=addr[3]=0;
      }
      System.arraycopy(addr,0,msgBytes,4,4);

      if(user != null){
         byte[] buf = user.getBytes();
         System.arraycopy(buf,0,msgBytes,8,buf.length);
         msgBytes[msgBytes.length -1 ] = 0;
      }
   }

   /**
    *Initialise from the stream
    *If clientMode is true attempts to read a server response
    *otherwise reads a client request
    *see read for more detail
    */
   public Socks4Message(InputStream in, boolean clientMode) throws IOException{
      msgBytes = null;
      read(in,clientMode);
   }

   public void read(InputStream in) throws IOException{
        read(in,true);
   }

   public void read(InputStream in, boolean clientMode) throws IOException{
       DataInputStream d_in = new DataInputStream(in);
       version= d_in.readUnsignedByte();
       command = d_in.readUnsignedByte();
       if(clientMode && command != REPLY_OK){
          String errMsg;
          if(command >REPLY_OK && command < REPLY_BAD_IDENTD)
             errMsg = replyMessage[command-REPLY_OK];
          else
             errMsg = "Unknown Reply Code";
          throw new SocksException(command,errMsg);
       }
       port = d_in.readUnsignedShort();
       byte[] addr = new byte[4];
       d_in.readFully(addr);
       ip=bytes2IP(addr);
       host = ip.getHostName();
       if(!clientMode){
          int b = in.read();
          //Hope there are no idiots with user name bigger than this
          byte[] userBytes = new byte[256];
          int i = 0;
          for(i =0;i<userBytes.length && b>0;++i){
             userBytes[i] = (byte) b;
             b = in.read();
          }
          user = new String(userBytes,0,i);
       }
   }
   public void write(OutputStream out) throws IOException{
      if(msgBytes == null){
         Socks4Message msg = new Socks4Message(version,command,ip,port,user);
         msgBytes = msg.msgBytes;
         msgLength = msg.msgLength;
      }
      out.write(msgBytes);
   }

   //Class methods
   static InetAddress bytes2IP(byte[] addr){
      String s = bytes2IPV4(addr,0);
      try{
         return InetAddress.getByName(s);
      }catch(UnknownHostException uh_ex){
        return null;
      }
   }

   //Constants

   static final String[] replyMessage ={
          "Request Granted",
          "Request Rejected or Failed",
          "Failed request, can't connect to Identd",
          "Failed request, bad user name"};

   static final int SOCKS_VERSION = 4;

   public final static int REQUEST_CONNECT		= 1;
   public final static int REQUEST_BIND   		= 2;

   public final static int REPLY_OK 			= 90;
   public final static int REPLY_REJECTED		= 91;
   public final static int REPLY_NO_CONNECT		= 92;
   public final static int REPLY_BAD_IDENTD		= 93;

}
