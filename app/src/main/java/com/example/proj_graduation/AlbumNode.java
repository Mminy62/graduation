package com.example.proj_graduation;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Random;

public class AlbumNode extends Node {
    private AnchorNode parent;
    private ModelRenderable[] musicNotes;
    private int[] timerArray; // 밀리세컨드 단위
    private float time = 0f;
    private int index = 0; // timerArray의 index
    private MediaPlayer mediaPlayer;
    private ArSceneView arSceneView;

    SoundPool soundPool;
    int effectSoundID;
    Context context;

    AlbumNode(AnchorNode parent, ModelRenderable albumModel,
              int[] timerArray, ModelRenderable[] musicNotes,
              MediaPlayer mediaPlayer, ArSceneView arSceneView){
        this.setRenderable(albumModel);
        //this.setRenderable(handModel);

        this.setLocalScale(new Vector3(0.4f, 0.4f, 0.4f));
        this.setLocalPosition(this.getUp().scaled(-2.5f)); // 스위치로 바꾸는 과정에서 이렇게함//-2.5f
        /*
        this.setLocalScale(new Vector3(1f, 1f, 1f));
        this.setLocalPosition(this.getUp().scaled(-0.5f));
*/
        this.setParent(parent);
        this.parent = parent;
        this.timerArray = timerArray;
        this.musicNotes = musicNotes;
        this.mediaPlayer = mediaPlayer;
        this.arSceneView = arSceneView;

        Vector3 cameraPos = arSceneView.getScene().getCamera().getWorldPosition();
        Vector3 objPos = this.getWorldPosition();
        Vector3 objToCam = Vector3.subtract(cameraPos, objPos).negated();
        Vector3 up = this.getUp();
        Quaternion direction = Quaternion.lookRotation(objToCam, up);
        this.setWorldRotation(direction);
    }

    // 음악을 시작했을 때 돌기 및 거리에 따른 크기 증가 및 시간에 따른 음표 오브젝트 생성
    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        Log.i("distance: ", ""+this.getLocalPosition().x +", "+this.getLocalPosition().y+", "+this.getLocalPosition().z);

        Vector3 cameraPos = arSceneView.getScene().getCamera().getWorldPosition();

        // Animation hasn't been set up.
        if(mediaPlayer.isPlaying()) {
            time = mediaPlayer.getCurrentPosition(); // 밀리 세컨드로 받아옴

            // 특정 시간에 음표 생성 (아직 딜레이 적용x)
            if (index < timerArray.length && time >= timerArray[index]) {
                Random rand = new Random();
                int i = rand.nextInt(musicNotes.length);

                //MusicNote m = new MusicNote(parent, musicNotes[i], cameraPos);
                index++;
            }
        }

        Vector3 v = Vector3.subtract(cameraPos, this.getWorldPosition());
        float distance = (float) Math.sqrt(Vector3.dot(v, v));

        // 사용자와 거리가 50m이상 벌어지면 삭제
        if(distance > 50){
            parent.removeChild(this);
            this.setParent(null);
            arSceneView.getScene().removeChild(parent);
            parent.getAnchor().detach();
            parent.setParent(null);
            parent = null;
            Log.i("AlbumNode", "object is removed");
        }
    }

    public void removeNode(){
        parent.removeChild(this);
        this.setParent(null);
        arSceneView.getScene().removeChild(parent);
        parent.getAnchor().detach();
        parent.setParent(null);
        parent = null;
        Log.i("AlbumNode", "object is removed");

    }
    // 뮤직게임 시작
    public void startGame(){
        time = 0f;
        index = 0;
    }

    // 뮤직게임 중지 (음표 오브젝트 생성x)
    public void stopGame(){
        time = 0f;
        index = 0;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

    public int getTimer(int i){
        return timerArray[i];
    }

    public int getCurrentMediaPosition(){
        return mediaPlayer.getCurrentPosition();
    }
}