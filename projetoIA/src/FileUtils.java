import java.io.*;
import java.util.Scanner;

public class FileUtils {
    public static void main(String[] args) {

    }

    public static String[][] lerDoCSV(String nomeArquivo) {
        String[][] entradas = new String[21][64];
        String[] entrada;
        try {
            int counter = 1;
            BufferedReader leitorCSV = new BufferedReader(new FileReader(nomeArquivo + ".csv"));
            String linha = leitorCSV.readLine();
            while (linha != null) {
                entrada = linha.split(",");
                entradas[counter - 1] = entrada;
                counter++;
                linha = leitorCSV.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entradas;
    }

    public void createFile() {
        try {
            File myFile = new File("pesos.txt");
            if (myFile.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");
        } catch (IOException e) {
            System.out.println("Error ocurred");
            e.printStackTrace();
        }
    }

    public void writeArrayToFile(double array[][], String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName + ".txt");
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    writer.write(String.valueOf(array[i][j]) + " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[][] readArrayFromFile(String fileName) {
        double[][] pesos = new double[9][7];
        try {
            File myFile = new File(fileName + ".txt");
            Scanner scanner = new Scanner(myFile);
            int count = 0;
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                double[] linhaDePesos = new double[7];
                String[] rowWeights = row.split(" ");
                for (int i = 0; i < rowWeights.length; i++) {
                    linhaDePesos[i] = Double.valueOf(rowWeights[i]);
                }
                pesos[count++] = linhaDePesos;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pesos;
    }


}
