import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.border.Border;
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

    public static void main(String[] args) throws Exception {
        montar();
        if (getConnection(servidor.getText() , banco_de_dados.getText() , usuario.getText() , senha.getText()) != null){
            montarComCbx();
        }
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
                public void actionPerformed(ActionEvent e) {
                    try {
                        getConnection(servidor.getText() , banco_de_dados.getText() , usuario.getText() , senha.getText());
                        System.out.println("Deu certo!");
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });

            // Criar um painel interno para os campos de texto e labels
            JPanel panelCampos = new JPanel();
            panelCampos.setLayout(new GridLayout(4, 2, 5, 5)); // 4 linhas, 2 colunas, espaçamento 5px
            panelCampos.add(LServidor);
            panelCampos.add(servidor);
            panelCampos.add(LBD);
            panelCampos.add(banco_de_dados);
            panelCampos.add(LUsuario);
            panelCampos.add(usuario);
            panelCampos.add(LSenha);
            panelCampos.add(senha);

            // Painel principal para organizar campos e botão
            container_area = new JPanel();
            container_area.setLayout(new BorderLayout(10, 10));
            container_area.add(panelCampos, BorderLayout.CENTER); // Campos no centro
            container_area.add(conectar, BorderLayout.SOUTH);     // Botão no sul

            janela.setLayout(new BorderLayout());
            janela.add(abas, BorderLayout.NORTH);           // Abas no topo
            janela.add(container_area, BorderLayout.CENTER); // Painel principal no centro
            janela.pack();
            janela.setVisible(true);
        }


        public static Connection getConnection(String servidorText , String bdText , String usuarioText , String senhaText) throws SQLException{
            //escrever a função para conectar ao bd aqui;
            String URL =
                    "jdbc:sqlserver://" + servidorText + ":1433;databaseName="+ bdText +
                            ";integratedSecurity=false;encrypt=false;trustServerCertificate=true";
            try{
                conexao = DriverManager.getConnection(URL , usuarioText , senhaText);
                return conexao;
            }
            catch(SQLException erro){
                System.out.println(erro.getMessage());
            }
            return null;
    }

    public static void montarComCbx(){
        abas.setEnabled(true);
        String[] opcoes = {"biblioteca1" , "biblioteca2"};  //aqui vai ser estatico e com o vetor de resultados do select sisbid.bibliotecas
        cbx = new JComboBox<>(opcoes);
        //colocar a lista de bibliotecas nele
        container_area.removeAll(); // limpa ele para fazer de novo
        janela.removeAll();

        container_area.setLayout(new GridLayout(5, 2, 5, 5)); // 5 linhas, 2 colunas, espaçamento de 10px
        container_area.add(LServidor);
        container_area.add(servidor);
        container_area.add(LBD);
        container_area.add(banco_de_dados);
        container_area.add(LUsuario);
        container_area.add(usuario);
        container_area.add(LSenha);
        container_area.add(senha);
        container_area.add(new JLabel());   //  adiciona um espaçamento para colocar o botão
        container_area.add(conectar , BorderLayout.SOUTH);
        container_area.add(new JLabel());
        container_area.add(cbx);

        janela.setLayout(new BorderLayout());
        janela.add(abas , BorderLayout.NORTH);  //  NORTH é a região da janela
        janela.add(container_area , BorderLayout.CENTER);
        janela.pack();
        janela.setVisible(true);
    }
}
