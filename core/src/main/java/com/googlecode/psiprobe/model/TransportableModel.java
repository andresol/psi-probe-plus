/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper class to assist marshalling of ModelAndView.getModel() Map to XML representation.
 * 
 * @author Vlad Ilyushchenko
 */
public class TransportableModel {
    private Map items = new HashMap();

    public Map getItems() {
        return items;
    }

    public void setItems(Map items) {
        this.items = items;
    }

    public void putAll(Map map) {
        items.putAll(map);
    }
}
