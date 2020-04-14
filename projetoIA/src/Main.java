import Utils.Utils;

/*
* A classe Main possui apenas o método main, responsável por instanciar e inicializar os objetos do tipo MultiLayerPerceptron.
* É através desse método que os métodos da classe MultiLayerPerceptron são executados.
*
* Através de mudanças nesse método que o programa deve ser testado, mudando o nome dos arquivos de entrada.
*
* */
public class Main {
    public static void main(String[] args) {
        int[] tamanhosDasCamadasCaracteres = new int[]{63, 63, 7};

        /*
         * Inicializa-se o MultiLayer Perceptron.
         * Passa-se para seu construtor a taxa de aprendizado, o número de camadas que deve possuir e um arranjo do tipo inteiro, cada posição possui o tamanho que a camada correspondente deve possuir.
         * */

        MultiLayerPerceptron mlpCaracteres = new MultiLayerPerceptron(0.5, 3, tamanhosDasCamadasCaracteres, "PCE_Caracteres", "PCS_Caracteres", "caracteres-limpo", "Caracteres");
        //mlp.inicializaPesosAleatorios(-1, 1, "PCE", "PCS");
        //mlpCaracteres.testar(21);

        /*
        * Inicializa-se abaixo os MultiLayer Perceptron para resolução de problemas lógicos.
        * Esse MLP é referente ao MLP para a resolução do problema XOr
        * */

        int[] tamanhoDasCamadasProblemaLogico = new int[]{2, 2, 1};


        MultiLayerPerceptron mlpXOR = new MultiLayerPerceptron(0.1, 3, tamanhoDasCamadasProblemaLogico, "PCE_XOR", "PCS_XOR", "problemXOR", "logico");
        //mlpXOR.inicializaPesosAleatorios(0, 1);
        mlpXOR.treinarProblemaLogico(10000, 4);
        mlpXOR.testar(4);

        MultiLayerPerceptron mlpAND = new MultiLayerPerceptron(0.1, 3, tamanhoDasCamadasProblemaLogico, "PCE_AND", "PCS_AND", "problemAND", "logico");
        //mlpAND.inicializaPesosAleatorios(0, 0.5);
        //mlpAND.treinarProblemaLogico(1000, 4);
        //mlpAND.testar(4);

        MultiLayerPerceptron mlpOR = new MultiLayerPerceptron(0.1, 3, tamanhoDasCamadasProblemaLogico, "PCE_OR", "PCS_OR", "problemOR", "logico");
        //mlpOR.inicializaPesosAleatorios(0, 1);
        //mlpOR.treinarProblemaLogico(10000, 4);
        //mlpOR.testar(4);
    }
}