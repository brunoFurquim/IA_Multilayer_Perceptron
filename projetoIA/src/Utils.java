import Models.Camada;

import java.text.DecimalFormat;
import java.util.Random;

public class Utils {

    public static double[][] transformaMatrizPesos(double p[]) {

        double pesos[][] = new double[9][7];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                pesos[i][j] = p[j % pesos[0].length + i * pesos[0].length];
            }
        }
        return pesos;
    }

    public static double[][] populaArranjoPesosAleatorio(double min, double max) {
        int linhas = 32;
        int colunas = 63;
        double[][] pesos = new double[linhas][colunas];
        Random random = new Random();

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                double valorAleatorio = min + (max - min) * random.nextDouble();
                pesos[i][j] = formataDouble(valorAleatorio);
            }
            System.out.println();
        }
        return pesos;
    }

    public static void imprimeMatrizInt(int[][] matriz) {
        int linhas = matriz.length;
        int colunas = matriz[0].length;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print(matriz[i][j] + ", ");
            }
            System.out.println();
        }
    }

    public static double formataDouble(double d) {
        DecimalFormat formatador = new DecimalFormat("#.##");
        return Double.valueOf(formatador.format(d));
    }

    public static void imprimeArranjo(double[] arranjo) {
        for (int i = 0; i < arranjo.length; i++) {
            System.out.print(arranjo[i] + ", ");
        }
        System.out.println();
    }

    public static void imprimeMatriz(double[][] matriz) {
        int rows = matriz.length;
        int cols = matriz[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(/*"(" + i + ", " + j + ")" + " - " + */matriz[i][j] + ", ");
            }
            System.out.println("");
        }
    }

    public static void leValoresCamada(Camada camada) {
        for (int i = 0; i < camada.neuronios.length; i++) {
            System.out.print(camada.neuronios[i].valorInicial + ", ");
        }
        System.out.println();
    }

    public static void lePesosCamada(Camada camada) {
        for (int i = 0; i < camada.neuronios.length; i++) {
            System.out.println("Neuronio " + i);
            for (int j = 0; j < camada.neuronios[i].pesos.length; j++) {
                System.out.print(camada.neuronios[i].pesos[j] + ", ");
            }
            System.out.println("\n\n");
        }
    }

    //Inicializa pesos dos neuronios de uma camada com valores aleatorios no intervalo [valorMinimo, valorMaximo]
    public static void inicializaPesosAleatorios(Camada camada, double valorMinimo, double valorMaximo) {
        Random random = new Random();
        double valorAleatorio;
        for (int i = 0; i < camada.neuronios.length; i++) {
            for (int j = 0; j < camada.neuronios[i].pesos.length; j++) {
                valorAleatorio = valorMinimo + (valorMaximo - valorMinimo) * random.nextDouble();
                //Método formataDouble da classe Utils formata o valor aleatorio para um double com apenas duas casas decimais
                camada.neuronios[i].pesos[j] = formataDouble(valorAleatorio);
            }
        }
    }
}
