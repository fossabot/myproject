package com.demoapp.core.tests.services.collision;

import com.demoapp.core.Application;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.services.collision.CollisionDetection;
import com.demoapp.core.services.physic.Material;
import com.demoapp.core.services.physic.PhysicEngine;
import com.demoapp.core.services.physic.PhysicType;
import com.demoapp.core.tests.template.ApplicationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionDetectionTest extends ApplicationTest {

    private Application app;
    private CollisionDetection cd;

    @BeforeEach
    void setUp() {
        app = new Application(new String[]{}, "test-cd.properties");
        cd = new CollisionDetection(app);
    }

    @AfterEach
    void tearDown() {
        app.dispose();
    }

    @Test
    void addGameObjectToCollisionDetection() {
        cd.add(new GameObject("test01cd"));
        assertTrue(
                cd.colliders.containsKey("test01cd"),
                "the GameObject test01cd has NOT been added to the CollisionDetection service");
    }

    @Test
    void removeNamedGameObjectFromCollisionDetection() {
        cd.add(new GameObject("test01cd"));
        cd.add(new GameObject("test02cd"));
        cd.add(new GameObject("test03cd"));

        cd.remove("test03cd");
        assertTrue(
                !cd.colliders.containsKey("test03cd"),
                "the GameObject test03cd has NOT been removed to the CollisionDetection service");
    }

    @Test
    void removeGameObjectFromCollisionDetection() {
        GameObject testCD003 = new GameObject("test03cd");
        cd.add(new GameObject("test01cd"));
        cd.add(new GameObject("test02cd"));
        cd.add(testCD003);

        cd.remove(testCD003);
        assertTrue(
                !cd.colliders.containsKey("test03cd"),
                "the GameObject test03cd has NOT been removed to the CollisionDetection service");
    }

    @Test
    void detectNoCollisionOnTwoGameObjectIntoCollisionDetection() {
        cd.add(new GameObject("test01cd")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPhysicType(PhysicType.DYNAMIC)
                .setPosition(10, 10)
                .setDimension(32, 32));
        cd.add(new GameObject("test02cd")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPhysicType(PhysicType.DYNAMIC)
                .setPosition(100, 100)
                .setDimension(32, 32));
        assertEquals(0, cd.colliders.get("test01cd").colliders.size(),
                "Some collision has been detected ?!");
        assertEquals(0, cd.colliders.get("test02cd").colliders.size(),
                "Some collision has been detected ?!");
    }

    @Test
    void detectCollidingObjectsDynamicVsStaticIntoCollisionDetection() {
        cd.add(
                new GameObject("test01cd")
                        .setPosition(10, 10)
                        .setDimension(32, 32)
                        .setPhysicType(PhysicType.DYNAMIC)
                        .setMaterial(Material.DEFAULT));
        GameObject testCD002 = new GameObject("test02cd")
                .setPosition(24, 24)
                .setDimension(32, 32)
                .setPhysicType(PhysicType.STATIC)
                .setMaterial(Material.DEFAULT);
        cd.add(testCD002);

        cd.update(16);
        assertEquals(1, cd.colliders.get("test01cd").colliders.size(),
                "no collision has been detected !");

        assertEquals(testCD002.getName(), cd.colliders.get("test01cd").colliders.get(0).getName(),
                "The collision between test01cd and test02cd has not been detected !");
    }

    @Test
    void detectCollidingObjectsDynamicVsDynamicIntoCollisionDetection() {
        GameObject testCD101 = new GameObject("test101cd")
                .setPosition(10, 10)
                .setDimension(32, 32)
                .setMass(10.0)
                .setPhysicType(PhysicType.DYNAMIC)
                .setMaterial(Material.DEFAULT);
        GameObject testCD102 = new GameObject("test102cd")
                .setPosition(24, 24)
                .setDimension(32, 32)
                .setMass(10.0)
                .setPhysicType(PhysicType.DYNAMIC)
                .setMaterial(Material.DEFAULT);

        cd.add(testCD101);
        cd.add(testCD102);

        cd.update(16);
        assertEquals(2, cd.colliders.get("test101cd").colliders.size(),
                "no collision has been detected !");

        assertEquals(testCD102.getName(), cd.colliders.get("test101cd").colliders.get(0).getName(),
                "The collision between test01cd and test102cd has not been detected !");
        assertEquals(testCD101.getName(), cd.colliders.get("test102cd").colliders.get(0).getName(),
                "The collision between test01cd and test102cd has not been detected !");
    }

}