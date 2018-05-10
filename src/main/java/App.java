import java.util.*;

public class App {

    public static final int POPULATION_SIZE = 40;
    public static final int NUM_GENERATIONS = 2000;
    public static final double MUTATION_CHANCE = .125;

    public static void main(String[] args) {

        //Initialize the first population
        List<QueensBoard> initialPopulation = createPopulation(POPULATION_SIZE);
        Collections.sort(initialPopulation);
        System.out.println("Original Population (average fitness " + getAverageFitness(initialPopulation)+")");
        for (QueensBoard q : initialPopulation) {
            System.out.println(q.toString());
        }

        //For Simulation Statics
        int bestGeneration = 1;
        double bestFitness = getAverageFitness(initialPopulation);
        int currentGeneration = 1;
        boolean solutionFound = false;
        int solutionFoundInGeneration = 0;
        QueensBoard firstSolution = null;

        //Run Simulation
        List<QueensBoard> currentPopulation = initialPopulation;
        List<QueensBoard> currentMatingPopulation;
        while (currentGeneration <= NUM_GENERATIONS) {

            currentMatingPopulation = chooseMatingPopulation(currentPopulation);
            double currentAvgFitness = getAverageFitness(currentMatingPopulation);
            System.out.println("Generation " + currentGeneration +": Fitness = " + currentAvgFitness);
            if (currentAvgFitness > bestFitness) {
                bestFitness = currentAvgFitness;
                bestGeneration = currentGeneration;
            }
            currentPopulation = runGeneration(currentMatingPopulation);
            for (QueensBoard q : currentPopulation) {
                if (q.getFitness() == 1.0) {
                    System.out.println("Solution found in Generation " + currentGeneration + ". Solution is: " + q.printQueensArray());
                    solutionFound = true;
                    firstSolution = q;
                    if (solutionFoundInGeneration == 0) {
                        solutionFoundInGeneration = currentGeneration;
                    }
                }
            }
            if (solutionFound) {
                System.out.println("Solution Generation Population:");
                for (QueensBoard q : currentPopulation) {
                    System.out.println(q.toString());
                }
                break;
            }
            currentGeneration += 1;
        }

        //Print some information out about the simulation results
        System.out.println("Best Average Fitness was: " + bestFitness + " from Generation " + bestGeneration);
        if (solutionFound) {
            System.out.println("Solution First Found in Generation " + solutionFoundInGeneration);
        }
        if (firstSolution != null) {
            System.out.println("First Solution was: ");
            System.out.println("   " + firstSolution.printQueensArray());
            firstSolution.printQueensBoard();
        }
    }

    public static QueensBoard mate(QueensBoard parent1, QueensBoard parent2) {
        int[] parent1Array = parent1.getQueensArray();
        int[] parent2Array = parent2.getQueensArray();
        int[] childArray = new int[parent1Array.length];
        for (int i = 0; i < parent1Array.length; i++) {
            if (i < parent1Array.length/2) {
                childArray[i] = parent1Array[i];
            }
            else {
                childArray[i] = parent2Array[i];
            }
        }
        QueensBoard child = new QueensBoard(childArray);
        double random = Math.random();
        if (random <= MUTATION_CHANCE) {
            child.mutate();
        }
        return child;
    }

    public static List<QueensBoard> createPopulation(int numberOfIndividuals) {
        List<QueensBoard> population = new ArrayList<QueensBoard>();
        for (int i = 0; i < numberOfIndividuals; i++) {
            population.add(new QueensBoard());
        }
        return population;
    }

    public static List<QueensBoard> chooseMatingPopulation(List<QueensBoard> population) {
        List<QueensBoard> matingPopulation = new ArrayList<QueensBoard>();
        Collections.sort(population);
        int popSize = population.size();
        while (matingPopulation.size() < popSize) {
            for(QueensBoard q : population) {
                double random = Math.random();
                if (random <= q.getFitness() && matingPopulation.size() < popSize) {
                    matingPopulation.add(q);
                }
            }
        }
        Collections.sort(matingPopulation);
        return matingPopulation;
    }

    public static List<QueensBoard> runGeneration(List<QueensBoard> currentGeneration) {
        List<QueensBoard> nextGeneration = new ArrayList<QueensBoard>();
        for(int i = 0; i < currentGeneration.size()-1;i++) {
            nextGeneration.add(mate(currentGeneration.get(i), currentGeneration.get(i+1)));
        }
        //For now to make sure we have the same population size as before, best fit also mates with third best fit. will clean this up later
        nextGeneration.add(mate(currentGeneration.get(0), currentGeneration.get(2)));
        Collections.sort(nextGeneration);
        return nextGeneration;
    }

    public static double getAverageFitness(List<QueensBoard> population) {
        double average = 0.0;
        for(QueensBoard q : population) {
            average += q.getFitness();
        }
        return average / (double)population.size();
    }
}
