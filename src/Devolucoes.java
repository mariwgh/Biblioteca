package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Devolucoes {
    public static JButton realizar;
    public static JPanel container, painelCampos;
    public static JTextField inputIdLeitor, inputCodLivro, inputNumExemplar;

    public static int idBibliotecaEscolhida;
    static {
        Login.setIdBibliotecaEscolhida();
        idBibliotecaEscolhida = Login.idBibliotecaEscolhida;
    }

    public static String dataDevolucao;
    static {
        dataDevolucao = Login.dataFormatada;
    }


    public static void montar() throws SQLException {
        if (painelCampos == null) {
            painelCampos = new JPanel();
        }
        painelCampos.setLayout(new GridLayout(3, 2, 5, 5)); // Layout de formulário completo

        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudança
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração

        inputIdLeitor = new JTextField(10);
        inputCodLivro = new JTextField(10);
        inputNumExemplar = new JTextField(10);
        JLabel idLeitor = new JLabel("Digite o ID do leitor: ");
        JLabel idLivro = new JLabel("Digite o código do livro: ");
        JLabel numExemplar = new JLabel("Digite o número do exemplar: ");

        painelCampos.add(idLeitor);
        painelCampos.add(inputIdLeitor);
        painelCampos.add(idLivro);
        painelCampos.add(inputCodLivro);
        painelCampos.add(numExemplar);
        painelCampos.add(inputNumExemplar);

        // painel principal para organizar campos e botão
        container = new JPanel();
        container.setLayout(new GridLayout(4 , 2 , 5 , 5));

        container.add(painelCampos, BorderLayout.CENTER);

        // atualiza o layout após alterações
        container.revalidate();
        container.repaint();

        //realiza a opcao escolhida
        realizar = new JButton("DEVOLVER");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //realizar a função escolhida no cbx, pegar os dados dos campos do formulario e fazer a consulta
                try {
                    updateEmprestimo();
                    verificarAtrasoESuspender();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // cria um painel interno para os campos de texto e labels
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(Login.voltar , BorderLayout.NORTH);
        painelBotoes.add(realizar , BorderLayout.SOUTH);

        container.add(painelBotoes, BorderLayout.SOUTH);        // botao em baixo
        container.add(Box.createVerticalStrut(15));     //espaçamento vertical fixo de 15 pixels no layout
    }

    public static JPanel realizarTudo() throws Exception {
        montar();
        container.setPreferredSize(new Dimension(891 ,478 ));
        return container;
    }

    public static void updateEmprestimo() throws SQLException {
        String sql = "update SisBib.Emprestimo set devolucaoEfetiva = ? where idLeitor = ? and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = ? and numeroExemplar = ? and idBiblioteca = ?) and devolucaoEfetiva is null";
        try {
            PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
            preparedStatement.setString(1, dataDevolucao);
            preparedStatement.setInt(2, Integer.parseInt(inputIdLeitor.getText()));
            preparedStatement.setString(3, String.valueOf(inputCodLivro.getText()));
            preparedStatement.setInt(4, Integer.parseInt(inputNumExemplar.getText()));
            preparedStatement.setInt(5, idBibliotecaEscolhida);

            int linhasAfetadas = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void verificarAtrasoESuspender() {
        String stringSql = "select idLeitor from SisBib.Emprestimo where devolucaoPrevista < devolucaoEfetiva and idLeitor = " + Integer.parseInt(inputIdLeitor.getText()) + " and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = '" + inputCodLivro.getText() + "' and numeroExemplar = " + Integer.parseInt(inputNumExemplar.getText()) + " and idBiblioteca = " + idBibliotecaEscolhida + ")";

        Statement comandoSql;

        try {
            comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
            ResultSet resultadoDoSelect = comandoSql.executeQuery(stringSql);

            //se houver resultados/exemplares
            if (resultadoDoSelect.next()) {
                String sql = "exec SisBib.suspenderLeitor ?";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(inputIdLeitor.getText()));

                    int linhasAfetadas = preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Leitor suspenso!\nLinhas afetadas: " + linhasAfetadas);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
            //nao houve atraso
            else {
                System.out.println("Não houve atraso.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
