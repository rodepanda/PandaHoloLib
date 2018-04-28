package net.rodepanda.holograms.Components.EntityComponents;

import com.comphenix.protocol.events.PacketContainer;
import net.rodepanda.holograms.Packet.PacketHandler;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.util.Vector;

public class EntityTitleComponent extends EntityComponent {

    private String title;

    public EntityTitleComponent(int id, Screen s, String title) {
        super(id, s);
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void updateTitle(String newTitle){
        title = newTitle;
        try {
            PacketHandler.get().sendServerPacket(s.owner, createMetadata(newTitle, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(Vector vec) {

        vec = new Vector(vec.getX(), vec.getY() + 0.2, vec.getZ());
        PacketContainer armorStand = getArmorStandPacket(vec);
        PacketContainer metadata = createMetadata(false, title, true, null);
        try {
            PacketHandler.get().sendServerPacket(s.owner, armorStand);
            PacketHandler.get().sendServerPacket(s.owner, metadata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
