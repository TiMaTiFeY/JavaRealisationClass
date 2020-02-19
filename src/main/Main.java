package main;

import cube.Cube;

public class Main {
    public static void main(String[] args) {
        Cube cubePROGRAM = new Cube();
        Cube b = new Cube();
        System.out.println(cubePROGRAM);
        cubePROGRAM.moveFace(Cube.Faces.RIGHT, 0, 0, true);
        cubePROGRAM.moveFace(Cube.Faces.LEFT, 0, 1, true);
        cubePROGRAM.moveFace(Cube.Faces.TOP, 0, 0, true);
        cubePROGRAM.moveFace(Cube.Faces.BACK, 0, 0, false);
        cubePROGRAM.moveFace(Cube.Faces.FRONT, 0, 0, true);
        cubePROGRAM.cubeRotateTo(Cube.Rotates.UP);
        cubePROGRAM.moveFace(Cube.Faces.FRONT, 0, 0, false);
        cubePROGRAM.moveFace(Cube.Faces.RIGHT, 0, 0, false);
        cubePROGRAM.moveFace(Cube.Faces.BOT, 0, 0, true);
        cubePROGRAM.moveFace(Cube.Faces.TOP, 0, 0, false);
        cubePROGRAM.moveFace(Cube.Faces.LEFT, 0, 0, false);
        cubePROGRAM.moveFace(Cube.Faces.FRONT, 0, 1, true);
        cubePROGRAM.cubeRotateTo(Cube.Rotates.RIGHT);
        cubePROGRAM.cubeRotateTo(Cube.Rotates.RIGHT);
        cubePROGRAM.cubeRotateTo(Cube.Rotates.UP);
        cubePROGRAM.cubeRotateTo(Cube.Rotates.LEFT);
        b.moveFace(Cube.Faces.RIGHT, 0, 0, true);
        b.moveFace(Cube.Faces.LEFT, 0, 1, true);
        b.moveFace(Cube.Faces.TOP, 0, 0, true);
        b.moveFace(Cube.Faces.BACK, 0, 0, false);
        b.moveFace(Cube.Faces.FRONT, 0, 0, true);
        b.cubeRotateTo(Cube.Rotates.UP);
        b.moveFace(Cube.Faces.FRONT, 0, 0, false);
        b.moveFace(Cube.Faces.RIGHT, 0, 0, false);
        b.moveFace(Cube.Faces.BOT, 0, 0, true);
        b.moveFace(Cube.Faces.TOP, 0, 0, false);
        b.moveFace(Cube.Faces.LEFT, 0, 0, false);
        b.moveFace(Cube.Faces.FRONT, 0, 1, true);
        b.cubeRotateTo(Cube.Rotates.RIGHT);
        b.cubeRotateTo(Cube.Rotates.RIGHT);
        b.cubeRotateTo(Cube.Rotates.UP);
        b.cubeRotateTo(Cube.Rotates.LEFT);
        cubePROGRAM.moveFace(Cube.Faces.RIGHT, 2, 2, false);
        b.moveFace(Cube.Faces.LEFT, 0, 0, true);
        System.out.println(cubePROGRAM);
        System.out.println(b);
    }
}
