package com.example.proj_graduation;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MusicUi{
    private MediaPlayer currentMediaPlayer;
    private ProgressBar musicBar;
    // String[] title = {"빨간맛\n- 레드벨벳", "Love Shot\n-엑소 ","영웅\n- NCT127"};
    private TextView titleText;
    private ImageView playBtn;
    private TextView textView;
    private TextView artistText;
    //private ImageView album;
     String[] title = {"빨간맛\n- 레드벨벳", "Love Shot\n-엑소 ","영웅\n- NCT127"};
  //  final int[] FILEROOT = {R.drawable.blackpink_howyoulikethat, R.drawable.bts_dna, R.drawable.redvelvet_redflavor};
    final int[] MEDIAROOT = {R.raw.red_velvet, R.raw.exo_loveshot, R.raw.nct127_hero};
    //MediaPlayer[] mediaPlayer = new MediaPlayer[3];
    List<MediaPlayer> mediaPlayers = new ArrayList<>(3);

    private final Activity mActivity;
    private com.example.proj_graduation.GameSystem gameSystem;

    private TextView scoreBar;



    MusicUi(Activity mActivity, Context context, ProgressBar musicBar, TextView titleText, ImageView playBtn){
        this.musicBar = musicBar;
        this.titleText = titleText;
        this.playBtn = playBtn;



        //this.album = album;

        for(int r : MEDIAROOT){
            mediaPlayers.add(MediaPlayer.create(context, r));
        }

        this.mActivity = mActivity;
    }

    public void setGameSystem(com.example.proj_graduation.GameSystem gameSystem){
        this.gameSystem = gameSystem;
    }

    public void setMediaPlayer(int number){
        this.currentMediaPlayer = mediaPlayers.get(number);
        currentMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicStop();
            }
        });
        musicBar.setMax(currentMediaPlayer.getDuration());
        musicBar.setProgress(currentMediaPlayer.getCurrentPosition());
        titleText.setText(title[number]);


      //  album.setImageResource(FILEROOT[number]);
    }

    public void musicPlay(){
        if(currentMediaPlayer != null) {
            //currentMediaPlayer.start();
            gameSystem.GameStart();
            currentMediaPlayer.start();

            playBtn.setImageResource(R.drawable.ic_media_stop);

            Thread musicThread = new Thread(new Runnable() {
                @Override
                public void run() { // Thread 로 작업할 내용을 구현
                    while(currentMediaPlayer.isPlaying()){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                musicBar.setProgress(currentMediaPlayer.getCurrentPosition());
                            }
                        });
                    }
                }

            });
            musicThread.start(); // 쓰레드 시작
        }
    }
//음악 일시 정지
    public void musicPause(){
        if(currentMediaPlayer != null) {
            currentMediaPlayer.pause();
            gameSystem.GamePause();
            playBtn.setImageResource(R.drawable.ic_media_play);
        }
    }

    //음악 완전히 정지 초기화 상태
    public void musicStop(){
        if(currentMediaPlayer != null) {
            currentMediaPlayer.pause();
            currentMediaPlayer.seekTo(0);
            gameSystem.GameStop();
            playBtn.setImageResource(R.drawable.ic_media_play);
            musicBar.setProgress(0);
        }
    }

    public MediaPlayer getCurrentMediaPlayer(){
        return currentMediaPlayer;
    }

    public MediaPlayer getMediaPlayer(int i){return mediaPlayers.get(i);}

    public boolean isPlaying(int i){
        return mediaPlayers.get(i).isPlaying();
    }

    public int getCurrentMediaPlayerIndex(){
        return mediaPlayers.indexOf(currentMediaPlayer);
    }
}
