package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Devolucoes {
    public static JButton realizar, voltar;
    public static JPanel container, painelCampos;
    public static JTextField inputIdLeitor, inputCodLivro, inputNumExemplar;

    // Inicializar o valor de idBibliotecaEscolhida
    public static int idBibliotecaEscolhida;
    static {
        // Chamando o método no bloco estático da classe login
        Login.setIdBibliotecaEscolhida();
        idBibliotecaEscolhida = Login.idBibliotecaEscolhida; //pega o valor definido dps de definir em login
    }

    // Inicializar o valor de dataCheckIn
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

        // Atualizar o layout após alterações
        container.revalidate();
        container.repaint();

        //realiza a opcao escolhida
        realizar = new JButton("DEVOLVER");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //realizar a função escolhida no cbx, pegar os dados
                //dos campos do formulario e fazer a consulta
                try {
                    updateEmprestimo();
                    verificarAtrasoESuspender();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Criar um painel interno para os campos de texto e labels
        JPanel painelBotoes = new JPanel();
        //painelBotoes.add(voltar , BorderLayout.NORTH);
        painelBotoes.add(realizar , BorderLayout.SOUTH);

        container.add(painelBotoes, BorderLayout.SOUTH);        // botao em baixo
        container.add(Box.createVerticalStrut(15));     //espaçamento vertical fixo de 15 pixels no layout
    }


    public static JPanel realizarTudo() throws Exception {
        montar();
        return container;
    }


    public static void updateEmprestimo() throws SQLException {
        String sql = "update SisBib.Emprestimo set devolucaoEfetiva = ? where idLeitor = ? and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = ? and numeroExemplar = ? and idBiblioteca = ?)";
        try {
            PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
            preparedStatement.setString(1, dataDevolucao);
            preparedStatement.setInt(2, Integer.parseInt(inputIdLeitor.getText()));
            preparedStatement.setString(3, String.valueOf(inputCodLivro.getText()));
            preparedStatement.setInt(4, Integer.parseInt(inputNumExemplar.getText()));
            preparedStatement.setInt(5, idBibliotecaEscolhida);

            System.out.println(sql);
            int linhasAfetadas = preparedStatement.executeUpdate();
            System.out.println("Linhas afetadas: " + linhasAfetadas);
            JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void verificarAtrasoESuspender() {
        //exec SisBib.suspenderLeitor idLeitor

        String stringSql = "select idLeitor from SisBib.Emprestimo where devolucaoPrevista < devolucaoEfetiva and idLeitor = " + Integer.parseInt(inputIdLeitor.getText()) + " and idExemplar = (select idExemplar from SisBib.Exemplar where codLivro = '" + inputCodLivro.getText() + "' and numeroExemplar = " + Integer.parseInt(inputNumExemplar.getText()) + " and idBiblioteca = " + idBibliotecaEscolhida + ")";
        System.out.println(stringSql);

        Statement comandoSql;

        try {
            comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
            ResultSet resultadoDoSelect = comandoSql.executeQuery(stringSql);

            //se houver resultados/exemplares
            if (resultadoDoSelect != null) {
                String sql = "exec SisBib.suspenderLeitor ?";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(inputIdLeitor.getText()));

                    System.out.println(sql);
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
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