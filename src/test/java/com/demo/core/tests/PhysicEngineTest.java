package com.demo.core.tests;

import com.demo.core.Application;
import com.demo.core.entity.GameObject;
import com.demo.core.math.Material;
import com.demo.core.math.PhysicEngine;
import com.demo.core.math.Vec2d;
import com.demo.core.tests.template.ApplicationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhysicEngineTest extends ApplicationTest {

    private Application app;
    private PhysicEngine pe;

    @BeforeEach
    void setUp() {
        app = new Application(new String[]{},"test-pe.properties");
        pe = new PhysicEngine(app);
    }

    @AfterEach
    void tearDown() {
        app.dispose();
    }

    @Test
    void update() {
        for(int i=0;i<10;i++){
            GameObject p=new GameObject("testpe_"+i)
                    .setPosition(i*100.0,0.0)
                    .setDimension(16.0,16.0)
                    .setSpeed(0.0,0.0)
                    .setMaterial(Material.DEFAULT);
            System.out.printf("new GameObject(name=%s,%s)%n",p.getName(),p.pos);
            p.addForce(new Vec2d(0.1,0.0));
            app.add(p);
        }

        pe.update(10000000.0);

        for(int i=0;i<10;i++) {
            GameObject p = app.getObject("testpe_"+i);
            System.out.printf("updated GameObject(name=%s,%s)%n",p.getName(),p.pos);
            assertEquals(5.0+(i*100), p.pos.x, 1.0, "the object 'testpe_"+i+"' has not been updated");
        }

    }

    @Test
    void updateObject() {
        GameObject p=new GameObject("test")
                .setPosition(0,0)
                .setDimension(16,16)
                .setSpeed(0,0)
                .setMaterial(Material.DEFAULT);
        p.addForce(new Vec2d(10.0,0));
        app.add(p);

        pe.updateObject(p,10000000.0);

        assertEquals(5.0,p.pos.x,1.0);
    }

    @Test
    void constrainedBy() {
        GameObject p=new GameObject("test")
                .setPosition(0,0)
                .setDimension(16,16)
                .setSpeed(0,0)
                .setMaterial(Material.DEFAULT);
        p.addForce(new Vec2d(10.0,10.0));
        app.add(p);
        // reset gravity
        pe.getWorld().gravity=new Vec2d();
        pe.updateObject(p,20000000.0);
        pe.constrainedBy(p,app.getGameArea());
        assertEquals(10,p.pos.x,p.w,
                "The GameObject '"+p.getName()+"' has not been constrained horizontally to the Game area");
        assertEquals(10,p.pos.y,p.h,
                "The GameObject '"+p.getName()+"' has not been constrained vertically to the Game area");
    }
}