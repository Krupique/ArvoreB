/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arvoreb;

/**
 *
 * @author Henrique K. Secchi
 */
public class ArvoreB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        BTree btree = new BTree();
        btree.insere_arvore(10);
        btree.insere_arvore(20);
        btree.excluir_arvore(10); //Primeiro caso: Remover um elemento e ainda sim continuar maior que TF.
        System.out.println("Primeiro caso");
        btree.in_ordem(btree.getRaiz());
       
        
        btree.insere_arvore(30);
        btree.insere_arvore(40);
        btree.insere_arvore(10);
        btree.insere_arvore(15); //20 mid
        btree.insere_arvore(1);
        btree.excluir_arvore(20); //Segunda caso: Remover a raiz e substituir pelo substituto.
        System.out.println("\n---------------\nSegundo caso");
        btree.in_ordem(btree.getRaiz());
        
        
        btree.insere_arvore(5); //20 mid
        btree.excluir_arvore(30); //Terceiro caso: Redistribui o no. (Pega dos vizinhos). //10 mid
        System.out.println("\n---------------\nTerceiro caso");
        btree.in_ordem(btree.getRaiz());
        
        
        
        btree.excluir_arvore(1); //Quarto caso: Não consegue redistribuir e faz a fuuuuusão, ha.
        System.out.println("\n---------------\nQuarto caso");
        btree.in_ordem(btree.getRaiz());
        // 5 - 10 - 15 - 40
        
    }
    
}
