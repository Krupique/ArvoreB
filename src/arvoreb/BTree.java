package arvoreb;

/**
 *
 * @author Henrique K.
 */
public class BTree {
    private No raiz;
    
    public BTree(){}
    
    public No getRaiz()
    {
        return raiz;
    }
    
    public No navegar_ate_folha(int info)
    {
        int i;
        No aux = raiz;
        while(aux.getVLig(0) != null)
        {
            i = 0;
            while(i < aux.getTl() && info > aux.getVInfo(i))
                i++;
            aux = aux.getVLig(i);
        }
        return aux;
    }
    
    public No navegar_ate_folha_exclusao(int info)
    {
        int i;
        No aux = raiz;
        while(aux.getVLig(0) != null)
        {
            i = 0;
            while(i < aux.getTl() && info > aux.getVInfo(i))
                i++;
            if(info == aux.getVInfo(i))
                return aux;
            else
                aux = aux.getVLig(i);
        }
        return aux;
    }
    
    public No localiza_pai(No folha, int info)
    {
        No aux, pai;
        int i;
        aux = raiz;
        pai = aux;
        while(aux != folha)
        {
            i = 0;
            while(i < aux.getTl() && info > aux.getVInfo(i))
                i++;
            pai = aux;
            aux = aux.getVLig(i);
        }
        return pai;
    }
    
    public void insere_arvore(int info)
    {
        No folha, pai;
        int pos;
        if(raiz == null)
        {
            raiz = new No(info);
        }
        else
        {
            folha = navegar_ate_folha(info);
            pos = folha.procura_posicao(info);
            folha.remaneja(pos);
            folha.setTl(folha.getTl() + 1);
            folha.setVInfo(pos, info);
            folha.setVPos(pos, info);
            if(folha.getTl() > 2 * TF.N)
            {
                pai = localiza_pai(folha, info);
                split(folha, pai);
            }
        }
    }
    
    public void split(No folha, No pai)
    {
        No cx1, cx2;
        int i, pos, info;
        cx1 = new No();
        cx2 = new No();
        for (i = 0; i < TF.N; i++) {
            cx1.setVInfo(i, folha.getVInfo(i));
            cx1.setVPos(i, folha.getVPos(i));
            cx1.setVLig(i, folha.getVLig(i));
        }
        cx1.setVLig(TF.N, folha.getVLig(TF.N));
        cx1.setTl(2);
        
        for (i = TF.N + 1; i < TF.N * 2 + 1; i++) {
            cx2.setVInfo(i - (TF.N + 1), folha.getVInfo(i));
            cx2.setVPos(i - (TF.N + 1), folha.getVPos(i));
            cx2.setVLig(i - (TF.N + 1), folha.getVLig(i));
        }
        cx2.setVLig(TF.N, folha.getVLig(TF.N * 2 + 1));
        cx2.setTl(2);
        
        if(folha == pai)
        {
            folha.setVInfo(0, folha.getVInfo(TF.N));
            folha.setVPos(0, folha.getVPos(TF.N));
            folha.setVLig(0, cx1);
            folha.setVLig(1, cx2);
            folha.setTl(1);
        }
        else
        {
            info = folha.getVInfo(TF.N);
            pos = pai.procura_posicao(info);
            pai.remaneja(pos);
            pai.setTl(pai.getTl() + 1);
            pai.setVInfo(pos, folha.getVInfo(TF.N));
            pai.setVPos(pos, folha.getVPos(TF.N));
            pai.setVLig(pos, cx1);
            pai.setVLig(pos + 1, cx2);
            if(pai.getTl() > 2 * TF.N)
            {
                folha = pai;
                info = folha.getVInfo(TF.N);
                pai = localiza_pai(raiz, info);
                split(folha, pai);
            }
        }
    }
    
    public boolean excluir_arvore(int info)
    {
        No no, pai;
        int i, pos;
        No subdir, subesq;
        if(raiz == null)
        {
            return false;
        }
        else
        {
            no = navegar_ate_folha_exclusao(info);
            pos = no.procura_posicao(info);
            if(no.getVInfo(pos) == info) //Achou o elemento
            {
                if(no.getVLig(0) != null) //Nó não é folha
                {
                    subdir = procura_subDir(no);
                    subesq = procura_subEsq(no);

                    if(subesq.getTl() > TF.N || subdir.getTl() <= TF.N)
                    {
                        no.setVInfo(pos, subesq.getVInfo(subesq.getTl() - 1));

                        no = subesq;
                        pos = subesq.getTl() + 1;
                    }
                    else
                    {
                        no.setVInfo(pos, subdir.getVInfo(0));

                        no = subdir;
                        pos = 0;
                    }
                }

                no.remaneja_exclusao(pos);
                no.setTl(no.getTl() - 1);
                if(no.getTl() < TF.N && no != raiz)
                {
                    pai = localiza_pai(no, no.getVInfo(0));
                    pos = pai.procura_posicao(no.getVInfo(0));
                    if(!redistribuir(no, pai, pos))
                    {
                        fusao(no, pai, pos);
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    private void fusao(No no, No pai, int pos)
    {
        No vizinho;
        No caixa = new No();
        if(pos == 0) //Pega vizinho da direita
        {
            vizinho = pai.getVLig(pos + 1);
            int i;
            for (i = 0; i < no.getTl(); i++) {
                caixa.setVInfo(i, no.getVInfo(i));
                caixa.setVPos(i, no.getVPos(i));
                caixa.setVLig(i, no.getVLig(i));
                caixa.setTl(caixa.getTl() + 1);
            }
            caixa.setVLig(i, no.getVLig(i));
            
            caixa.setVInfo(i, pai.getVInfo(pos));
            no.setVPos(no.getTl(), pai.getVPos(pos));
            pai.remaneja_exclusao(pos);
            pai.setTl(pai.getTl() - 1);
            caixa.setTl(caixa.getTl() + 1);
            i++;

            int j;
            for (j = 0; j < vizinho.getTl(); j++) {
                caixa.setVInfo(i, vizinho.getVInfo(j));
                caixa.setVPos(i, caixa.getVPos(j));
                caixa.setVLig(i, vizinho.getVLig(j));
                caixa.setTl(caixa.getTl() + 1);
                i++;
            }
            caixa.setVLig(i, vizinho.getVLig(j));
        }
        else
        {
            vizinho = pai.getVLig(pos - 1);
            int i = 0;
            for (i = 0; i < vizinho.getTl(); i++) {
                caixa.setVInfo(i, vizinho.getVInfo(i));
                caixa.setVPos(i, caixa.getVPos(i));
                caixa.setVLig(i, vizinho.getVLig(i));
                caixa.setTl(caixa.getTl() + 1);
            }
            caixa.setVLig(i, no.getVLig(i));
            caixa.setVInfo(i, pai.getVInfo(pos - 1));
            no.setVPos(no.getTl(), pai.getVPos(pos - 1));
            caixa.setTl(caixa.getTl() + 1);
            pai.remaneja_exclusao(pos - 1);
            pai.setTl(pai.getTl() - 1);
            i++;
            
            int j;
            for (j = 0; j < no.getTl(); j++) {
                caixa.setVInfo(i, no.getVInfo(j));
                caixa.setVPos(i, no.getVPos(j));
                caixa.setVLig(i, no.getVLig(j));
                caixa.setTl(caixa.getTl() + 1);
                i++;
            }
            caixa.setVLig(i, no.getVLig(j));
        }
        
        if(pai.getTl() < TF.N)
        {
            if(pai == raiz && pai.getTl() == 0)
                pai = raiz = caixa;
            else if(pai == raiz && pai.getTl() > 0)
                pai.setVLig(pos, caixa);
            else
            {
                pai.setVLig(pos, caixa);
                no = navegar_ate_folha_exclusao(pai.getVInfo(0));
                pai = localiza_pai(no, no.getVInfo(0));
                pos = pai.procura_posicao(no.getVInfo(0));
                fusao(no, pai, pos);
            }
        }
        else
        {
            pai.setVLig(pos, caixa);
        }
    }
    
    private boolean redistribuir(No no, No pai, int pos)
    {
        //A funcao de pegar os vizinhos.
        No vizinho;
        
        if(pos == 0) //Pega vizinho da direita.
        {
            vizinho = pai.getVLig(pos + 1);
            if(vizinho.getTl() > TF.N)
            {
                no.setVInfo(no.getTl(), pai.getVInfo(pos));
                no.setVPos(no.getTl(), pai.getVPos(pos));
                no.setTl(no.getTl() + 1);
                
                pai.setVInfo(pos, vizinho.getVInfo(0));
                pai.setVPos(pos, vizinho.getVPos(0));
                
                vizinho.remaneja_exclusao(0);
                vizinho.setTl(vizinho.getTl() - 1);
                return true;
            }
            
            //Nesse caso não tem vizinho na esquerda.
        }
        else //Vizinho da esquerda
        {
            vizinho = pai.getVLig(pos - 1);
            if(vizinho.getTl() > TF.N)
            {
                no.remaneja(0);
                no.setVInfo(0, pai.getVInfo(pos - 1));
                no.setVPos(0, pai.getVPos(pos - 1));
                no.setTl(no.getTl() + 1);
                
                pai.setVInfo(pos - 1, vizinho.getVInfo(vizinho.getTl() - 1));
                pai.setVPos(pos - 1, vizinho.getVPos(vizinho.getTl() - 1));
                
                vizinho.setTl(vizinho.getTl() - 1);
                return true;
            }
            else if(pos + 1 < pai.getTl() + 1) //Tenta pegar o vizinh da direita
            {
                vizinho =  pai.getVLig(pos + 1);
                if(vizinho.getTl() > TF.N)
                {
                    no.setVInfo(no.getTl(), pai.getVInfo(pos));
                    no.setVPos(no.getTl(), pai.getVPos(pos));
                    no.setTl(no.getTl() + 1);

                    pai.setVInfo(pos, vizinho.getVInfo(0));
                    pai.setVPos(pos, vizinho.getVPos(0));

                    vizinho.remaneja_exclusao(0);
                    vizinho.setTl(vizinho.getTl() - 1);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public No procura_subEsq(No no)
    {
        No esq = no.getVLig(0);
        while(esq.getVLig(esq.getTl()) != null)
            esq = esq.getVLig(esq.getTl());
        
        return esq;
    }
    
    public No procura_subDir(No no)
    {
        No dir = no.getVLig(no.getTl());
        while(dir.getVLig(0) != null)
            no = no.getVLig(0);
        
        return dir;
    }
    
    public void in_ordem(No raiz)
    {
        if(raiz != null)
        {
            for (int i = 0; i < raiz.getTl(); i++) {
                in_ordem(raiz.getVLig(i));
                System.out.print(raiz.getVInfo(i) + " ");
            }
            System.out.println("");
            in_ordem(raiz.getVLig(raiz.getTl()));
        }
    }
}
