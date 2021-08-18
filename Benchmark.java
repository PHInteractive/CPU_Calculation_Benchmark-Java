class Calculations implements Runnable{

    private int ident = 0;

    public Calculations(int id) {
        this.ident = id;
    }

    @Override
    public void run(){
        do {
            try{
            Thread.sleep(3000);
            }catch(InterruptedException e){
                System.out.println( "thread interrupted");
            }
        } while (Benchmark.RunBenchmark == false);	//used to stop function from starting before all threads are created
        
        int currentcalculations = 0;
        int foundPrimeNumbers = 0;
        int count = 0;
        int currentIteration;
        int currentNumber = 0;
        System.out.println("Starting on ID " + ident);
        do {
            for (currentIteration = 2; currentIteration <= currentNumber / 2; currentIteration++) {
                if (currentNumber % currentIteration == 0) {
                    currentcalculations++;
                    count++;
                    break;
                }
            }
            if (count == 0 && currentNumber != 1 && currentNumber != 0) {
                foundPrimeNumbers++;
            }
            currentNumber++;
            count = 0;
        } while (Benchmark.ThreadExit == false);
        Benchmark.CalculationDone[ident] = currentcalculations;
        Benchmark.PrimeNumbersFound[ident] = foundPrimeNumbers;
    }
}

public class Benchmark extends Thread{

    static private int LogicalProcessorCores;
    static public boolean RunBenchmark = false;
    static public boolean ThreadExit = false;
    static private int BenchmarkRuntimeSeconds = 10;
    static public int[] PrimeNumbersFound;
    static public int[] CalculationDone;


    public Benchmark(){
    }
    
    public static void main(String argv[]){
        LogicalProcessorCores = Runtime.getRuntime().availableProcessors();
        System.out.println(LogicalProcessorCores + " Logical Cores detectet");

        PrimeNumbersFound = new int[LogicalProcessorCores];
        CalculationDone = new int [LogicalProcessorCores];

        //dynamicly Spawn Threads
        Thread[] t1 = new Thread[50];
        for(int i=0; i<= LogicalProcessorCores - 1 ; i++){
            t1[i] = new Thread(new Calculations(i));
            t1[i].start();    
        }

        try{
        Thread.sleep(3000);
        }catch(InterruptedException e){
            System.out.println( "thread interrupted");
        }

        RunBenchmark = true;
        System.out.println(RunBenchmark);
        try{
        Thread.sleep(BenchmarkRuntimeSeconds * 1000);
        }catch(InterruptedException e){
            System.out.println( "thread interrupted");
        }

        ThreadExit = true;

        //Wait for all createt Threads to finish
        try  
        {    
            for(int i=0; i<= LogicalProcessorCores - 1; i++){
                t1[i].join();    
                System.out.println("Calculation Thread " + i + " finished");
            }
        }catch(Exception e){System.out.println(e);}  

        int TotalCalculations = 0;
        int TotalPrimeNumbers = 0;
        try  
        {    
            for(int i=0; i<= LogicalProcessorCores - 1; i++){
                TotalCalculations = TotalCalculations + CalculationDone[i];
                TotalPrimeNumbers = TotalPrimeNumbers + PrimeNumbersFound[i];
            }
        }catch(Exception e){System.out.println(e);}  
    
        System.out.println("Total PrimeNumbers found: " + TotalPrimeNumbers);
        System.out.println("Total Calculations done: " + TotalCalculations);
    }

}

