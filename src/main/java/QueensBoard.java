public class QueensBoard implements Comparable {

    private int[] queensArray;
    private double fitness;
    private int sizeOfBoard;
    private final int defaultSizeOfBoard = 8;

    public QueensBoard() {
        this.sizeOfBoard = defaultSizeOfBoard;
        this.queensArray = createQueensArray(sizeOfBoard);
        this.fitness = computeFitness(queensArray);
    }

    public QueensBoard(int sizeOfBoard) {
        this.sizeOfBoard = sizeOfBoard;
        this.queensArray = createQueensArray(sizeOfBoard);
        this.fitness = computeFitness(queensArray);
    }

    public QueensBoard(int[] queensArray) {
        this.sizeOfBoard = queensArray.length;
        this.queensArray = queensArray;
        this.fitness = computeFitness(queensArray);
    }

    public int[] getQueensArray() {
        return queensArray;
    }

    public double getFitness() {
        return fitness;
    }

    private int[] createQueensArray(int sizeOfBoard) {
        int[] board = new int[sizeOfBoard];
        for (int i = 0; i < board.length; i++) {
            board[i] = (int)Math.floor(Math.random() * sizeOfBoard);
        }
        return board;
    }

    public String printQueensArray() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < queensArray.length; i++) {
            sb.append( queensArray[i] + " ");
        }
        return sb.toString();
    }

    private int[][] createQueensBoard() {
        int[][] currentBoard = new int[queensArray.length][queensArray.length];

        for (int i = 0; i < queensArray.length; i++) {
            currentBoard[queensArray[i]][i] = 1;
        }
        return currentBoard;
    }

    public void printQueensBoard() {
        int[][] queensBoard = this.createQueensBoard();
        for (int i = 0; i < queensBoard.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < queensBoard.length; j++) {
                System.out.print(queensBoard[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("  |---------------");
        System.out.println("  |0 1 2 3 4 5 6 7");
        System.out.println("  |fitness = " + this.getFitness());
        System.out.println("  |---------------\n");
    }

    public int computeRawFitness(int[] queensArray) {
        int fitnessCounter = 0;
        for (int i = 0; i < queensArray.length-1; i++) {
            for (int j = i+1; j < queensArray.length; j++) {
                //check if same row
                if (queensArray[i] == queensArray[j]) {
                    fitnessCounter += 1;
                }
                //check if diagonal threat
                int indexOffset = j - i;
                if (queensArray[j] - indexOffset == queensArray[i] || queensArray[i] - indexOffset == queensArray[j]) {
                    fitnessCounter += 1;
                }
            }
        }
        //System.out.println("Raw Fitness = " + fitnessCounter);
        return fitnessCounter;
    }

    //computes the worst possible fitness score possible given the size of the board
    public int computeWorstFitnessScore(int length) {
        int[] badBoard = new int[length];
        for (int i = 0; i < length; i++) {
            badBoard[i] = 1;
        }
        return computeRawFitness(badBoard);
    }

    public double normalizeFitness(int fitnessScore, int worstFitnessScore) {
        //fitnessScore = fitnessScore + 1; //in case score is zero
        //worstFitnessScore = worstFitnessScore + 1; // in case a score is zero
        double normalizedFitness = 1.0 - ((double)fitnessScore/((double)worstFitnessScore));
        return normalizedFitness;
    }

    private double computeFitness(int[] queensArray) {
        int rawFitness = computeRawFitness(queensArray);
        int worstFitness = computeWorstFitnessScore(queensArray.length);
        double normalizedFitness = normalizeFitness(rawFitness, worstFitness);
        return normalizedFitness;
    }

    public void mutate() {
        int randomIndex = (int)Math.floor(Math.random() * 8);
        int randomNumber = (int)Math.floor(Math.random() * 8);
        this.queensArray[randomIndex] = randomNumber;
        this.fitness = computeFitness(queensArray);
    }

    public String toString() {
        return printQueensArray() + ", Fitness = " + this.getFitness();
    }

    public int compareTo(Object compareQueen) {
        double compareFitness = ((QueensBoard)compareQueen).getFitness();
        return (int)(compareFitness*100 - this.getFitness()*100);
    }

}
