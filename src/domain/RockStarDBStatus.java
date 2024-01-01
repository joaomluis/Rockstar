package domain;

public enum RockStarDBStatus {
    DB_USER_ALREADY_EXISTS,
    DB_USER_ADDED,
    DB_USER_FAILED_TO_SAVE,

    DB_USER_LOGIN_SUCCESS,
    DB_USER_LOGIN_FAILED,

    DB_INCORRET_FORMAT_NUMBER,

    DB_MUSIC_ADDED,
    DB_MUSIC_NAME_EMPTY, DB_MUSIC_NAME_FAILED, DB_MUSIC_NAME_HAS_CHANGED,
    DB_MUSIC_PRICE_HAS_CHANGED,

    DB_ALBUM_NAME_FAILED, DB_ALBUM_NAME_HAS_CHANGED,

    DB_SONG_ALREADY_IN_CART, DB_SONG_ADDED_TO_CART, DB_SONG_ALREADY_BOUGHT;
}
