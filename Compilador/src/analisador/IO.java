package analisador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IO {
	
	BufferedReader br = null;
	
	public char getNextChar() { //função getNextChar que retorna um char correspondente à entrada lida, e caso seja o EOF retorna 0
		int aux;
		try {
			aux = br.read();
			if(aux != -1){
				return (char)aux;
			}else{
				return 0;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public void recarregar(){ //o método recarregar reinicia a leitura a partir do início do documento
		try {
			br = new BufferedReader(new FileReader("exemplo.c"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public IO(){ //construtor vazio que abre o documento a ser lido
		try {
			br = new BufferedReader(new FileReader("exemplo.c"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void abrir() throws IOException { //abre o arquivo de saida
			    BufferedWriter writer = new BufferedWriter(new FileWriter("saida.txt"));
			    writer.write("");
			     
			    writer.close();
			}
	
	public void escrever(Token token) throws IOException { //escreve no arquivo de saida
			    BufferedWriter writer = new BufferedWriter(new FileWriter("saida.txt", true));
			    writer.append(token.getNome()+" "+token.getLinha()+" "+token.getLexema());
			    writer.append('\n');
			     
			    writer.close();
			}
}
