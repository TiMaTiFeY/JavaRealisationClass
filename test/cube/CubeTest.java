package cube;

import org.junit.Test;
import static org.junit.Assert.*;

public class CubeTest {
    @Test
    public void moveFace() {
        Cube cubePROGRAM = new Cube(3);
        Cube cubeREAL = new Cube(3);
        cubeREAL.setFace(Cube.Faces.FRONT, new int[][]{
                {4, 3, 3},
                {0, 5, 0},
                {3, 4, 4}
        });
        cubeREAL.setFace(Cube.Faces.BACK, new int[][]{
                {1, 1, 2},
                {1, 0, 2},
                {1, 2, 5}
        });
        cubeREAL.setFace(Cube.Faces.RIGHT, new int[][]{
                {5, 4, 4},
                {3, 3, 0},
                {5, 3, 0}
        });
        cubeREAL.setFace(Cube.Faces.LEFT, new int[][]{
                {2, 0, 1},
                {2, 1, 1},
                {2, 1, 0}
        });
        cubeREAL.setFace(Cube.Faces.TOP, new int[][]{
                {5, 5, 3},
                {4, 4, 3},
                {0, 2, 2}
        });
        cubeREAL.setFace(Cube.Faces.BOT, new int[][]{
                {4, 5, 1},
                {5, 2, 5},
                {0, 4, 3}
        });
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
        assertEquals(cubePROGRAM, cubeREAL);
    }

    @Test
    public void cubeRotateTo() {
        Cube cube1 = new Cube(2);
        Cube cube2 = new Cube(2);
        Cube cube3 = new Cube(3);
        cube2.cubeRotateTo(Cube.Rotates.RIGHT);
        cube2.cubeRotateTo(Cube.Rotates.UP);
        cube2.cubeRotateTo(Cube.Rotates.RIGHT);
        cube2.cubeRotateTo(Cube.Rotates.RIGHT);
        cube2.cubeRotateTo(Cube.Rotates.DOWN);
        cube2.cubeRotateTo(Cube.Rotates.LEFT);
        assertEquals(cube1, cube2);
        assertNotEquals(cube1, cube3);
    }
}
