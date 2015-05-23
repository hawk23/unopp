package com.gamble.unopp.model.game;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Albert on 18.05.2015.
 */
public abstract class GameUpdate implements Serializable {

    protected int ID;

    public GameUpdate(int ID) {
        this.ID = ID;
    }

    public String serializeUpdate () {

        try {

            ByteArrayOutputStream   baos            = new ByteArrayOutputStream();
            ObjectOutputStream      oos             = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        }
        catch (Exception ex) {

            ex.printStackTrace();
        }

        return null;
    }

    public static GameUpdate deserializeUpdate (String bytes) {

        try {

            byte [] data = Base64.decode(bytes, Base64.DEFAULT);

            ByteArrayInputStream    bais            = new ByteArrayInputStream(data);
            ObjectInputStream       ois             = new ObjectInputStream(bais);
            GameUpdate              gameUpdate      = (GameUpdate) ois.readObject();
            ois.close();

            return gameUpdate;
        }
        catch (Exception ex) {

            ex.printStackTrace();
        }

        return null;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
