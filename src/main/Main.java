package main;

import cube.Cube;

public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube(3);
        System.out.println(cube);
        cube.moveFace(Cube.Faces.FRONT, 0, 0, true);
        System.out.println(cube);
        cube.cubeRotateTo(Cube.Rotates.RIGHT);
        cube.mixingRandomCube(5);
        System.out.println(cube);
        System.out.println(cube.getSize());
    }
}
