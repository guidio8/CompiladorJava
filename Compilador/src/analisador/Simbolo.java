package analisador;

public class Simbolo {
	private String lexema, tipo, valor;
	private int linha;
	
	public Simbolo(String lexema, String tipo, String valor, int linha){
		this.lexema = lexema;
		this.tipo = tipo;
		this.valor = valor;
		this.linha = linha;
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

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}
	
}
