package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/*
4.	Empréstimos de exemplares de livros da biblioteca informada no Login –
relacionar leitor com exemplar de livro,
informando as datas de realização do empréstimo (pegar no checkin do login) e a data prevista para devolução.

Mostrar os livros em atraso numa guia de um TabControl usando a view do item 6.

Usar a trigger do item 7 para capturar exceções e impedir o empréstimo de ser feito, avisando o usuário.
No momento do empréstimo, o campo devolucaoEfetiva ficará nulo, pois o livro ainda não foi devolvido.
*/

public class Emprestimos {
    public static JButton realizar, voltar;
    public static JPanel container, painelCampos;
    public static JTextField inputIdLeitor, inputNumExemplar, inputDevolucaoPrevista;
    public static JTable tabelaResultadoSql;

    // Inicializar o valor de idBibliotecaEscolhida
    public static int idBibliotecaEscolhida;
    static {
        // Chamando o método no bloco estático da classe login
        Login.setIdBibliotecaEscolhida();
        idBibliotecaEscolhida = Login.idBibliotecaEscolhida; //pega o valor definido dps de definir em login
    }

    // Inicializar o valor de dataCheckIn
    public static java.util.Date dataCheckIn;
    static {
        dataCheckIn = Login.dataSelecionada;
    }


    //no caso so o realizar
    public static void montarBotoesPrincipais(){
        JPanel painelBotoes;

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

        // Criar um painel interno para os campos de texto e labels
        painelBotoes = new JPanel();
        //painelBotoes.add(voltar , BorderLayout.NORTH);
        painelBotoes.add(realizar , BorderLayout.SOUTH);

        // painel principal para organizar campos e botão
        container = new JPanel();
        container.setLayout(new GridLayout(7 , 4 , 5 , 5));
        container.add(painelBotoes, BorderLayout.SOUTH);        // botao em baixo
        container.add(Box.createVerticalStrut(15));     //espaçamento vertical fixo de 15 pixels no layout
    }


    public static void mostrarInserirEView() {
        if (painelCampos == null) {
            painelCampos = new JPanel();
        }

        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudança
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração

        inputIdLeitor = new JTextField(10);
        inputNumExemplar = new JTextField(10);

        JLabel idLeitor = new JLabel("Digite o ID do leitor: ");
        JLabel numExemplar = new JLabel("Digite o número do exemplar: ");

        painelCampos.removeAll();
        painelCampos.setLayout(new GridLayout(2, 2, 5, 5)); // Layout de formulário completo

        painelCampos.add(idLeitor);
        painelCampos.add(inputIdLeitor);
        painelCampos.add(numExemplar);
        painelCampos.add(inputNumExemplar);

        container.add(painelCampos, BorderLayout.CENTER);

        //MENTIRA Q EU QUERO UM CALENDARIO P DEVOLUCAO PREVISTA TB
        //inputDevolucaoPrevista = new JTextField(10);

        // Atualizar o layout após alterações
        container.revalidate();
        container.repaint();
    }


    public static JPanel realizarTudo() throws Exception {
        montarBotoesPrincipais();
        return container;
    }


    public static void consultas() throws SQLException {
        Statement comandoSql;
       
        //PASSAR COMO PARAMETRO O INSERT INTO COM OS DADOS
        String sql = "INSERT INTO SisBib.Exemplar(idBiblioteca, codLivro, numeroExemplar) values (? , ? , ?)";
        try {
            PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
            preparedStatement.setInt(1, idBibliotecaEscolhida);     //primeiro '?'
            preparedStatement.setString(2, inputCodLivro.getText());                   //segundo '?'
            preparedStatement.setInt(3, Integer.parseInt(inputNumExemplar.getText()));                //terceiro '?'

            System.out.println(sql);
            int linhasAfetadas = preparedStatement.executeUpdate();
            System.out.println("Linhas afetadas: " + linhasAfetadas);
            JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
