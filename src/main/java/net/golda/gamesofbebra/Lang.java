package net.golda.gamesofbebra;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang
{
    TITLE("title", "&4&lИгры в Бебру | "),
    BOSS_BAR_TITLE("boss-bar-title", "&6До выдачи следующего предмета: &4%time% &6сек."),
    BOSS_BAR_ENDGAME("boss-bar-end", "&aИгра окончена"),
    CMD_WRONG_USAGE("cmd-wrong-usage", "&6Неправильное использование команды"),
    CMD_USAGE("cmd-usage","&6/game &ecreate | join | start | end | "),
    //GAME
    GAME_PLAYER_DIED("game-player-died", "&6Игрок &b%player% &6выбыл из игры"),
    GAME_CHANGE_TIME("game-change-time","&6Параметр &b\"время\" &6изменён на &b%time% &6сек."),
    GAME_CANT_GIVE("game-cant-give", "&6Нет доступных слотов для выдачи предмета"),
    //CREATE
    GAME_CREATED("game-created", "&aИГРА СОЗДАНА. &6 Организатор >> &b%player%. &6Для подключения введите &b/game join"),
    GAME_CREATE_CANCELED("game-create-canceled", "&6Невозможно создать ещё одну игру"),

    //JOIN
    GAME_PLAYER_JOINED("game-player-joined", "&b%player% &6присоединился к игре"),
    GAME_PLAYER_JOIN_CANCELED("game-player-join-canceled", "&6Вы не можете присоединиться к игре"),
    GAME_CANT_JOIN("game-join-canceled-no-game", "&6Для начала кто-то должен создать игру"),

    //GAME START
    GAME_START("game-start","&aИГРА НАЧАЛАСЬ"),
    GAME_START_CANCELED("game-start-canceled", "&6Для начала нужно создать игру"),
    GAME_START_CANCELED_NOT_OWNER("game-start-canceled-not-owner", "&6Вы не можете начать эту игру"),
    GAME_START_CANCELED_NO_PLAYERS("game-start-canceled-no-players", "&6Недостаточно игроков для начала игры"),
    GAME_START_CANCELED_ALREADY_STARTED("game-start-canceled-already-started", "&6Игра уже начата"),

    //GAME END
    GAME_END_BY_PLAYER("game-end-by-player", "&aИГРА ОКОНЧЕНА. &6Организатор &b%player% &6закончил игру"),
    GAME_END_OWNER_LEFT("game-end-owner-left", "&aИГРА ОКОНЧЕНА. &6Организатор &b%player% &6покинул игру"),
    GAME_END_PLAYER_LEFT("game-end-player-left", "&aИГРА ОКОНЧЕНА. &6Игрок &b%player% &6покинул игру"),
    GAME_END_CANCELED("game-end-canceled", "&6Лол заканчивать-то нечего"),
    GAME_END_CANCELED_NOT_OWNER("game-end-canceled-not-owner", "&6Вы не можете закончить эту игру"),
    GAME_END("game-end","&aИГРА ОКОНЧЕНА. &6Победитель >> &b%player%");

    private String path;
    private String def;
    private static YamlConfiguration LANG;

    Lang(String path, String start)
    {
        this.path = path;
        this.def = start;
    }

    public static void setFile(YamlConfiguration config)
    {
        LANG = config;
    }

    @Override
    public String toString()
    {
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }

    public String getDefault()
    {
        return this.def;
    }

    public String getPath()
    {
        return this.path;
    }
}