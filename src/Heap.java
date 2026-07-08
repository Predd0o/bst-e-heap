import java.util.*;                                                        // importa classes utilitárias (não usadas diretamente aqui, mas mantém padrão)

//implementação de um Max Heap usando array
public class Heap {
    private int[] heap;                                                     // array que armazena os elementos do heap
    private int tail;                                                       // índice do último elemento presente no heap (-1 se vazio)
    private boolean min = false;                                            // false = comporta-se como max-heap (padrão), true = comporta-se como min-heap

    public Heap(int capacidade){                                            // construtor: cria heap vazio com a capacidade informada
        this.heap = new int[capacidade];                                    // aloca o array com o tamanho pedido
        this.tail = -1;                                                     // -1 indica que não há elementos ainda
    }

    public Heap(int[] array){                                               // construtor: recebe um array pronto e transforma em heap (Build Heap)
        this.heap = array;                                                  // usa o próprio array recebido como array interno
        this.tail = this.heap.length - 1;                                   // tail aponta para o último índice ocupado (array cheio)
        this.buildHeap();                                                   // aplica heapify de baixo para cima para virar um Heap válido
    }

    public boolean isEmpty(){                                               // verifica se o heap está vazio
        return this.tail == -1;                                             // vazio quando tail é -1 (nenhum elemento inserido)
    }

    public int left(int index){                                            // retorna o índice do filho esquerdo de um nó
        return 2 * index + 1;                                               // fórmula: 2*index + 1
    }

    public int right(int index){                                           // retorna o índice do filho direito de um nó
        return 2 * (index + 1);                                             // fórmula: 2*(index + 1)
    }

    public int parent(int index){                                          // retorna o índice do pai de um nó
        return Math.floorDiv(index - 1, 2);                                 // fórmula: floorDiv(index - 1, 2)
    }

    private boolean isValidIndex(int index){                                // verifica se um índice está dentro dos limites do heap
        return index >= 0 && index <= tail;                                 // válido se estiver entre 0 e tail (inclusive)
    }

    private boolean isLeaf(int index){                                      // verifica se o nó no índice é uma folha
        return index > parent(tail) && index <= tail;                      // folha: está além do pai da última posição ocupada
    }

    private void swap(int i, int j){                                        // troca os valores armazenados nos índices i e j
        int aux = this.heap[i];                                             // guarda o valor de i temporariamente
        this.heap[i] = this.heap[j];                                        // i recebe o valor de j
        this.heap[j] = aux;                                                 // j recebe o valor antigo de i
    }

    private void resize(){                                                  // dobra o tamanho do array quando ele está cheio
        int novaCapacidade = (this.heap.length == 0) ? 1 : this.heap.length * 2; // evita capacidade 0
        this.heap = Arrays.copyOf(this.heap, novaCapacidade);               // copia elementos antigos para o array maior
    }

    public void add(int n){                                                 // insere um novo elemento no heap, mantendo a propriedade de max heap
        if(tail >= (heap.length - 1))                                       // se não há espaço livre no array
            resize();                                                      // aumenta a capacidade do array

        tail += 1;                                                         // avança tail para a próxima posição livre
        this.heap[tail] = n;                                                // insere o elemento na última posição livre (mantém completude)

        int i = tail;                                                       // i começa na posição recém-inserida
        while(i > 0 && (this.min ? this.heap[parent(i)] > this.heap[i]      // min-heap: sobe enquanto o pai for maior que o elemento
                                  : this.heap[parent(i)] < this.heap[i])){   // max-heap: sobe enquanto o pai for menor que o elemento
            swap(i, parent(i));                                             // troca elemento com o pai (sobe na árvore)
            i = parent(i);                                                  // atualiza i para a posição do pai
        }
    }

    public int max(){                                                       // retorna (sem remover) o elemento do topo do heap
        if(isEmpty()) throw new RuntimeException("Heap vazio");            // não há topo em heap vazio
        return this.heap[0];                                                // a raiz (índice 0) é o maior se for max-heap, ou o menor se for min-heap
    }

    public int remove(){                                                    // remove e retorna o elemento do topo (raiz) do heap
        if(isEmpty()) throw new RuntimeException("Heap vazio");            // não é possível remover de heap vazio

        int element = this.heap[0];                                        // guarda o valor da raiz para retornar depois
        this.heap[0] = this.heap[tail];                                    // move a última folha para a raiz
        this.tail -= 1;                                                     // "remove" a última folha apenas diminuindo tail

        this.heapify(0);                                                    // restaura a propriedade de heap (max ou min) a partir da raiz

        return element;                                                     // retorna o antigo valor do topo
    }

    private int bestIndex(int index, int left, int right){                 // encontra, entre index, left e right, quem deve ficar no topo (maior se max-heap, menor se min-heap)
        int melhor = index;                                                 // assume inicialmente que index já é o "melhor"

        if(isValidIndex(left) && (this.min ? this.heap[left] < this.heap[melhor]  // min-heap: left é "melhor" se for menor
                                            : this.heap[left] > this.heap[melhor])) // max-heap: left é "melhor" se for maior
            melhor = left;                                                  // atualiza melhor para left

        if(isValidIndex(right) && (this.min ? this.heap[right] < this.heap[melhor] // min-heap: right é "melhor" se for menor
                                             : this.heap[right] > this.heap[melhor])) // max-heap: right é "melhor" se for maior
            melhor = right;                                                 // atualiza melhor para right

        return melhor;                                                      // retorna o índice do "melhor" entre os três
    }

    private void heapify(int index){                                       // corrige a propriedade de heap a partir do índice informado, descendo na árvore
        if(isLeaf(index) || !isValidIndex(index))                          // condição de parada: folha ou índice inválido
            return;                                                         // nada a fazer

        int indexBest = bestIndex(index, left(index), right(index));      // compara index com seus filhos para achar quem deve ficar no topo

        if(indexBest != index){                                             // se o "melhor" não for o próprio nó
            swap(index, indexBest);                                        // troca o nó com o filho que deveria estar no topo
            heapify(indexBest);                                             // continua o heapify a partir da nova posição do valor
        }
    }

    private void buildHeap(){                                               // transforma um array qualquer em um Heap válido
        for(int i = parent(this.tail); i >= 0; i--)                        // começa no pai da última folha e sobe até a raiz
            heapify(i);                                                     // aplica heapify em cada índice
    }

    public void toMinHeap(){                                                // transforma o heap atual (max-heap) em um min-heap, in-place
        this.min = true;                                                    // a partir de agora bestIndex/add passam a priorizar o menor valor
        this.buildHeap();                                                   // reaproveita o heapify já existente para reorganizar o array como min-heap
    }

    public int size(){                                                      // retorna a quantidade de elementos presentes no heap
        return this.tail + 1;                                               // tail é o índice do último elemento, então size = tail + 1
    }

    public void printArray(){                                              // imprime o array interno do heap (apenas as posições ocupadas)
        for(int i = 0; i <= tail; i++)                                     // percorre do índice 0 até tail
            System.out.print(this.heap[i] + " ");                         // imprime cada elemento ocupado
        System.out.println();                                               // pula linha ao final
    }
}