package org.skife.jdbi.v2.sqlobject;

import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;
import org.skife.jdbi.v2.tweak.HandleCallback;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class GetHandleHelper
{
    static Map<Method, Handler> handlers()
    {
        try {
            Map<Method, Handler> h = new HashMap<Method, Handler>();
            h.put(GetHandle.class.getMethod("getHandle"), new GetHandleHandler());
            h.put(GetHandle.class.getMethod("withHandle", HandleCallback.class), new WithHandleHandler());
            return h;
        }
        catch (NoSuchMethodException e) {
            throw new IllegalStateException("someone wonkered up the bytecode", e);
        }

    }
}
