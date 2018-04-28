package net.rodepanda.holograms.Components.EntityComponents;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.rodepanda.holograms.Packet.PacketHandler;
import net.rodepanda.holograms.Projector.Page;
import net.rodepanda.holograms.Projector.Screen;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;

public class EntityItemComponent extends EntityComponent {

    private ItemStack item;
    private String name;
    private boolean nameVisible;
    private EntityTitleComponent tc;

    public EntityItemComponent(int id, Screen s, String name, boolean nameVisible, ItemStack mat) {
        super(id, s);
        this.name = name;
        this.nameVisible = nameVisible;
        item = mat;
    }

    public EntityItemComponent(int id, Screen s, String name, boolean nameVisible, Material mat) {
        this(id, s, name, nameVisible, new ItemStack(mat));
    }

    @Override
    public void init(Vector vec) {
        double y = (item.getType().isBlock() || item.getType() == Material.SKULL_ITEM) ? 0.9 : 1.25;
        PacketContainer as = getArmorStandPacket(vec.clone().subtract(new Vector(0,  y, 0)));
        PacketContainer meta = createMetadata(false, null, false, null);

        PacketContainer itemData = PacketHandler.get().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        itemData.getIntegers().write(0, id);
        itemData.getItemSlots().write(0, item.getType().isBlock() ? EnumWrappers.ItemSlot.HEAD : EnumWrappers.ItemSlot.HEAD);
        itemData.getItemModifier().write(0, item);

        if(nameVisible) {
            tc = new EntityTitleComponent(s.getEntityId(), s, name);
            tc.init(vec.clone().add(new Vector(0, 0.1, 0)));
        }

        try{
            PacketHandler.get().sendServerPacket(s.owner, as);
            PacketHandler.get().sendServerPacket(s.owner, meta);
            PacketHandler.get().sendServerPacket(s.owner, itemData);
        }catch (Exception e){
            //Empty Catch block
        }
    }

    @Override
    public void destroy(Page p){
        p.addToDestroy(id);
        tc.destroy(p);
    }

    public void updateTitle(String updated){
        name = updated;
        tc.updateTitle(updated);
    }

    public void setGlowing(boolean glowing){
        try {
            PacketHandler.get().sendServerPacket(s.owner, createMetadata(glowing, null, false, null));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLocation(Vector loc){
        super.updateLocation(loc);
        if(nameVisible){
            tc.updateLocation(new Vector(loc.getX(), tc.getLoc().getY(), loc.getZ()));
        }
    }
}
