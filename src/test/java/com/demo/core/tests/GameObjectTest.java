package com.demo.core.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.demo.core.entity.GameObject;
import com.demo.core.math.Material;
import com.demo.core.math.PhysicType;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GameObjectTest {

    @Test
    public void gameObjectHasName() {
        GameObject go = new GameObject("go-name");
        assertNotNull(go, "The GameObject is not created");
        assertEquals("go-name", go.getName(), "the GameObject name has not been set");
    }

    @Test
    public void gameObjectHasPosition() {
        GameObject go = new GameObject("go-name").setPosition(12.0, 13.0);
        assertEquals(12.0, go.pos.x, 0.01, "the GameObject position on x axis has not been set");
        assertEquals(13.0, go.pos.y, 0.01, "the GameObject position on y axis has not been set");
    }

    @Test
    public void gameObjectHasDimension() {
        GameObject go = new GameObject("go-name").setDimension(8.0, 16.0);
        assertEquals(8.0, go.w, 0.01, "the GameObject width has not been set");
        assertEquals(16.0, go.h, 0.01, "the GameObject height has not been set");
    }

    @Test
    public void gameObjectHasSpeed() {
        GameObject go = new GameObject("go-name").setSpeed(0.01, -0.01);
        assertEquals(0.01, go.speed.x, 0.0000001, "the GameObject speed on x axis has not been set");
        assertEquals(-0.01, go.speed.y, 0.0000001, "the GameObject speed on y axis has not been set");
    }

    @Test
    public void gameObjectHasAcceleration() {
        GameObject go = new GameObject("go-name").setAcc(0.01, -0.01);
        assertEquals(0.01, go.acc.x, 0.0000001, "the GameObject acceleration on x axis has not been set");
        assertEquals(-0.01, go.acc.y, 0.0000001, "the GameObject acceleration on y axis has not been set");
    }

    @Test
    public void gameObjectHasAType() {
        GameObject go = new GameObject("go-name").setType(GameObject.ObjectType.IMAGE);
        assertEquals(GameObject.ObjectType.IMAGE, go.type, "the GameObject type has not been set");
    }

    @Test
    public void gameObjectHasAPhysicType() {
        GameObject go = new GameObject("go-name").setPhysicType(PhysicType.STATIC);
        assertEquals(PhysicType.STATIC, go.getPhysicType(), "the GameObject physicType has not been set");
    }


    @Test
    public void gameObjectHasAMaterial() {
        GameObject go = new GameObject("go-name").setMaterial(Material.DEFAULT);
        assertNotNull(go.material, "the GameObject material has not been set");
        assertEquals("default", go.material.name, "the GameObject material has not been set to default");
    }

    @Test
    public void gameObjectHasALayer() {
        GameObject go = new GameObject("go-name").setLayer(1);
        assertEquals(1, go.layer, "the GameObject layer has not been set");
    }

    @Test
    public void gameObjectHasAPriority() {
        GameObject go = new GameObject("go-name").setPriority(1);
        assertEquals(1, go.priority, "the GameObject priority has not been set");
    }

    @Test
    public void gameObjectHasABorderColor() {
        GameObject go = new GameObject("go-name").setBorderColor(Color.RED);
        assertEquals(Color.RED, go.borderColor, "the GameObject border color has not been set");
    }

    @Test
    public void gameObjectHasAFillColor() {
        GameObject go = new GameObject("go-name").setFillColor(Color.BLUE);
        assertEquals(Color.BLUE, go.fillColor, "the GameObject fill color has not been set");
    }

    @Test
    public void gameObjectHasAImage() {
        BufferedImage bf = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        GameObject go = new GameObject("go-name").setImage(bf);
        assertNotNull(go.image, "The GameObject image has not been set");
        assertEquals(bf, go.image, "the GameObject fill color has not been set");
    }

}
