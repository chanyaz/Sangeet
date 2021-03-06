package com.gaurav.sangeet.views.implementations.bottomsheet;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicstate.MusicState;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.views.interfaces.BottomSheetView;
import com.gaurav.sangeet.views.uievents.bottomsheet.BaseViewUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.BottomSheetUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.NextUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.PlayPauseUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.PrevUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.RepeatUIEvent;
import com.gaurav.sangeet.views.uievents.bottomsheet.ShuffleUIEvent;
import com.gaurav.sangeet.views.viewstates.BottomSheetViewState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class BottomSheetViewImpl implements BottomSheetView {
    private View baseView;
    private ImageView songArtwork;
    private TextView songName;
    private TextView artistName;
    private TextView albumName;
    private ListView songQueueView;
    private ImageButton prevButton;
    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton shuffleButton;
    private ImageButton repeatButton;
    private TextView progressSeekbar;

    private PublishSubject<BottomSheetUIEvent> uiEventSubject;

    // helper private objects
    private MusicState musicState;

    public BottomSheetViewImpl(View baseView) {

        this.baseView = baseView;
        songArtwork = baseView.findViewById(R.id.songArtwork);
        songName = baseView.findViewById(R.id.songTitle);
        artistName = baseView.findViewById(R.id.artistName);
        albumName = baseView.findViewById(R.id.albumName);
        songQueueView = baseView.findViewById(R.id.songQueue);
        prevButton = baseView.findViewById(R.id.prev);
        playPauseButton = baseView.findViewById(R.id.playPause);
        nextButton = baseView.findViewById(R.id.next);
        shuffleButton = baseView.findViewById(R.id.shuffle);
        repeatButton = baseView.findViewById(R.id.repeat);
        progressSeekbar = baseView.findViewById(R.id.progress);

        uiEventSubject = PublishSubject.create();


        baseView.setOnClickListener(v -> uiEventSubject.onNext(new BaseViewUIEvent(baseView)));
        playPauseButton.setOnClickListener(v -> uiEventSubject.onNext(new PlayPauseUIEvent()));
        prevButton.setOnClickListener(v -> uiEventSubject.onNext(new PrevUIEvent()));
        nextButton.setOnClickListener(v -> uiEventSubject.onNext(new NextUIEvent()));
        shuffleButton.setOnClickListener(v -> uiEventSubject.onNext(new ShuffleUIEvent()));
        repeatButton.setOnClickListener(v -> uiEventSubject.onNext(new RepeatUIEvent()));
        // TODO: 7/8/18 add seekbar on click listener to action
    }

    @Override
    public void render(BottomSheetViewState viewState) {
        this.musicState = viewState.getMusicState();
        if (viewState.isUpdateCurrentSongDetails()) {
            updateCurrentSongDetails(viewState.getCurrentSong());
        }
        updateSongQueue(musicState.getSongQueue());
        updatePlayPauseButtonState(musicState.isPlaying());
        updateRepeatShuffleState(musicState.isRepeat(), musicState.isShuffle());
        updateProgress(musicState.getProgress());
        if (baseView.getVisibility() != View.VISIBLE) {
            show();
        }
    }

    @Override
    public PublishSubject<BottomSheetUIEvent> getUIEvents() {
        return uiEventSubject;
    }

    /* Private helper functions */

    private void updateProgress(long progress) {
        progressSeekbar.setText(String.valueOf(progress));
    }

    private void updateCurrentSongDetails(Song currentSong) {
        // todo fetch and update artwork
        songName.setText(currentSong.title);
        artistName.setText(currentSong.artist);
        albumName.setText(currentSong.album);
    }

    private void updateSongQueue(List<Song> songQueue) {
        // TODO: 7/8/18 Implement diffUtils in the update of songQueueView.
        List<String> data = new ArrayList<>();
        for (Song song : songQueue) {
            data.add(song.title);
        }
        this.songQueueView.setAdapter(new ArrayAdapter<String>(baseView.getContext(),
                android.R.layout.simple_list_item_1, data));

    }

    private BottomSheetViewImpl updatePlayPauseButtonState(boolean isPlaying) {
        if (isPlaying) {
            this.playPauseButton.setImageDrawable(baseView.getContext()
                    .getDrawable(android.R.drawable.ic_media_pause));
        } else {
            this.playPauseButton.setImageDrawable(baseView.getContext()
                    .getDrawable(android.R.drawable.ic_media_play));
        }
        return this;
    }

    private void updateRepeatShuffleState(boolean repeat, boolean shuffle) {
        repeatButton.setAlpha(repeat ? 1f : 0.5f);
        shuffleButton.setAlpha(shuffle ? 1f : 0.5f);
    }

    private void show() {
        baseView.setVisibility(View.VISIBLE);
    }

    public View getBaseView() {
        return baseView;
    }
}
