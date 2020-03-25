import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

public class Teste {

    public static double taxaDeAprendizado = 0.1;
    public static String fileName;
    public static Utils utils = new Utils();
    public static FileUtils fileUtils = new FileUtils();

    public static void testar(int linhaArquivoEntrada) {

        //double pesos[][] = utils.readArrayFromFile("pesos" + letra.toUpperCase());
        int entradas[][] = criaArranjoEntrada(linhaArquivoEntrada);
        String resultado = "";
        for (int i = 1; i <= 7; i++) {
            double pesos[][];
            if (i <= 5)
                pesos = fileUtils.readArrayFromFile("pesos" + (char) (i + 96));
            else if (i == 6)
                pesos = fileUtils.readArrayFromFile("pesosJ");
            else
                pesos = fileUtils.readArrayFromFile("pesosK");
            resultado += String.valueOf(funcaoAtivacao(y_in(0.25, entradas, pesos)));
        }
        System.out.println(resultado + ": " + lerResultado(resultado));
    }

    public static String lerResultado(String resultado) {

        if (resultado.equals("1-1-1-1-1-1-1")) return "A";
        else if (resultado.equals("-11-1-1-1-1-1")) return "B";
        else if (resultado.equals("-1-11-1-1-1-1")) return "C";
        else if (resultado.equals("-1-1-11-1-1-1")) return "D";
        else if (resultado.equals("-1-1-1-11-1-1")) return "E";
        else if (resultado.equals("-1-1-1-1-11-1")) return "J";
        else if (resultado.equals("-1-1-1-1-1-11")) return "K";
        else return "";
    }

    public static void main(String[] args) {
        //fileName = "pesosA";
        //double pesos[][] = utils.readArrayFromFile(fileName);
        //int entradas[][] = criaArranjoEntrada(1);
        //taxaDeAprendizado = calculaTaxaDeAprendizado();
        //System.out.println("Taxa de aprendizado: " + taxaDeAprendizado);

        for (int i = 1; i <= 21; i++)
        testar(i);

        //int i = 0;
        //while (i < 1) {
        //    int j = 1;
        //    while (j < 22) {
        //        entradas = criaArranjoEntrada(j);
        //        int t = j == 1 || j == 8 || j == 15 ? 1 : -1;
        //        treinamento(entradas, pesos, t);
        //       j++;
        //    }
        //    i++;
        //}

        //imprimeMatriz(pesos);


        //double pesosA[][] = transformaMatrizPesos(pesos[0]);
        //imprimeMatriz(pesosA);


        //utils.createFile();
        //utils.writeArrayToFile(pesosA);
    }

    public static int[][] criaArranjoEntrada(int linha) {
        String entradas[][] = fileUtils.lerDoCSV("caracteres-ruido");
        return transformaMatrizEntrada(entradas[linha - 1]);
    }

    public static int[][] transformaMatrizEntrada(String entrada[]) {
        int entradas[][] = new int[9][7];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                String e = entrada[j % entradas[0].length + i * entradas[0].length];
                if (e.equals("1")) entradas[i][j] = 1;
                else entradas[i][j] = -1;
            }
            System.out.println("");
        }
        System.out.println();

        return entradas;
    }

    public static double calculaNovoPeso(double antigoPeso, double t, int entrada) {
        return utils.formataDouble(antigoPeso + taxaDeAprendizado * t * entrada);
    }

    public static int funcaoAtivacao(double y_in) {
        if (y_in < 0) return -1;
        return 1;
    }


    public static void treinamento(int[][] entradas, double pesos[][], int t) {
        int resultado = t * -1;
        while (resultado != t) {
            resultado = funcaoAtivacao(y_in(0.25, entradas, pesos));
            System.out.println("Resultado: " + resultado);
            if (resultado != t) {
                pesos = recalculaPesos(entradas, pesos, t);
                fileUtils.writeArrayToFile(pesos, fileName);
                System.out.println();
            }
        }
    }

    public static double[][] recalculaPesos(int[][] entradas, double[][] pesos, double t) {
        for (int i = 0; i < pesos.length; i++) {
            for (int j = 0; j < pesos[0].length; j++) {
                pesos[i][j] = calculaNovoPeso(pesos[i][j], t, entradas[i][j]);
            }
        }
        return pesos;
    }


    public static double y_in(double bias, int entradas[][], double pesos[][]) {
        double y_in = bias + calculaSomatorioEntradasEPesos(entradas, pesos);
        return y_in;
    }

    public static double calculaSomatorioEntradasEPesos(int entradas[][], double pesos[][]) {
        double somatorio = 0;
        for (int i = 0; i < pesos.length; i++) {
            for (int j = 0; j < pesos[0].length; j++) {
                somatorio += entradas[i][j] * pesos[i][j];
            }
        }
        return somatorio;
    }


    public static double calculaTaxaDeAprendizado() {
        Random random = new Random();
        return utils.formataDouble(random.nextDouble());
    }


}
