package cube;

import java.lang.StringBuilder;

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
            ANSI_WHITE + "W",
            ANSI_GREEN + "G",
            ANSI_PURPLE + "P",
            ANSI_BLUE + "B",
            ANSI_RED + "R",
            ANSI_YELLOW + "Y"};

    private final static int countSymbolsForPrint = 6;

    public enum CubeRotates {UP, RIGHT, DOWN, LEFT}

    public enum FacesName {FRONT, TOP, RIGHT, BOT, LEFT, BACK}

    private Face[] faces = new Face[6]; //Массив граней
    private int[] currentLookingFaces = new int[]{0, 1, 2, 3, 4, 5};
    //0 - front, 1 - top, 2 - right, 3 - under, 4 - left, 5 - back

    public Cube(int size) { //Конструктор
        if (size < 0) throw new NumberFormatException("CUBE SIZE MUST BE POSITIVE INTEGER");
        this.size = size;
        for (int color = 0; color < 6; color++) {
            int[][] faceArray = new int[size][size];
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    faceArray[i][j] = color;
            this.faces[color] = new Face(size, faceArray);
        }
    }

    public int getSize() {
        return size;
    }

    private class Face { //Грань
        private int faceSize;
        private int[][] colors;

        private Face(int faceSize, int[][] colors) {
            this.faceSize = faceSize;
            this.colors = colors;
        }

        public int getFaceSize() {
            return faceSize;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("FACE (size = " + faceSize + "):\n{\n");
            for (int i = 0; i < faceSize; i++) {
                StringBuilder currentRow = new StringBuilder();
                for (int j = 0; j < faceSize - 1; j++) {
                    currentRow.append(colorsArray[colors[i][j]].substring(0, countSymbolsForPrint) + " ");
                }
                currentRow.append(colorsArray[colors[i][faceSize - 1]].substring(0, countSymbolsForPrint) + "\n");
                sb.append(currentRow);
            }
            return sb.append(ANSI_RESET + "}").toString();
        }
    }

    public Face getFace(FacesName name) {
        int num = name.ordinal();
        if (!(num >= 0 && num <= 5))
            throw new ArrayIndexOutOfBoundsException("WRONG NAME OF FACE: " + num);
        return faces[currentLookingFaces[num]];
    }

    private void setFace(FacesName name, Face newFace) {
        faces[currentLookingFaces[name.ordinal()]] = newFace;
    }

    private void rotateFace(FacesName name, boolean clockwise) {
        Face ourFace = getFace(name);
        int len = ourFace.faceSize;
        Face newFaceAfterRotate = new Face(len, new int[len][len]);
        for (int i = 0; i < len; i++)
            for (int j = 0; j < len; j++)
                newFaceAfterRotate.colors[i][j] =
                        clockwise ? ourFace.colors[len - j - 1][i] : ourFace.colors[j][len - i - 1];
        setFace(name, newFaceAfterRotate);
    }

    public void moveFace(FacesName name, int delta, boolean clockwise, boolean allLayersForDeltaMove) {
        int[] bufferLine = new int[size];
        if (!(delta >= 0 && delta < size))
            throw new ArrayIndexOutOfBoundsException("WRONG DELTA FOR SIZE = " + size + ": " + delta);
        if (allLayersForDeltaMove) {
            for (int d = 0; d <= delta; d++) moveFace(name, d, clockwise, false);
        } else {
            switch (name) {
                case RIGHT:
                    if (delta == 0) rotateFace(FacesName.RIGHT, clockwise);
                    for (int i = 0; i < size; i++)
                        bufferLine[i] =
                                getFace(FacesName.FRONT).colors[i][size - 1 - delta];
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[i][size - 1 - delta] =
                                    getFace(FacesName.BOT).colors[i][size - 1 - delta];
                            getFace(FacesName.BOT).colors[i][size - 1 - delta] =
                                    getFace(FacesName.BACK).colors[i][size - 1 - delta];
                            getFace(FacesName.BACK).colors[i][size - 1 - delta] =
                                    getFace(FacesName.TOP).colors[i][size - 1 - delta];
                            getFace(FacesName.TOP).colors[i][size - 1 - delta] = bufferLine[i];
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[i][size - 1 - delta] =
                                    getFace(FacesName.TOP).colors[i][size - 1 - delta];
                            getFace(FacesName.TOP).colors[i][size - 1 - delta] =
                                    getFace(FacesName.BACK).colors[i][size - 1 - delta];
                            getFace(FacesName.BACK).colors[i][size - 1 - delta] =
                                    getFace(FacesName.BOT).colors[i][size - 1 - delta];
                            getFace(FacesName.BOT).colors[i][size - 1 - delta] = bufferLine[i];
                        }
                    }
                    break;
                case LEFT:
                    if (delta == 0) rotateFace(FacesName.LEFT, clockwise);
                    for (int i = 0; i < size; i++)
                        bufferLine[i] =
                                getFace(FacesName.FRONT).colors[i][delta];
                    if (!clockwise) {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[i][delta] =
                                    getFace(FacesName.BOT).colors[i][delta];
                            getFace(FacesName.BOT).colors[i][delta] =
                                    getFace(FacesName.BACK).colors[i][delta];
                            getFace(FacesName.BACK).colors[i][delta] =
                                    getFace(FacesName.TOP).colors[i][delta];
                            getFace(FacesName.TOP).colors[i][delta] = bufferLine[i];
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[i][delta] =
                                    getFace(FacesName.TOP).colors[i][delta];
                            getFace(FacesName.TOP).colors[i][delta] =
                                    getFace(FacesName.BACK).colors[i][delta];
                            getFace(FacesName.BACK).colors[i][delta] =
                                    getFace(FacesName.BOT).colors[i][delta];
                            getFace(FacesName.BOT).colors[i][delta] = bufferLine[i];
                        }
                    }
                    break;
                case TOP:
                    if (delta == 0) rotateFace(FacesName.TOP, clockwise);
                    for (int i = 0; i < size; i++)
                        bufferLine[i] =
                                getFace(FacesName.FRONT).colors[delta][i];
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[delta][i] =
                                    getFace(FacesName.RIGHT).colors[delta][i];
                            getFace(FacesName.RIGHT).colors[delta][i] =
                                    getFace(FacesName.BACK).colors[size - 1 - delta][size - 1 - i];
                            getFace(FacesName.BACK).colors[size - 1 - delta][size - 1 - i] =
                                    getFace(FacesName.LEFT).colors[delta][i];
                            getFace(FacesName.LEFT).colors[delta][i] = bufferLine[i];
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[delta][i] =
                                    getFace(FacesName.LEFT).colors[delta][i];
                            getFace(FacesName.LEFT).colors[delta][i] =
                                    getFace(FacesName.BACK).colors[size - 1 - delta][size - 1 - i];
                            getFace(FacesName.BACK).colors[size - 1 - delta][size - 1 - i] =
                                    getFace(FacesName.RIGHT).colors[delta][i];
                            getFace(FacesName.RIGHT).colors[delta][i] = bufferLine[i];
                        }
                    }
                    break;
                case BOT:
                    if (delta == 0) rotateFace(FacesName.BOT, clockwise);
                    for (int i = 0; i < size; i++)
                        bufferLine[i] =
                                getFace(FacesName.FRONT).colors[size - 1 - delta][i];
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[size - 1 - delta][i] =
                                    getFace(FacesName.LEFT).colors[size - 1 - delta][i];
                            getFace(FacesName.LEFT).colors[size - 1 - delta][i] =
                                    getFace(FacesName.BACK).colors[delta][size - 1 - i];
                            getFace(FacesName.BACK).colors[delta][size - 1 - i] =
                                    getFace(FacesName.RIGHT).colors[size - 1 - delta][i];
                            getFace(FacesName.RIGHT).colors[size - 1 - delta][i] = bufferLine[i];
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.FRONT).colors[size - 1 - delta][i] =
                                    getFace(FacesName.RIGHT).colors[size - 1 - delta][i];
                            getFace(FacesName.RIGHT).colors[size - 1 - delta][i] =
                                    getFace(FacesName.BACK).colors[delta][size - 1 - i];
                            getFace(FacesName.BACK).colors[delta][size - 1 - i] =
                                    getFace(FacesName.LEFT).colors[size - 1 - delta][i];
                            getFace(FacesName.LEFT).colors[size - 1 - delta][i] = bufferLine[i];
                        }
                    }
                    break;
                case FRONT:
                    if (delta == 0) rotateFace(FacesName.FRONT, clockwise);
                    for (int i = 0; i < size; i++)
                        bufferLine[i] =
                                getFace(FacesName.TOP).colors[size - 1 - delta][i];
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.TOP).colors[size - 1 - delta][i] =
                                    getFace(FacesName.LEFT).colors[size - 1 - i][size - 1 - delta];
                            getFace(FacesName.LEFT).colors[size - 1 - i][size - 1 - delta] =
                                    getFace(FacesName.BOT).colors[delta][size - 1 - i];
                            getFace(FacesName.BOT).colors[delta][size - 1 - i] =
                                    getFace(FacesName.RIGHT).colors[i][delta];
                            getFace(FacesName.RIGHT).colors[i][delta] = bufferLine[i];
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.TOP).colors[size - 1 - delta][i] =
                                    getFace(FacesName.RIGHT).colors[i][delta];
                            getFace(FacesName.RIGHT).colors[i][delta] =
                                    getFace(FacesName.BOT).colors[delta][size - 1 - i];
                            getFace(FacesName.BOT).colors[delta][size - 1 - i] =
                                    getFace(FacesName.LEFT).colors[size - 1 - i][size - 1 - delta];
                            getFace(FacesName.LEFT).colors[size - 1 - i][size - 1 - delta] = bufferLine[i];
                        }
                    }
                    break;
                case BACK:
                    if (delta == 0) rotateFace(FacesName.BACK, clockwise);
                    for (int i = 0; i < size; i++)
                        bufferLine[i] =
                                getFace(FacesName.TOP).colors[delta][i];
                    if (clockwise) {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.TOP).colors[delta][i] =
                                    getFace(FacesName.RIGHT).colors[i][size - 1 - delta];
                            getFace(FacesName.RIGHT).colors[i][size - 1 - delta] =
                                    getFace(FacesName.BOT).colors[size - 1 - delta][size - 1 - i];
                            getFace(FacesName.BOT).colors[size - 1 - delta][size - 1 - i] =
                                    getFace(FacesName.LEFT).colors[size - 1 - i][delta];
                            getFace(FacesName.LEFT).colors[size - 1 - i][delta] = bufferLine[i];
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            getFace(FacesName.TOP).colors[delta][i] =
                                    getFace(FacesName.LEFT).colors[size - 1 - i][delta];
                            getFace(FacesName.LEFT).colors[size - 1 - i][delta] =
                                    getFace(FacesName.BOT).colors[size - 1 - delta][size - 1 - i];
                            getFace(FacesName.BOT).colors[size - 1 - delta][size - 1 - i] =
                                    getFace(FacesName.RIGHT).colors[size - 1 - i][size - 1 - delta];
                            getFace(FacesName.RIGHT).colors[size - 1 - i][size - 1 - delta] = bufferLine[i];
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void cubeRotateTo(CubeRotates move) {
        switch (move) {
            case UP:
                currentLookingFaces = new int[]{
                        currentLookingFaces[FacesName.TOP.ordinal()],//TOP -> FRONT
                        currentLookingFaces[FacesName.BACK.ordinal()],//BACK -> TOP
                        currentLookingFaces[FacesName.RIGHT.ordinal()],//RIGHT
                        currentLookingFaces[FacesName.FRONT.ordinal()],//FRONT -> UNDER
                        currentLookingFaces[FacesName.LEFT.ordinal()],//LEFT
                        currentLookingFaces[FacesName.BOT.ordinal()],//BOT -> BACK
                };
                rotateFace(FacesName.RIGHT, false);
                rotateFace(FacesName.LEFT, true);
                break;
            case RIGHT:
                currentLookingFaces = new int[]{
                        currentLookingFaces[FacesName.RIGHT.ordinal()],//RIGHT -> FRONT
                        currentLookingFaces[FacesName.TOP.ordinal()],//TOP
                        currentLookingFaces[FacesName.BACK.ordinal()],//BACK -> RIGHT
                        currentLookingFaces[FacesName.BOT.ordinal()],//BOT
                        currentLookingFaces[FacesName.FRONT.ordinal()],//FRONT -> LEFT
                        currentLookingFaces[FacesName.LEFT.ordinal()],//LEFT -> BACK
                };
                rotateFace(FacesName.TOP, true);
                rotateFace(FacesName.BOT, false);
                rotateFace(FacesName.BACK, true);
                rotateFace(FacesName.BACK, true);
                rotateFace(FacesName.RIGHT, true);
                rotateFace(FacesName.RIGHT, true);

                break;
            case DOWN:
                currentLookingFaces = new int[]{
                        currentLookingFaces[FacesName.BOT.ordinal()],//BOT -> FRONT
                        currentLookingFaces[FacesName.FRONT.ordinal()],//FRONT -> TOP
                        currentLookingFaces[FacesName.RIGHT.ordinal()],//RIGHT
                        currentLookingFaces[FacesName.BACK.ordinal()],//BACK -> BOT
                        currentLookingFaces[FacesName.LEFT.ordinal()],//LEFT
                        currentLookingFaces[FacesName.TOP.ordinal()],//TOP -> BACK
                };
                rotateFace(FacesName.RIGHT, false);
                rotateFace(FacesName.LEFT, true);
                break;
            case LEFT:
                currentLookingFaces = new int[]{
                        currentLookingFaces[FacesName.LEFT.ordinal()],//LEFT -> FRONT
                        currentLookingFaces[FacesName.TOP.ordinal()],//TOP
                        currentLookingFaces[FacesName.BACK.ordinal()],//BACK -> RIGHT
                        currentLookingFaces[FacesName.BOT.ordinal()],//BOT
                        currentLookingFaces[FacesName.FRONT.ordinal()],//FRONT -> LEFT
                        currentLookingFaces[FacesName.RIGHT.ordinal()],//RIGHT -> BACK
                };
                rotateFace(FacesName.TOP, false);
                rotateFace(FacesName.BOT, true);
                rotateFace(FacesName.BACK, true);
                rotateFace(FacesName.BACK, true);
                rotateFace(FacesName.LEFT, true);
                rotateFace(FacesName.LEFT, true);
                break;
            default:
                break;
        }
    }

    private static int randomIntInRange0To(int num) {
        return (int) (Math.random() * 100) % (num + 1);
    }

    public void mixingRandomCube(int countMoves) {
        for (int i = 0; i < countMoves; i++) {
            FacesName randomFace = FacesName.values()[randomIntInRange0To(5)];
            int randomDelta = randomIntInRange0To(size % 2 == 0 ? size / 2 - 1 : size / 2);
            this.moveFace(randomFace, randomDelta, true, false);
            //System.out.println(randomFace + " " + randomDelta);
        }
    }

    @Override
    public String toString() {
        String wall = ANSI_RESET + "|";
        String bot = ANSI_RESET + "=";
        StringBuilder sb = new StringBuilder("CUBE (size = " + size + "):\n{\n");
        final String tab = " ".repeat(2 * size + 2);
        final String oneSizeLine = tab + bot.repeat(2 * size + 3) + "\n";
        //Back
        sb.append(oneSizeLine);
        for (int i = 0; i < size; i++) {
            sb.append(tab + wall + " ");
            for (int j = 0; j < size - 1; j++)
                sb.append(colorsArray[faces[currentLookingFaces[FacesName.BACK.ordinal()]]
                        .colors[i][j]].substring(0, countSymbolsForPrint) + " ");
            sb.append(colorsArray[faces[currentLookingFaces[FacesName.BACK.ordinal()]]
                    .colors[i][size - 1]].substring(0, countSymbolsForPrint) + " " + wall + "\n");
        }
        sb.append(oneSizeLine);
        //Top
        for (int i = 0; i < size; i++) {
            sb.append(tab + wall + " ");
            for (int j = 0; j < size - 1; j++)
                sb.append(colorsArray[faces[currentLookingFaces[FacesName.TOP.ordinal()]]
                        .colors[i][j]].substring(0, countSymbolsForPrint) + " ");
            sb.append(colorsArray[faces[currentLookingFaces[FacesName.TOP.ordinal()]]
                    .colors[i][size - 1]].substring(0, countSymbolsForPrint) + " " + wall + "\n");
        }
        String longBOT = bot.repeat(6 * size + 7) + "\n";
        sb.append(longBOT);
        //Left + Front + Right
        for (int i = 0; i < size; i++) {
            sb.append(wall + " ");
            for (int j = 0; j < size - 1; j++)
                sb.append(colorsArray[faces[currentLookingFaces[FacesName.LEFT.ordinal()]]
                        .colors[i][j]].substring(0, countSymbolsForPrint) + " ");
            sb.append(colorsArray[faces[currentLookingFaces[FacesName.LEFT.ordinal()]]
                    .colors[i][size - 1]].substring(0, countSymbolsForPrint) + " " + wall + " ");
            for (int j = 0; j < size - 1; j++)
                sb.append(colorsArray[faces[currentLookingFaces[FacesName.FRONT.ordinal()]]
                        .colors[i][j]].substring(0, countSymbolsForPrint) + " ");
            sb.append(colorsArray[faces[currentLookingFaces[FacesName.FRONT.ordinal()]]
                    .colors[i][size - 1]].substring(0, countSymbolsForPrint) + " " + wall + " ");
            for (int j = 0; j < size - 1; j++)
                sb.append(colorsArray[faces[currentLookingFaces[FacesName.RIGHT.ordinal()]]
                        .colors[i][j]].substring(0, countSymbolsForPrint) + " ");
            sb.append(colorsArray[faces[currentLookingFaces[FacesName.RIGHT.ordinal()]]
                    .colors[i][size - 1]].substring(0, countSymbolsForPrint) + " " + wall + "\n");
        }
        sb.append(longBOT);
        //Bot
        for (int i = 0; i < size; i++) {
            sb.append(tab + wall + " ");
            for (int j = 0; j < size - 1; j++)
                sb.append(colorsArray[faces[currentLookingFaces[FacesName.BOT.ordinal()]]
                        .colors[i][j]].substring(0, countSymbolsForPrint) + " ");
            sb.append(colorsArray[faces[currentLookingFaces[FacesName.BOT.ordinal()]]
                    .colors[i][size - 1]].substring(0, countSymbolsForPrint) + " " + wall + "\n");
        }
        sb.append(oneSizeLine + ANSI_RESET + "}");
        return sb.toString();
    }
}
