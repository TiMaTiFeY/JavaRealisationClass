package main;

import cube.Cube;

public class Main {
    public static void main(String[] args) {
       Cube a = new Cube(3);
       System.out.println(a);
       a.mixingRandomCube(50);
       System.out.println(a);
    }
}
