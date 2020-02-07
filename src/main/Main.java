package main;

import cube.Cube;

public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube(3);
        System.out.println(cube);
        cube.mixingRandomCube(5);
        System.out.println(cube);
        cube.cubeRotateTo(Cube.CubeRotates.LEFT);
        System.out.println(cube);
    }
}
