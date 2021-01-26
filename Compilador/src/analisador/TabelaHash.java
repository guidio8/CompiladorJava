package analisador;

public class TabelaHash {
	String lexema;
	String tipo;
	int linha;
	int tamTabela;
	String valor;
	boolean disponivel = true;
	
	public TabelaHash(){
		this.lexema = "";
		this.tipo = "";
		this.linha = -1;
		this.tamTabela = 300;
		this.valor = "";
		this.disponivel = true;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int hashCode(Simbolo token){
		return token.getLexema().hashCode() % tamTabela;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getTamTabela() {
		return tamTabela;
	}
	public boolean isDisponivel(TabelaHash tabela){
		return tabela.disponivel;
	}
	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}
	public void imprimeTabela(TabelaHash[] tabela){
		System.out.println("TABELA DE SIMBOLOS\n");
		System.out.println("Lexema | Tipo | Valor | Linha");
		for(int i = 0; i < tamTabela; i++){
			if(tabela[i].disponivel == false){
				System.out.println(tabela[i].getLexema()+" "+tabela[i].getTipo()+" "+tabela[i].getValor()+" "+tabela[i].getLinha());
			}
		}
	}
	
}
