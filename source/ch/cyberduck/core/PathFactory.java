package ch.cyberduck.core;

/*
 *  Copyright (c) 2005 David Kocher. All rights reserved.
 *  http://cyberduck.ch/
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Bug fixes, suggestions and comments should be sent to:
 *  dkocher@cyberduck.ch
 */

import com.apple.cocoa.foundation.NSDictionary;

import java.util.HashMap;
import java.util.Map;

public abstract class PathFactory {

    private static Map factories = new HashMap();

    protected abstract Path create(Session session);

    protected abstract Path create(Session session, String path);

    protected abstract Path create(Session session, String parent, String name);

    protected abstract Path create(Session session, String path, Local file);

    protected abstract Path create(Session session, NSDictionary dict);

    public static void addFactory(String protocol, PathFactory f) {
        factories.put(protocol, f);
    }

    public static Path createPath(Session session) {
        loadClass(session.getHost().getProtocol());
        return ((PathFactory) factories.get(session.getHost().getProtocol())).create(session);
    }

    public static Path createPath(Session session, String parent, String name) {
        loadClass(session.getHost().getProtocol());
        return ((PathFactory) factories.get(session.getHost().getProtocol())).create(session, parent, name);
    }

    public static Path createPath(Session session, String path) {
        loadClass(session.getHost().getProtocol());
        return ((PathFactory) factories.get(session.getHost().getProtocol())).create(session, path);
    }

    public static Path createPath(Session session, String path, Local file) {
        loadClass(session.getHost().getProtocol());
        return ((PathFactory) factories.get(session.getHost().getProtocol())).create(session, path, file);
    }

    public static Path createPath(Session session, NSDictionary dict) {
        loadClass(session.getHost().getProtocol());
        return ((PathFactory) factories.get(session.getHost().getProtocol())).create(session, dict);
    }

    private static void loadClass(String id) {
        if (!factories.containsKey(id)) {
            try {
                // Load dynamically
                Class.forName("ch.cyberduck.core." + id + "." + id.toUpperCase() + "Path");
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException("No class for type: " + id);
            }
            // See if it was put in:
            if (!factories.containsKey(id)) {
                throw new RuntimeException("No class for type: " + id);
            }
        }
    }
}