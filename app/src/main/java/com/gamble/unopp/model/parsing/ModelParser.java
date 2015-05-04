package com.gamble.unopp.model.parsing;

import com.gamble.unopp.model.Player;

import org.w3c.dom.Element;

/**
 * Created by Mario on 04.05.2015.
 */
public class ModelParser {

    public static <T> T parseModelFromElement (Element element, Class<T> type) {

        if (type.equals(Player.class)) {
            return (T) parsePlayerFromElement(element);
        }

        return null;
    }

    private static Player parsePlayerFromElement (Element element) {

        int         id                  = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
        String      name                = element.getElementsByTagName("name").item(0).getTextContent();

        return new Player(id, name);
    }
}
