package com.wizered67.game.scripting.lua;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import org.luaj.vm2.lib.jse.LuajavaLib;


/**
 * Used to override "luajava" library for Lua scripting. Extends LuajavaLib to maintain all functionality,
 * but overrides classForName to use LibGDX's ClassReflection to find classes.
 * @author Adam Victor
 */
public class LuajavaGdxReflection extends LuajavaLib {
    //constructor must be kept public to be used with Lua!
    public LuajavaGdxReflection() {

    }
    @Override
    protected Class classForName(String name) throws ClassNotFoundException {
        try {
            return ClassReflection.forName(name);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        return null;

    }
}
