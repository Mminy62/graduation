package com.example.proj_graduation;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

public class Destination extends Node {
    Destination(AnchorNode parent, ModelRenderable bofLogoModel,
                ArSceneView arSceneView){
        this.setRenderable(bofLogoModel);
        this.setLocalScale(new Vector3(1f, 1f, 1f));
        this.setParent(parent);
        this.setLocalPosition(this.getUp().scaled(+0.5f));

        Vector3 cameraPos = arSceneView.getScene().getCamera().getWorldPosition();
        Vector3 objPos = this.getWorldPosition();
        Vector3 objToCam = Vector3.subtract(cameraPos, objPos).negated();
        Vector3 up = this.getUp();

        Quaternion direction = Quaternion.lookRotation(objToCam, up);
        this.setWorldRotation(direction);
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
    }
}
