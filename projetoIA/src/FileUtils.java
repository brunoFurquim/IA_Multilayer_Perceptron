import Models.Camada;

import java.io.*;
import java.nio.Buffer;
import java.util.Scanner;

public class FileUtils {
    public static void main(String[] args) {

    }

    public static void salvarPesosCSV(Camada camada, String nomeArquivo) {
        try {
            FileWriter writer = new FileWriter(nomeArquivo + ".csv");
            for (int i = 0; i < camada.neuronios.length; i++) {
                for (int j = 0; j < camada.neuronios[i].pesos.length; j++) {
                    writer.write(String.valueOf(camada.neuronios[i].pesos[j]));
                    if (j != camada.neuronios[i].pesos.length - 1) writer.append(',');
                }
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Camada lerPesosCSV(Camada camada, String nomeArquivo) {
        String[][] pesos = new String[camada.neuronios.length][camada.neuronios[0].pesos.length];
        int contador = 0;
        try {
            BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo + ".csv"));
            String linhaArquivo = leitor.readLine();

            while (linhaArquivo != null) {
                pesos[contador] = linhaArquivo.split(",");
                linhaArquivo = leitor.readLine();
                contador++;
            }

            for (int i = 0; i < camada.neuronios.length; i++) {
                for (int j = 0; j < camada.neuronios[i].pesos.length; j++) {
                    camada.neuronios[i].pesos[j] = Double.parseDouble(pesos[i][j]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camada;
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

    public void createFile(String fileName) {
        try {
            File myFile = new File(fileName);
            if (myFile.createNewFile())
                System.out.println("File created");
            else
                System.out.println("File already exists");
        } catch (IOException e) {
            System.out.println("Error ocurred");
            e.printStackTrace();
        }
    }

    public void writeArrayToFile(double array[], String fileName) {
        try {
            FileWriter writer = new FileWriter("Camada_Escondida/entradas.txt");
            for (int i = 0; i < array.length; i++) {
                writer.write(String.valueOf(array[i]) + " ");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMatrixToFile(double matrix[][], String fileName) {
        try {
            createFile("Pesos1_2/" + fileName + ".txt");
            FileWriter writer = new FileWriter("Pesos1_2/" + fileName + ".txt");
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(String.valueOf(matrix[i][j]) + " ");
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

    //Carrega os valores de um arquivo CSV para os neuronios da camada informada
    public static Camada carregaCamadaCSV(Camada camada, String fileName, int linhaDoArquivo) {
        //Le o arquivo CSV e transforma em uma matriz de Strings
        String entradas[][] = lerDoCSV(fileName);
        for (int i = 0; i < entradas[linhaDoArquivo].length - 1; i++) {
            if (entradas[linhaDoArquivo][i].equals("1"))
                camada.neuronios[i].valorInicial = 1;
            else
                camada.neuronios[i].valorInicial = -1;
            //O método parseDouble está lançando uma exceção para valores negativos, investigar a causa
            //camada.neuronios[i].valorInicial = Double.parseDouble(entradas[linha][i]);
        };
        return camada;
    }


}
