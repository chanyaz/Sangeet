package com.gaurav.sangeet.views.uievents.songs;

import com.gaurav.domain.models.Song;

public class SongItemClickUIEvent implements SongViewUIEvent {
    Song song;

    public SongItemClickUIEvent(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}