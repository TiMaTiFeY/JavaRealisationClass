package cube;

import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.Random;

public final class Cube {
    private final int size;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_GREEN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private final static String[] colorsArray = {
            ANSI_WHITE + "W",   //FRONT
            ANSI_GREEN + "G",   //TOP
            ANSI_PURPLE + "P",  //RIGHT
            ANSI_BLUE + "B",    //BOT
            ANSI_RED + "R",     //LEFT
            ANSI_YELLOW + "Y"}; //BACK

    public enum Rotates {UP, RIGHT, DOWN, LEFT, FRONT_RIGHT, FRONT_LEFT}

    public enum Faces {FRONT, TOP, RIGHT, BOT, LEFT, BACK}

    private Face[] faces = new Face[6]; //Массив граней
    private int[] currentLookingFaces = new int[]{
            Faces.FRONT.ordinal(),
            Faces.TOP.ordinal(),
            Faces.RIGHT.ordinal(),
            Faces.BOT.ordinal(),
            Faces.LEFT.ordinal(),
            Faces.BACK.ordinal()
    };

    public Cube(int size) { //Конструктор
        if (size < 0) throw new NumberFormatException("CUBE SIZE MUST BE POSITIVE INTEGER");
        this.size = size;
        for (int color = 0; color < 6; color++) {
            int[][] faceArray = new int[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    faceArray[i][j] = color;
            this.faces[color] = new Face(faceArray);
        }
    }

    public Cube() {
        this.size = 3;
        for (int color = 0; color < 6; color++) {
            int[][] faceArray = new int[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    faceArray[i][j] = color;
            this.faces[color] = new Face(faceArray);
        }
    }

    public int getSize() {
        return size;
    }

    private class Face { //Грань
        private int[][] colors;

        private Face(int[][] colors) {
            this.colors = colors;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("FACE (size = ");
            sb.append(size);
            sb.append("):\n{\n");
            for (int i = 0; i < size; i++) {
                StringBuilder currentRow = new StringBuilder();
                for (int j = 0; j < size - 1; j++) {
                    currentRow.append(colorsArray[colors[i][j]]);
                    currentRow.append(" ");
                }
                currentRow.append(colorsArray[colors[i][size - 1]]);
                currentRow.append("\n");
                sb.append(currentRow);
            }
            sb.append(ANSI_RESET);
            return sb.append("}").toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof Face) {
                Face other = (Face) obj;
                boolean res = true;
                for (int i = 0; i < this.colors.length; i++)
                    res &= Arrays.equals(this.colors[i], other.colors[i]);
                return res;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return colors.hashCode();
        }
    }

    protected void setFace(Faces name, int[][] newColors) {
        faces[currentLookingFaces[name.ordinal()]].colors = newColors;
    }

    public Face getFace(Faces name) {
        return faces[currentLookingFaces[name.ordinal()]];
    }

    private int[][] getColors(Faces name) {
        return faces[currentLookingFaces[name.ordinal()]].colors;
    }

    private void rotateFace(Faces name, boolean clockwise) {
        Face ourFace = getFace(name);
        int[][] newColors = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                newColors[i][j] =
                        clockwise ? ourFace.colors[size - j - 1][i] : ourFace.colors[j][size - i - 1];
        ourFace.colors = newColors;
    }

    public void moveFace(Faces name, int delta, int secondDelta, boolean clockwise) {
        if (delta > secondDelta) throw new NumberFormatException("SECOND DELTA IS MORE FIRST");
        if (!(delta >= 0 && delta < size) || !(secondDelta >= 0 && secondDelta < size))
            throw new ArrayIndexOutOfBoundsException("WRONG DELTA FOR SIZE");
        if (delta != secondDelta) {
            for (int d = delta; d <= secondDelta; d++) moveFace(name, d, d, clockwise);
        } else {
            int bufferColor;
            int last = size - 1 - delta;
            switch (name) {
                case RIGHT:
                    if (delta == 0) rotateFace(Faces.RIGHT, clockwise);
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[i][last];
                            getColors(Faces.FRONT)[i][last] = getColors(Faces.BOT)[i][last];
                            getColors(Faces.BOT)[i][last] = getColors(Faces.BACK)[i][last];
                            getColors(Faces.BACK)[i][last] = getColors(Faces.TOP)[i][last];
                            getColors(Faces.TOP)[i][last] = bufferColor;
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[i][last];
                            getColors(Faces.FRONT)[i][last] = getColors(Faces.TOP)[i][last];
                            getColors(Faces.TOP)[i][last] = getColors(Faces.BACK)[i][last];
                            getColors(Faces.BACK)[i][last] = getColors(Faces.BOT)[i][last];
                            getColors(Faces.BOT)[i][last] = bufferColor;
                        }
                    }
                    break;
                case LEFT:
                    if (delta == 0) rotateFace(Faces.LEFT, clockwise);
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[i][delta];
                            getColors(Faces.FRONT)[i][delta] = getColors(Faces.TOP)[i][delta];
                            getColors(Faces.TOP)[i][delta] = getColors(Faces.BACK)[i][delta];
                            getColors(Faces.BACK)[i][delta] = getColors(Faces.BOT)[i][delta];
                            getColors(Faces.BOT)[i][delta] = bufferColor;

                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[i][delta];
                            getColors(Faces.FRONT)[i][delta] = getColors(Faces.BOT)[i][delta];
                            getColors(Faces.BOT)[i][delta] = getColors(Faces.BACK)[i][delta];
                            getColors(Faces.BACK)[i][delta] = getColors(Faces.TOP)[i][delta];
                            getColors(Faces.TOP)[i][delta] = bufferColor;
                        }
                    }
                    break;
                case TOP:
                    if (delta == 0) rotateFace(Faces.TOP, clockwise);
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[delta][i];
                            getColors(Faces.FRONT)[delta][i] = getColors(Faces.RIGHT)[delta][i];
                            getColors(Faces.RIGHT)[delta][i] = getColors(Faces.BACK)[last][size - 1 - i];
                            getColors(Faces.BACK)[last][size - 1 - i] = getColors(Faces.LEFT)[delta][i];
                            getColors(Faces.LEFT)[delta][i] = bufferColor;
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[delta][i];
                            getColors(Faces.FRONT)[delta][i] = getColors(Faces.LEFT)[delta][i];
                            getColors(Faces.LEFT)[delta][i] = getColors(Faces.BACK)[last][size - 1 - i];
                            getColors(Faces.BACK)[last][size - 1 - i] = getColors(Faces.RIGHT)[delta][i];
                            getColors(Faces.RIGHT)[delta][i] = bufferColor;
                        }
                    }
                    break;
                case BOT:
                    if (delta == 0) rotateFace(Faces.BOT, clockwise);
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[last][i];
                            getColors(Faces.FRONT)[last][i] = getColors(Faces.LEFT)[last][i];
                            getColors(Faces.LEFT)[last][i] = getColors(Faces.BACK)[delta][size - 1 - i];
                            getColors(Faces.BACK)[delta][size - 1 - i] = getColors(Faces.RIGHT)[last][i];
                            getColors(Faces.RIGHT)[last][i] = bufferColor;
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.FRONT)[last][i];
                            getColors(Faces.FRONT)[last][i] = getColors(Faces.RIGHT)[last][i];
                            getColors(Faces.RIGHT)[last][i] = getColors(Faces.BACK)[delta][size - 1 - i];
                            getColors(Faces.BACK)[delta][size - 1 - i] = getColors(Faces.LEFT)[last][i];
                            getColors(Faces.LEFT)[last][i] = bufferColor;
                        }
                    }
                    break;
                case FRONT:
                    if (delta == 0) rotateFace(Faces.FRONT, clockwise);
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.TOP)[last][i];
                            getColors(Faces.TOP)[last][i] = getColors(Faces.LEFT)[size - 1 - i][last];
                            getColors(Faces.LEFT)[size - 1 - i][last] = getColors(Faces.BOT)[delta][size - 1 - i];
                            getColors(Faces.BOT)[delta][size - 1 - i] = getColors(Faces.RIGHT)[i][delta];
                            getColors(Faces.RIGHT)[i][delta] = bufferColor;
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.TOP)[last][i];
                            getColors(Faces.TOP)[last][i] = getColors(Faces.RIGHT)[i][delta];
                            getColors(Faces.RIGHT)[i][delta] = getColors(Faces.BOT)[delta][size - 1 - i];
                            getColors(Faces.BOT)[delta][size - 1 - i] = getColors(Faces.LEFT)[size - 1 - i][last];
                            getColors(Faces.LEFT)[size - 1 - i][last] = bufferColor;
                        }
                    }
                    break;
                case BACK:
                    if (delta == 0) rotateFace(Faces.BACK, clockwise);
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.TOP)[delta][i];
                            getColors(Faces.TOP)[delta][i] = getColors(Faces.RIGHT)[i][last];
                            getColors(Faces.RIGHT)[i][last] = getColors(Faces.BOT)[last][size - 1 - i];
                            getColors(Faces.BOT)[last][size - 1 - i] = getColors(Faces.LEFT)[size - 1 - i][delta];
                            getColors(Faces.LEFT)[size - 1 - i][delta] = bufferColor;
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            bufferColor = getColors(Faces.TOP)[delta][i];
                            getColors(Faces.TOP)[delta][i] = getColors(Faces.LEFT)[size - 1 - i][delta];
                            getColors(Faces.LEFT)[size - 1 - i][delta] = getColors(Faces.BOT)[last][size - 1 - i];
                            getColors(Faces.BOT)[last][size - 1 - i] = getColors(Faces.RIGHT)[i][last];
                            getColors(Faces.RIGHT)[i][last] = bufferColor;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void cubeRotateTo(Rotates move) {
        int bufferIndex;
        switch (move) {
            case UP:
                bufferIndex = currentLookingFaces[Faces.FRONT.ordinal()];
                currentLookingFaces[Faces.FRONT.ordinal()] = currentLookingFaces[Faces.TOP.ordinal()];
                currentLookingFaces[Faces.TOP.ordinal()] = currentLookingFaces[Faces.BACK.ordinal()];
                currentLookingFaces[Faces.BACK.ordinal()] = currentLookingFaces[Faces.BOT.ordinal()];
                currentLookingFaces[Faces.BOT.ordinal()] = bufferIndex;
                rotateFace(Faces.RIGHT, false);
                rotateFace(Faces.LEFT, true);
                break;
            case RIGHT:
                bufferIndex = currentLookingFaces[Faces.FRONT.ordinal()];
                currentLookingFaces[Faces.FRONT.ordinal()] = currentLookingFaces[Faces.RIGHT.ordinal()];
                currentLookingFaces[Faces.RIGHT.ordinal()] = currentLookingFaces[Faces.BACK.ordinal()];
                currentLookingFaces[Faces.BACK.ordinal()] = currentLookingFaces[Faces.LEFT.ordinal()];
                currentLookingFaces[Faces.LEFT.ordinal()] = bufferIndex;
                rotateFace(Faces.TOP, true);
                rotateFace(Faces.BOT, false);
                rotateFace(Faces.BACK, true);
                rotateFace(Faces.BACK, true);
                rotateFace(Faces.RIGHT, true);
                rotateFace(Faces.RIGHT, true);
                break;
            case DOWN:
                bufferIndex = currentLookingFaces[Faces.FRONT.ordinal()];
                currentLookingFaces[Faces.FRONT.ordinal()] = currentLookingFaces[Faces.BOT.ordinal()];
                currentLookingFaces[Faces.BOT.ordinal()] = currentLookingFaces[Faces.BACK.ordinal()];
                currentLookingFaces[Faces.BACK.ordinal()] = currentLookingFaces[Faces.TOP.ordinal()];
                currentLookingFaces[Faces.TOP.ordinal()] = bufferIndex;
                rotateFace(Faces.RIGHT, false);
                rotateFace(Faces.LEFT, true);
                break;
            case LEFT:
                bufferIndex = currentLookingFaces[Faces.FRONT.ordinal()];
                currentLookingFaces[Faces.FRONT.ordinal()] = currentLookingFaces[Faces.LEFT.ordinal()];
                currentLookingFaces[Faces.LEFT.ordinal()] = currentLookingFaces[Faces.BACK.ordinal()];
                currentLookingFaces[Faces.BACK.ordinal()] = currentLookingFaces[Faces.RIGHT.ordinal()];
                currentLookingFaces[Faces.RIGHT.ordinal()] = bufferIndex;
                rotateFace(Faces.TOP, false);
                rotateFace(Faces.BOT, true);
                rotateFace(Faces.BACK, true);
                rotateFace(Faces.BACK, true);
                rotateFace(Faces.LEFT, true);
                rotateFace(Faces.LEFT, true);
                break;
            case FRONT_RIGHT:
                bufferIndex = currentLookingFaces[Faces.TOP.ordinal()];
                currentLookingFaces[Faces.TOP.ordinal()] = currentLookingFaces[Faces.LEFT.ordinal()];
                currentLookingFaces[Faces.LEFT.ordinal()] = currentLookingFaces[Faces.BOT.ordinal()];
                currentLookingFaces[Faces.BOT.ordinal()] = currentLookingFaces[Faces.RIGHT.ordinal()];
                currentLookingFaces[Faces.RIGHT.ordinal()] = bufferIndex;
                rotateFace(Faces.FRONT, true);
                rotateFace(Faces.RIGHT, true);
                rotateFace(Faces.LEFT, true);
                rotateFace(Faces.TOP, true);
                rotateFace(Faces.BOT, true);
                rotateFace(Faces.BACK, false);
                break;
            case FRONT_LEFT:
                bufferIndex = currentLookingFaces[Faces.TOP.ordinal()];
                currentLookingFaces[Faces.TOP.ordinal()] = currentLookingFaces[Faces.RIGHT.ordinal()];
                currentLookingFaces[Faces.RIGHT.ordinal()] = currentLookingFaces[Faces.BOT.ordinal()];
                currentLookingFaces[Faces.BOT.ordinal()] = currentLookingFaces[Faces.LEFT.ordinal()];
                currentLookingFaces[Faces.LEFT.ordinal()] = bufferIndex;
                rotateFace(Faces.FRONT, false);
                rotateFace(Faces.RIGHT, false);
                rotateFace(Faces.LEFT, false);
                rotateFace(Faces.TOP, false);
                rotateFace(Faces.BOT, false);
                rotateFace(Faces.BACK, true);
                break;
            default:
                break;
        }
    }

    public void mixingRandomCube(int countMoves) {
        System.out.println("Mixing: {");
        Random random = new Random();
        for (int i = 0; i < countMoves; i++) {
            Faces randomFace = Faces.values()[random.nextInt(6)];
            int randomDelta = random.nextInt(size % 2 == 0 ? size / 2 : size / 2 + 1);
            moveFace(randomFace, randomDelta, randomDelta, true);
            System.out.println("Face: " + randomFace + "  Delta: " + randomDelta);
        }
        System.out.println("}");
    }

    @Override
    public String toString() {
        String wall = ANSI_RESET + "|";
        String bot = ANSI_RESET + "=";
        StringBuilder sb = new StringBuilder("CUBE (size = ");
        sb.append(size);
        sb.append("):\n{\n");
        final String tab = " ".repeat(2 * size + 2);
        final String oneSizeLine = tab + bot.repeat(2 * size + 3) + "\n";
        //Back
        sb.append(oneSizeLine);
        for (int i = 0; i < size; i++) {
            sb.append(tab);
            sb.append(wall);
            sb.append(" ");
            for (int j = 0; j < size - 1; j++) {
                sb.append(colorsArray[getColors(Faces.BACK)[i][j]]);
                sb.append(" ");
            }
            sb.append(colorsArray[getColors(Faces.BACK)[i][size - 1]]);
            sb.append(" ");
            sb.append(wall);
            sb.append("\n");
        }
        sb.append(oneSizeLine);
        //Top
        for (int i = 0; i < size; i++) {
            sb.append(tab);
            sb.append(wall);
            sb.append(" ");
            for (int j = 0; j < size - 1; j++) {
                sb.append(colorsArray[getColors(Faces.TOP)[i][j]]);
                sb.append(" ");
            }
            sb.append(colorsArray[getColors(Faces.TOP)[i][size - 1]]);
            sb.append(" ");
            sb.append(wall);
            sb.append("\n");
        }
        String longBOT = bot.repeat(6 * size + 7) + "\n";
        sb.append(longBOT);
        //Left + Front + Right
        for (int i = 0; i < size; i++) {
            sb.append(wall);
            sb.append(" ");
            for (int j = 0; j < size - 1; j++) {
                sb.append(colorsArray[getColors(Faces.LEFT)[i][j]]);
                sb.append(" ");
            }
            sb.append(colorsArray[getColors(Faces.LEFT)[i][size - 1]]);
            sb.append(" ");
            sb.append(wall);
            sb.append(" ");
            for (int j = 0; j < size - 1; j++) {
                sb.append(colorsArray[getColors(Faces.FRONT)[i][j]]);
                sb.append(" ");
            }
            sb.append(colorsArray[getColors(Faces.FRONT)[i][size - 1]]);
            sb.append(" ");
            sb.append(wall);
            sb.append(" ");
            for (int j = 0; j < size - 1; j++) {
                sb.append(colorsArray[getColors(Faces.RIGHT)[i][j]]);
                sb.append(" ");
            }
            sb.append(colorsArray[getColors(Faces.RIGHT)[i][size - 1]]);
            sb.append(" ");
            sb.append(wall);
            sb.append("\n");
        }
        sb.append(longBOT);
        //Bot
        for (int i = 0; i < size; i++) {
            sb.append(tab);
            sb.append(wall);
            sb.append(" ");
            for (int j = 0; j < size - 1; j++) {
                sb.append(colorsArray[getColors(Faces.BOT)[i][j]]);
                sb.append(" ");
            }
            sb.append(colorsArray[getColors(Faces.BOT)[i][size - 1]]);
            sb.append(" ");
            sb.append(wall);
            sb.append("\n");
        }
        sb.append(oneSizeLine);
        sb.append(ANSI_RESET);
        return sb.append("}").toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Cube) {
            Cube other = (Cube) obj;
            boolean resultFace;
            boolean mainRes = false;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int m = 0; m < 4; m++) {
                        resultFace = true;
                        for (int i = 0; i < 6; i++) {
                            resultFace &= this.faces[this.currentLookingFaces[i]]
                                            .equals(other.faces[other.currentLookingFaces[i]]);
                        }
                        mainRes |= resultFace;
                        other.cubeRotateTo(Rotates.RIGHT);
                    }
                    other.cubeRotateTo(Rotates.FRONT_RIGHT);
                }
                other.cubeRotateTo(Rotates.UP);
                other.cubeRotateTo(Rotates.FRONT_RIGHT);
                other.cubeRotateTo(Rotates.FRONT_RIGHT);
            }
            return mainRes;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return faces.hashCode();
    }
}