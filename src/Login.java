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

import org.jdatepicker.impl.*;
import java.text.SimpleDateFormat;

// tela de login ao bd
public class Login {
    public static JFrame janela;
    public static JPanel container_area;
    public static JTabbedPane abas;
    public static JTextField servidor, banco_de_dados , usuario;
    public static JPasswordField senha;
    public static JButton conectar , verSenha , selecionarBiblioteca , voltar;
    public static JLabel LServidor , LBD , LUsuario , LSenha;
    public static JComboBox cbx;
    public static Connection conexao;
    public static int idBibliotecaEscolhida;
    public static java.util.Date dataSelecionada;
    public static String dataFormatada;
    public static ResultSet resultadoSelect;


    public static void main(String[] args) throws Exception {
        montar();
    }

    public static void montar() {
        janela = new JFrame();
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setPreferredSize(new Dimension(891,478));
        janela.setTitle("Sistema de Biblioteca");
        janela.setLayout(new BorderLayout());

        abas = new JTabbedPane();
        abas.addTab("Sair" , new JPanel());
        abas.addTab("Livros", new JPanel());
        abas.addTab("Exemplares", new JPanel());
        abas.addTab("Empréstimos", new JPanel());
        abas.addTab("Devoluções", new JPanel());
        abas.setEnabled(false);
        abas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                
                int index = abas.getSelectedIndex();
                if (index == 0){
                    mostrarTelaLogin();
                }
                else if (index == 1) {
                    try {
                        mostrarLivros();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (index == 2) {
                    try {
                        mostrarExemplares();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                else if (index == 3) {
                    try {
                        mostrarEmprestimos();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (index == 4) {
                    try {
                        mostrarDevolucoes();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
        abas.setBackground(Color.WHITE);
        //abas.setForeground(Color.BLACK);
        abas.setBorder(null);
        Font fonte = new Font("Times New Roman" , Font.BOLD , 14);
        abas.setFont(fonte);

        janela.add(abas, BorderLayout.NORTH);

        container_area = new JPanel();
        janela.add(container_area, BorderLayout.CENTER);

        mostrarTelaLogin();

        voltar = new JButton("Sair");
        voltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTelaLogin();
                abas.setEnabled(false);
            }
        });

        // configurações finais
        janela.pack();
        janela.setVisible(true);
    }

    public static void mostrarTelaLogin() {
        JPanel painelCamposLogin = new JPanel();
        painelCamposLogin.setLayout(new GridLayout(6, 2, 5, 5)); // 6 linhas, 2 colunas

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
                        System.out.println("Deu certo a conexão!");
                        verificar();
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        verSenha = new JButton("Mostrar Senha");
        verSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (senha.getEchoChar() == '•'){
                    senha.setEchoChar((char)0);
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

        abas.setEnabled(false);

        // cria um painel interno para os campos de texto
        painelCamposLogin.add(LServidor, BorderLayout.CENTER);
        painelCamposLogin.add(servidor, BorderLayout.CENTER);
        painelCamposLogin.add(LBD, BorderLayout.CENTER);
        painelCamposLogin.add(banco_de_dados, BorderLayout.CENTER);
        painelCamposLogin.add(LUsuario,BorderLayout.CENTER);
        painelCamposLogin.add(usuario,BorderLayout.CENTER);
        painelCamposLogin.add(LSenha,BorderLayout.CENTER);
        painelCamposLogin.add(senha,BorderLayout.CENTER);
        painelCamposLogin.add(conectar , BorderLayout.SOUTH);
        painelCamposLogin.add(verSenha , BorderLayout.SOUTH);
        painelCamposLogin.add(cbx , BorderLayout.CENTER);
        painelCamposLogin.add(selecionarBiblioteca , BorderLayout.CENTER);

        // painel principal para organizar o login e o calendário
        JPanel painelLogin = new JPanel(new BorderLayout());
        painelLogin.add(painelCamposLogin, BorderLayout.CENTER);

        //calendario ao lado do login
        JPanel panelCalendario = new JPanel();                                          // criação de um painel para juncao o calendário e o texto
        panelCalendario.setLayout(new BoxLayout(panelCalendario, BoxLayout.Y_AXIS));    // layout para empilhar os componentes verticalmente
        panelCalendario.add(new JLabel("Check-In Date: "), BorderLayout.EAST);
        UtilDateModel model = new UtilDateModel();                                      //cria um modelo de dados para o componente de seleção de data
        Properties p = new Properties();                                                //cria um objeto de propriedades que será usado para personalizar o painel de seleção de datas
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");                                                     //define os textos personalizados para o painel de seleção de datas
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);                        //cria o painel de calendário com base no UtilDateModel (modelo de dados) e nas propriedades configuradas
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());  //cria o componente DatePicker propriamente dito (escolhe data)
        model.addChangeListener(e -> {
            dataSelecionada = (java.util.Date) datePicker.getModel().getValue();
            dataFormatada = formatarData(dataSelecionada);
        });
        datePicker.setBounds(110, 100, 200, 25);                    //define a posição
        model.setSelected(true);                                                        //ativa o modelo, indicando que uma data foi selecionada
        datePicker.setVisible(true);                                                    //deixa visivel

        panelCalendario.add(datePicker);
        painelLogin.add(panelCalendario, BorderLayout.EAST);

        // add o painel de login ao container principal
        container_area.removeAll();
        container_area.setLayout(new BorderLayout());
        container_area.add(painelLogin, BorderLayout.CENTER);

        container_area.revalidate();
        container_area.repaint();
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

            opcoes = new String[quantasBibliotecas];    // criar um vetor com tamanho de quantas bibliotecas para exibir no cbx

            resultadoSelect.beforeFirst();

            int i = 0;
            while (resultadoSelect.next()){     // coloca os elementos do select em um vetor
                opcoes[i] = resultadoSelect.getString("nome");
                i++;
            }

            for (int x = 0 ; x < quantasBibliotecas ; x++){
                String bibliotecaAtual = opcoes[x];
                cbx.addItem(bibliotecaAtual);
            }

            cbx.setEnabled(true);
            abas.setEnabled(true);
            selecionarBiblioteca.setEnabled(true);
            //panelCampos.add(cbx , BorderLayout.SOUTH);
            janela.pack();
            janela.setVisible(true);
        }

        catch(SQLException erro){
            System.out.println(erro.getMessage());
        }
    }

    public static void verificar() throws SQLException{
        if (conexao != null){
            JOptionPane.showMessageDialog(null , "Login feito com sucesso!");
            depoisLogin();
        }
        else{
            System.out.println("Login sem sucesso.");
        }
    }


    public static Component mostrarLivros() throws Exception{    //só retorna Component porque o JTabbed precisa que esse método retorne um componente
        Livros objetoLivro = new Livros();
        container_area.removeAll();
        JPanel painelLivros = objetoLivro.realizarTudo();
        container_area.add(painelLivros , BorderLayout.CENTER);
        janela.add(container_area);
        janela.pack();

        return null;
    }

    public static Component mostrarExemplares() throws Exception{
        Exemplares objetoExemplar = new Exemplares();
        container_area.removeAll();
        JPanel painelExemplar = objetoExemplar.realizarTudo();
        container_area.add(painelExemplar , BorderLayout.CENTER);
        janela.add(container_area);
        janela.pack();

        return null;
    }

    public static Component mostrarEmprestimos() throws Exception{
        Emprestimos objetoEmprestimo = new Emprestimos();
        container_area.removeAll();
        JPanel painelEmprestimo = objetoEmprestimo.realizarTudo();
        container_area.add(painelEmprestimo , BorderLayout.CENTER);
        janela.add(container_area);
        janela.pack();

        return null;
    }

    public static Component mostrarDevolucoes() throws Exception{
        Devolucoes objetoDevolucoes = new Devolucoes();
        container_area.removeAll();
        JPanel painelDevolucoes = objetoDevolucoes.realizarTudo();
        container_area.add(painelDevolucoes , BorderLayout.CENTER);
        janela.add(container_area);
        janela.pack();

        return null;
    }


    public static int setIdBibliotecaEscolhida() {
        String biblioteca = cbx.getSelectedItem().toString();
        try {
            Statement comandoSql = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultadoSelect = comandoSql.executeQuery("select * from SisBib.Biblioteca where nome = '" + biblioteca + "'");

            if (resultadoSelect.next()) {
                idBibliotecaEscolhida = resultadoSelect.getInt("idBiblioteca");

                return idBibliotecaEscolhida;
            } else {
                throw new SQLException("Biblioteca não encontrada!");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return -1;
    }

    public static String formatarData(java.util.Date data) {
        if (data == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(data);
    }
}
