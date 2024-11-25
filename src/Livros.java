package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Livros {

    public static JComboBox operacao , cbxAntigo , cbxNovo;
    public static JButton realizar , voltar , selecionar;
    public static int idBibliotecaEscolhida;
    public static JPanel container , painelCampos;
    public static JTextField inpId, inpTitulo , inpAutor , inpArea , dadoAntigo , dadoNovo;
    public static JTable tabelaResultadoSql;
    //public static JLabel mensagem;
    public static String opcao;

   /* public static void main(String[] args){
        montar();
    }*/
    public static void montar(){
        JPanel panelCampos;

        String[] opcoes = new String[]{"INCLUIR" , "DELETAR" , "ALTERAR" , "BUSCAR"};

        operacao = new JComboBox(opcoes);
        operacao.setPreferredSize(new Dimension(100 , 25));


        realizar = new JButton("REALIZAR");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultas();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null , ex.getMessage());
                }
            }
        });

        selecionar = new JButton("SELECIONAR");
        selecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (tabelaResultadoSql != null){
                        tabelaResultadoSql.removeAll();
                        container.remove(tabelaResultadoSql);
                        container.repaint();
                        container.revalidate();
                    }
                    mostrarInputs();
                }
                catch (Exception erro){
                    System.out.println(erro.getMessage());
                }

            }
        });


        // Criar um painel interno para os campos de texto e labels
        panelCampos = new JPanel();
        panelCampos.add(realizar , BorderLayout.NORTH);
        panelCampos.add(selecionar , BorderLayout.NORTH);
        panelCampos.add(operacao , BorderLayout.NORTH);
        panelCampos.add(Login.voltar , BorderLayout.NORTH);

        // painel principal para organizar campos e botão

        container = new JPanel();
        container.setLayout(new GridLayout(5 , 4 , 5 , 5));
        container.add(panelCampos, BorderLayout.NORTH); // Campos no centro
        //container.setPreferredSize(new Dimension(699 , 459));
        //container.add(Box.createVerticalStrut(15));
    }


    public static void mostrarInputs() {
        opcao = new String();

        if (painelCampos == null) {
            painelCampos = new JPanel();
        }


        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudanã
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração
        //container.remove(tabelaResultadoSql);


        opcao = operacao.getSelectedItem().toString();


        if (opcao.equals("BUSCAR")) {
            inpId = new JTextField(10);
            inpTitulo = new JTextField(10);
            JLabel id = new JLabel("Digite o id do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(2, 2, 5, 5)); // Layout de formulário simples
            painelCampos.add(id);
            painelCampos.add(inpId);
            painelCampos.add(titulo);
            painelCampos.add(inpTitulo);

            container.add(painelCampos, BorderLayout.CENTER);
        }
        else if (opcao.equals("INCLUIR")) {
            inpId = new JTextField(10);
            inpTitulo = new JTextField(10);
            inpAutor = new JTextField(10);
            inpArea = new JTextField(10);
            JLabel id = new JLabel("Digite o id do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");
            JLabel idAutor = new JLabel("Digite o id do autor: ");
            JLabel idArea = new JLabel("Digite o id da área que o livro pertence: ");

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(4, 2, 5, 5)); // Layout de formulário completo
            painelCampos.add(id);
            painelCampos.add(inpId);
            painelCampos.add(titulo);
            painelCampos.add(inpTitulo);
            painelCampos.add(idAutor);
            painelCampos.add(inpAutor);
            painelCampos.add(idArea);
            painelCampos.add(inpArea);

            container.add(painelCampos, BorderLayout.CENTER);
        }
        else if (opcao.equals("DELETAR")) {
            JLabel LID = new JLabel("Digite o código do livro: ");
            JLabel LTitulo = new JLabel("Digite o título do livro: ");
            JLabel ou = new JLabel("OU");
            inpTitulo = new JTextField(10);
            inpId = new JTextField(10);

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(3, 2, 5, 5));
            painelCampos.add(LTitulo);
            painelCampos.add(inpTitulo);
            painelCampos.add(ou);
            painelCampos.add(new JPanel());
            painelCampos.add(LID);
            painelCampos.add(inpId);

            container.add(painelCampos, BorderLayout.CENTER);
        }
        else if (opcao.equals("ALTERAR")) {
            String[] dados = new String[] { "Código livro", "Título", "Id autor", "Id área" };
            JLabel whereAntigo = new JLabel("Qual é o dado de referência?");
            cbxAntigo = new JComboBox<>(dados);
            JLabel LDadoAntigo = new JLabel("Digite o dado de referência: ");
            dadoAntigo = new JTextField(10);
            JLabel qualAlterar = new JLabel("Qual dado quer alterar?");
            cbxNovo = new JComboBox<>(dados);
            JLabel LNovo = new JLabel("Qual é o novo dado?");
            dadoNovo = new JTextField(10);

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(6, 2, 5, 5));
            painelCampos.add(whereAntigo);
            painelCampos.add(cbxAntigo);
            painelCampos.add(LDadoAntigo);
            painelCampos.add(dadoAntigo);
            painelCampos.add(qualAlterar);
            painelCampos.add(cbxNovo);
            painelCampos.add(LNovo);
            painelCampos.add(dadoNovo);

            container.add(painelCampos, BorderLayout.CENTER);
        }

        // Atualizar o layout após alterações
        container.revalidate();
        container.repaint();
    }


    public static JPanel realizarTudo() throws Exception{
        montar();
        container.setPreferredSize(new Dimension(891 ,478 ));
        //container.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alinha os componentes à esquerda de maneira compacta
        //container.setSize(300,200); // Limita o tamanho do painel
        return container;
    }

    public static void consultas() throws SQLException {
        Statement comandoSql;
        switch (opcao){
            case "INCLUIR":

                //PASSAR COMO PARAMETRO O INSERT INTO COM OS DADOS
                String sql = "INSERT INTO SisBib.Livro(codLivro , titulo , idAutor , idArea) values (? , ? , ? , ?)";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setString(1 , inpId.getText());  //muda o primeiro '?'
                    preparedStatement.setString(2 , inpTitulo.getText());   //  muda o segundi '?'
                    preparedStatement.setInt(3 , Integer.parseInt(inpAutor.getText()));
                    preparedStatement.setInt(4 , Integer.parseInt(inpArea.getText()));
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                    JOptionPane.showMessageDialog(null , "Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());;
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "DELETAR":

                if (inpId.getText().equals("")){
                    sql = "delete from SisBib.Livro where titulo= ?";

                    try {
                        PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                        preparedStatement.setString(1 , inpTitulo.getText());
                        int linhasAfetadas = preparedStatement.executeUpdate();
                        //System.out.println("Linhas afetadas: " + linhasAfetadas);
                        JOptionPane.showMessageDialog(null ,"Linhas afetadas: " + linhasAfetadas );
                    }
                    catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null , ex.getMessage());
                    }
                }
                else if (inpTitulo.getText().equals("")) {
                    sql = "delete from SisBib.Livro where codLivro= ?";

                    try {
                        PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                        preparedStatement.setString(1 , inpId.getText());
                        int linhasAfetadas = preparedStatement.executeUpdate();
                        //System.out.println("Linhas afetadas: " + linhasAfetadas);
                        JOptionPane.showMessageDialog(null ,"Linhas afetadas: " + linhasAfetadas );
                    }
                    catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null , ex.getMessage());
                    }

                }

                else{
                    System.out.println("Dados inválidos");
                }
                break;

            case "ALTERAR":
                sql = "update SisBib.Exemplar set " + cbxNovo.getSelectedItem() + " = ? where " + cbxAntigo.getSelectedItem() + " = ? and idBiblioteca = " + idBibliotecaEscolhida;
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);

                    preparedStatement.setInt(1, Integer.parseInt(dadoNovo.getText()));      //dado novo
                    preparedStatement.setInt(2, Integer.parseInt(dadoAntigo.getText()));      //dado antigo

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                    JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "BUSCAR":
                String id = inpId.getText();
                String titulo = inpTitulo.getText();

                /*if (tabelaResultadoSql != null) {
                    container.remove(tabelaResultadoSql);
                    container.repaint();
                    container.revalidate();
                }*/

                if (titulo.equals("") && id.equals("")){
                    try{
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                //System.out.println(resultadoDoSelect.getInt("idArea"));
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "Código";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "Título";
                            resultadoSQL[0][2] = "id Autor";
                            resultadoSQL[0][3] = "id Área";

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
                            tabelaResultadoSql.setVisible(true);

                            container.add(tabelaResultadoSql);
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
                        System.out.println(erro.getMessage());;
                    }

                }
                else {
                    if (titulo.equals("")){ //se o usuario tiver digitado somente o id
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro where codLivro = '" + inpId.getText() + "'");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                System.out.println(resultadoDoSelect.getInt("idArea")); //ELE TA PEGANDO AQUI
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "Código";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "Título";
                            resultadoSQL[0][2] = "id Autor";
                            resultadoSQL[0][3] = "id Área";

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
                            container.add(tabelaResultadoSql);
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
                    else if (id.equals("")) {   //usuario digitou somente o titulo
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro where titulo = '" + inpTitulo.getText() + "'");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                System.out.println(resultadoDoSelect.getInt("idArea")); //ELE TA PEGANDO AQUI
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "Código";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "Título";
                            resultadoSQL[0][2] = "id Autor";
                            resultadoSQL[0][3] = "id Área";

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
                            container.add(tabelaResultadoSql);
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
