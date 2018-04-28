package net.rodepanda.holograms.Projector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import net.rodepanda.holograms.Packet.PacketHandler;
import net.rodepanda.holograms.Components.GuiComponents.GuiButtonComponent;
import net.rodepanda.holograms.Components.GuiComponents.GuiComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Page {

    final Set<GuiComponent> components = new HashSet<>();
    private Screen screen;
    private List<Integer> despawnList = new ArrayList<>();

        private boolean pageActive = false;

    public void addComponent(GuiComponent c){
        components.add(c);
        if(pageActive)
            c.init(screen);
    }

    /**
     * Internal use.
     * @param s
     */
    public void init(Screen s){
        if(pageActive)return;
        if(components.isEmpty())return;
        screen = s;
        for(GuiComponent c : components)
            c.init(screen);
        pageActive = true;
    }

    void update(){
        if(components.isEmpty())return;
        for(GuiComponent c : components)
            c.update();
    }

    /**
     * Internal use.
     */
    public void destroy(){
        if(components.isEmpty())return;
        for(GuiComponent c : components)
            c.destroy(this);
        int[] list = new int[despawnList.size()];
        for(int i = 0; i < despawnList.size(); i++) {
            list[i] = despawnList.get(i);
        }
        ProtocolManager pm = PacketHandler.get();
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        //packet.getIntegers().write(0, despawnList.size());
        packet.getIntegerArrays().write(0, list);
        try {
            pm.sendServerPacket(screen.owner, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal use.
     * @param id
     */
    public void addToDestroy(int id){
        despawnList.add(id);
    }

    /**
     * Internal use.
     */
    void interact(){
        if(components.isEmpty())return;
        for(GuiComponent c : components)
            if(c instanceof GuiButtonComponent)
                ((GuiButtonComponent)c).interact();
    }

}
