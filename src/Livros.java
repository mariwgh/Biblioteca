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
                try {
                    consultas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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
        container.setLayout(new GridLayout(5 , 4 , 5 , 5));
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

    public static void consultas() throws SQLException {
        String funcao = operacao.getSelectedItem().toString();
        Statement comandoSql;
        switch (funcao){
            case "INCLUIR":
                //PASSAR COMO PARAMETRO O INSERT INTO COM OS DADOS
                String sql = "INSERT INTO SisBib.Livro values (? , ? , ? , ?)";
                try {
                    PreparedStatement preparedStatement = Login.conexao.prepareStatement(sql);
                    preparedStatement.setString(1 , inpId.getText());  //muda o primeiro '?'
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
                String id = inpId.getText();
                String titulo = inpTitulo.getText();
                //System.out.println(inpTitulo.getText());
                //System.out.println(inpTitulo.getText());
                if (titulo.equals("") && id.equals("")){
                    try{

                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);  //esses parâmetros permitem o 'cursor' voltar
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                //System.out.println(resultadoDoSelect.getInt("idArea"));
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "Código";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "Título";
                            resultadoSQL[0][2] = "id Autor";
                            resultadoSQL[0][3] = "id Área";

                            for (int i = 1 ; i < linhas ; i++){
                                resultadoDoSelect.next();
                                //resultadoSQL[i][0] = resultadoDoSelect.getInt("codLivro");
                                //System.out.println(Integer.toString(resultadoDoSelect.getInt("codLivro")));
                                resultadoSQL[i][0] = resultadoDoSelect.getString("codLivro");
                                resultadoSQL[i][1] = resultadoDoSelect.getString("titulo");
                                resultadoSQL[i][2] = resultadoDoSelect.getString("idAutor");
                                resultadoSQL[i][3] = resultadoDoSelect.getString("idArea");
                            }

                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            tabelaResultadoSql.setVisible(true);
                            /*if (tabelaResultadoSql != null){
                                System.out.println("A tabela tem coisa");
                            }
                            else{
                                System.out.println("tabela nao está guardando dados");
                            }*/

                            container.add(tabelaResultadoSql);
                            container.setVisible(true);

                            if (resultadoDoSelect != null){
                                System.out.println("Deu certo a busca");
                            }
                            else{
                                System.out.println("Deu errado!");
                            }
                        }
                        else{
                            System.out.println("o resultado está dando nulo");
                        }


                    }
                    catch (SQLException erro){
                        System.out.println(erro.getMessage());;
                    }

                }
                else {
                    if (titulo.equals("")){ //se o usuario tiver digitado somente o id
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro where codLivro = '" + inpId.getText() + "'");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                System.out.println(resultadoDoSelect.getInt("idArea")); //ELE TA PEGANDO AQUI
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "Código";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "Título";
                            resultadoSQL[0][2] = "id Autor";
                            resultadoSQL[0][3] = "id Área";

                            for (int i = 1 ; i < linhas ; i++){
                                resultadoDoSelect.next();
                                //resultadoSQL[i][0] = resultadoDoSelect.getInt("codLivro");
                                //System.out.println(Integer.toString(resultadoDoSelect.getInt("codLivro")));
                                resultadoSQL[i][0] = resultadoDoSelect.getString("codLivro");
                                resultadoSQL[i][1] = resultadoDoSelect.getString("titulo");
                                resultadoSQL[i][2] = resultadoDoSelect.getString("idAutor");
                                resultadoSQL[i][3] = resultadoDoSelect.getString("idArea");
                            }

                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            if (tabelaResultadoSql != null){
                                System.out.println("A tabela tem coisa");
                            }
                            else{
                                System.out.println("tabela nao está guardando dados");
                            }
                            container.add(tabelaResultadoSql);
                            container.setVisible(true);

                            if (resultadoDoSelect != null){
                                System.out.println("Deu certo a busca");
                            }
                            else{
                                System.out.println("Deu errado!");
                            }
                        }
                        else{
                            System.out.println("o resultado está dando nulo");
                        }
                    }
                    else if (id.equals("")) {   //usuario digitou somente o titulo
                        int linhas = 0;
                        //só funciona com a busca geral, mostra tudo
                        //tem q fzr o where idBiblioteca
                        comandoSql = Login.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet resultadoDoSelect = comandoSql.executeQuery("select * from SisBib.Livro where titulo = '" + inpTitulo.getText() + "'");        //tem que fazer um where do idBiblioteca
                        if (resultadoDoSelect != null){
                            while(resultadoDoSelect.next()){      //conta quantas linhas tem o resultado
                                System.out.println(resultadoDoSelect.getInt("idArea")); //ELE TA PEGANDO AQUI
                                linhas +=1;
                            }

                            linhas += 1;    //para colocar as colunas também

                            String[] colunas = new String[]{"codLivro" , "titulo" , "idAutor" , "idArea"};
                            String[][] resultadoSQL = new String[linhas][4]; //com x linhas e cada linha tem 4 campos (colunas)

                            resultadoDoSelect.beforeFirst();        //volta para o inicio para guardar os dados no resultadoSQL

                            resultadoSQL[0][0] = "Código";  //definir o "nome das colunas"
                            resultadoSQL[0][1] = "Título";
                            resultadoSQL[0][2] = "id Autor";
                            resultadoSQL[0][3] = "id Área";

                            for (int i = 1 ; i < linhas ; i++){
                                resultadoDoSelect.next();
                                //resultadoSQL[i][0] = resultadoDoSelect.getInt("codLivro");
                                //System.out.println(Integer.toString(resultadoDoSelect.getInt("codLivro")));
                                resultadoSQL[i][0] = resultadoDoSelect.getString("codLivro");
                                resultadoSQL[i][1] = resultadoDoSelect.getString("titulo");
                                resultadoSQL[i][2] = resultadoDoSelect.getString("idAutor");
                                resultadoSQL[i][3] = resultadoDoSelect.getString("idArea");
                            }

                            DefaultTableModel modelo = new DefaultTableModel(resultadoSQL, colunas);
                            tabelaResultadoSql = new JTable(modelo);
                            if (tabelaResultadoSql != null){
                                System.out.println("A tabela tem coisa");
                            }
                            else{
                                System.out.println("tabela nao está guardando dados");
                            }
                            container.add(tabelaResultadoSql);
                            container.setVisible(true);
                            //Login.janela.pack();

                            if (resultadoDoSelect != null){
                                System.out.println("Deu certo a busca");
                            }
                            else{
                                System.out.println("Deu errado!");
                            }
                        }
                        else{
                            System.out.println("o resultado está dando nulo");
                        }
                    }
                    else{
                        System.out.println("Dados inválidos!");
                    }
                }

        }
    }

}
