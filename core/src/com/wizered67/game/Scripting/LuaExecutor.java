package com.wizered67.game.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.StringReader;

/**
 * Created by Adam on 12/11/2016.
 */
public class LuaExecutor {
    public void init() {
        Globals globals = JsePlatform.standardGlobals();
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
    }
}
