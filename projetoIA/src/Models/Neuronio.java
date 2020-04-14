package Models;

/*
* A classe Neuronio é uma das principais estruturas de dados utilizadads no MultiLayer Perceptron.
* Possui 5 atributos: dois arranjos do tipo double e três dados do tipo double.
* Seus atributos são necessários para armazenar os valores necessários para o treinamento e utilização de um MultiLayer Perceptron.
* Possui dois construtores diferentes, um para os neuronios da primeira camada e outro para os demais neuronios.
* */
public class Neuronio {
    /*
    * O atributo "valorInicial" representa o valor que o objeto do tipo Neuronio assume.
    * Ele é calculado na etapa Feed-Forward do algoritmo, ao passar o y_in do neuronio por uma função de ativação.
    * */
    public double valorInicial = 0;

    /*
    * O atributo "y_in" é calculado na etapa Feed-Forward do algoritmo.
    * É feito o somatório da multiplicação das entradas que afetam o Neuronio em questão pelos respectivos pesos.
    * O somatório é, então, adicionado ao "bias" da camada, e o resultado dessa soma é armazenado no atributo "y_in".
    * */
    public double y_in = 0;

    /*
    * O atributo "pesos" é um arranjo do tipo double.
    * É responsável por armazenar os pesos entre o Neuronio em questão e todos os outros neuronios da camada anterior.
    * */
    public double[] pesos;

    /*
    * O atributo "correcoes" é um arranjo do tipo double.
    * É responsável por armazenar as correções que devem ser feitas para cada um dos pesos do Neuronio.
    * */
    public double[] correcoes;

    /*
    * O atributo "delta" representa o termo de informação de erro que é calculado nas etapas de Retropropagação de Erro do algoritmo.
    * O termo de informação de erro é calculado e o valor é armazenado nesse atributo para que possa ser usado em etapas subsequentes do algoritmo,
    * como por exemplo o cálculo de correções dos pesos do Neuronio.
    * */
    public double delta = 0;

    /*
    * Esse é o primeiro construtor da classe Neuronio.
    * É utilizado para inicializar os neuronios da primeira camada.
    * É um construtor padrâo, pois os arranjos "pesos" e "correcoes" não devem ser inicializados, pois não serão utilizados.
    * Como explicado na definição do atributo "pesos", os pesos de um neuronio são entre o neuronio e todos os neuronios da camada anterior.
    * Portanto, como a camada de entrada não possui camada anterior, nenhum peso é salvo.
    * */
    public Neuronio() {};

    /*
    * Esse é o segundo construtor da classe Neuronio.
    * É utilizado para inicializar os neuronios de todas as camadas com exceção da primeira.
    * Recebe como parâmetro um dado do tipo inteiro que representa o tamanho da camada anterior a qual o Neuronio sendo inicializado pertence.
    * Como em cada Neuronio há um peso para cada Neuronio da camada anterior, o parâmetro é utilizado para inicializar os arranjos do tipo double
    * com o mesmo tamanho.
    * */
    public Neuronio(int tamanhoCamadaAnterior) {
        pesos = new double[tamanhoCamadaAnterior];
        correcoes = new double[tamanhoCamadaAnterior];
    }
}
