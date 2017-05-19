package com.wizered67.game.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.StringReader;

/**
 * Created by Adam on 12/11/2016.
 */
public class LuaExecutor {
    public static void main(String[] args) {
        LuaExecutor l = new LuaExecutor();
        l.init();
    }
    public void init() {
        Globals globals = JsePlatform.standardGlobals();
        globals.load("arguments = ...").call(LuaValue.listOf(new LuaValue[]{LuaString.valueOf("Hello"), LuaString.valueOf("World."), LuaInteger.valueOf(55)}));
        LuaValue v = globals.get("arguments");
        globals.load("print(arguments[1])").call();
        /*
        globals.get("print").call(LuaValue.valueOf("hello, world"));
        globals.set("x", LuaValue.valueOf("true"));
        globals.get("print").call(globals.get("x"));
        globals.load("x = false");
        globals.load("x = 5", "main.lua").call();
        globals.load("print(os.time())").call();
        globals.load("math.randomseed(os.time())").call();
        globals.load("x = math.random()", "test").call();
        globals.load( new StringReader("print(x)"), "main.lua" ).call();
        //globals.invokemethod("main.lua");
        globals.load("array = {1, 2}").call();
        globals.load("print(array[1]); print(array[2])").call();
        LuaValue result = globals.load("return x > 0.5").call();
        System.out.println(result.toboolean());
        */
    }
}
