package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/*codLivro varchar(6),
numeroExemplar int*/

public class Exemplares {
    public static JComboBox operacao;
    public static JButton realizar , voltar , selecionar;
    //public static int idBibliotecaEscolhida;
    public static JPanel container , painelCampos;
    public static JTextField inputCodLivro, inputNumExemplar;
    public static JTextField inputIdExemplar;
    public static JTable tabelaResultadoSql;
    public static String opcao;

    // Inicializar o valor de idBibliotecaEscolhida
    public static int idBibliotecaEscolhida;

    static {
        // Chamando o método no bloco estático
        Login.setIdBibliotecaEscolhida();
        idBibliotecaEscolhida = Login.idBibliotecaEscolhida;
        System.out.println(idBibliotecaEscolhida);
    }


    public static void montar(){
        JPanel panelCampos;

        String[] opcoes = new String[]{"INCLUIR" , "DELETAR" , "ALTERAR" , "BUSCAR"};

        operacao = new JComboBox(opcoes);
        operacao.setPreferredSize(new Dimension(100 , 25));

        //realiza a opcao escolhida
        realizar = new JButton("REALIZAR");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //realizar a função escolhida no cbx, pegar os dados
                //dos campos do formulario e fazer a consulta
                try {
                    consultas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        selecionar = new JButton("SELECIONAR");
        selecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    mostrarInputs();
                }
                catch (Exception erro){
                    System.out.println(erro.getMessage());
                }
            }
        });

        // Criar um painel interno para os campos de texto e labels
        panelCampos = new JPanel();
        panelCampos.add(operacao , BorderLayout.NORTH);
        //panelCampos.add(voltar , BorderLayout.NORTH);
        panelCampos.add(selecionar , BorderLayout.NORTH);
        panelCampos.add(realizar , BorderLayout.NORTH);

        // painel principal para organizar campos e botão
        container = new JPanel();
        container.setLayout(new GridLayout(5 , 4 , 5 , 5));
        container.add(panelCampos, BorderLayout.NORTH); // Campos no centro
        container.add(Box.createVerticalStrut(15));
    }

//    public static void setIdBibliotecaEscolhida(Login id){
//        idBibliotecaEscolhida = Integer.parseInt(id.toString());
//        // AQUI VAI TER QUE FAZER UM WHERE COM O ID DA BIBLIOTECA SELECIONADA
//        // EM TUDO QUE FOR FAZER
//        Login.setIdBibliotecaEscolhida();
//        int idBibliotecaEscolhida = Login.idBibliotecaEscolhida;
//    }

    public static void mostrarInputs() {
        opcao = new String();

        if (painelCampos == null) {
            painelCampos = new JPanel();
        }

        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudança
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração

        opcao = operacao.getSelectedItem().toString();

        switch (opcao) {
            case "BUSCAR" -> {
                inputCodLivro = new JTextField(10);
                inputNumExemplar = new JTextField(10);
                JLabel codLivro = new JLabel("Digite o código do livro: ");
                JLabel ou = new JLabel("ou: ");
                JLabel numExemplar = new JLabel("Digite o número do exemplar: ");

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(2, 2, 5, 5)); // Layout de formulário simples

                painelCampos.add(codLivro);
                painelCampos.add(inputCodLivro);
                painelCampos.add(ou);
                painelCampos.add(numExemplar);
                painelCampos.add(inputNumExemplar);

                container.add(painelCampos, BorderLayout.CENTER);
            }
            case "INCLUIR" -> {
                inputCodLivro = new JTextField(10);
                inputNumExemplar = new JTextField(10);

                JLabel codLivro = new JLabel("Digite o código do livro: ");
                JLabel numExemplar = new JLabel("Digite o número do exemplar: ");

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(4, 2, 5, 5)); // Layout de formulário completo

                painelCampos.add(codLivro);
                painelCampos.add(inputCodLivro);
                painelCampos.add(numExemplar);
                painelCampos.add(inputNumExemplar);

                container.add(painelCampos, BorderLayout.CENTER);
            }
            case "DELETAR" -> {
                JLabel idExemplar = new JLabel("Digite o ID do exemplar: ");
                inputIdExemplar = new JTextField(10);

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(1, 2, 5, 5));
                painelCampos.add(idExemplar);
                painelCampos.add(inputNumExemplar);

                container.add(painelCampos, BorderLayout.CENTER);
            }
            case "ALTERAR" -> {
                String[] dados = new String[]{"Id Exemplar", "Id Biblioteca", "Código Livro", "Número Exemplar"};
                JLabel whereAntigo = new JLabel("Qual é o dado de referência?");
                JComboBox<String> cbxAntigo = new JComboBox<>(dados);
                JLabel LDadoAntigo = new JLabel("Digite o dado de referência: ");
                JTextField dadoAntigo = new JTextField(10);
                JLabel qualAlterar = new JLabel("Qual dado quer alterar?");
                JComboBox<String> cbxAlterar = new JComboBox<>(dados);
                JLabel LNovo = new JLabel("Qual é o novo dado?");
                JTextField novoDado = new JTextField(10);

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(6, 2, 5, 5));
                painelCampos.add(whereAntigo);
                painelCampos.add(cbxAntigo);
                painelCampos.add(LDadoAntigo);
                painelCampos.add(dadoAntigo);
                painelCampos.add(qualAlterar);
                painelCampos.add(cbxAlterar);
                painelCampos.add(LNovo);
                painelCampos.add(novoDado);

                container.add(painelCampos, BorderLayout.CENTER);
            }
        }

        // Atualizar o layout após alterações
        container.revalidate();
        container.repaint();
    }

    public static JPanel realizarTudo() throws Exception{
        montar();
        return container;
    }

    public static void consultas() throws SQLException {
        Statement comandoSql;
        switch (opcao){
            case "INCLUIR":
                //PASSAR COMO PARAMETRO O INSERT INTO COM OS DADOS
                String sql = "INSERT INTO SisBib.Exemplar(idBiblioteca, codLivro, numeroExemplar) values (? , ? , ?)";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setString(1 , String.valueOf(idBibliotecaEscolhida));     //primeiro '?'
                    preparedStatement.setString(2 , inputCodLivro.getText());                   //segundo '?'
                    preparedStatement.setString(3 , inputNumExemplar.getText());                //terceiro '?'

                    System.out.println(sql);
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                    JOptionPane.showMessageDialog(null , "Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "DELETAR":
                sql = "delete from SisBib.Exemplar where idExemplar = ?";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setString(1 , inputIdExemplar.getText());

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                    JOptionPane.showMessageDialog(null , "Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {

                    throw new RuntimeException(ex);
                }
                break;
            case "ALTERAR":
                sql = "update SisBib.Exemplar set codLivro = ? where numeroExemplar = ?";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1 , Integer.parseInt(inputCodLivro.getText()));
                    preparedStatement.setInt(2 , Integer.parseInt(inputNumExemplar.getText()));
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "BUSCAR":
                String codLivro = inputCodLivro.getText();
                String numExemplar = inputNumExemplar.getText();

                if (codLivro.equals("") && numExemplar.equals("")){
                    try{
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida);        //tem que fazer um where do idBiblioteca

                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                //System.out.println(resultadoDoSelect.getInt("idArea"));
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"idExemplar" , "idBiblioteca" , "codLivro" , "numeroExemplar"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "idExemplar";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "idBiblioteca";
                            resultadoSQL[0][2] = "CÓdigo Livro";
                            resultadoSQL[0][3] = "Número Exemplar";

                            for (int i = 1 ; i < linhas ; i++){
                                resultadoDoSelect.next();
                                //resultadoSQL[i][0] = resultadoDoSelect.getInt("codLivro");
                                //System.out.println(Integer.toString(resultadoDoSelect.getInt("codLivro")));
                                resultadoSQL[i][0] = resultadoDoSelect.getString("idExemplar");
                                resultadoSQL[i][1] = resultadoDoSelect.getString("idBiblioteca");
                                resultadoSQL[i][2] = resultadoDoSelect.getString("codLivro");
                                resultadoSQL[i][3] = resultadoDoSelect.getString("numeroExemplar");
                            }

                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            tabelaResultadoSql.setVisible(true);

                            JScrollPane scrollPane = new JScrollPane(tabelaResultadoSql);
                            container.add(scrollPane, BorderLayout.CENTER);

                            container.setVisible(true);

                            if (resultadoDoSelect != null){
                                System.out.println("Deu certo a busca");
                            }
                            else{
                                System.out.println("Deu errado!");
                            }
                        }
                        else{
                            System.out.println("o resultado está dando nulo");
                        }
                    }
                    catch (SQLException erro){
                        System.out.println(erro.getMessage());
                    }
                }
                else {
                    if (numExemplar.equals("")){ //se o usuario tiver digitado somente o cod
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Exemplar where codLivro = '" + inputCodLivro.getText() + "'");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                System.out.println(resultadoDoSelect.getInt("idExemplar")); //ELE TA PEGANDO AQUI
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"idExemplar" , "idBiblioteca" , "codLivro" , "numeroExemplar"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "idExemplar";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "idBiblioteca";
                            resultadoSQL[0][2] = "CÓdigo Livro";
                            resultadoSQL[0][3] = "Número Exemplar";

                            for (int i = 1 ; i < linhas ; i++){
                                resultadoDoSelect.next();
                                //resultadoSQL[i][0] = resultadoDoSelect.getInt("codLivro");
                                //System.out.println(Integer.toString(resultadoDoSelect.getInt("codLivro")));
                                resultadoSQL[i][0] = resultadoDoSelect.getString("codLivro");
                                resultadoSQL[i][1] = resultadoDoSelect.getString("titulo");
                                resultadoSQL[i][2] = resultadoDoSelect.getString("idAutor");
                                resultadoSQL[i][3] = resultadoDoSelect.getString("idArea");
                            }

                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            if (tabelaResultadoSql != null){
                                System.out.println("A tabela tem coisa");
                            }
                            else{
                                System.out.println("tabela nao está guardando dados");
                            }
                            JScrollPane scrollPane = new JScrollPane(tabelaResultadoSql);
                            container.add(scrollPane, BorderLayout.CENTER);

                            container.setVisible(true);

                            if (resultadoDoSelect != null){
                                System.out.println("Deu certo a busca");
                            }
                            else{
                                System.out.println("Deu errado!");
                            }
                        }
                        else{
                            System.out.println("o resultado está dando nulo");
                        }
                    }
                    else if (numExemplar.equals("")) {   //usuario digitou somente o titulo
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Exemplar where numeroExemplar = '" + inputNumExemplar.getText() + "'");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                System.out.println(resultadoDoSelect.getInt("idExemplar")); //ELE TA PEGANDO AQUI
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"idExemplar" , "idBiblioteca" , "codLivro" , "numeroExemplar"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "idExemplar";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "idBiblioteca";
                            resultadoSQL[0][2] = "CÓdigo Livro";
                            resultadoSQL[0][3] = "Número Exemplar";

                            for (int i = 1 ; i < linhas ; i++){
                                resultadoDoSelect.next();
                                //resultadoSQL[i][0] = resultadoDoSelect.getInt("codLivro");
                                //System.out.println(Integer.toString(resultadoDoSelect.getInt("codLivro")));
                                resultadoSQL[i][0] = resultadoDoSelect.getString("idExemplar");
                                resultadoSQL[i][1] = resultadoDoSelect.getString("idBiblioteca");
                                resultadoSQL[i][2] = resultadoDoSelect.getString("codLivro");
                                resultadoSQL[i][3] = resultadoDoSelect.getString("numeroExemplar");
                            }

                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            if (tabelaResultadoSql != null){
                                System.out.println("A tabela tem coisa");
                            }
                            else{
                                System.out.println("tabela nao está guardando dados");
                            }

                            JScrollPane scrollPane = new JScrollPane(tabelaResultadoSql);
                            container.add(scrollPane, BorderLayout.CENTER);

                            container.setVisible(true);
                            //Login.janela.pack();

                            if (resultadoDoSelect != null){
                                System.out.println("Deu certo a busca");
                            }
                            else{
                                System.out.println("Deu errado!");
                            }
                        }
                        else{
                            System.out.println("o resultado está dando nulo");
                        }
                    }
                    else{
                        System.out.println("Dados inválidos!");
                    }
                }
        }
    }
}