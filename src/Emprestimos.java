package src;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

import static src.Login.formatarData;


public class Emprestimos {
    public static JButton realizar;
    public static JPanel container, painelCampos;
    public static JTextField inputIdLeitor, inputIDExemplar;
    public static int idExemplar;
    public static String dataPrevista;
    public static JTable tabelaResultadoSqlAtrasos;

    public static int idBibliotecaEscolhida;
    static {
        Login.setIdBibliotecaEscolhida();
        idBibliotecaEscolhida = Login.idBibliotecaEscolhida;
    }

    // inicializa o valor de dataCheckIn
    public static String dataCheckIn;
    static {
        dataCheckIn = Login.dataFormatada;
    }


    public static void montar() throws SQLException {
        if (painelCampos == null) {
            painelCampos = new JPanel();
        }
        painelCampos.setLayout(new GridLayout(2, 2, 5, 5));

        painelCampos.removeAll();   //limpa o container se tiver alguma coisa
        painelCampos.revalidate();  //meio que valida ele após alguma mudança
        painelCampos.repaint();     //redesenha o container na tela após alguma alteração

        inputIdLeitor = new JTextField(10);
        inputIDExemplar = new JTextField(10);
        JLabel idLeitor = new JLabel("Digite o ID do leitor: ");
        JLabel idExemplar = new JLabel("Digite o ID do exemplar: ");

        painelCampos.add(idLeitor);
        painelCampos.add(inputIdLeitor);
        painelCampos.add(idExemplar);
        painelCampos.add(inputIDExemplar);

        // painel principal para organizar campos e botão
        container = new JPanel();
        container.setLayout(new GridLayout(7 , 4 , 5 , 5));

        container.add(painelCampos, BorderLayout.CENTER);

        //calendario EM BAIXO do campos
        JPanel painelCalendario = new JPanel();                                             // criação de um painel para juncao o calendário e o texto
        painelCalendario.setLayout(new BoxLayout(painelCalendario, BoxLayout.Y_AXIS));      // layout para empilhar os componentes verticalmente
        painelCalendario.add(new JLabel("Devolução prevista: "), BorderLayout.CENTER);
        UtilDateModel model = new UtilDateModel();                                          //cria um modelo de dados para o componente de seleção de data
        Properties p = new Properties();                                                    //cria um objeto de propriedades que será usado para personalizar o painel de seleção de datas
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");                                                         //define os textos personalizados para o painel de seleção de datas
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);                            //cria o painel de calendário com base no UtilDateModel (modelo de dados) e nas propriedades configuradas
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());  //cria o componente DatePicker propriamente dito (escolhe data)
        model.addChangeListener(e -> {
            dataPrevista = formatarData((Date) datePicker.getModel().getValue());
        });
        datePicker.setBounds(110, 100, 200, 25);            //define a posição
        model.setSelected(true);                                                //ativa o modelo, indicando que uma data foi selecionada
        datePicker.setVisible(true);                                            //deixa visivel
        painelCalendario.add(datePicker);

        container.add(painelCalendario, BorderLayout.CENTER);

        // atualiza o layout após alterações
        container.revalidate();
        container.repaint();

        //realiza a opcao escolhida
        realizar = new JButton("REALIZAR");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //realizar a função escolhida no cbx, pegar os dados dos campos do formulario e fazer a consulta
                try {
                    inserirEmprestimo();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(Login.voltar , BorderLayout.NORTH);
        painelBotoes.add(realizar , BorderLayout.SOUTH);

        container.add(painelBotoes, BorderLayout.SOUTH);        // botao em baixo
        container.add(Box.createVerticalStrut(15));     //espaçamento vertical fixo de 15 pixels no layout

        buscarAtrasosView();
    }

    public static JPanel realizarTudo() throws Exception {
        montar();
        container.setPreferredSize(new Dimension(891 ,478 ));
        return container;
    }

    public static void inserirEmprestimo() throws SQLException {
        String sql = "INSERT INTO SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista) values (? , ? ,  ? , NULL , ?)";
        try {
            PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(inputIdLeitor.getText()));
            preparedStatement.setInt(2, Integer.parseInt(inputIDExemplar.getText()));
            preparedStatement.setString(3, String.valueOf(dataCheckIn));
            preparedStatement.setString(4, String.valueOf(dataPrevista));

            int linhasAfetadas = preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Linhas afetadas: " + linhasAfetadas);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static void buscarAtrasosView() throws SQLException {
        String stringSql = "select a.* from SisBib.atrasos as a inner join SisBib.Exemplar as ex on ex.idExemplar = a.idExemplar inner join SisBib.Biblioteca as b on b.idBiblioteca = ex.idBiblioteca where b.idBiblioteca = " + idBibliotecaEscolhida;

        Statement comandoSql;

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

                String[] colunas = new String[]{"idExemplar", "titulo", "Valor Multa"};
                String[][] resultadoSQL = new String[linhas][3]; //com x linhas e cada linha tem 4 campos (colunas)

                resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                //definir o "nome das colunas"
                resultadoSQL[0][0] = colunas[0];
                resultadoSQL[0][1] = colunas[1];
                resultadoSQL[0][2] = colunas[2];

                for (int i = 1; i < linhas; i++) {
                    resultadoDoSelect.next();

                    resultadoSQL[i][0] = resultadoDoSelect.getString("idExemplar");
                    resultadoSQL[i][1] = resultadoDoSelect.getString("titulo");
                    resultadoSQL[i][2] = resultadoDoSelect.getString("Valor Multa");
                }

                DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                tabelaResultadoSqlAtrasos = new JTable(modelo);

                container.add(tabelaResultadoSqlAtrasos);
                container.setVisible(true);
            }
            else {
                System.out.println("o resultado está dando nulo");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}
