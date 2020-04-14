import Models.Camada;

import java.io.*;
import java.nio.Buffer;
import java.util.Scanner;

/*
 * A classe FileUtils foi criada para juntar métodos necessários para a leitura e escrita de dados para arquivos.
 * Isso possibilita que ao final de cada época, os pesos atualizados de cada neurônio sejam salvos para um arquivo .CSV
 * e que os dados de entrada sejam lidos também de arquivos .CSV
 * */
public class FileUtils {

    /*
    * Esse método foi criado para salvar o arranjo de pesos de cada neuronio dentro de um objeto do tipo Camada.
    * Recebe como parametros a Camada cujos pesos devem ser salvos e o nome do arquivo.
    * Itera-se sobre todos os neuronios de uma camada e, dentro desse laço, itera-se sobre todos os pesos de cada neuronio.
    * O peso, então, e salvo em um arquivo .CSV.
    * */
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

    /*
    * De acordo com a primeira etapa do algoritmo, os neurônios devem ter seus pesos inicializados.
    * Esse método foi implementado para poder ler dados armazenados em um arquivo do tipo .CSV e passá-los para objetos do tipo Neuronio.
    * Recebe como parametros o objeto do tipo Camada cujos Neuronios receberão os valores e o nome do arquivo .CSV que deverá ser lido.
    * Itera-se sobre o arquivo .CSV, linha por linha, e cada String é, então, dividida (com a regex sendo uma vírgula, já que os valores no arquivo .CSV são divididos por vírgulas)
    * e os arranjos obtidos são armazenados em uma matriz do tipo String.
    * Após o primeiro laço, itera-se sobre todos os neuronios da Camada passada como argumento e, em cada peso do neurônio sendo iterado, armazena-se o valor equivalente que foi lido no arquivo.
    * */
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

            //Nesse laço os pesos lidos no arquivo são transformados em elementos do tipo double utilizando o método "parseDouble" e armazenados nos neurônios.
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

    /*
    * Esse método também foi implementado com o objetivo de ler dados de um arquivo .CSV.
    * Tem como objetivo ler o arquivo cujo nome é passado como parametro do método.
    * O método itera sobre as linhas do arquivo .CSV, as divide (pelas virgulas) em arranjos do tipo String e armazena esses arranjos em uma matrix de Strings.
    * Essa matriz é, então, retornada pelo método.
    * */
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

}
