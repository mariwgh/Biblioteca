package src;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Livros {

    public static JComboBox operacao;
    public static JButton realizar , voltar , selecionar;
    public static int idBibliotecaEscolhida;
    public static JPanel container , painelCampos;
    public static JTextField inpId, inpTitulo , inpAutor , inpArea;
    public static JTable tabelaResultadoSql;

   /* public static void main(String[] args){
        montar();
    }*/
    public static void montar(){
        JPanel panelCampos;

        String[] opcoes = new String[]{"INCLUIR" , "DELETAR" , "ALTERAR" , "BUSCAR"};

        operacao = new JComboBox(opcoes);
        operacao.setPreferredSize(new Dimension(100 , 25));


        realizar = new JButton("REALIZAR");
        realizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //realizar a função escolhida no cbx, pegar os dados
                //dos campos do formulario e fazer a consulta
                consultas();
            }
        });

        selecionar = new JButton("SELECIONAR");
        selecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    mostrarInputs();
                }
                catch (Exception erro){
                    System.out.println(erro.getMessage());
                }

            }
        });


        // Criar um painel interno para os campos de texto e labels
        panelCampos = new JPanel();
        panelCampos.add(realizar , BorderLayout.NORTH);
        //panelCampos.add(voltar , BorderLayout.NORTH);
        panelCampos.add(selecionar , BorderLayout.NORTH);
        panelCampos.add(operacao , BorderLayout.NORTH);

        // painel principal para organizar campos e botão

        container = new JPanel();
        container.setLayout(new GridLayout(2 , 4 , 5 , 5));
        container.add(panelCampos, BorderLayout.NORTH); // Campos no centro
        container.add(Box.createVerticalStrut(15));
}

    public static void setIdBibliotecaEscolhida(Login id){
        idBibliotecaEscolhida = Integer.parseInt(id.toString());
        // AQUI VAI TER QUE FAZER UM WHERE COM O ID DA BIBLIOTECA SELECIONADA
        // EM TUDO QUE FOR FAZER
    }

    public static void mostrarInputs(){
        painelCampos = new JPanel();
        if (operacao.getSelectedItem().toString() == "BUSCAR"){
            inpId = new JTextField(10);
            inpTitulo = new JTextField(10);
            inpAutor = new JTextField(10);
            inpArea = new JTextField(10);
            System.out.println("A operação selecionada foi buscar!");
            JLabel id = new JLabel("Digite o id do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");
            if (painelCampos != null){
                painelCampos.removeAll();

            }
            painelCampos.add(id);
            painelCampos.add(inpId);
            painelCampos.add(titulo);
            painelCampos.add(inpTitulo);
            container.add(painelCampos);
        }
        else if (operacao.getSelectedItem().toString() == "INCLUIR") {
            inpId = new JTextField(10);
            inpTitulo = new JTextField(10);
            inpAutor = new JTextField(10);
            inpArea = new JTextField(10);
            System.out.println("A operação selecionada foi incluir!");
            JLabel id = new JLabel("Digite o id do livro: ");
            JLabel titulo = new JLabel("Digite o título do livro: ");
            JLabel idAutor = new JLabel("Digite o id do autor: ");
            JLabel idArea = new JLabel("Digite o id da área que o livro pertence: ");

            painelCampos.setLayout(new GridLayout(4 , 2 , 5 , 5));  //deixa ele parecendo um formulário
            if (painelCampos != null){
                painelCampos.removeAll();
            }

            painelCampos.add(id);
            painelCampos.add(inpId);
            painelCampos.add(titulo);
            painelCampos.add(inpTitulo);
            painelCampos.add(idAutor);
            painelCampos.add(inpAutor);
            painelCampos.add(idArea);
            painelCampos.add(inpArea);

            container.add(new JPanel());
            container.add(painelCampos ,BorderLayout.CENTER);
            //tem que alinhar esses elementos no meio da janela
            //NAO TA FICANDO *EMOJI BRAVO*
        }

        else if(operacao.getSelectedItem().toString() == "DELETAR"){
            System.out.println("A operação selecionada foi excluir!");
            JLabel LTitulo = new JLabel("Digite o título do livro: ");
            inpTitulo = new JTextField(10);
            if (painelCampos != null){
                painelCampos.removeAll();
            }
            painelCampos.add(LTitulo);
            painelCampos.add(inpTitulo);
            container.add(painelCampos);
            //depois fazer um if para ver se algum JTExtielf está vazio
            //e fazer o select com bases nos que estão preenchidos
        }
        else if (operacao.getSelectedItem().toString() == "ALTERAR") {
            System.out.println("A operação selecionada foi alterar!");
            String[] dados = new String[]{"Id livro" , "Título" , "Id autor" , "Id área"};
            JLabel whereAntigo = new JLabel("Qual é o dado de referência?");
            JComboBox cbxAntigo = new JComboBox(dados);
            JLabel LDadoAntigo = new JLabel("Digite o dado de referência: ");
            JTextField dadoAntigo = new JTextField();
            JLabel qualAlterar = new JLabel("Qual dado quer alterar?");
            JComboBox cbxAlterar = new JComboBox(dados);
            JLabel LNovo = new JLabel("Qual é o novo dado?");
            JTextField novoDado = new JTextField();
            container.add(whereAntigo);
            container.add(cbxAntigo);
            container.add(LDadoAntigo);
            container.add(dadoAntigo);
            container.add(qualAlterar);
            container.add(cbxAlterar);
            container.add(LNovo);
            container.add(novoDado);
            //tem q pegar todos esses dados e passar para realizar a consulta no sql e dps mostrar

        }
    }

    public static JPanel realizarTudo() throws Exception{
        montar();
        //container.setLayout(new FlowLayout(FlowLayout.LEFT)); // Alinha os componentes à esquerda de maneira compacta
        //container.setSize(300,200); // Limita o tamanho do painel
        return container;
    }

    public static void consultas(){
        String funcao = operacao.getSelectedItem().toString();
        Statement comandoSql;
        switch (funcao){
            case "INCLUIR":
                //PASSAR COMO PARAMETRO O INSERT INTO COM OS DADOS
                String sql = "INSERT INTO SisBib.Livro values (? , ? , ? , ?)";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1 , Integer.parseInt(inpId.getText()));  //muda o primeiro '?'
                    preparedStatement.setString(2 , inpTitulo.getText());   //  muda o segundi '?'
                    preparedStatement.setInt(3 , Integer.parseInt(inpAutor.getText()));
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "DELETAR":
                sql = "delete *  from SisBib.Livro where titulo= ?";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setString(1 , inpTitulo.getText());
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "ALTERAR":
                sql = "update SisBib.Livro set idArea = ? where titulo = ?";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setInt(1 , Integer.parseInt(inpArea.getText()));
                    preparedStatement.setString(2 , inpTitulo.getText());
                    int linhasAfetadas = preparedStatement.executeUpdate();
                    System.out.println("Linhas afetadas: " + linhasAfetadas);
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "BUSCAR":
                try{
                    //só funciona com a busca geral, mostra tudo
                    //tem q fzr o where idBiblioteca
                    comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro");        //tem que fazer um where do idBiblioteca

                    String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                    DefaultTableModel modelo = new DefaultTableModel(resultadoDoSelect, colunas);
                    tabelaResultadoSql = new JTable(modelo);
                    if (resultadoDoSelect != null){
                        System.out.println("Deu certo a busca");
                    }else{
                        System.out.println("Deu errado!");
                    }
                }
                catch (SQLException erro){
                    throw new RuntimeException();
                }

        }
    }

}
