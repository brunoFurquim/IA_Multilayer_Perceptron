import Models.Camada;
import Models.FuncaoSigmoid;
import Models.Neuronio;

import java.util.Random;

public class Main {
    public static FileUtils fileUtils = new FileUtils();
    public static Utils utils = new Utils();
    public static double taxaDeAprendizado = 0.1;
    public static int linhaDoArquivo = 0;

    public static void main(String[] args) {
        //Inicializa o MLP para possuir 3 camadas, uma de entrada com 63 neuronios, uma escondida com 32 neuronios e uma de saida com 7 neuronios
        Camada camadas[] = inicializaCamadas(3);

        //Inicializa os pesos dos neuronios da camada escondida e da camada de saida com valores no intervalo entre -1 e 1.
        //inicializaPesosAleatorios(camadas);

        //Carrega os valores para a camada de entrada do CSV
        camadas[0] = FileUtils.carregaCamadaCSV(camadas[0], "caracteres-limpo", 0);
        //Carrega os valores dos pesos dos neuronios da camada escondida do arquivo CSV correspondente (PCE = Pesos Camada Escondida)
        camadas[1] = FileUtils.lerPesosCSV(camadas[1], "PCE");
        //Carrega os valores dos pesos dos neuronios da camada de saida do arquivo CSV correspondente (PCS = Pesos Camada Saida)
        camadas[2] = FileUtils.lerPesosCSV(camadas[2], "PCS");

        Utils.leValoresCamada(camadas[0]);
        Utils.lePesosCamada(camadas[1]);
        Utils.lePesosCamada(camadas[2]);

/*

        camadas[0] = FileUtils.carregaCamadaCSV(camadas[0], "caracteres-limpo", 20);
        camadas[1] = FileUtils.lerPesosCSV(camadas[1], "PCE");
        camadas[2] = FileUtils.lerPesosCSV(camadas[2], "PCS");
        treinamento(camadas, 1);*/
/*
        camadas[0] = FileUtils.carregaCamadaCSV(camadas[0], "caracteres-limpo", 0);
        camadas[1] = FileUtils.lerPesosCSV(camadas[1], "PCE");
        camadas[2] = FileUtils.lerPesosCSV(camadas[2], "PCS");
        teste(camadas);*/

    }

    public static void inicializaPesosAleatorios(Camada camadas[]) {
        Utils.inicializaPesosAleatorios(camadas[1], -1, 1);
        Utils.inicializaPesosAleatorios(camadas[2], -1, 1);

        FileUtils.salvarPesosCSV(camadas[1], "PCE");
        FileUtils.salvarPesosCSV(camadas[2], "PCS");
    }

    public static void teste(Camada camadas[]) {
        calcularYInEntreCamadas(camadas[0], camadas[1]);
        calcularYInEntreCamadas(camadas[1], camadas[2]);

        utils.leValoresCamada(camadas[2]);
    }

    //Camada de entrada ja possui valores vindos do csv. Camada escondida e saida ja possuem neuronios com pesos aleatorios entre -1 e 1
    public static void treinamento(Camada camadas[], int linhaDoArquivo) {
        Camada entrada = camadas[0];
        Camada escondida = camadas[1];
        Camada saida = camadas[2];



        int i = 0;
        //Cada iteração do laço é uma época
        while (i < 1000) {
            //Calcula o valor dos neuronios da camada escondida
            calcularYInEntreCamadas(entrada, escondida);
            //Calcula o valor dos neuronios da camada de saida
            calcularYInEntreCamadas(escondida, saida);


            for (int j = 0; j < saida.neuronios.length; j++) {
                if (j == 6)
                    calculaDeltaNeuronio(saida.neuronios[j], 1, saida.neuronios[j].valorInicial);
                else
                    calculaDeltaNeuronio(saida.neuronios[j], -1, saida.neuronios[j].valorInicial);
                calculaCorrecaoDosPesos(escondida, saida.neuronios[j]);
            }
            calculaErroCamada(escondida, saida);
            for (int x = 0; x < camadas[2].neuronios.length; x++) {
                atualizarPesos(camadas[2].neuronios[x]);
            }
            fileUtils.salvarPesosCSV(escondida, "PCE");
            fileUtils.salvarPesosCSV(saida, "PCS");
            i++;
        }

    }

    //Calcula o delta do neuronio
    public static double calculaDeltaNeuronio(Neuronio neuronio, double valorEsperado, double valorAtingido) {
        double erro = valorEsperado - valorAtingido;
        neuronio.delta = erro * FuncaoSigmoid.derivadaSigmoide(neuronio.y_in);
        return neuronio.delta;
    }

    //Recalcula o delta de cada neuronio da camada atual baseando-se no erro dos neuronios da camada seguinte
    public static void calculaErroCamada(Camada atual, Camada seguinte) {
        double erro = 0;
        //Itera por todos os neuronios da camada seguinte
        for (int i = 0; i < atual.neuronios.length; i++) {
            //Somatoria do delta de cada neuronio com cada um dos seus pesos
            for (int j = 0; j < seguinte.neuronios.length; j++) {
                erro += seguinte.neuronios[j].delta * seguinte.neuronios[j].pesos[i];
            }
            atual.neuronios[i].delta = erro * FuncaoSigmoid.derivadaSigmoide(atual.neuronios[i].y_in);
        }
    }

    //Atualiza os pesos de um neuronio baseando-se nas entradas da camada anterior
    public static void atualizarPesos(Neuronio neuronio) {
        for (int i = 0; i < neuronio.pesos.length; i++) {
            neuronio.pesos[i] += neuronio.correcoes[i];
        }
    }

    //Calcula correcao dos pesos baseando-se no delta do neuronio e nas entradas da camada anterior
    public static void calculaCorrecaoDosPesos(Camada anterior, Neuronio neuronio) {
        for (int i = 0; i < neuronio.pesos.length; i++) {
            neuronio.correcoes[i] = taxaDeAprendizado * neuronio.delta * anterior.neuronios[i].valorInicial;
            System.out.print(neuronio.correcoes[i] + ", ");
        }
        System.out.println();
    }

    public static void calcularYInEntreCamadas(Camada entrada, Camada calculo) {
        for (int i = 0; i < calculo.neuronios.length; i++) {
            double y_in = calculaYInNeuronio(entrada, calculo.neuronios[i]);
            double valorFinal = FuncaoSigmoid.sigmoid(y_in);
            calculo.neuronios[i].valorInicial = valorFinal;
            calculo.neuronios[i].y_in = y_in;
        }
    }

    public static double calculaYInNeuronio(Camada entrada, Neuronio saida) {
        double y_in;
        double somatorio = 0;
        for (int i = 0; i < entrada.neuronios.length; i++) {
            somatorio += (entrada.neuronios[i].valorInicial * saida.pesos[i]);
        }
        y_in = somatorio + saida.bias;
        return y_in;
    }

    public static Camada[] inicializaCamadas(int numeroDeCamadas) {
        Camada camadas[] = new Camada[numeroDeCamadas];

        //Camada de entrada possui 63 neuronios
        camadas[0] = new Camada(63);

        //Camada escondida possui 32 neuronios
        camadas[1] = new Camada(32, 63);

        //Camada de saida possui 7 neuronios
        camadas[2] = new Camada(7, 32);
        return camadas;
    }


}
