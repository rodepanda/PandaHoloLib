package net.rodepanda.holograms.MinecraftReflection;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import java.lang.reflect.InvocationTargetException;

public class Vector3fSerializerWrapper {

    public static WrappedDataWatcher.Serializer get(){

        return WrappedDataWatcher.Registry.get(MinecraftReflection.getMinecraftClass("Vector3f"));
    }

    public static Object createVector3f(float x, float y, float z) {
        Class vecClass = MinecraftReflection.getMinecraftClass("Vector3f");
        try {
            return vecClass.getConstructor(float.class, float.class, float.class).newInstance(x,y,z);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
        //return new Vector3f(x,y,z);
    }

}
