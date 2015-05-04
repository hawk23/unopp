package com.gamble.unopp.model;

import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by Albert on 02.05.2015.
 */
public class GameSession extends ModelObject {

    private static int MAX_PLAYERS = 6;

    private int ID;
    private String name;
    private boolean started;
    private int currentPlayers;
    private Player host;
    private ArrayList<Player> players;
    private int maxPlayers;

    public GameSession(int ID, String name, Player host) {

        this.ID = ID;
        this.name = name;
        this.host = host;
        this.players = new ArrayList<Player>();
        this.players.add(this.host);
        this.host.setGameSession(this);
        this.currentPlayers = getCurrentPlayerCount();
        this.started = false;

    }

    public int getCurrentPlayerCount() {
        return this.players.size();
    }

    public boolean addPlayer(Player player) {

        if (this.players.contains(player) && getCurrentPlayerCount() < MAX_PLAYERS) {
            this.players.add(player);
            player.setGameSession(this);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean removePlayer(Player player) {

        if (this.players.contains(player)) {
            this.players.remove(player);
            player.setGameSession(null);
            return true;
        }
        else {
            return false;
        }
    }

    public static int get_MAX_PLAYERS() {
        return MAX_PLAYERS;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
