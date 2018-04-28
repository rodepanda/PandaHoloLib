package net.rodepanda.holograms.Components.EntityComponents;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.rodepanda.holograms.MinecraftReflection.Vector3fSerializerWrapper;
import net.rodepanda.holograms.Packet.PacketHandler;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Util.VectorCalc;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public abstract class EntityComponent {

    private WrappedDataWatcher.Serializer stringSerializer = WrappedDataWatcher.Registry.get(String.class);
    private WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    private WrappedDataWatcher.Serializer booleanSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
    private WrappedDataWatcher.Serializer vectorSerializer = Vector3fSerializerWrapper.get();
    private ProtocolManager pm = PacketHandler.get();
    private Vector loc;
    final int id;
    Screen s;

    EntityComponent(int id, Screen s){
        this.id = id;
        this.s = s;
    }

    public Vector getLoc(){
        return loc.clone();
    }

    public abstract void init(Vector vec);

    PacketContainer getArmorStandPacket(Vector loc){
        this.loc = loc;
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, id).write(6,78)
                .write(5, (int)(VectorCalc.vectorToYaw(s.getScreenNormal())));
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getDoubles().write(0, loc.getX()).write(1, loc.getY()).write(2, loc.getZ());
        return packet;
    }

    PacketContainer createMetadata(String name, Vector rotation){
        return createMetadata(false, name, true, rotation);
    }

    PacketContainer createMetadata(boolean glowing, String name, boolean nameVisible, Vector rotation){
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher dw = new WrappedDataWatcher();
        packet.getIntegers().write(0, id);
//        int flags = Holo.debug ? 0x00 : 0x20;
        int flags = 0x20;
        flags += glowing ? 0x40 : 0;
        dw.setObject(0, byteSerializer, (byte)flags);
        if(name != null && name.length() != 0) {
            dw.setObject(2, stringSerializer, ChatColor.translateAlternateColorCodes('&', name));
            dw.setObject(3, booleanSerializer, (Boolean)nameVisible);
        }
        dw.setObject(4, booleanSerializer, (Boolean)true);
        dw.setObject(5, booleanSerializer, (Boolean)true);
        dw.setObject(11, byteSerializer, (byte)0x1d);
        dw.setObject(14, vectorSerializer, Vector3fSerializerWrapper.createVector3f(90f,0,0));
        packet.getWatchableCollectionModifier().write(0, dw.getWatchableObjects());
        return packet;
    }

    public void updateTitle(String newTitle){
        try {
            PacketHandler.get().sendServerPacket(s.owner, createMetadata(newTitle, s.getScreenNormal().normalize()));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void updateLocation(Vector vec){
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, id);
        packet.getDoubles().write(0, vec.getX());
        packet.getDoubles().write(1, vec.getY());
        packet.getDoubles().write(2, vec.getZ());
        packet.getBytes().write(0, (byte)VectorCalc.vectorToYaw(s.getScreenNormal()));
        try {
            pm.sendServerPacket(s.owner, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void destroy(Page p){
        p.addToDestroy(id);
    }

    public int getEntityId(){
        return id;
    }

}