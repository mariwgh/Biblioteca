import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// tela de login ao bd

public class Login {
    public static JFrame janela;
    public static JPanel container_area;
    public static JTabbedPane abas;
    public static JTextField servidor, banco_de_dados , usuario , senha;
    public static JButton conectar;
    public static JLabel LServidor , LBD , LUsuario , LSenha;

    public static void main(String[] args) throws Exception{
        janela = new JFrame();
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        abas = new JTabbedPane();
        abas.addTab("Livros" , new JPanel());
        abas.addTab("Exemplares" , new JPanel());
        abas.addTab("Empréstimos" , new JPanel());
        abas.addTab("Devoluções" , new JPanel());
        abas.setEnabled(false);

        LServidor = new JLabel("Servidor:");
        LBD = new JLabel("Banco de dados:");
        LUsuario = new JLabel("Usuário:");
        LSenha = new JLabel("Senha:");

        //fazer um check box para mostrar a senha;


        servidor = new JTextField(30);    //pegar o valor --> servidor.getText();
        banco_de_dados = new JTextField(20);
        usuario = new JTextField(10);
        senha = new JTextField(10);

        conectar = new JButton("Conectar");
        conectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    getConnection();
                    System.out.println("Deu certo!");
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        container_area = new JPanel();
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
        container_area.add(conectar);

        janela.setLayout(new BorderLayout());
        janela.add(abas , BorderLayout.NORTH);  //  NORTH é a região da janela
        janela.add(container_area , BorderLayout.CENTER);
        janela.pack();
        janela.setVisible(true);

    }

    public static Connection getConnection() throws Exception{
        String servidorText = servidor.getText();
        String bdText = banco_de_dados.getText();
        String usuarioText = usuario.getText();
        String senhaText = senha.getText();
        //escrever a função para conectar ao bd aqui;
        String URL =
                "jdbc:sqlserver://" + servidorText + ":1433;databaseName="+ bdText +
                        ";integratedSecurity=false;encrypt=false;trustServerCertificate=true";

        return DriverManager.getConnection(URL , usuarioText , senhaText);
    }
}
