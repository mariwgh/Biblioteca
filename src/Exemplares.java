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
    public static JButton realizar, selecionar;
    public static JPanel container, painelCampos;
    public static JTextField inputCodLivro, inputNumExemplar, inputIdExemplar;
    public static JTable tabelaResultadoSql;
    public static String opcao;
    public static JComboBox<String> colunaRef, colunaAlterar;
    public static JTextField dadoRef, dadoNovo;

    // inicializa o valor de idBibliotecaEscolhida
    public static int idBibliotecaEscolhida;
    static {
        // chama o método no bloco estático da classe login
        idBibliotecaEscolhida = Login.setIdBibliotecaEscolhida(); //pega o valor definido dps de definir em login
    }


    public static void montarBotoesPrincipais(){
        JPanel painelBotoes;

        String[] opcoes = new String[]{"INCLUIR" , "DELETAR" , "ALTERAR" , "BUSCAR"};

        operacao = new JComboBox(opcoes);
        operacao.setPreferredSize(new Dimension(100 , 25));

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
                    mostrarEscolhaSelecionada();
                }
                catch (Exception erro){
                    System.out.println(erro.getMessage());
                }
            }
        });

        //realiza a opcao escolhida
        realizar = new JButton("REALIZAR");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        painelBotoes = new JPanel();
        painelBotoes.add(Login.voltar , BorderLayout.NORTH);
        painelBotoes.add(operacao , BorderLayout.NORTH);
        painelBotoes.add(selecionar , BorderLayout.NORTH);
        painelBotoes.add(realizar , BorderLayout.NORTH);

        container = new JPanel();
        container.setLayout(new GridLayout(7 , 4 , 5 , 5));
        container.add(painelBotoes, BorderLayout.NORTH);        // botoes em cima
        container.add(Box.createVerticalStrut(15));     //espaçamento vertical fixo de 15 pixels no layout
    }

    public static void mostrarEscolhaSelecionada() {
        opcao = new String();

        if (painelCampos == null) {
            painelCampos = new JPanel();
        }

        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudança
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração

        opcao = operacao.getSelectedItem().toString();

        switch (opcao) {
            case "INCLUIR":
                inputCodLivro = new JTextField(10);
                inputNumExemplar = new JTextField(10);

                JLabel codLivro = new JLabel("Digite o código do livro: ");
                JLabel numExemplar = new JLabel("Digite o número do exemplar: ");

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(2, 2, 5, 5)); // Layout de formulário completo

                painelCampos.add(codLivro);
                painelCampos.add(inputCodLivro);
                painelCampos.add(numExemplar);
                painelCampos.add(inputNumExemplar);

                container.add(painelCampos, BorderLayout.CENTER);
                break;

            case "DELETAR":
                JLabel idExemplar = new JLabel("Digite o ID do exemplar: ");
                inputIdExemplar = new JTextField(10);

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(1, 2, 5, 5));

                painelCampos.add(idExemplar);
                painelCampos.add(inputIdExemplar);

                container.add(painelCampos, BorderLayout.CENTER);
                break;

            case "ALTERAR":
                String[] dados = new String[]{"idExemplar", "idBiblioteca", "codLivro", "numeroExemplar"};
                String[] dadosAlterar = new String[]{"idBiblioteca", "codLivro", "numeroExemplar"};

                JLabel whereColuna = new JLabel("Qual é o dado de referência?");
                colunaRef = new JComboBox<>(dados);

                JLabel whereDado = new JLabel("Digite o dado de referência: ");
                dadoRef = new JTextField(10);

                JLabel setColuna = new JLabel("Qual dado quer alterar?");
                colunaAlterar = new JComboBox<>(dadosAlterar);

                JLabel setDado = new JLabel("Digite o novo dado: ");
                dadoNovo = new JTextField(10);

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(6, 2, 5, 5));

                painelCampos.add(whereColuna);
                painelCampos.add(colunaRef);
                painelCampos.add(whereDado);
                painelCampos.add(dadoRef);
                painelCampos.add(setColuna);
                painelCampos.add(colunaAlterar);
                painelCampos.add(setDado);
                painelCampos.add(dadoNovo);

                container.add(painelCampos, BorderLayout.CENTER);
                break;

            case "BUSCAR":
                inputIdExemplar = new JTextField(10);
                inputCodLivro = new JTextField(10);
                inputNumExemplar = new JTextField(10);

                idExemplar = new JLabel("Digite o id do exemplar: ");
                codLivro = new JLabel("Digite o código do livro: ");
                numExemplar = new JLabel("Digite o número do exemplar: ");

                painelCampos.removeAll();
                painelCampos.setLayout(new GridLayout(5, 2, 5, 5)); // Layout de formulário simples

                painelCampos.add(idExemplar);
                painelCampos.add(inputIdExemplar);
                painelCampos.add(new JLabel("OU/E"));
                painelCampos.add(new JLabel(""));
                painelCampos.add(codLivro);
                painelCampos.add(inputCodLivro);
                painelCampos.add(new JLabel("OU/E"));
                painelCampos.add(new JLabel(""));
                painelCampos.add(numExemplar);
                painelCampos.add(inputNumExemplar);

                container.add(painelCampos, BorderLayout.CENTER);
                break;
        }

        // atualiza o layout após alterações
        container.revalidate();
        container.repaint();
    }

    public static JPanel realizarTudo() throws Exception {
        montarBotoesPrincipais();
        container.setPreferredSize(new Dimension(891 ,478 ));
        return container;
    }

    public static void consultas() throws SQLException {
        Statement comandoSql;

        switch (opcao) {
            case "INCLUIR":
                String sql = "INSERT INTO SisBib.Exemplar(idBiblioteca, codLivro, numeroExemplar) values (? , ? , ?)";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1, idBibliotecaEscolhida);                             //primeiro '?'
                    preparedStatement.setString(2, inputCodLivro.getText());                        //segundo '?'
                    preparedStatement.setInt(3, Integer.parseInt(inputNumExemplar.getText()));      //terceiro '?'

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "DELETAR":
                sql = "delete SisBib.Exemplar where idExemplar = ? and idBiblioteca = " + idBibliotecaEscolhida;
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(inputIdExemplar.getText()));

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "ALTERAR":
                sql = "update SisBib.Exemplar set " + colunaAlterar.getSelectedItem() + " = ? where " + colunaRef.getSelectedItem() + " = ? and idBiblioteca = " + idBibliotecaEscolhida;
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);

                    if (colunaAlterar.getSelectedItem() == "codLivro") {
                        preparedStatement.setString(1, dadoNovo.getText());
                    } else {
                        preparedStatement.setInt(1, Integer.parseInt(dadoNovo.getText()));      //dado novo
                    }

                    if (colunaRef.getSelectedItem() == "codLivro") {
                        preparedStatement.setString(2, dadoRef.getText());
                    } else {
                        preparedStatement.setInt(2, Integer.parseInt(dadoRef.getText()));      //dado novo
                    }

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                break;

            case "BUSCAR":
                String idExemplar = inputIdExemplar.getText();
                String codLivro = inputCodLivro.getText();
                String numExemplar = inputNumExemplar.getText();

                String stringSql = "select * from SisBib.Exemplar where idBiblioteca = " + idBibliotecaEscolhida;

                //se nenhum campo for fornecido, a stringSql continuara a mesma e n entrara nos id
                //se forneceu todos os campos, entrará em todos os if

                //se ele forneceu idExemplar
                if (!idExemplar.equals("")) {
                    stringSql += " and idExemplar = " + Integer.parseInt(idExemplar);
                }

                //se ele forneceu codExemplar
                if (!codLivro.equals("")) {
                    stringSql += " and codLivro = '" + codLivro + "'";
                }

                //se ele forneceu numExemplar
                if (!numExemplar.equals("")) {
                    stringSql += " and numeroExemplar = " + Integer.parseInt(numExemplar);
                }

                try {
                    int linhas = 0;
                    comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
                    ResultSet resultadoDoSelect = comandoSql.executeQuery(stringSql);

                    //se houver resultados/exemplares
                    if (resultadoDoSelect != null) {
                        while (resultadoDoSelect.next()) {      //conta quantas linhas tem o resultado
                            linhas += 1;
                        }

                        linhas += 1;    //para colocar as colunas também

                        String[] colunas = new String[]{"idExemplar", "idBiblioteca", "codLivro", "numeroExemplar"};
                        String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                        resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                        //definir o "nome das colunas"
                        resultadoSQL[0][0] = colunas[0];
                        resultadoSQL[0][1] = colunas[1];
                        resultadoSQL[0][2] = colunas[2];
                        resultadoSQL[0][3] = colunas[3];

                        for (int i = 1; i < linhas; i++) {
                            resultadoDoSelect.next();

                            resultadoSQL[i][0] = resultadoDoSelect.getString("idExemplar");
                            resultadoSQL[i][1] = resultadoDoSelect.getString("idBiblioteca");
                            resultadoSQL[i][2] = resultadoDoSelect.getString("codLivro");
                            resultadoSQL[i][3] = resultadoDoSelect.getString("numeroExemplar");
                        }

                        if (resultadoDoSelect != null){
                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            container.add(tabelaResultadoSql);
                            container.setVisible(true);
                        }
                        else{
                            System.out.println("está dando nulo");
                            JOptionPane.showMessageDialog(null , "o resultado do selct deu nulo");
                        }
                    }
                    else {
                        System.out.println("o resultado está dando nulo");
                    }
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
        }
    }
}
