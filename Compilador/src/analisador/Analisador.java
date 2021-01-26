package analisador;

import java.io.IOException;

public class Analisador {
	static Token[] tokens = new Token[1000]; //variaveis globais para poder ser usadas em todas as fuções dessa classe
	static int cont_tokens = 0;
	static int cont_simbolos = 0;
	static int linha = 1;
	static TabelaHash hash = new TabelaHash();
	public static TabelaHash[] tabela = new TabelaHash[300];
	static Simbolo[] simbolos = new Simbolo[100];
	
	public static void main(String[] args) throws IOException {
		Analisador automato = new Analisador();
		automato.iniciar(); //inicializa o automato
		System.out.println("Analise Lexica conluída");
		IO escrever = new IO(); //cria um novo objeto da classe IO, ja que o "teste" está fora dessa função para poder escrever os resultados
		escrever.abrir();
		for(int i = 0; i<cont_tokens; i++){
			escrever.escrever(tokens[i]);
		}
		for(int j = 0; j<300; j++){
			tabela[j] = new TabelaHash();
		}
		Analisador sintatico = new Analisador();
		sintatico.analiseSintatica();
		System.out.println("Analise Sintática conluída");
		hash.imprimeTabela(tabela);
	}
	IO teste = new IO();
	char entrada = teste.getNextChar(); //le a primeira entrada
	int i = 0;
	
	public void casa(Simbolo token){ //a funcao casa serve para colocar os simbolos na tabela
		boolean encontrou = false;
		int index = hash.hashCode(token);
		//simbolo não está na tabela e sua posição é livre
		if(tabela[index].disponivel == true){
			tabela[index].lexema = token.getLexema();
			tabela[index].linha = token.getLinha();
			tabela[index].disponivel = false;
			tabela[index].valor = token.getValor();
			tabela[index].tipo = token.getTipo();
		} else {
			if(tabela[index].lexema.equals(token.getLexema())) {
				//simbolo já inserido, deve ser atualizado
				tabela[index].linha = token.getLinha();
				tabela[index].valor = token.getValor();
			} else {
				//caso de colisão na posição, verifica se o símbolo já está inserido
				for(int j = 0; j < 300; j++) {
					if(tabela[index].lexema.equals(token.getLexema())){ 
						//simbolo já inserido, deve ser atualizado
						tabela[j].linha = token.getLinha();
						tabela[j].valor = token.getValor();
						encontrou = true;
					}
				}
				//Posição do simbolo está ocupada e ele não está na tabela
				//é então adicionado na próxima posição livre
				if(encontrou == false) {
					while(tabela[index].disponivel == false){
						index++;
						if (index >= hash.getTamTabela()){
							index = 0;
						}
					}
					tabela[index].lexema = token.getLexema();
					tabela[index].linha = token.getLinha();
					tabela[index].disponivel = false;
				}
			}
		}
	}
	
	public void iniciar(){
		String palavra = new String(); //cria uma palavra vazia que vai ser a concatenação das entradas, formando os tokens
		q0(palavra); //chama o estado inicial
	}
	
	public void q0(String palavra){
		while(entrada != 0){ //enquanto não ler o final do arquivo (se ler EOF o getNextChar() retorna 0)
			if(entrada == 10){ //se ler um \n adiciona uma linha
				linha++;
				entrada = teste.getNextChar(); //sempre que for feita uma leitura e comparação, le a proxima entrada
				if(entrada == 0){
					break; //se for lido o EOF logo após o final de uma linha(a ultima linha estando em branco) quebra o loop e encerra a função
				}
			}
			while(entrada == 32 || entrada == 9){ //se for lido um espaço ou um tab não é necessário fazer nada, apenas ler a próxima entrada
				entrada = teste.getNextChar();
				if(entrada == 10){
					linha++;
				}
			}
			if(entrada == 40){ //comparando sempre o valor do char lido, com o correspondente na tabela asc ii verifica qual é o "valor" da entrada
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha); //caso a entrada seja um ( cria um token para o left bracket
				cont_tokens++; //sempre que for criado um token novo, adiciona 1 no contador de tokens
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 41){ //compara entrada com ")"
				tokens[cont_tokens] = new Token("RBRACKET", ")", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 123){ //compara entrada com "{" e assim sucessivamente
				tokens[cont_tokens] = new Token("LBRACE", "{", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 125){
				tokens[cont_tokens] = new Token("RBRACE", "}", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 93){
				tokens[cont_tokens] = new Token("RCOL", "]", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 61){
				entrada = teste.getNextChar();
				if(entrada == 61){ //essa comparação é para saber se foi lido um equals ou apenas uma atribuição
					tokens[cont_tokens] = new Token ("EQ", "==", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}else{
				tokens[cont_tokens] = new Token("ATTR", "=", linha);
				cont_tokens++;
				q0("");
				}
			}
			if(entrada == 47){
				tokens[cont_tokens] = new Token("DIV", "/", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 42){
				tokens[cont_tokens] = new Token("MULT", "*", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 45){
				tokens[cont_tokens] = new Token("MINUS", "-", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 43){
				tokens[cont_tokens] = new Token("PLUS", "+", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 59){
				tokens[cont_tokens] = new Token("PCOMMA", ";", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 44){
				tokens[cont_tokens] = new Token("COMMA", ",", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 124){
				entrada = teste.getNextChar();
				if(entrada == 124){ //se for lido um || é criado um token para tal, mas se for lido apenas um "|" então cria um token inválido
					tokens[cont_tokens] = new Token("OR", "||", linha);
					cont_tokens++;
				}else{
					tokens[cont_tokens] = new Token ("INVALID ID", "|", linha);
					cont_tokens++;
				}
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 38){
				entrada = teste.getNextChar();
				if(entrada == 38){
					tokens[cont_tokens] = new Token("AND", "&&", linha);
					cont_tokens++;
				}else{
					tokens[cont_tokens] = new Token ("INVALID ID", "&", linha);
					cont_tokens++;
				}
				entrada = teste.getNextChar();
				q0("");
			}
			if(entrada == 60){
				entrada = teste.getNextChar();
				if(entrada == 61){ // como o if de fora verifica se foi lido um "<", caso a proxima entrada seja um "=" então foi lido um LE
					tokens[cont_tokens] = new Token("LE", "<=", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}else{ //caso contrario com certeza foi lido um LT ja que o "<" foi lido
					tokens[cont_tokens] = new Token("LT", "<", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}
			}
			if(entrada == 62){
				entrada = teste.getNextChar();
				if(entrada == 61){
					tokens[cont_tokens] = new Token("GE", ">=", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}else{
					tokens[cont_tokens] = new Token("GT", ">", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}
			}
			if(entrada >= 48 && entrada <= 57){ //verifica se a entrada foi um número
				while(entrada >= 48 && entrada <= 57){ //enquanto estiver lendo números vai concatenando um ao outro, por exemplo se ler 123, concatena o 1, depois o 2, depois o 3
					palavra = palavra + entrada;
					entrada = teste.getNextChar();
				}if(entrada != 46){ //se nao ler nenhum . cria o INT_CONST
					tokens[cont_tokens] = new Token("INT_CONST", palavra, linha); 
					cont_tokens++;
					q0("");
				}else if(entrada == 46){ //se por acaso ler um "." em algum momento do numero, vai ser lido um número decimal
					palavra = palavra + entrada; //concatena o . e lê a próxima entrada
					entrada = teste.getNextChar();
					while(entrada >= 48 && entrada <= 57){ //enquanto estiver lendo numeros vai concatenando um ao outro
						palavra = palavra + entrada;
						entrada = teste.getNextChar();
					}
					tokens[cont_tokens] = new Token("FLOAT_CONST", palavra, linha); //quando ler algo que não seja um número cria um FLOAT_CONST para representar o float lidp
					cont_tokens++;
					if(entrada == 59){
						tokens[cont_tokens] = new Token("PCOMMA", ";", linha); //caso o número seja seguido do ";" ja cria um token PCOMMA
						cont_tokens++;
					}
					entrada = teste.getNextChar();
					q0("");
				}
			}
			while((entrada >= 65 && entrada <= 90) || (entrada >= 97 && entrada <= 122)){
				//apos ter feito as comparações com símbolos e números, quando a entrada for uma letra, começa uma concatenação para verificar qual token vai representar a palavra
				palavra = palavra + entrada;
				entrada = teste.getNextChar();
			}
				q1(palavra); //chama o próximo estado que vai verificar qual é o token que representa a palavra lida
		}
	}
	
	public void q1(String palavra){
		//compara a palavra lida com as palavras reservadas (int, float, main, etc) e se forem iguais cria o respectivo token
		if(palavra.equals("int")){
			tokens[cont_tokens] = new Token("INT", palavra, linha);
			cont_tokens++;
			entrada = teste.getNextChar();
			q0("");
		}else if(palavra.equals("main")){
			tokens[cont_tokens] = new Token("MAIN", palavra, linha);
			cont_tokens++;
			if(entrada == 40){
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else{
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("float")){
			tokens[cont_tokens] = new Token("FLOAT", palavra, linha);
			cont_tokens++;
			entrada = teste.getNextChar();
			q0("");
		}else if(palavra.equals("if")){
			tokens[cont_tokens] = new Token("IF", palavra, linha);
			cont_tokens++;
			if(entrada == 40){
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else{
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("else")){
			tokens[cont_tokens] = new Token("ELSE", palavra, linha);
			cont_tokens++;
			entrada = teste.getNextChar();
			if(entrada == 32) {
				linha++;
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("while")){
			tokens[cont_tokens] = new Token("WHILE", palavra, linha);
			cont_tokens++;
			if(entrada == 40){
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else{
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("for")){
			tokens[cont_tokens] = new Token("FOR", palavra, linha);
			cont_tokens++;
			if(entrada == 40){
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else{
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("read")){
			tokens[cont_tokens] = new Token("READ", palavra, linha);
			cont_tokens++;
			if(entrada == 40){
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else{
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("print")){
			tokens[cont_tokens] = new Token("PRINT", palavra, linha);
			cont_tokens++;
			if(entrada == 40){
				tokens[cont_tokens] = new Token("LBRACKET", "(", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else if(entrada == 91){
				tokens[cont_tokens] = new Token("LCOL", "[", linha);
				cont_tokens++;
				entrada = teste.getNextChar();
			}else{
				entrada = teste.getNextChar();
			}
			q0("");
		}else if(palavra.equals("") || palavra.equals(" ")){
			//Essa comparação serve para caso seja lido uma combinação de espaços e \t para não criar um ID vazio
			entrada = teste.getNextChar();
			q0("");
		}else if(palavra.charAt(0) >= 97 && palavra.charAt(0) <= 122){
			//se a palavra não for igual a nenhuma palavra reservada, que dizer que é um ID, mas o ID precisa começar especificamente com uma letra minúscula
			tokens[cont_tokens] = new Token("ID", palavra, linha);
			cont_tokens++;
			if(entrada == 59){
				tokens[cont_tokens] = new Token("PCOMMA", ";", linha);
				cont_tokens++;
			}
			if(entrada == 44){
				tokens[cont_tokens] = new Token("COMMA", ",", linha);
				cont_tokens++;
			}
			if(entrada == 61){
				tokens[cont_tokens] = new Token("ATTR", "=", linha);
				cont_tokens++;
			}
			if(entrada == 41){
				tokens[cont_tokens] = new Token("RBRACKET", ")", linha);
				cont_tokens++;
			}
			if(entrada == 43){
				tokens[cont_tokens] = new Token("PLUS", "+", linha);
				cont_tokens++;
			}
			if(entrada == 45){
				tokens[cont_tokens] = new Token("MINUS", "-", linha);
				cont_tokens++;
			}
			if(entrada == 42){
				tokens[cont_tokens] = new Token("MULT", "*", linha);
				cont_tokens++;
			}
			if(entrada == 47){
				tokens[cont_tokens] = new Token("DIV", "/", linha);
				cont_tokens++;
			}
			if(entrada == 91){
				tokens[cont_tokens] = new Token("LCOL", "[", linha);
				cont_tokens++;
			}
			if(entrada == 60){
				entrada = teste.getNextChar();
				if(entrada == 61){ // como o if de fora verifica se foi lido um "<", caso a proxima entrada seja um "=" então foi lido um LE
					tokens[cont_tokens] = new Token("LE", "<=", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}else{ //caso contrario com certeza foi lido um LT ja que o "<" foi lido
					tokens[cont_tokens] = new Token("LT", "<", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}
			}
			if(entrada == 62){
				entrada = teste.getNextChar();
				if(entrada == 61){ // como o if de fora verifica se foi lido um ">", caso a proxima entrada seja um "=" então foi lido um GE
					tokens[cont_tokens] = new Token("GE", "<=", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}else{ //caso contrario com certeza foi lido um GT ja que o ">" foi lido
					tokens[cont_tokens] = new Token("GT", ">", linha);
					cont_tokens++;
					entrada = teste.getNextChar();
				}
			}
			entrada = teste.getNextChar();
			q0("");
		}
	}

	public void analiseSintatica(){
		programa();
	}
	
	public void match(String esperado){
		
		if(tokens[i].getNome().equals(esperado)){
			if(i<=cont_tokens){
				i++; //caso o token que esta sendo avaliado seja o mesmo que o esperado, a função vai casar e ler o próximo token, se houver
			}
		}else{
			System.out.println("Deu problema leu: "+tokens[i].getNome()+" era para ter lido "+esperado); //caso nao seja o mesmo, avisa do erro
			if(i<(cont_tokens-1)){//e caso ainda existam tokens na entrada, le o proximo e tenta casar com o esperado, realizando a sincronizacao
				i++;
				match(esperado);
			}else{
				System.out.println("Acabou a entrada");//caso nao haja mais tokens na entrada, avisa que nao ha mais tokens na entrada e termina o programa
				System.exit(1);
			}
		}
		if(esperado.equals("ID")){ //para colocar os simbolos na tabela de simbolo, primeiro verifica se o token é um ID, ja que essa parte só sera realizada
			//se e somente se o token lido for um ID E estiver esperando um ID
			if(cont_simbolos>0){//cria um novo simbolo para a tabela de simbolos, o novo simbolo sempre tera o mesmo tipo do simbolo anterior lido
				simbolos[cont_simbolos] = new Simbolo(tokens[i-1].getLexema(), simbolos[cont_simbolos-1].getTipo(), "0", tokens[i-1].getLinha());
			}else{//primeiro simbolo comeca com o tipo null
				simbolos[cont_simbolos] = new Simbolo(tokens[i-1].getLexema(), "null", "0", tokens[i-1].getLinha());
			}
			if(tokens[i].getNome().equals("ATTR")){//se ler uma atribuicao, tem que mudar o valor do simbolo lido
				simbolos[cont_simbolos].setValor(tokens[i+1].getLexema());
			}
			if(tokens[i-2].getNome().equals("INT")){//essa comparacao serve para mudar o simbolo para o tipo certo caso ele seja o primeiro da linha
				simbolos[cont_simbolos].setTipo("INT");
			}else if(tokens[i-2].getNome().equals("FLOAT")){
				simbolos[cont_simbolos].setTipo("FLOAT");
			}
			casa(simbolos[cont_simbolos]);//casa os simbolos
			cont_simbolos++;
		}
	}
	
	public void erroNaLinha(int linha, String esperado){
		System.out.println("Erro na linha: "+linha+" era esperado: "+esperado);
	}
	//as funcoes a seguir, sao baseadas na Gramatica dada, a funcao match é chamada sempre que existe um não terminal no lado direito da produção
	public void programa(){
		match("INT");
		match("MAIN");
		match("LBRACKET");
		match("RBRACKET");
		match("LBRACE");
		decl_comando();
		match("RBRACE");
	}
	
	public void decl_comando(){
		if(tokens[i].getNome() == "INT" || tokens[i].getNome() == "FLOAT"){//essa linha esta fazendo a função de descobrir o first para saber se chama declaracao ou comando
			declaracao();
			decl_comando();
		}else if(tokens[i].getNome() == "LBRACE" || 
				tokens[i].getNome() == "ID" || 
				tokens[i].getNome() == "IF" || 
				tokens[i].getNome() == "WHILE" || 
				tokens[i].getNome() == "READ" || 
				tokens[i].getNome() == "PRINT" || 
				tokens[i].getNome() == "FOR"){
			comando();
			decl_comando();
		}
	}
	
	public void declaracao(){
		String x = new String();
		x = tipo();
		match(x);
		match("ID");
		decl2();
	}
	
	public String tipo(){
		if(tokens[i].getNome().equals("INT")){
			return "INT";
		}
		if(tokens[i].getNome().equals("FLOAT")){
			return "FLOAT";
		}
		return null;
	}
	
	public void decl2(){
		if(tokens[i].getNome().equals("COMMA")){
			//System.out.println("Casou COMMA");
			if(i<cont_tokens-1){
				i++;
			}
			match("ID");
			decl2();
		}else if(tokens[i].getNome().equals("PCOMMA")){
			//System.out.println("Casou PCOMMA");
			if(i<cont_tokens-1){
				i++;
			}
		}else if(tokens[i].getNome().equals("ATTR")){
			//System.out.println("Casou ATTR");
			if(i<cont_tokens-1){
				i++;
			}
			expressao();
			decl2();
		}else{
			String esperado  = "COMMA ou PCOMMA ou ATTR";
			erroNaLinha(tokens[i].getLinha(), esperado);
			if(i<cont_tokens-1){
				i++;
				decl2();
			}else{
				erroNaLinha(tokens[i].getLinha(), esperado);
				System.exit(1);
			}
		}
	}
	
	public void expressao(){
		adicao();
		relacaoOpc();
	}
	
	public void relacaoOpc(){
		if(tokens[i].getNome().equals("LT")){
			match("LT");
			adicao();
			relacaoOpc();
		}else if(tokens[i].getNome().equals("LE")){
			match("LE");
			adicao();
			relacaoOpc();
		}else if(tokens[i].getNome().equals("GT")){
			match("GT");
			adicao();
			relacaoOpc();
		}else if(tokens[i].getNome().equals("GE")){
			match("GE");
			adicao();
			relacaoOpc();
		}else if(tokens[i].getNome().equals("EQ")){
			match("EQ");
			adicao();
			relacaoOpc();
		}else if(tokens[i].getNome().equals("OR")){
			match("OR");
			adicao();
			relacaoOpc();
		}else if(tokens[i].getNome().equals("AND")){
			match("AND");
			adicao();
			relacaoOpc();
		}
	}
	
	public void adicao(){
		 termo();
		 adicaoOpc();
	}
	
	public void adicaoOpc(){
		if(tokens[i].getNome().equals("PLUS")){
			match("PLUS");
			termo();
			adicaoOpc();
		}else if(tokens[i].getNome().equals("MINUS")){
			match("MINUS");
			termo();
			adicaoOpc();
		}
	}
	
	public void termo(){
		fator();
		termoOpc();
	}
	
	public void termoOpc(){
		if(tokens[i].getNome().equals("MULT")){
			match("MULT");
			fator();
			termoOpc();
		}else if(tokens[i].getNome().equals("DIV")){
			match("DIV");
			fator();
			termoOpc();
		}
	}
	
	public void fator(){
		if(tokens[i].getNome().equals("ID")){
			match("ID");
		}else if(tokens[i].getNome().equals("INT_CONST")){
			match("INT_CONST");
		}else if(tokens[i].getNome().equals("FLOAT_CONST")){
			match("FLOAT_CONST");
		}else if(tokens[i].getNome().equals("LBRACKET")){
			match("LBRACKET");
			expressao();
			match("RBRACKET");
		}
	}
	
	public void comando(){
		if(tokens[i].getNome().equals("LBRACE")){
			match("LBRACE");
			bloco();
		}else if(tokens[i].getNome().equals("ID")){
			match("ID");
			atribuicao();
		}else if(tokens[i].getNome().equals("IF")){
			match("IF");
			comandoSe();
		}else if(tokens[i].getNome().equals("WHILE")){
			match("WHILE");
			comandoEnquanto();
		}else if(tokens[i].getNome().equals("READ")){
			match("READ");
			comandoRead();
		}else if(tokens[i].getNome().equals("PRINT")){
			match("PRINT");
			comandoPrint();
		}else if(tokens[i].getNome().equals("FOR")){
			match("FOR");
			comandoFor();
		}
	}
	
	public void bloco(){
		decl_comando();
		match("RBRACE");
	}

	public void atribuicao(){
		match("ATTR");
		expressao();
		match("PCOMMA");
	}
	
	public void comandoSe(){
		match("LBRACKET");
		expressao();
		match("RBRACKET");
		comando();
		comandoSenao();
	}
	
	public void comandoSenao(){
		if(tokens[i].getNome().equals("ELSE")){
			match("ELSE");
			comando();
		}
	}

	public void comandoEnquanto(){
		match("LBRACKET");
		expressao();
		match("RBRACKET");
		comando();
	}

	public void comandoRead(){
		match("ID");
		match("PCOMMA");
	}

	public void comandoPrint(){
		match("LBRACKET");
		expressao();
		match("RBRACKET");
		match("PCOMMA");
	}

	public void comandoFor(){
		match("LBRACKET");
		atribuicaoFor();
		match("PCOMMA");
		expressao();
		match("PCOMMA");
		atribuicaoFor();
		match("RBRACKET");
		comando();
	}
	
	public void atribuicaoFor(){
		match("ID");
		match("ATTR");
		expressao();
	}
}