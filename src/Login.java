package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
/*
import java.sql.Statement;		// permite criar um objeto de execução de comandos no servidor
import java.sql.PreparedStatement;  	// para usar parÂmetros em comandos SQL e evitar SQL Injection
import java.sql.CallableStatement;  	// para chamar stored procedures
import java.sql.Date;
*/

// tela de login ao bd
public class Login {
    public static JFrame janela;
    public static JPanel container_area;
    public static JTabbedPane abas;
    public static JTextField servidor, banco_de_dados , usuario , senha;
    public static JButton conectar;
    public static JLabel LServidor , LBD , LUsuario , LSenha;
    public static JComboBox cbx;
    public static Connection conexao;
    public static JPanel panelCampos;
    public static ResultSet resultadoSelect;

    public static void main(String[] args) throws Exception {
        montar();
    }

    public static void montar(){

            janela = new JFrame();
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            abas = new JTabbedPane();
            abas.addTab("Livros", new JPanel());
            abas.addTab("Exemplares", new JPanel());
            abas.addTab("Empréstimos", new JPanel());
            abas.addTab("Devoluções", new JPanel());
            abas.setEnabled(false);

            LServidor = new JLabel("Servidor:");
            LServidor.setPreferredSize(new Dimension(50 , 50));
            LBD = new JLabel("Banco de dados:");
            LUsuario = new JLabel("Usuário:");
            LSenha = new JLabel("Senha:");

            servidor = new JTextField(30);
            banco_de_dados = new JTextField(20);
            usuario = new JTextField(10);
            senha = new JTextField(10);

            conectar = new JButton("Conectar");
            conectar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        getConnection(servidor.getText() , banco_de_dados.getText() , usuario.getText() , senha.getText());
                        if(conexao != null){
                            System.out.println("Deu certo!");
                            verificar();
                        }
                    }
                    catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });

            // Criar um painel interno para os campos de texto e labels
            panelCampos = new JPanel();
            panelCampos.setLayout(new GridLayout(5 ,2, 5, 5)); // 4 linhas, 2 colunas, espaçamento 5px
            panelCampos.add(LServidor, BorderLayout.CENTER);
            panelCampos.add(servidor, BorderLayout.CENTER);
            panelCampos.add(LBD, BorderLayout.CENTER);
            panelCampos.add(banco_de_dados, BorderLayout.CENTER);
            panelCampos.add(LUsuario,BorderLayout.CENTER);
            panelCampos.add(usuario,BorderLayout.CENTER);
            panelCampos.add(LSenha,BorderLayout.CENTER);
            panelCampos.add(senha,BorderLayout.CENTER);
            panelCampos.add(conectar , BorderLayout.SOUTH);


            // painel principal para organizar campos e botão

            container_area = new JPanel();
            container_area.add(panelCampos, BorderLayout.CENTER); // Campos no centro
            //container_area.add(conectar, BorderLayout.SOUTH);     // Botão no sul

            janela.setLayout(new BorderLayout());
            janela.add(abas, BorderLayout.NORTH);           // Abas no topo
            janela.add(container_area); // Painel principal no centro

            janela.pack();
            janela.setVisible(true);
        }


        public static void getConnection(String servidorText , String bdText , String usuarioText , String senhaText) throws SQLException{
            //escrever a função para conectar ao bd aqui;
            if (servidorText != ""){
                String URL =
                        "jdbc:sqlserver://" + servidorText + ":1433;databaseName="+ bdText +
                                ";integratedSecurity=false;encrypt=false;trustServerCertificate=true";
                try{
                    conexao = DriverManager.getConnection(URL , usuarioText , senhaText);
                }
                catch(SQLException erro){
                    System.out.println(erro.getMessage());
                }
            }
        }

    public static void montarComCbx() throws SQLException {
        String[] opcoes;
        int quantasBibliotecas = 0;

        try{
            Statement comandoSql = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultadoSelect = comandoSql.executeQuery("select nome from SisBib.Biblioteca");

            while(resultadoSelect.next()){      //conta quantas bibliotecas tem no resultado
                quantasBibliotecas +=1;
            }

            System.out.println(quantasBibliotecas + " é o número de bibliotecas cadastradas");

            opcoes = new String[quantasBibliotecas];    // criar um vetor com tamanho de quantas bibliotecas para exibir no cbx

            resultadoSelect.beforeFirst();

            int i = 0;
            while (resultadoSelect.next()){
                opcoes[i] = resultadoSelect.getString("nome");
                i++;
            }
            cbx = new JComboBox<>(opcoes);
            cbx.setPreferredSize(new Dimension(100 , 25));
            abas.setEnabled(true);
            panelCampos.add(cbx , BorderLayout.SOUTH);
            janela.pack();
            janela.setVisible(true);
        }

        catch(SQLException erro){
            System.out.println(erro.getMessage());
        }
    }

    public static void verificar() throws SQLException{
        if (conexao != null){
            montarComCbx();
        }
        else{
            System.out.println("Não entra no if para montar com o combo box");
        }

    }
}
