package Models;

/*
* A classe Camada é uma das principais estruturas de dados utilizadas no MultiLayer Perceptron que foi implementado.
* Possui dois atributos: Um arranjo de objetos do tipo "Neuronio" e um inteiro que armazena o tamanho do arranjo "neuronios".
* Possui dois construtores, um para a camada de entrada e um para as camadas subsequentes.
* O motivo dessa distinção está explicado na definição de cada método.
* */
public class Camada {
    public Neuronio neuronios[];
    public int tamanho;

    /*
     * Esse é o primeiro construtor para a classe Camada.
     * Foi implementado para possibilitar que a primeira camada de um MultiLayer Perceptron pudesse ser instanciada.
     * A primeira camada de um MLP possui um neurônio para cada dado do conjunto de dados.
     * Itera-se sobre o arranjo de neuronios e inicializa-se um por um, criando novos objetos do tipo "Neuronio".
     * Como é a primeira camada, recebe como parâmetro apenas o tamanho que seu arranjo de neuronios deve possuir.
     * Os neuronios inicializados não recebem nenhum parâmetro em seu construtor. O motivo está explicado em sua definição.
     * */
    public Camada(int tamanho) {
        this.tamanho = tamanho;
        neuronios = new Neuronio[tamanho];

        for (int i = 0; i < tamanho; i++) {
            neuronios[i] = new Neuronio();
        }
    }

    /*
    * Esse é o segundo construtor para a classe Camada.
    * Foi implementado para as demais camadas do MultiLayer Perceptron.
    * Recebe como argumento dois dados do tipo inteiro: O tamanho da camada que está sendo instanciada (número de neuronios)
    * e o tamanho da camada anterior (número de neuronios da camada anterior).
    * Itera-se sobre o arranjo de neuronios e incializa-se um por um. O construtor do objeto do tipo "Neuronio" recebe, como parâmetro,
    * o tamanho da camada anterior. O motivo está explicado em sua definição.
    * */
    public Camada(int tamanho, int tamanhoCamadaAnterior) {
        this.tamanho = tamanho;
        neuronios = new Neuronio[tamanho];

        for (int i = 0; i < tamanho; i++) {
            neuronios[i] = new Neuronio(tamanhoCamadaAnterior);
        }
    }
}
