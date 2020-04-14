import Models.Camada;
import Models.Neuronio;

import java.text.DecimalFormat;
import java.util.Random;

/*
* A classe Utils foi criada para juntar métodos necessários para o funcionamento do MultiLayer Perceptron.
* Os métodos que estão definidos nessa classe são usados, em sua maioria, para iterar sobre as estruturas de dados
* e imprimir as informações de cada objeto.
* */
public class Utils {

    /*
    * Esse método foi criado para formatar variáveis do tipo double.
    * Foi utilizado apenas uma vez no método "inicializaPesosAleatorios", cuja explicação é feita em sua definição.
    * Formata o número passado como parâmetro para um número com apenas duas casas decimais.
    * */
    public static double formataDouble(double d) {
        DecimalFormat formatador = new DecimalFormat("#.##");
        return Double.valueOf(formatador.format(d));
    }

    /*
    * Itera sobre todos os neuronios de um objeto do tipo Camada passado como parametro e imprime
    * os valores dos neuronios da camada
    * */
    public static void leValoresCamada(Camada camada) {
        for (int i = 0; i < camada.neuronios.length; i++) {
            System.out.print(camada.neuronios[i].valorInicial + ", ");
        }
        System.out.println();
    }

    /*
    * Esse método foi criado para identificar qual letra foi fornecida como conjunto de dados de entrada.
    * Os neurônios da camada de saída são responsáveis por identificar qual letra foi dada como entrada.
    * O primeiro neurônio é responsável pela letra A, o segundo pela letra B e assim sucessivamente, até o sétimo neurônio responsável pela letra K
    * O método funciona da seguinte maneira: Os valores de saída de cada neuronio da ultima camada são colocados em um arranjo do tipo double.
    * Esse arranjo é ordenado de menor para o maior (usando o método "sort", cuja explicação está sendo feita em sua definição) e o índice do maior elemento é retornado
    * O índice é comparado com as possíveis alternativas (0-6) e cada uma das alternativas corresponde a uma letra.
    * Quanto mais próximo de 1 (ou seja, quanto maior for o valor do neuronio de saída), maior a probabilidade de corresponder a letra pela qual o neuronio é responsável
    * */
    public static void leResposta(Camada camada) {
        double valores[] = new double[7];
        //Itera por todos os neuronios da camada recebida como parametro
        for (int i = 0; i < camada.tamanho; i++) {
            valores[i] = camada.neuronios[i].valorInicial;
        }

        int index[] = sort(valores);
        int indexDoMaior = index[index.length - 1];
        System.out.println("\nRESPOSTA: ");

        if (indexDoMaior == 0) System.out.println("A");
        if (indexDoMaior == 1) System.out.println("B");
        if (indexDoMaior == 2) System.out.println("C");
        if (indexDoMaior == 3) System.out.println("D");
        if (indexDoMaior == 4) System.out.println("E");
        if (indexDoMaior == 5) System.out.println("J");
        if (indexDoMaior == 6) System.out.println("K");
    }

    /*
    * Método para ordenar arranjos do tipo double, do menor elemento para o maior.
    * Recebe como parametro um arranjo de elementos do tipo double e retorna um arranjo de elementos do tipo int.
    * O retorno do método é um arranjo contendo os índices dos elementos do arranjo original, ordenados do menor para o maior.
    * O algoritmo de implementação utilizado é o Bubble Sort.
    *
    * Esse algoritmo tem uma performance menor que outros algoritmos de ordenação, foi utilizado, entretanto, por causa da simplicidade de implementação.
    * Como o número de elementos do arranjo é pequeno, a diferença de performance ganha por outros algoritmos não justifica a implementação dos mesmos.
    * */
    public static int[] sort(double[] array) {
        final int size = array.length;

        final int[] result = new int[size];
        for (int i = 0; i < size; i++)
            result[i] = i;

        boolean sorted;
        do {
            sorted = true;
            int bubble = result[0];
            for (int i = 0; i < size - 1; i++) {
                if (array[bubble] > array[result[i + 1]]) {
                    result[i] = result[i + 1];
                    result[i + 1] = bubble;
                    sorted = false;
                } else {
                    bubble = result[i + 1];
                }
            }
        } while (!sorted);

        return result;
    }

    /*
    * Esse método itera sobre os neuronios da camada passada como parametros e, para cada neuronio, itera sobre os pesos e imprime cada um deles.
    * Foi implementado para possibilitar a visualização dos pesos após lê-los de um arquivo .CSV e para poder visualizá-los após a última etapa do algoritmo de treinamento,
    * que é responsável pela atualização dos pesos.
    * */
    public static void lePesosCamada(Camada camada) {
        for (int i = 0; i < camada.neuronios.length; i++) {
            System.out.println("Neuronio " + i);
            for (int j = 0; j < camada.neuronios[i].pesos.length; j++) {
                System.out.print(camada.neuronios[i].pesos[j] + ", ");
            }
            System.out.println("\n\n");
        }
    }

    /*
    * Esse método é uma versão mais abrangente do método definido acima.
    * Recebe-se um objeto do tipo Neuronio como parametro e itera-se sobre seus pesos e
    * sobre as correções de cada peso, imprimindo um a um.
    * Após isso, imprime o y_in calculado para aquele neurônio, o valor do neurônio (obtido através do resultado da função de ativação sigmoid) e seu delta.
    * Cada um desses atributos está explicado na definição da estrutura de dados, no arquivo Neuronio.java.
    * */
    public static void leValoresNeuronio(Neuronio neuronio) {
        System.out.println("Pesos: ");
        for (int i = 0; i < neuronio.pesos.length; i++) {
            System.out.print(neuronio.pesos[i] + ", ");
        }
        System.out.println();
        System.out.println("Correções: ");
        for (int i = 0; i < neuronio.correcoes.length; i++) {
            System.out.print(neuronio.correcoes[i] + ", ");
        }
        System.out.println();
        System.out.println("Y_in: " + neuronio.y_in);
        System.out.println("Valor (igual a funcao sigmoide de y_in): " + neuronio.valorInicial);
        System.out.println("Delta: " + neuronio.delta);
        System.out.println("\n");
    }

    /*
    * Esse método foi utilizado apenas uma vez durante a primeira etapa do algoritmo de treinamento do MultiLayer Perceptron
    * Recebe um objeto do tipo Camada e um intervalo como parametros.
    * Itera-se sobre todos os neuronios de uma camada e atribui para cada um deles um valor aleatório dentro do intervalo [valorMinimo, valorMaximo].
    * O valor aleatório é então passado como argumento para o método "formataDouble" para que o peso seja um dado do tipo "double" com apenas duas casas decimais.
    * A explicação do método "formataDouble" está sendo feita em sua definição.
    * */
    public static void inicializaPesosAleatorios(Camada camada, double valorMinimo, double valorMaximo) {
        Random random = new Random();
        double valorAleatorio;
        for (int i = 0; i < camada.neuronios.length; i++) {
            for (int j = 0; j < camada.neuronios[i].pesos.length; j++) {
                valorAleatorio = valorMinimo + (valorMaximo - valorMinimo) * random.nextDouble();
                camada.neuronios[i].pesos[j] = formataDouble(valorAleatorio);
            }
        }
    }

    /*
    * Esse método foi implementado com o objetivo de ler um arquivo .CSV o conjunto de dados de entrada da Camada de entrada do MultiLayer Perceptron.
    * Recebe como parametros a Camada cujos neuronios devem armazenar os dados do arquivo, o nome do arquivo que contém os dados de entrada e a linha do arquivo que
    * deve ser armazenada nos neuronios da camada.
    * */
    public static Camada carregaCamadaCSV(Camada camada, String fileName, int linhaDoArquivo) {
        //Le o arquivo CSV e transforma em uma matriz de Strings, o funcionamento do método "lerDoCSV" está explicado em sua definição.
        String entradas[][] = FileUtils.lerDoCSV(fileName);
        for (int i = 0; i < entradas[linhaDoArquivo].length - 1; i++) {
            //O if-else abaixo teve que ser usado pois o método "parseDouble()" da classe Double estava lançando uma exceção para valores negativos
            if (entradas[linhaDoArquivo][i].equals("1"))
                camada.neuronios[i].valorInicial = 1;
            else
                camada.neuronios[i].valorInicial = -1;
            /*
            * O equivalente ao if-else acima seria a linha
            * camada.neuronios[i].valorInicial = Double.parseDouble(entradas[linha][i]);
            * */
        };
        return camada;
    }
}
