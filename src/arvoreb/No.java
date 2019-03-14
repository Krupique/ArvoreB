package arvoreb;

/**
 *
 * @author Henrique K.
 */
public class No implements TF{
    private int[] vInfo;
    private int[] vPos;
    private No[] vLig;
    private int tl;
    
    public No()
    {
        vInfo = new int[N * 2 + 1];
        vPos  = new int[N * 2 + 1];
        vLig = new No[N * 2 + 2];
        tl = 0;
    }
    
    public No(int info)
    {
        vInfo = new int[N * 2 + 1];
        vPos  = new int[N * 2 + 1];
        vLig = new No[N * 2 + 2];
        
        vInfo[0] = info;
        tl = 1;
    }
    
    public int getVInfo(int pos){
        return vInfo[pos];
    }
    
    public void setVInfo(int pos, int info){
        vInfo[pos] = info;
    }
    
    public int getVPos(int pos){
        return vPos[pos];
    }
    
    public void setVPos(int pos, int info){
        vPos[pos] = info;
    }
    
    public No getVLig(int pos){
        return vLig[pos];
    }
    
    public void setVLig(int pos, No no){
        vLig[pos] = no;
    }

    public int getTl() {
        return tl;
    }

    public void setTl(int tl) {
        this.tl = tl;
    }
    
    public int procura_posicao(int info)
    {
        int i = 0;
        while(i < tl && info > vInfo[i])
            i++;
        return i;
    }
    
    public void remaneja(int pos)
    {
        vLig[tl + 1] = vLig[tl];
        for (int i = tl; i > pos; i--) {
            vInfo[i] = vInfo[i - 1];
            vPos[i] = vPos[i - 1];
            vLig[i] = vLig[i - 1];
        }
    }
    
    public void remaneja_exclusao(int pos)
    {
        for (int i = pos; i < tl - 1; i++) {
            vInfo[i] = vInfo[i + 1];
            vLig[i] = vLig[i + 1];
            vPos[i] = vPos[i + 1];
        }
        vLig[tl] = vLig[tl + 1];
    }
}
