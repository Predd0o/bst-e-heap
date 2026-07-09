import java.util.*;                                                       // importa todas as classes do pacote util (Deque, LinkedList, etc.)
//implementaçaão da BST
public class BST {
    private Node root;                                                     // referência para a raiz da árvore


    public boolean isEmpty(){                                              // verifica se a árvore está vazia
        return this.root == null;                                          // true se não houver nó raiz
    }

    public void add(int element){                                          // inserção iterativa de um elemento na BST
        if(isEmpty()){
            this.root = new Node(element);                                 // se a árvore está vazia, o novo nó vira a raiz
        }else{
            Node aux = this.root;                                           // começa a busca pelo local correto a partir da raiz

            while (aux != null){                                            // percorre a árvore até encontrar uma posição nula para inserir
                if(element < aux.value){
                    if(aux.left == null){                                   // se o valor é menor que o do nó atual e não há filho esquerdo
                        Node newNode = new Node(element);
                        aux.left = newNode;                                  // encontrou posição vazia à esquerda -> cria nó e conecta
                        newNode.parent = aux;                                // define o pai
                        return;                                             // inserção concluída
                    }
                    aux = aux.left;                                        // continua descendo à esquerda
                }else{
                    if(aux.right == null){                                  // caso contrário (>=) e não há filho direito
                        Node newNode = new Node(element);
                        aux.right = newNode;                                 // encontrou posição vazia à direita -> cria nó e conecta
                        newNode.parent = aux;                                // define o pai
                        return;                                             // inserção concluída
                    }
                    aux = aux.right;                                       // continua descendo à direita
                }
            }
        }
    }

    public void recursiveAdd(int element){                                 // inserção pública que usa versão recursiva
        if(isEmpty()) this.root = new Node(element);                        // se vazia, torna raiz
        else recursiveAdd(this.root, element);                              // caso contrário, chama rotina recursiva passando a raiz
    }

    private void recursiveAdd(Node node, int element){                      // rotina recursiva que insere element em a partir do nó fornecido
        if(element < node.value){
            if(node.left == null){                                          // se menor, tenta inserir na esquerda
                Node newNode = new Node(element);
                node.left = newNode;                                        // conecta como filho esquerdo
                newNode.parent = node;                                      // seta pai
                return;                                                     // fim
            }
            recursiveAdd(node.left, element);                              // desce recursivamente pela esquerda
        }else{
            if(node.right == null){                                         // se maior ou igual, tenta pela direita
                Node newNode = new Node(element);
                node.right = newNode;                                       // conecta como filho direito
                newNode.parent = node;                                      // seta pai
                return;                                                     // fim
            }
            recursiveAdd(node.right, element);                             // desce recursivamente pela direita
        }
    }

    public Node search(int element){                                       // busca iterativa: retorna o nó que contém o elemento ou null se não existir
        Node aux = this.root;                                               // começa pela raiz

        while(aux != null){
            if(aux.value == element) return aux;                           // encontrou -> retorna
            if(element < aux.value) aux = aux.left;                        // busca na esquerda
            else aux = aux.right;                                           // busca na direita
        }
        return null;                                                        // percorreu até um ponteiro nulo -> não achou
    }

    public Node sucessor(Node node){                                       // encontra o sucessor em ordem do nó dado
        if(node == null) return null;                                       // segurança: se nó nulo retorna nulo
        if(node.right != null) return min(node.right);                      // se existe subárvore direita, sucessor é o mínimo dessa subárvore
        else{
            Node aux = node.parent;                                         // caso contrário, sobe pela cadeia de pais até encontrar um ancestral maior
            while(aux != null && aux.value < node.value) aux = aux.parent;  // continua subindo enquanto o ancestral for menor
            return aux;                                                     // pode ser null se não houver sucessor
        }
    }

    public void remove(Node toRemove){                                     // remove um nó da árvore (recebe referência ao nó a ser removido)
        if (toRemove.isLeaf()){
            if(toRemove == this.root) this.root = null;                    // caso 1: nó folha e raiz -> árvore fica vazia
            else{
                if(toRemove.value < toRemove.parent.value)                // caso 1: nó folha -> apenas desconecta do pai
                    toRemove.parent.left = null;                          // era filho esquerdo -> remove
                else
                    toRemove.parent.right = null;                         // era filho direito -> remove
            }
        }else if(toRemove.hasOnlyLeftChild()){
            if (toRemove == this.root){
                this.root = toRemove.left;                                 // caso 2: apenas filho esquerdo -> filho vira raiz
                this.root.parent = null;                                   // atualiza pai da nova raiz
            }else{
                toRemove.left.parent = toRemove.parent;                   // conecta o filho com o avô
                if(toRemove.value < toRemove.parent.value)
                    toRemove.parent.left = toRemove.left;                // ajusta ponteiro do pai
                else
                    toRemove.parent.right = toRemove.left;
            }
        } else if (toRemove.hasOnlyRightChild()) {
            if (toRemove == this.root) {
                this.root = toRemove.right;                                // caso 3: apenas filho direito -> filho vira raiz
                this.root.parent = null;                                   // atualiza pai
            } else {
                toRemove.right.parent = toRemove.parent;                  // conecta o filho direito com o avô
                if (toRemove.value < toRemove.parent.value)
                    toRemove.parent.left = toRemove.right;               // ajusta ponteiro do pai
                else
                    toRemove.parent.right = toRemove.right;
            }
        }else{
            Node sucessor = sucessor(toRemove);                            // caso 4: nó com dois filhos -> encontra o sucessor em ordem
            toRemove.value = sucessor.value;                               // copia o valor do sucessor para o nó a ser removido
            remove(sucessor);                                               // remove recursivamente o sucessor (caso mais simples)
        }
    }

    public Node min(){                                                     // retorna o menor elemento da árvore (a partir da raiz)
        if (isEmpty()) return null;                                         // se vazia -> null
        return min(this.root);                                              // chama versão que recebe nó
    }

    private Node min(Node node){                                           // mínimo recursivo: desce sempre à esquerda
        if(node.left ==  null) return node;                                 // se não há filho esquerdo, este é o mínimo
        else return min(node.left);                                         // senão continua descendo
    }

    public Node max(){
        if (isEmpty()) return null;                                         // se vazia -> null
        return max(this.root);                                              // chama versão que recebe nó
    }

    private Node max(Node node){                                           // máximo recursivo: desce sempre à direita
        if (node == null) return null;                                      // se nó nulo retorna null
        if (node.right == null) return node;                                // se não há filho direito, este é o máximo
        else return max(node.right);                                         // senão continua descendo recursivamente à direita
    }

    public int countNodes(){                                               // retorna o número de nós na árvore
        return countNodes(this.root);
    }

    private int countNodes(Node node){                                     // conta nós recursivamente
        if (node == null) return 0;                                         // se nó nulo, não conta
        return 1 + countNodes(node.left) + countNodes(node.right);          // 1 para este nó + filhos
    }

    public int countLessThan(int k){                                       // conta nós com valor menor que k
        return countLessThan(this.root, k);
    }

    private int countLessThan(Node node, int k){                            // conta recursivamente nós cuja chave é menor que k
        if (node == null) return 0;                                         // nó nulo não contribui
        if (node.value < k) {                                               // este nó é menor que k
            return 1 + countLessThan(node.left, k) + countLessThan(node.right, k);
        } else {                                                            // este nó não é menor que k
            return countLessThan(node.left, k);                            // apenas subárvore esquerda pode ter valores menores
        }
    }

    public int sumLeaves(){                                                // soma os valores de todas as folhas da árvore
        return sumLeaves(this.root);
    }

    private int sumLeaves(Node node){                                      // rotina recursiva para somar folhas
        if (node == null) return 0;                                         // nada somado em subárvore vazia
        if (node.isLeaf()) return node.value;                               // só soma o valor se for folha
        return sumLeaves(node.left) + sumLeaves(node.right);                // soma das folhas das subárvores
    }

    public int height(){                                                   // altura da árvore (profundidade máxima). Convenção: árvore vazia = -1
        return height(this.root);
    }

    private int height(Node node){                                         // calculo recursivo da altura
        if(node == null) return -1;                                         // base: nó nulo tem altura -1
        else return 1 + Math.max(height(node.left), height(node.right));   // 1 + maior altura entre filhos
    }

    public Node predecessor(Node node){                                    // predecessor em ordem: maior valor à esquerda ou ancestral menor
        if(node == null) return null;                                       // segurança
        if(node.left != null) return max(node.left);                        // se existe subárvore esquerda, predecessor é o máximo dela
        else{
            Node aux = node.parent;                                         // sobe até encontrar ancestral menor
            while(aux != null && aux.value > node.value) aux = aux.parent;  // continua subindo
            return aux;                                                     // pode ser null
        }
    }

    public void preOrder(){ preOrder(this.root); }                         // percorre em pré-ordem (raiz, esquerda, direita)
    private void preOrder(Node node){
        if(node != null){
            System.out.print(node.value + " ");                           // visita raiz
            preOrder(node.left);                                            // percorre esquerda
            preOrder(node.right);                                           // percorre direita
        }
    }
 
    public void inOrder(){ inOrder(this.root); }                           // percorre em ordem (esquerda, raiz, direita)
    private void inOrder(Node node){
        if(node != null){
            inOrder(node.left);                                             // esquerda
            System.out.print(node.value + " ");                           // raiz
            inOrder(node.right);                                            // direita
        }
    }

    public void posOrder(){ posOrder(this.root); }                         // percorre em pós-ordem (esquerda, direita, raiz)
    private void posOrder(Node node){
        if(node != null){
            posOrder(node.left);                                            // esquerda
            posOrder(node.right);                                           // direita
            System.out.print(node.value + " ");                           // raiz
        }
    }

    public void printBFS(){                                                 // impressão em largura (BFS) usando fila (Deque)
        Deque<Node> queue = new LinkedList<Node>();                         // cria uma fila dupla ligada
        if(isEmpty()){                                                       // NOTE: provavelmente deveria ser if (!isEmpty()) — condição atual entra apenas quando está vazia
            queue.addLast(this.root);                                        // enfileira raiz
            while (!queue.isEmpty()){
                Node current = queue.removeFirst();                         // retira da frente
                System.out.println(current);                                // imprime o nó (toString retorna o valor)
                if(current.left != null) queue.addLast(current.left);       // enfileira filho esquerdo
                if(current.right != null) queue.addLast(current.right);     // enfileira filho direito
            }
        }
    }
}

class Node{                                                                // classe que representa um nó da BST
    int value;                                                              // valor armazenado no nó
    Node left;                                                              // referência para filho esquerdo
    Node right;                                                             // referência para filho direito
    Node parent;                                                            // referência para nó pai

    Node(int v){ this.value = v; }                                          // construtor: inicializa o valor
    boolean isLeaf(){ return this.left == null && this.right == null; }     // é folha se não tiver filhos
    boolean hasOnlyLeftChild(){ return this.left != null && this.right == null; } // apenas filho esquerdo
    boolean hasOnlyRightChild(){ return this.left == null && this.right != null; } // apenas filho direito
    @Override public String toString(){ return String.valueOf(value); }     // converte o valor para string para impressão
}