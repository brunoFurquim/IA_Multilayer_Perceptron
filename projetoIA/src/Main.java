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

        //Inicializa os pesos dos neuronios da camada escondida e da camada de saida com valores no intervalo passado como parametro.
        //inicializaPesosAleatorios(camadas, -0.25, 0.25);

        //Carrega os valores para a camada de entrada do CSV
        camadas[0] = FileUtils.carregaCamadaCSV(camadas[0], "caracteres-limpo", 0);
        //Carrega os valores dos pesos dos neuronios da camada escondida do arquivo CSV correspondente (PCE = Pesos Camada Escondida)
        camadas[1] = FileUtils.lerPesosCSV(camadas[1], "PCE");
        //Carrega os valores dos pesos dos neuronios da camada de saida do arquivo CSV correspondente (PCS = Pesos Camada Saida)
        camadas[2] = FileUtils.lerPesosCSV(camadas[2], "PCS");

        //Passa as camadas inicializadas para o algoritmo de treinamento
        treinamento(camadas);
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

    //Camada de entrada ja possui valores vindos do csv. Camada escondida e saida ja possuem neuronios com pesos aleatorios entre -1 e 1
    public static void treinamento(Camada camadas[]) {
        //Define as camadas recebidas como parametro do metodo para facilitar a manipulacao
        Camada entrada = camadas[0];
        Camada escondida = camadas[1];
        Camada saida = camadas[2];

        int epoca = 0;
        //Cada iteração do laço é uma época
        while (epoca < 1000) {

            //Calcula o valor de cada neuronio da camada escondida utilizando os pesos entre os neuronios de entrada e os neuronios da camada escondida
            calcularYInEntreCamadas(entrada, escondida);
            //Calcula o valor de cada neuronio da camada de saida utilizando os pesos entre os neuronios da camada escondida e os neuronios da camada de saida
            calcularYInEntreCamadas(escondida, saida);

            System.out.println("Neuronios da camada escondida");
            for (int i = 0; i < escondida.tamanho; i++) {
                Utils.leValoresNeuronio(escondida.neuronios[i]);
            }

            System.out.println("Neuronios da camada de saida");
            for (int i = 0; i < saida.tamanho; i++) {
                Utils.leValoresNeuronio(saida.neuronios[i]);
            }

            //Para cada unidade de saida (cada neuronio da ultima camada), considerar o valor esperado e o valor obtido e computar termo de informacao de erro
            for (int i = 0; i < saida.tamanho; i++) {
                //O valor esperado deve ser ajustado de acordo com o treinamento
                //Caso os dados de entrada sejam da letra A, por exemplo, o valorEsperado deve ser 1 para o primeiro neuronio (de indice 0) e -1 para todos os outros neuronios
                calculaErroCamadaSaida(saida.neuronios[i], escondida, i == 0 ? 1 : -1);
            }

            //Para cada unidade escondida (cada neuronio da camada escondida), somar o termo de erro de todos os neuronios da camada seguinte que essa unidade influencia com os respectivos pesos.
            //O delta de cada neuronio escondido é igual ao somatorio dos valores mencionados a cima (termo de erro e peso) vezes a derivada da funcão sigmoide, onde o valor da função é o valor de cada neuronio escondido
            for (int i = 0; i < escondida.tamanho; i++) {
                Neuronio neuronioEscondido = escondida.neuronios[i];
                double somatorio = 0;
                for (int j = 0; j < saida.tamanho; j++) {
                    Neuronio neuronioSaida = saida.neuronios[j];
                    double peso = neuronioSaida.pesos[i];
                    double termoDeInfoErro = neuronioSaida.delta;
                    somatorio += (peso * termoDeInfoErro);
                }
                neuronioEscondido.delta = somatorio * FuncaoSigmoid.derivadaSigmoide(neuronioEscondido.valorInicial);

                //Para cada peso do neuronio escondido sendo iterado, é calculado a correção que deve ser feita e a informação é guardada em um arranjo dentro do proprio neuronio.
                for (int j = 0; j < neuronioEscondido.pesos.length; j++) {
                    neuronioEscondido.correcoes[j] = taxaDeAprendizado * neuronioEscondido.delta * entrada.neuronios[j].valorInicial;
                }
            }

            //De acordo com o passo 8 do algoritmo (baseado nos passos descritos nos slides), os pesos de cada neuronio da camada de saida tem seu valor alterado para ser o valor antigo adicionado da correção calculada no passo anterior
            atualizarPesosCamada(saida);
            //O mesmo acontece para os neuronios da camada escondida
            atualizarPesosCamada(escondida);
            epoca++;
        }

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

    public static void calculaErroCamadaSaida(Neuronio neuronio, Camada escondida, double valorEsperado) {
        //O erro é calculado como a diferença entre o valor esperado e o valor obtido
        double erro = valorEsperado - neuronio.valorInicial;
        //O termo de erro é a multiplicação do erro pela derivada da Sigmoid do valor do neuronio, como explicado no passo 6 - slide 72 (Perceptron Simples e MultiLayer Perceptron)
        double termoDeInfoErro = erro * FuncaoSigmoid.derivadaSigmoide(neuronio.y_in);
        //Nesse programa, o delta de cada neuronio é equivalente ao termo de erro calculado
        neuronio.delta = termoDeInfoErro;

        //Para cada peso do neuronio sendo iterado (entre ele e a camada anterior), é calculada a correção que deve ser feita e a informação é guardada em um arranjo. Cada elemento desse arranjo corresponde com um elemento do arranjo de pesos
        //O elemento neuronio.correcoes[0] corresponde a correção que deve ser feita ao elemento neuronio.pesos[0], e assim sucessivamente.
        for (int j = 0; j < neuronio.pesos.length; j++) {
            neuronio.correcoes[j] = taxaDeAprendizado * neuronio.delta * escondida.neuronios[j].valorInicial;
        }
    }

    //Itera por todos os neuronios da camada e roda o metodo de atualizarPesos para cada um deles
    public static void atualizarPesosCamada(Camada camada) {
        for (int i = 0; i < camada.tamanho; i++) {
            Neuronio neuronio = camada.neuronios[i];
            atualizarPesos(neuronio);
        }
    }

    //Atualiza os pesos de um neuronio baseando-se nas correcoes calculadas
    public static void atualizarPesos(Neuronio neuronio) {
        for (int i = 0; i < neuronio.pesos.length; i++) {
            neuronio.pesos[i] += neuronio.correcoes[i];
        }
    }

    public static void inicializaPesosAleatorios(Camada camadas[], double valorMinimo, double valorMaximo) {
        Utils.inicializaPesosAleatorios(camadas[1], valorMinimo, valorMaximo);
        Utils.inicializaPesosAleatorios(camadas[2], valorMinimo, valorMaximo);

        FileUtils.salvarPesosCSV(camadas[1], "PCE");
        FileUtils.salvarPesosCSV(camadas[2], "PCS");
    }

    public static void teste(Camada camadas[]) {
        calcularYInEntreCamadas(camadas[0], camadas[1]);
        calcularYInEntreCamadas(camadas[1], camadas[2]);

        utils.leValoresCamada(camadas[2]);
    }
}
