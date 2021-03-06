package com.gaurav.domain.interfaces;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MusicRepository {
    Completable init();

    Observable<List<Song>> getAllSongs();

    Observable<List<Album>> getAllAlbums();

    Observable<List<Artist>> getAllArtists();

    Single<Album> getAlbum(long albumId);

    Single<Artist> getArtist(long artistId);
}
