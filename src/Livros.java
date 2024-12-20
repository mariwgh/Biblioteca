package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Livros {
    public static JComboBox operacao , cbxAntigo , cbxNovo;
    public static JButton realizar , selecionar;
    public static JPanel container , painelCampos;
    public static JTextField inpCodLivro, inpTitulo , inpAutor , inpArea , dadoAntigo , dadoNovo;
    public static JTable tabelaResultadoSql;
    public static String opcao;


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

        // cria um painel interno para os campos de texto
        panelCampos = new JPanel();
        panelCampos.add(Login.voltar , BorderLayout.NORTH);
        panelCampos.add(operacao , BorderLayout.NORTH);
        panelCampos.add(selecionar , BorderLayout.NORTH);
        panelCampos.add(realizar , BorderLayout.NORTH);

        container = new JPanel();
        container.setLayout(new GridLayout(5 , 4 , 5 , 5));
        container.add(panelCampos, BorderLayout.NORTH);
    }

    public static void mostrarInputs() {
        opcao = new String();

        if (painelCampos == null) {
            painelCampos = new JPanel();
        }

        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudanã
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração

        opcao = operacao.getSelectedItem().toString();

        if (opcao.equals("INCLUIR")) {
            inpCodLivro = new JTextField(10);
            inpTitulo = new JTextField(10);
            inpAutor = new JTextField(10);
            inpArea = new JTextField(10);
            JLabel codLivro = new JLabel("Digite o código do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");
            JLabel idAutor = new JLabel("Digite o id do autor: ");
            JLabel idArea = new JLabel("Digite o id da área que o livro pertence: ");

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(4, 2, 5, 5));
            painelCampos.add(codLivro);
            painelCampos.add(inpCodLivro);
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
            JLabel ou = new JLabel("OU/E");
            inpTitulo = new JTextField(10);
            inpCodLivro = new JTextField(10);

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(3, 2, 5, 5));
            painelCampos.add(LTitulo);
            painelCampos.add(inpTitulo);
            painelCampos.add(ou);
            painelCampos.add(new JPanel());
            painelCampos.add(LID);
            painelCampos.add(inpCodLivro);

            container.add(painelCampos, BorderLayout.CENTER);
        }

        else if (opcao.equals("ALTERAR")) {
            String[] dados = new String[] {"codLivro", "titulo", "idAutor", "idArea" };
            String[] dadosAlterar = new String[] {"titulo", "idAutor", "idArea" };

            JLabel whereAntigo = new JLabel("Qual é o dado de referência?");
            cbxAntigo = new JComboBox<>(dados);
            JLabel LDadoAntigo = new JLabel("Digite o dado de referência: ");
            dadoAntigo = new JTextField(10);
            JLabel qualAlterar = new JLabel("Qual dado quer alterar?");
            cbxNovo = new JComboBox<>(dadosAlterar);
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

        else if (opcao.equals("BUSCAR")) {
            inpCodLivro = new JTextField(10);
            inpTitulo = new JTextField(10);
            JLabel id = new JLabel("Digite o código do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");

            painelCampos.removeAll();
            painelCampos.setLayout(new GridLayout(3, 2, 5, 5));
            painelCampos.add(id);
            painelCampos.add(inpCodLivro);
            painelCampos.add(new JLabel("OU/E"));
            painelCampos.add(new JPanel());
            painelCampos.add(titulo);
            painelCampos.add(inpTitulo);

            container.add(painelCampos, BorderLayout.CENTER);
        }

        // atualiza o layout dps alterações
        container.revalidate();
        container.repaint();
    }

    public static JPanel realizarTudo() throws Exception{
        montar();
        container.setPreferredSize(new Dimension(891 ,478 ));
        return container;
    }

    public static void consultas() throws SQLException {
        Statement comandoSql;

        switch (opcao) {
            case "INCLUIR":
                String sql = "INSERT INTO SisBib.Livro(codLivro , titulo , idAutor , idArea) values (? , ? , ? , ?)";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setString(1, inpCodLivro.getText());
                    preparedStatement.setString(2, inpTitulo.getText());
                    preparedStatement.setInt(3, Integer.parseInt(inpAutor.getText()));
                    preparedStatement.setInt(4, Integer.parseInt(inpArea.getText()));

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "DELETAR":
                if (inpCodLivro.getText().equals("")) {
                    sql = "delete from SisBib.Livro where titulo= ?";
                    try {
                        PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                        preparedStatement.setString(1, inpTitulo.getText());

                        int linhasAfetadas = preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                } else if (inpTitulo.getText().equals("")) {
                    sql = "delete from SisBib.Livro where codLivro= ?";
                    try {
                        PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                        preparedStatement.setString(1, inpCodLivro.getText());

                        int linhasAfetadas = preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                } else {
                    System.out.println("Dados inválidos");
                }
                break;

            case "ALTERAR":
                sql = "update SisBib.Livro set " + cbxNovo.getSelectedItem().toString() + " = ? where " + cbxAntigo.getSelectedItem().toString() + " = ? ";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);

                    if (cbxNovo.getSelectedItem() == "titulo" || cbxNovo.getSelectedItem() == "codLivro" ) {
                        preparedStatement.setString(1, dadoNovo.getText());
                    }
                    else if (cbxNovo.getSelectedItem() == "idAutor" || cbxNovo.getSelectedItem() == "idArea" ) {
                        preparedStatement.setInt(1, Integer.parseInt(dadoNovo.getText()));      //dado novo
                    }

                    if (cbxAntigo.getSelectedItem() == "titulo" || cbxAntigo.getSelectedItem() == "codLivro") {
                        preparedStatement.setString(2, dadoAntigo.getText());
                    }
                    else if (cbxAntigo.getSelectedItem() == "idAutor" || cbxAntigo.getSelectedItem() == "idArea" )  {
                        preparedStatement.setInt(2, Integer.parseInt(dadoAntigo.getText()));      //dado novo
                    }

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "BUSCAR":
                String codLivro = inpCodLivro.getText();
                String titulo = inpTitulo.getText();

                String stringSql = "select * from SisBib.Livro where idArea in (select idArea from SisBib.Livro) ";

                //se nenhum campo for fornecido, a stringSql continuara a mesma e n entrara nos id
                //se forneceu todos os campos, entrará em todos os if

                if (!codLivro.equals("")) {
                    stringSql += " and codLivro = '" + codLivro + "'";
                }

                if (!titulo.equals("")) {
                    stringSql += " and titulo = '" + titulo + "'";
                }

                try {
                    int linhas = 0;
                    comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
                    ResultSet resultadoDoSelect = comandoSql.executeQuery(stringSql);        //tem que fazer um where do idBiblioteca

                    //se houver resultados/exemplares
                    if (resultadoDoSelect != null) {
                        while (resultadoDoSelect.next()) {      //conta quantas linhas tem o resultado
                            linhas += 1;
                        }

                        linhas += 1;    //para colocar as colunas também

                        String[] colunas = new String[]{"codLivro", "titulo", "idAutor", "idArea"};
                        String[][] resultadoSQL = new String[linhas][4];    //com x linhas e cada linha tem 4 campos (colunas)

                        resultadoDoSelect.beforeFirst();                    //volta para o inicio para guardar os dados no resultadoSQL

                        //definir o "nome das colunas"
                        resultadoSQL[0][0] = colunas[0];
                        resultadoSQL[0][1] = colunas[1];
                        resultadoSQL[0][2] = colunas[2];
                        resultadoSQL[0][3] = colunas[3];

                        for (int i = 1; i < linhas; i++) {
                            resultadoDoSelect.next();

                            resultadoSQL[i][0] = resultadoDoSelect.getString("codLivro");
                            resultadoSQL[i][1] = resultadoDoSelect.getString("titulo");
                            resultadoSQL[i][2] = resultadoDoSelect.getString("idAutor");
                            resultadoSQL[i][3] = resultadoDoSelect.getString("idArea");
                        }

                        if (resultadoDoSelect != null) {
                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            container.add(tabelaResultadoSql);
                            container.setVisible(true);
                        } else {
                            System.out.println("está dando nulo");
                            JOptionPane.showMessageDialog(null, "o resultado do select deu nulo");
                        }

                    } else {
                        System.out.println("o resultado está dando nulo");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
        }
    }
}
