import java.io.*;

public class CSVWriter {
    public static String file_name ;
    public static void header()
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(file_name), true))) {

            StringBuilder sb = new StringBuilder();

            sb.append("type"); //0
            sb.append(',');
            sb.append("number of bins"); //1
            sb.append(',');
            sb.append("total spaces"); // 2
            sb.append(',');
            sb.append("average waste"); // 3
            sb.append(',');
            sb.append("time"); ///4
            sb.append(',');
            sb.append("bin Capacity"); ///5
            sb.append(',');
            sb.append("number of objects"); ///6
            sb.append(',');
            sb.append("number of workers"); ///6

            sb.append('\n');


            writer.write(sb.toString());

          //  System.out.println("writing headers");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteData(String[] Data)
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(file_name), true))) {

            StringBuilder sb = new StringBuilder();
            for (String s : Data) {
                sb.append(s);
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);

            sb.append('\n');


            writer.write(sb.toString());

         //   System.out.println("writing data");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
