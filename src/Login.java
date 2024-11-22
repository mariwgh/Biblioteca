package src;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BaseMultiResolutionImage;
import java.sql.*;
import java.util.Properties;

/*
import java.sql.Statement;		        // permite criar um objeto de execução de comandos no servidor
import java.sql.PreparedStatement;  	// para usar parÂmetros em comandos SQL e evitar SQL Injection
import java.sql.CallableStatement;  	// para chamar stored procedures
import java.sql.Date;
*/

import org.jdatepicker.impl.*;


// tela de login ao bd
public class Login {
    public static JFrame janela;
    public static JPanel container_area;
    public static JTabbedPane abas;
    public static JTextField servidor, banco_de_dados , usuario;
    public static JPasswordField senha;
    public static JButton conectar , verSenha , selecionarBiblioteca;
    public static JLabel LServidor , LBD , LUsuario , LSenha;
    public static JComboBox cbx;
    public static Connection conexao;
    public static int idBibliotecaEscolhida;

    public static ResultSet resultadoSelect;

    public static void main(String[] args) throws Exception {
        montar();
    }

    public static void montar() {
        JPanel panelCampos;

        janela = new JFrame();
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setTitle("Sistema de Biblioteca");

        abas = new JTabbedPane();
        abas.addTab("Livros", new JPanel());
        abas.addTab("Exemplares", new JPanel());
        abas.addTab("Empréstimos", new JPanel());
        abas.addTab("Devoluções", new JPanel());
        abas.setEnabled(false);
        abas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = abas.getSelectedIndex();  // Obtém o índice da aba selecionada
                System.out.println("Aba selecionada: " + index);  // Exibe o índice da aba selecionada

                // Ações baseadas na aba selecionada
                if (index == 0) {
                    System.out.println("Redirecionando para Livros...");
                    try {
                        mostrarLivros();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (index == 1) {
                    System.out.println("Redirecionando para Exemplares...");
                    try {
                        mostrarExemplares();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                else if (index == 2) {
                    System.out.println("Redirecionando para Empréstimos...");
                    try {
                        mostrarEmprestimos();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (index == 3) {
                    System.out.println("Redirecionando para Devoluções...");
                    try {
                        mostrarDevolucoes();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });

        LServidor = new JLabel("Servidor:");
        LServidor.setPreferredSize(new Dimension(50 , 50));
        LBD = new JLabel("Banco de dados:");
        LUsuario = new JLabel("Usuário:");
        LSenha = new JLabel("Senha:");

        servidor = new JTextField(30);
        servidor.setText("regulus.cotuca.unicamp.br");
        banco_de_dados = new JTextField(20);
        usuario = new JTextField(10);
        senha = new JPasswordField(10);

        cbx = new JComboBox();
        cbx.setPreferredSize(new Dimension(100 , 25));
        cbx.setEnabled(false);

        conectar = new JButton("Conectar");
        conectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getConnection();
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

        verSenha = new JButton("Mostrar Senha");
        //verSenha.setPreferredSize(new Dimension(4 , 5));
        verSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (senha.getEchoChar() == '•'){
                    senha.setEchoChar((char)0); //nao entendi mas é
                    verSenha.setText("Mostrar Senha");
                }
                else{
                    senha.setEchoChar('•');
                    verSenha.setText("Ocultar Senha");
                }

            }
        });

        selecionarBiblioteca = new JButton("Selecionar biblioteca");
        selecionarBiblioteca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setIdBibliotecaEscolhida();
            }
        });


        // Criar um painel interno para os campos de texto e labels
        panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(6 ,2, 5, 5)); // 6 linhas, 2 colunas, espaçamento 5px
        panelCampos.add(LServidor, BorderLayout.CENTER);
        panelCampos.add(servidor, BorderLayout.CENTER);
        panelCampos.add(LBD, BorderLayout.CENTER);
        panelCampos.add(banco_de_dados, BorderLayout.CENTER);
        panelCampos.add(LUsuario,BorderLayout.CENTER);
        panelCampos.add(usuario,BorderLayout.CENTER);
        panelCampos.add(LSenha,BorderLayout.CENTER);
        panelCampos.add(senha,BorderLayout.CENTER);
        panelCampos.add(conectar , BorderLayout.SOUTH);
        panelCampos.add(verSenha , BorderLayout.SOUTH);
        panelCampos.add(cbx , BorderLayout.CENTER);


        // painel principal para organizar campos e botão

        container_area = new JPanel();
        container_area.add(panelCampos, BorderLayout.CENTER); // Campos no centro

        //janela.setTitle("Login ao BD");
        janela.setLayout(new BorderLayout());
        janela.add(abas, BorderLayout.NORTH);           // Abas no topo


        //calendario ao lado direto? do login
        Container cntForm = janela.getContentPane();                              // acessa a área de 'desenho'
        cntForm.setLayout(new BorderLayout());
        //espaco
        UtilDateModel model = new UtilDateModel();                               //cria um modelo de dados para o componente de seleção de data
        Properties p = new Properties();                                        //cria um objeto de propriedades que será usado para personalizar o painel de seleção de datas
        p.put("text.today", "Today"); p.put("text.month", "Month");
        p.put("text.year", "Year");                                             //define os textos personalizados para o painel de seleção de datas
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);                //cria o painel de calendário com base no UtilDateModel (modelo de dados) e nas propriedades configuradas
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());  //cria o componente DatePicker propriamente dito (escolhe data)
        datePicker.setBounds(110, 100, 200, 25);            //define a posição
        model.setSelected(true);                                                //ativa o modelo, indicando que uma data foi selecionada
        datePicker.setVisible(true);                                            //deixa visivel
        cntForm.add(datePicker, BorderLayout.EAST);                             //para ficar ao xxxx do formulario


        janela.add(container_area);                     // Painel principal no centro

        janela.pack();
        janela.setVisible(true);
        //janela.setPreferredSize(new Dimension(699 , 469));
    }

    public static void getConnection() throws SQLException {
        String server = servidor.getText();
        String db = banco_de_dados.getText();
        String user = usuario.getText();
        String passWord = senha.getText();
        String URL = "jdbc:sqlserver://" + server + ":1433;databaseName=" + db +";integratedSecurity=false;encrypt=false;trustServerCertificate=true";
        try{
            conexao = DriverManager.getConnection(URL, user, passWord);
        }
        catch (SQLException erro){
            System.out.println(erro.getMessage());
        }
    }

    public static void depoisLogin() throws SQLException {
        String[] opcoes;
        int quantasBibliotecas = 0;

        try{
            Statement comandoSql = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultadoSelect = comandoSql.executeQuery("select nome from SisBib.Biblioteca");

            while(resultadoSelect.next()){      //conta quantas bibliotecas tem no resultado
                quantasBibliotecas +=1;
            }

            //System.out.println(quantasBibliotecas + " é o número de bibliotecas cadastradas");

            opcoes = new String[quantasBibliotecas];    // criar um vetor com tamanho de quantas bibliotecas para exibir no cbx

            resultadoSelect.beforeFirst();

            int i = 0;
            while (resultadoSelect.next()){ // coloca os elementos do select em um vetor
                opcoes[i] = resultadoSelect.getString("nome");
                i++;
            }

            for (int x = 0 ; x < quantasBibliotecas ; x++){
                String bibliotecaAtual = opcoes[x];
                cbx.addItem(bibliotecaAtual);
            }

            cbx.setEnabled(true);
            abas.setEnabled(true);
            //panelCampos.add(cbx , BorderLayout.SOUTH);
            janela.pack();
            janela.setVisible(true);
        }

        catch(SQLException erro){
            System.out.println(erro.getMessage());
        }
    }

    //talvez colocar isso em uma outra classe seja melhor!
    /*public static ResultSet realizarSQL() throws SQLException {
        Statement comandoSQL = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultadoSelect = comandoSQL.executeQuery("select nome from SisBib.Biblioteca");    //aqui vai receber como parâmetro a consulta desejada
        //cuidar depois do SQLInjection
        return resultadoSelect;
    }*/

    public static void verificar() throws SQLException{
        if (conexao != null){
            JOptionPane.showMessageDialog(null , "Login feito com sucesso!");
            depoisLogin();
        }
        else{
            System.out.println("Não entra no if para montar com o combo box");
        }
    }

    public static Component mostrarLivros() throws Exception{    //  só retorna Component porque o JTabbed precisa que esse método retorne um componente
        Livros objetoLivro = new Livros();
        container_area.removeAll();
        //janela.removeAll();
        JPanel painelLivros = objetoLivro.realizarTudo();
        container_area.add(painelLivros , BorderLayout.CENTER);
        janela.pack();
        //container_area.add(cbx , BorderLayout.SOUTH);
        janela.add(container_area);
        janela.pack();
        return null;
    }

    public static Component mostrarExemplares() throws Exception{    //  só retorna Component porque o JTabbed precasa que esse método retorne um componente
        Exemplares objetoExemplar = new Exemplares();
        container_area.removeAll();
        //janela.removeAll();
        JPanel painelExemplar = objetoExemplar.realizarTudo();
        container_area.add(painelExemplar , BorderLayout.CENTER);

        janela.pack();
        janela.add(container_area);
        janela.pack();
        return null;
    }

    public static Component mostrarEmprestimos() throws Exception{    //  só retorna Component porque o JTabbed precasa que esse método retorne um componente
        Emprestimos objetoEmprestimo = new Emprestimos();
        container_area.removeAll();
        //janela.removeAll();
        JPanel painelEmprestimo = objetoEmprestimo.realizarTudo();
        container_area.add(painelEmprestimo , BorderLayout.CENTER);
        janela.pack();
        janela.add(container_area);
        janela.pack();
        return null;
    }

    public static Component mostrarDevolucoes() throws Exception{    //  só retorna Component porque o JTabbed precasa que esse método retorne um componente
        Devolucoes objetoDevolucoes = new Devolucoes();
        container_area.removeAll();
        //janela.removeAll();
        JPanel painelDevolucoes = objetoDevolucoes.realizarTudo();
        container_area.add(painelDevolucoes , BorderLayout.CENTER);
        janela.pack();
        janela.add(container_area);
        janela.pack();
        return null;
    }


    public static void setIdBibliotecaEscolhida() {
        String biblioteca = cbx.getSelectedItem().toString();
        try {
            Statement comandoSql = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultadoSelect = comandoSql.executeQuery("select * from SisBib.Biblioteca where nome = '" + biblioteca + "'");

            if (resultadoSelect.next()) {
                idBibliotecaEscolhida = resultadoSelect.getInt("idBiblioteca");
            } else {
                throw new SQLException("Biblioteca não encontrada!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
