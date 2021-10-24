import java.util.*;
import java.io.*;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class A5{
    static long endTime;
    static long startTime;
    static long elapsedTime;
    static String [] Data = new String[7];
    public final static String[] Algorithm={"FF","FFDS","FFDP"};
    static int CurrentTrial;
    static final double Capacity=500;
    static final double minSize = 0.2*Capacity;
    static final double maxSize= 0.8*Capacity;
    static boolean logs=false;
    static int ParralelLevel=0;


    static int objects=100000 + (10000 *6);
    static int Maxparals=3;
    static final int MaxTrials=1;
    static final int expermint_type=1;

    public static void main(String[] args)
    {
        if(expermint_type==0)
        {
            final double[] Weights = GetData(objects,"temp.txt");
            //final double[] Weights = ReadData("temp.txt");

            CSVWriter.file_name ="24_10__Ex2 take 2.csv";
            //CSVWriter.header();

            for ( CurrentTrial=0; CurrentTrial< MaxTrials; CurrentTrial++)
            {
                for (String alg : Algorithm)
                {

                    //String alg="FFDP";
                    Data = new String[8];
                    Double[] temp_objects = new Double[objects];
                    for (int k=0;k<Weights.length;k++)
                    {
                        temp_objects[k]=Weights[k];
                    }

                    BinPack( temp_objects, alg);

                    System.out.println(" alg "+ alg);
                    System.out.println(" trail "+ CurrentTrial);

                    System.gc();
                    Runtime.getRuntime().gc();
                }
            }

        }
        else
        {
            final double[] Weights = GetData(objects,"temp.txt");
            //final double[] Weights = ReadData("temp.txt");

            CSVWriter.file_name ="FFDP comaprison2.csv";
            CSVWriter.header();
            for ( CurrentTrial=0; CurrentTrial< MaxTrials; CurrentTrial++)
            {

                    for (ParralelLevel=2; ParralelLevel<Maxparals; ParralelLevel++)
                    {
                        Data = new String[8];
                        double[] temp_objects =new double[objects];
                        for (int k=0;k<Weights.length;k++)
                        {
                            temp_objects[k]=Weights[k];
                        }

                        Parralized_BinPack( temp_objects);

                        System.out.println(" level "+ ParralelLevel);
                        System.out.println(" trail "+ CurrentTrial);

                        System.gc();
                        Runtime.getRuntime().gc();
                    }
            }

        }





        System.exit(0);


    }//End of main



    private static double[] ReadData(String filename) {
        double[] allValues = new double[objects];

        ArrayList<Float> result = new ArrayList<>();

        try (Scanner s = new Scanner(new FileReader(filename))) {
            while (s.hasNext()) {
                result.add(s.nextFloat());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int k=0;k<result.size();k++)
        {
            allValues[k]= result.get(k);
        }
        return allValues;
    }


    public static double[] GetData(int size, String file_name)
    {
        double[] Weights = new double[size];
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(file_name)))) {

            StringBuilder sb = new StringBuilder();

            double MEAN = Capacity/2;
            double VARIANCE = Capacity/4;
            Random fRandom = new Random();
            for (int idx = 0; idx < size; idx++){
                double rand = MEAN+ fRandom.nextGaussian() * VARIANCE;
                if(rand>minSize && rand<maxSize)
                {
                    Weights[idx]=Math.round(rand * 100.0) / 100.0;
                    sb.append(Weights[idx]);
                    sb.append('\n');
                }
                else {idx--;}
            }
            writer.write(sb.toString());
           // System.out.println("writing inputs");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Weights;
    }




    public static void BinPack( Double stuff[] ,String method)
    {
        ArrayList<Double> bins = new ArrayList<Double>();
        bins.add(Capacity);
        double NumberBins =1;
        startTime = System.nanoTime();

        if(method=="FFDS")
        {
            Arrays.sort(stuff,Collections.reverseOrder());
           // System.out.println("here");
        }
        if(method=="FFDP")
        {
            //ParallelMergeSort.Level=ParralelLevel;
            //ParallelMergeSort.parallelMergeSort(stuff);
            Arrays.parallelSort(stuff,Collections.reverseOrder());
        }
        for(Double item :stuff) {
            boolean placed = false;
            for (int i=0;i< bins.size();i++) {
                if(bins.get(i) >=item)
                {
                    Double temp= bins.get(i);
                    bins.set(i,temp-item);
                    placed = true;
                    break;
                }
            }
            if(!placed)
            {
                bins.add(Capacity-item);
                placed = true;
                NumberBins++;
            }
        }
        endTime = System.nanoTime();
        elapsedTime = (endTime- startTime);

        Data[0]=method;
        Data[1]=""+NumberBins;
        Double sum= Double.valueOf(0);
        for(Double bin : bins) {sum+=bin;}
        sum=Math.round(sum * 100.0) / 100.0;
        Data[2]=""+sum;
        Data[3]=""+sum/NumberBins;
        Data[4]=""+elapsedTime;
        Data[5]=""+Capacity;
        Data[6]=""+objects;
        Data[7]=""+ParralelLevel;
        CSVWriter.WriteData(Data);
        if(logs)
        {
            System.out.println("methon = "+ method);
            for(Double item :stuff){
                System.out.println(item);
            }
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
        }
        stuff=null;
    }
    private static void Parralized_BinPack(double[] stuff) {

        ArrayList<Double> bins = new ArrayList<Double>();
        bins.add(Capacity);
        double NumberBins =1;
        startTime = System.nanoTime();

        ParallelRankSort sort = new ParallelRankSort(objects,ParralelLevel,stuff);
        double[]sorted = sort.start();
        /////////////


        for(Double item :sorted) {
            boolean placed = false;
            for (int i=0;i< bins.size();i++) {
                if(bins.get(i) >=item)
                {
                    Double temp= bins.get(i);
                    bins.set(i,temp-item);
                    placed = true;
                    break;
                }
            }
            if(!placed)
            {
                bins.add(Capacity-item);
                placed = true;
                NumberBins++;
            }
        }
        endTime = System.nanoTime();
        elapsedTime = (endTime- startTime);
//        elapsedTime=elapsedTime/1000000;

        Data[0]="FFDP";
        Data[1]=""+NumberBins;
        Double sum= Double.valueOf(0);
        for(Double bin : bins) {sum+=bin;}
        sum=Math.round(sum * 100.0) / 100.0;
        Data[2]=""+sum;
        Data[3]=""+sum/NumberBins;
        Data[4]=""+elapsedTime;
        Data[5]=""+Capacity;
        Data[6]=""+objects;
        Data[7]=""+ParralelLevel;
        CSVWriter.WriteData(Data);
        if(logs)
        {
            System.out.println("methon = FFDP");
            for(Double item :stuff){
                System.out.println(item);
            }
            System.out.println(" ");
            System.out.println(" ");
            System.out.println(" ");
        }
        stuff=null;
        sorted=null;
    }


}//End of A5.Java