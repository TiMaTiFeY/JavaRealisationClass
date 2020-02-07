package main;

import cube.Cube;

public class Main {
    public static void main(String[] args) {
        Cube cube = new Cube(4);
        System.out.println(cube);
        cube.mixingRandomCube(100);
        System.out.println(cube);
    }
}
