package net.rodepanda.holograms.Packet;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class PacketHandler {

    private static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();;

    public static ProtocolManager get(){
        return protocolManager;
    }

}
